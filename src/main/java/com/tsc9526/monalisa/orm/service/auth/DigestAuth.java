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
package com.tsc9526.monalisa.orm.service.auth;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.actions.Action;
import com.tsc9526.monalisa.orm.service.actions.ActionFilter;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.helper.Helper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DigestAuth implements ActionFilter {
	static Logger logger=Logger.getLogger(DigestAuth.class);
	
	public final static String SESSION_KEY_AUTH_USERNAME ="monalisa.auth.username";
	public final static String SESSION_KEY_AUTH_NONCE    ="monalisa.auth.nonce";
	
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
		
		String keyAuthUsername=SESSION_KEY_AUTH_USERNAME+":"+dbname;
		String keyAuthNonce   =SESSION_KEY_AUTH_NONCE   +":"+dbname;
		
		HttpSession session=args.getReq().getSession();
		String authUsername=(String)session.getAttribute(keyAuthUsername);
		if(authUsername==null){
			String authrization=args.getReq().getHeader("Authorization");
			String nonce       =(String)session.getAttribute(keyAuthNonce);
			GigestAuthrization ga=new GigestAuthrization(args.getReq().getMethod(),authrization);
			
			if(isAuthOk(ga,nonce)){
				logger.info("Auth ok: "+ga.username+", dbname: "+dbname);
				
				session.setAttribute(keyAuthUsername, ga.username);
				session.removeAttribute(keyAuthNonce);
				
				args.setAuthType(this.getClass().getName());
				args.setAuthUsername(ga.username);
				
				return null;
			}else{
				byte[] bytes=new byte[8];
				new Random().nextBytes(bytes);
				nonce=Base64.getUrlEncoder().encodeToString(bytes);
				
				session.setAttribute(keyAuthNonce, nonce);
				
				return new AuthResponse(nonce); 
			}
		}else{
			args.setAuthUsername(authUsername);
			
			return null;
		}
	}

	protected boolean isAuthOk(GigestAuthrization ga,String nonce){
		if(nonce!=null && ga.digest){
			String realm="Realm";
			String pwd=userAuths.getString(ga.username);
			if(pwd!=null){
				//If the algorithm directive's value is "MD5" or unspecified, then HA1 is
				String HA1=Helper.MD5(ga.username+":"+realm+":"+pwd);
				
				//If the qop directive's value is "auth" or is unspecified, then HA2 is
				String HA2=Helper.MD5(ga.method+":"+ga.uri);
				
				//If the qop directive's value is "auth" or "auth-int", then compute the response as follows:
				//response=MD5(HA1:nonce:nonceCount:cnonce:qop:HA2)
				String response=Helper.MD5(HA1+":"+nonce+":"+ga.nc+":"+ga.cnonce+":"+ga.qop+":"+HA2);
				 
				return response.equalsIgnoreCase(ga.response);
			}
		}
		 
		return false;
	}
		
	
	class GigestAuthrization{
		boolean digest  =false;
		String method;
		String username ;
		String realm    ;
		String nonce    ;
		String uri      ;
		String cnonce   ;
		String nc       ;
		String qop      ;
		String response ;
		
		private DataMap as=new DataMap();
		
		GigestAuthrization(String method,String authrization){
			this.method=method;
			
			if(authrization!=null && authrization.startsWith("Digest")){
				digest=true;
				
				authrization=authrization.substring("Digest".length()).trim();
				for(String x:authrization.split(",")){
					x=x.trim();
					
					int p=x.indexOf("=");
					if(p>0){
						String name =x.substring(0,p).trim();
						String value=x.substring(p+1).trim();
						if(value.startsWith("\"")){
							value=value.substring(1,value.length()-1);
						}
						as.put(name, value);
					}
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
	}
	
	class AuthResponse extends Response{
		private static final long serialVersionUID = 1L;
		
		String nonce;
		
		AuthResponse(String nonce){
			super(Response.REQUEST_UNAUTHORIZED, "Full authentication is required to access this resource.");
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
