/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tsc9526.monalisa.tools.datatable.CaseInsensitiveMap;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.xml.XMLDocument;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Repository {
	static Logger logger=Logger.getLogger(Repository.class);
	
	private List<String> activeProfiles=new ArrayList<String>();
	
	private String localRepository;
	
	private List<String> remoteRepositoryUrls=new ArrayList<String>();
	
	private CaseInsensitiveMap<String> mirrors = new CaseInsensitiveMap<String>();
	
	private CaseInsensitiveMap<Profile> profiles = new CaseInsensitiveMap<Profile>();
	
	private XMLDocument parser;
	private Document document;
	
	public Repository(){
		localRepository      = System.getProperty("user.home") + "/.m2/repository";
		
		remoteRepositoryUrls.add("https://repo1.maven.org/maven2");
		
		loadFromSettings();
	}
	
	
	public String getLocalRepository(){
		return localRepository;
	}
	
	public List<String> getRemoteRepositoryUrls(){
		return remoteRepositoryUrls;
	}
	
	protected void loadFromSettings(){
		String settings = System.getProperty("user.home") + "/.m2/settings.xml";
		if (!new File(settings).exists()) {
			String home = System.getProperty("M2_HOME", System.getenv("M2_HOME"));
			if(home==null){
				home = System.getProperty("MAVEN_HOME", System.getenv("MAVEN_HOME"));
			}
			
			if (home != null) {
				settings = home + "/conf/settings.xml";
			}
		}
		
		File fs=new File(settings);
		if (fs.exists()){
			try{
				parser = new XMLDocument();
				document = parser.parseDocument(fs);
				
				parseSettings(); 
			}catch(Exception e){
				logger.error("Exception parse maven settings: "+fs.getAbsolutePath(),e);
			}
		}
	}

	
	protected void parseSettings()throws Exception {
		parseLocalRespository();

		parseMirrors();
		
		parseProfiles();
		
		parseActiveProfiles();
		
		parseRemoteRepositoryUrls();
	}
	
	protected void parseLocalRespository()throws Exception {
		localRepository = parser.getNodeStringValue(document, "/settings/localRepository");
	}

	protected void parseMirrors()throws Exception {
		NodeList ns=parser.selectNodes(document, "/settings/mirrors/mirror");
		if(ns!=null && ns.getLength()>0){
			for(int i=0;i<ns.getLength();i++){
				Node mirror=ns.item(i);
				
				String id =parser.getNodeStringValue(mirror,"id");
				String url=parser.getNodeStringValue(mirror, "url");
				
				mirrors.put(id,url);
			}
		}
	}
	
	protected void parseProfiles()throws Exception {
		NodeList ns=parser.selectNodes(document, "/settings/profiles/profile/repositories/repository");
		if(ns!=null && ns.getLength()>0){
			for(int i=0;i<ns.getLength();i++){
				Node nr=ns.item(i);
				
				String id =parser.getNodeStringValue(nr,"id");
				String url=parser.getNodeStringValue(nr, "url");
				
				
				Node np=nr.getParentNode().getParentNode();
				String profileId =parser.getNodeStringValue(np,"id");
				
				Profile profile=profiles.get(profileId);
				if(profile==null){
					CaseInsensitiveMap<String> rs=new CaseInsensitiveMap<String>();
					rs.put(id, url);
					
					profiles.put(profileId, new Profile(profileId,rs));
				}else{
					profile.repositories.put(id,url);
				}
				
			}
		}
	}

	protected void parseActiveProfiles()throws Exception {
		NodeList ns = parser.selectNodes(document, "/settings/activeProfiles/activeProfile");
		if(ns!=null && ns.getLength()>0){
			for(int i=0;i<ns.getLength();i++){
				Node node=ns.item(i);
				String profile=node.getTextContent();
				activeProfiles.add(profile);
			}
		}
	}
	
	protected void parseRemoteRepositoryUrls()throws Exception {
		Set<String> mirrorUrls=new LinkedHashSet<String>();
		Set<String> urls=new LinkedHashSet<String>();
		
		for(String profileId:activeProfiles){
			Profile profile=profiles.get(profileId);
			if(profile!=null){
				for(String id:profile.repositories.keySet()){
					String url=mirrors.get(id);
					if(url!=null && url.trim().length()>1){
						mirrorUrls.add(url.trim());
					}
					
					urls.add(profile.repositories.get(id));
				}
			}
		}
		
		mirrorUrls.addAll(urls);
		
		remoteRepositoryUrls.clear();
		remoteRepositoryUrls.addAll(mirrorUrls);
		remoteRepositoryUrls.add("https://repo1.maven.org/maven2");
	}
  
	static class Profile {
		private String id;
		private CaseInsensitiveMap<String> repositories;
		
		public Profile(String id,CaseInsensitiveMap<String> repositories){
			this.id=id;
			this.repositories=repositories;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public CaseInsensitiveMap<String> getRepositories() {
			return repositories;
		}

		public void setRepositories(CaseInsensitiveMap<String> repositories) {
			this.repositories = repositories;
		}
	}
}
