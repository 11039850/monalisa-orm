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
package test.com.tsc9526.monalisa.service.servlet;
 
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.TestConstants;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.service.DBS;
import com.tsc9526.monalisa.service.actions.ActionExecutor;
import com.tsc9526.monalisa.service.auth.DigestAuth;
import com.tsc9526.monalisa.service.servlet.DbWebContainerInitializer;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
@SuppressWarnings("unchecked")
public class DbWebContainerInitializerTest {
	static Logger logger=Logger.getLogger(DbWebContainerInitializerTest.class);
	
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		dbs="dbs101",
		dbsAuthUsers="name1010:pwd1010|name1011:pwd1011")
	private interface TestDB001Service {}
	
	public void testServlet1()throws Exception{
		String dbsname="dbs101";
		 		
		DBS.remove(dbsname);
		Assert.assertNull(DBS.getDB(dbsname));
		
		DbWebContainerInitializer web=new DbWebContainerInitializer();
		
		MockServletContainer mock=new MockServletContainer();
		ServletContext servletContext=mock.createServletContext();
		Set<Class<?>> dbAnnotationClasses=new HashSet<Class<?>>();
		dbAnnotationClasses.add(TestDB001Service.class);
		
		web.onStartup(dbAnnotationClasses, servletContext);
		Assert.assertNotNull(mock.listenerClass);
		Assert.assertNotNull(mock.servlet);
		mock.servlet.init(mock.servletConfig); 
	 	
		
		DBS dbs=DBS.getDB(dbsname);
		Assert.assertNotNull(dbs);
		
		ActionExecutor executor=dbs.getExecutor();
		DigestAuth auth=(DigestAuth)executor.getFilters().get(0);
		DataMap userAuths=auth.getUserAuths();
		Assert.assertEquals(userAuths.size(),2);
		Assert.assertEquals(userAuths.get("name1010"),"pwd1010");
		Assert.assertEquals(userAuths.get("name1011"),"pwd1011");
		 
	}
	
	
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		dbs="dbs201",
		dbsAuthUsers="name2010:pwd2010|name2011:pwd2011")
	private interface TestDB101Service {}
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		dbs="dbs202",
		dbsAuthUsers="name2020:pwd2020|name2021:pwd2021")
	private interface TestDB102Service {}
	
	public void testServlet2()throws Exception{
		String dbsname1="dbs201",dbsname2="dbs202";
		 		
		DBS.remove(dbsname1);
		DBS.remove(dbsname2);
		Assert.assertNull(DBS.getDB(dbsname1));
		Assert.assertNull(DBS.getDB(dbsname2));
		
		DbWebContainerInitializer web=new DbWebContainerInitializer();
		
		MockServletContainer mock=new MockServletContainer();
		ServletContext servletContext=mock.createServletContext();
		Set<Class<?>> dbAnnotationClasses=new HashSet<Class<?>>();
		dbAnnotationClasses.add(TestDB101Service.class);
		dbAnnotationClasses.add(TestDB102Service.class);
		
		web.onStartup(dbAnnotationClasses, servletContext);
		Assert.assertNotNull(mock.listenerClass);
		Assert.assertNotNull(mock.servlet);
		mock.servlet.init(mock.servletConfig); 
	 	
		
		DBS dbs1=DBS.getDB(dbsname1);
		Assert.assertNotNull(dbs1);
		
		ActionExecutor executor1=dbs1.getExecutor();
		DigestAuth auth1=(DigestAuth)executor1.getFilters().get(0);
		DataMap userAuths1=auth1.getUserAuths();
		Assert.assertEquals(userAuths1.size(),2);
		Assert.assertEquals(userAuths1.get("name2010"),"pwd2010");
		Assert.assertEquals(userAuths1.get("name2011"),"pwd2011");
		
		
		DBS dbs2=DBS.getDB(dbsname2);
		Assert.assertNotNull(dbs2);
		
		ActionExecutor executor2=dbs2.getExecutor();
		DigestAuth auth2=(DigestAuth)executor2.getFilters().get(0);
		DataMap userAuths2=auth2.getUserAuths();
		Assert.assertEquals(userAuths2.size(),2);
		Assert.assertEquals(userAuths2.get("name2020"),"pwd2020");
		Assert.assertEquals(userAuths2.get("name2021"),"pwd2021");
		 
	}	
	
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		configFile="src/test/resources/dbconfig/TestDB30xService.cfg",
		configName="db301")
	private interface TestDB301Service {}
	
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		configFile="src/test/resources/dbconfig/TestDB30xService.cfg",
		configName="db302")
	private interface TestDB302Service {}
	 
	@DB(url=TestConstants.url, 
		username=TestConstants.username, 
		password=TestConstants.password,
		configFile="src/test/resources/dbconfig/TestDB30xService.cfg",
		configName="db303")
	private interface TestDB303Service {}
	
	public void testServlet3()throws Exception{
		String dbsname1="dbs301",dbsname2="dbs302";
 		
		DBS.remove(dbsname1);
		DBS.remove(dbsname2);
		Assert.assertNull(DBS.getDB(dbsname1));
		Assert.assertNull(DBS.getDB(dbsname2));
		
		DbWebContainerInitializer web=new DbWebContainerInitializer();
		
		MockServletContainer mock=new MockServletContainer();
		ServletContext servletContext=mock.createServletContext();
		Set<Class<?>> dbAnnotationClasses=new HashSet<Class<?>>();
		dbAnnotationClasses.add(TestDB301Service.class);
		dbAnnotationClasses.add(TestDB302Service.class);
		
		web.onStartup(dbAnnotationClasses, servletContext);
		Assert.assertNotNull(mock.listenerClass);
		Assert.assertNotNull(mock.servlet);
		mock.servlet.init(mock.servletConfig); 
	 	
		
		DBS dbs1=DBS.getDB(dbsname1);
		Assert.assertNotNull(dbs1);
		
		ActionExecutor executor1=dbs1.getExecutor();
		Assert.assertEquals(executor1.getFilters().size(),2);
		Assert.assertEquals(executor1.getFilters().get(1).getClass().getName(),LoggerActionFilter.class.getName());
		
		DigestAuth auth1=(DigestAuth)executor1.getFilters().get(0);
		DataMap userAuths1=auth1.getUserAuths();
		Assert.assertEquals(userAuths1.size(),2);
		Assert.assertEquals(userAuths1.get("name3010"),"pwd3010");
		Assert.assertEquals(userAuths1.get("name3011"),"pwd3011");
		
		
		DBS dbs2=DBS.getDB(dbsname2);
		Assert.assertNotNull(dbs2);
		
		ActionExecutor executor2=dbs2.getExecutor();
		Assert.assertEquals(executor2.getFilters().size(),1);
		Assert.assertEquals(executor2.getFilters().get(0).getClass().getName(),LoggerActionFilter.class.getName());
	}
	
	private class MockServletContainer{
		Set<String> servletMappings=new HashSet<String>();
		Class<? extends EventListener> listenerClass=null; 	
		MockServletConfig servletConfig=null;
		Servlet servlet=null;
		ServletRegistration.Dynamic dynamic=createServletRegistration();
		
		private ServletContext createServletContext(){
			
			final MockServletContext sc = Mockito.spy(new MockServletContext(){
				public void addListener(Class<? extends EventListener> listenerClass) {
					MockServletContainer.this.listenerClass=listenerClass;
				}
				
				public ServletRegistration.Dynamic addServlet(String name,Servlet servlet) {
					MockServletContainer.this.servlet=servlet;
					return dynamic;
				}
			});
		 	
			servletConfig=new MockServletConfig(sc,DbWebContainerInitializer.DBS_SERVLET_NAME);
			
			sc.setContextPath("");
			
			return sc;
		}
		
		private ServletRegistration.Dynamic createServletRegistration(){
			ServletRegistration.Dynamic dynamic=mock(ServletRegistration.Dynamic.class);
			
			when(dynamic.addMapping(any(String[].class))).thenAnswer(new Answer<Set<String>>() {
				public Set<String> answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments(); 
					for(Object x:args){
						servletMappings.add((String)x);
					}
					return servletMappings;
				}
			});
			
			when(dynamic.setInitParameter(any(String.class), any(String.class))).thenAnswer(new Answer<Boolean>() {
				public Boolean answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments(); 
					servletConfig.addInitParameter((String)args[0],(String)args[1]);
					return true;
				}
			});
			
			when(dynamic.setInitParameters(any(Map.class))).thenAnswer(new Answer<Set<String>>() {
				public Set<String> answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments(); 
					Map<String,String> params=(Map<String,String>)args[0];
					for(String key:params.keySet()){
						servletConfig.addInitParameter(key, params.get(key));
					}
					return new HashSet<String>();
				}
			});
		 	 
		 	
			return dynamic;
		}
	}
}
