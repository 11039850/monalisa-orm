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
package com.tsc9526.monalisa.orm.tools.maven;

import java.io.File;
import java.io.FileFilter;

import com.tsc9526.monalisa.orm.tools.helper.FileHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class RepositoryClean {
	static Logger logger=Logger.getLogger(RepositoryClean.class);
	
	public static void main(String[] args) {
		boolean remove=true;
		
		if(args.length>0){
			remove="true".equalsIgnoreCase(args[0]);
		}
		
		RepositoryClean clean=new RepositoryClean(remove);
		clean.clean();
	}
	
	private int     removeCount=0;
	private boolean remove=true;
	
	public RepositoryClean(boolean remove){
		this.remove=remove;
	}
	
	public void clean(){
		Repository r=new Repository();
		String local=r.getLocalRepository();
		
		logger.info("Scan local repository [remove:"+remove+"] "+ local +" ...");
		 
		scan(new File(local));
		
		if(remove){
			logger.info("Finished, total remove: "+removeCount);
		}else{
			logger.info("Finished, total found: "+removeCount);
		}
	}
	
	public void scan(File dir){
		if(dir.isDirectory()){
			File[] includeDirs=dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			
			if(includeDirs!=null && includeDirs.length>0){
				for(File f:dir.listFiles()){
					if(f.isDirectory()){
						scan(f);
					}
				}
			}else{
				//artifact directory
				if(!check(dir)){
					remove(dir);	
				}
			}
		}
	}

	private void remove(File dir){
		removeCount++;
		if(remove){
			logger.info("Remove ["+removeCount+"]: "+dir.getAbsolutePath());
			FileHelper.delete(dir, true); 
		}else{
			logger.info("Found ["+removeCount+"]: "+dir.getAbsolutePath());
		}
	}

	private boolean check(File dir) {
		if(dir.getName().startsWith(".")){
			return true;
		}
		
		if(dir.listFiles().length==0){
			return false;
		}
	 	
		final String flag=".lastUpdated";
		File[] updates=dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String name=pathname.getName();
				return name.endsWith(flag);
			}
		});
		
		if(updates!=null && updates.length>0){
			for(File f:updates){
				String path=f.getAbsolutePath();
				path=path.substring(0,path.length()-flag.length());
				
				if(!new File(path).exists()){
					return false;
				}
			}
		}
		
		return true;
	}
		
}