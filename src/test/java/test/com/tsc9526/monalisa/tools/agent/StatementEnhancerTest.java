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
package test.com.tsc9526.monalisa.tools.agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.StatementEnhancer;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class StatementEnhancerTest {

	public void testCreateStatement() throws Exception {
		String sql = "SELECT * FROM _THIS_TABLE";
		 
//		Class<?> proxyClazz = StatementEnhancer.createClass(SimpleStatement.class);
//		SimpleStatement s1=(SimpleStatement)proxyClazz.newInstance();
//		((Factory) s1).setCallbacks(new Callback[] { new StatementEnhancer.Interceptor() }); 
//		ResultSet rs=s1.executeQuery(sql); 
//		System.out.println(rs + ":" + proxyClazz);
//		
//		
//		SimpleStatement s2=(SimpleStatement)proxyClazz.getConstructor(String.class).newInstance("zzg");
//		
//		System.out.println(s2 + ":" + proxyClazz);
	}

	public static class SimpleStatement implements Statement {
		SimpleStatement(){}
		
		SimpleStatement(String name){}
		
		public <T> T unwrap(Class<T> iface) throws SQLException {

			return null;
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {

			return false;
		}

		public ResultSet executeQuery(final String sql) throws SQLException {
			return (ResultSet)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{ResultSet.class},
				new InvocationHandler(){
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if(MelpClass.OBJECT_METHODS.contains(method.getName())){
							return method.invoke(this, args);
						}else{
							return null;
						}
					}
			});
			
		}

		public int executeUpdate(String sql) throws SQLException {

			return 0;
		}

		public void close() throws SQLException {

		}

		public int getMaxFieldSize() throws SQLException {

			return 0;
		}

		public void setMaxFieldSize(int max) throws SQLException {

		}

		public int getMaxRows() throws SQLException {

			return 0;
		}

		public void setMaxRows(int max) throws SQLException {

		}

		public void setEscapeProcessing(boolean enable) throws SQLException {

		}

		public int getQueryTimeout() throws SQLException {

			return 0;
		}

		public void setQueryTimeout(int seconds) throws SQLException {

		}

		public void cancel() throws SQLException {

		}

		public SQLWarning getWarnings() throws SQLException {

			return null;
		}

		public void clearWarnings() throws SQLException {

		}

		public void setCursorName(String name) throws SQLException {

		}

		public boolean execute(String sql) throws SQLException {

			return false;
		}

		public ResultSet getResultSet() throws SQLException {

			return null;
		}

		public int getUpdateCount() throws SQLException {

			return 0;
		}

		public boolean getMoreResults() throws SQLException {

			return false;
		}

		public void setFetchDirection(int direction) throws SQLException {

		}

		public int getFetchDirection() throws SQLException {

			return 0;
		}

		public void setFetchSize(int rows) throws SQLException {

		}

		public int getFetchSize() throws SQLException {

			return 0;
		}

		public int getResultSetConcurrency() throws SQLException {

			return 0;
		}

		public int getResultSetType() throws SQLException {

			return 0;
		}

		public void addBatch(String sql) throws SQLException {

		}

		public void clearBatch() throws SQLException {

		}

		public int[] executeBatch() throws SQLException {

			return null;
		}

		public Connection getConnection() throws SQLException {

			return null;
		}

		public boolean getMoreResults(int current) throws SQLException {

			return false;
		}

		public ResultSet getGeneratedKeys() throws SQLException {

			return null;
		}

		public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {

			return 0;
		}

		public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {

			return 0;
		}

		public int executeUpdate(String sql, String[] columnNames) throws SQLException {

			return 0;
		}

		public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {

			return false;
		}

		public boolean execute(String sql, int[] columnIndexes) throws SQLException {

			return false;
		}

		public boolean execute(String sql, String[] columnNames) throws SQLException {

			return false;
		}

		public int getResultSetHoldability() throws SQLException {

			return 0;
		}

		public boolean isClosed() throws SQLException {

			return false;
		}

		public void setPoolable(boolean poolable) throws SQLException {

		}

		public boolean isPoolable() throws SQLException {

			return false;
		}

		public void closeOnCompletion() throws SQLException {

		}

		public boolean isCloseOnCompletion() throws SQLException {

			return false;
		}

	}
}
