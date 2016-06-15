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
package test.com.tsc9526.monalisa.core.tools;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.ClassPathHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ClassPathHelperTest {

	public void testSplitClassPaths(){
		List<String> rs=ClassPathHelper.splitClassPaths("c:\\a\\a.jar;c:\\b\\b.jar");
		Assert.assertEquals(rs.size(), 2);
		Assert.assertEquals(rs.get(0), "c:\\a\\a.jar");
		Assert.assertEquals(rs.get(1), "c:\\b\\b.jar");
		
		
		rs=ClassPathHelper.splitClassPaths("c:\\a\\a.jar");
		Assert.assertEquals(rs.size(), 1);
		Assert.assertEquals(rs.get(0), "c:\\a\\a.jar");
		
		rs=ClassPathHelper.splitClassPaths("/tmp/a/a.jar:/tmp/b/b.jar");
		Assert.assertEquals(rs.size(), 2);
		Assert.assertEquals(rs.get(0), "/tmp/a/a.jar");
		Assert.assertEquals(rs.get(1), "/tmp/b/b.jar");
		
		rs=ClassPathHelper.splitClassPaths("/tmp/a/a.jar");
		Assert.assertEquals(rs.size(), 1);
		Assert.assertEquals(rs.get(0), "/tmp/a/a.jar");
	}
	
	public void testJarClass()throws Exception {
		File f=ClassPathHelper.getClassOrJarFile(Mockito.class);
		
		assertTrue(f.getName().endsWith(".jar"));
		assertTrue(f.exists());
	}
	
	public void testFileClass()throws Exception {
		File f=ClassPathHelper.getClassOrJarFile(Query.class);
		
		assertTrue(f.getName().endsWith(".class"));
		assertTrue(f.exists());
	}
	
	public void testLinuxUrl()throws Exception{
		URL url=new URL("jar:file:/root/demo/lib/Simple-0.0.1-SNAPSHOT.jar!/example/monalisa/SqlResult.class");
		
		String path=ClassPathHelper.getFilePathfromResourceUrl(url);
		Assert.assertEquals(path, "/root/demo/lib/Simple-0.0.1-SNAPSHOT.jar");
	}
	
	public void testWindowsUrl()throws Exception{
		URL url=new URL("jar:file:/D:/root/demo/lib/Simple-0.0.1-SNAPSHOT.jar!/example/monalisa/SqlResult.class");
		
		String path=ClassPathHelper.getFilePathfromResourceUrl(url);
		Assert.assertEquals(path, "D:/root/demo/lib/Simple-0.0.1-SNAPSHOT.jar");
	}
	
	public void testFolderUrl()throws Exception{
		File f=new File("src/test/resources/lab/demo.java");
		 
		String path=ClassPathHelper.getFilePathfromResourceUrl(f.toURI().toURL());
		
		String expect=f.getAbsolutePath().replace("\\","/");
		 
		Assert.assertEquals(path, expect);
	}
}
