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
package com.tsc9526.monalisa.service.auth;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.actions.Action;
import com.tsc9526.monalisa.service.actions.ActionFilter;
import com.tsc9526.monalisa.service.args.ModelArgs;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpCodec;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DigestAuth implements ActionFilter {
	static Logger logger=Logger.getLogger(DigestAuth.class);
	
	public final static String SESSION_KEY_AUTH_USER ="monalisa.auth.user"; 
	public final static String SESSION_KEY_AUTH_NONCE="monalisa.auth.nonce";
	 	
	public static Pattern authrizationPattern=Pattern.compile(""+/**~!{*/""
		+ "[0-9a-zA-Z_]+\\s*=\\s*((\"[^\"]*\")|([0-9a-zA-Z_]+))"
	+ "\r\n"/**}*/.trim());
		
	protected DataMap userAuths=new DataMap();
	
	public DigestAuth(List<String[]> userpwds){
		for(String[] uv:userpwds){
			userAuths.put(uv[0].trim(),uv[1].trim());
		}
	}
	
	public boolean accept(Action action){
		return true;
	}

	public Response doFilter(Action action) {
		ModelArgs args=action.getActionArgs();
		
		String dbname=args.getDBS().getDbName();
		
		String keyAuthUser  =SESSION_KEY_AUTH_USER+":"+dbname;
		String keyAuthNonce =SESSION_KEY_AUTH_NONCE   +":"+dbname;
		
		HttpSession session=args.getReq().getSession();
		AuthUser authUser=(AuthUser)session.getAttribute(keyAuthUser);
		if(authUser!=null && userAuths.getString(authUser.username,"").equals(authUser.password)){
			args.setAuthUsername(authUser.username);
			return null;
		}else{
			String authrization=args.getReq().getHeader("Authorization");
			String nonce       =(String)session.getAttribute(keyAuthNonce);
			GigestAuthrization ga=new GigestAuthrization(args.getReq().getMethod(),authrization);
			
			if(isAuthOk(ga,nonce)){
				logger.info("Auth ok: "+ga.username+", dbname: "+dbname);
				
				authUser=new AuthUser(ga.username,userAuths.getString(ga.username));
				session.setAttribute(keyAuthUser,authUser);
				session.removeAttribute(keyAuthNonce);
				
				args.setAuthType(this.getClass().getName());
				args.setAuthUsername(ga.username);
				
				return null;
			}else{
				AuthResponse r= getAuthResponse(); 
				nonce=r.nonce;
				
				session.setAttribute(keyAuthNonce, nonce);
				return r;
			}
		}
	}
	
	protected AuthResponse getAuthResponse(){
		byte[] bytes=new byte[8];
		new Random().nextBytes(bytes);
		String nonce=Base64.getUrlEncoder().encodeToString(bytes);
	  	return new AuthResponse(nonce); 
	}

	protected boolean isAuthOk(GigestAuthrization ga,String nonce){
		if(nonce!=null && ga.digest){
			String realm="Realm";
			String pwd=userAuths.getString(ga.username);
			if(pwd!=null){
				//If the algorithm directive's value is "MD5" or unspecified, then HA1 is
				String HA1=MelpCodec.MD5(ga.username+":"+realm+":"+pwd);
				
				//If the qop directive's value is "auth" or is unspecified, then HA2 is
				String HA2=MelpCodec.MD5(ga.method+":"+ga.uri);
				
				//If the qop directive's value is "auth" or "auth-int", then compute the response as follows:
				//response=MD5(HA1:nonce:nonceCount:cnonce:qop:HA2)
				String response=MelpCodec.MD5(HA1+":"+nonce+":"+ga.nc+":"+ga.cnonce+":"+ga.qop+":"+HA2);
				 
				return response.equalsIgnoreCase(ga.response);
			}
		}
		 
		return false;
	}
	
	class AuthUser{
		String username;
		String password;
		
		AuthUser(String username,String password){
			this.username=username;
			this.password=password;
		}
	}
	
	
	public static class GigestAuthrization{
		private boolean digest  =false;
		private String method;
		private String username ;
		private String realm    ;
		private String nonce    ;
		private String uri      ;
		private String cnonce   ;
		private String nc       ;
		private String qop      ;
		private String response ;
		
		private DataMap as=new DataMap();
		
		public GigestAuthrization(String method,String authrization){
			this.method=method;
			
			if(authrization!=null && authrization.startsWith("Digest")){
				digest=true;
				
				authrization=authrization.substring("Digest".length()).trim();
				 
				Matcher m=authrizationPattern.matcher(authrization);
				while(m.find()){
					String g=m.group();
					int p=g.indexOf("=");
					
					String name =g.substring(0,p).trim();
					String value=g.substring(p+1).trim();
					if(value.startsWith("\"")){
						value=value.substring(1,value.length()-1);
					}
					
					as.put(name, value);
				}
				
				username=as.getString("username");
				realm   =as.getString("realm");
				nonce   =as.getString("nonce");
				uri     =as.getString("uri");
				cnonce  =as.getString("cnonce");
				nc      =as.getString("nc");
				qop     =as.getString("qop");
				response=as.getString("response");
			}
		}

		public boolean isDigest() {
			return digest;
		}

		public void setDigest(boolean digest) {
			this.digest = digest;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getRealm() {
			return realm;
		}

		public void setRealm(String realm) {
			this.realm = realm;
		}

		public String getNonce() {
			return nonce;
		}

		public void setNonce(String nonce) {
			this.nonce = nonce;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getCnonce() {
			return cnonce;
		}

		public void setCnonce(String cnonce) {
			this.cnonce = cnonce;
		}

		public String getNc() {
			return nc;
		}

		public void setNc(String nc) {
			this.nc = nc;
		}

		public String getQop() {
			return qop;
		}

		public void setQop(String qop) {
			this.qop = qop;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public DataMap getAs() {
			return as;
		}

		public void setAs(DataMap as) {
			this.as = as;
		}
	}
	
	class AuthResponse extends Response{
		private static final long serialVersionUID = 1L;
		
		String nonce;
		
		AuthResponse(String nonce){
			super(Response.REQUEST_UNAUTHORIZED, "Authentication is required to access this resource, see system console for detail message!");
			this.nonce=nonce;
		}
		
		public void writeResponse(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
			String authenticate=""+/**~!{*/""
				+ "Digest realm=\"Realm\",qop=\"auth\",nonce=\"" +((nonce))+ "\",algorithm=\"MD5\""
			+ "\r\n"/**}*/.trim();
		 	
			resp.addHeader("WWW-Authenticate", authenticate);
			
			resp.sendError(this.status,this.message);	 
		} 
	}

}
