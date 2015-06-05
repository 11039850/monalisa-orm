package com.tsc9526.monalisa.plugin.eclipse.generator;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.generator.DBGenerator;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JavaWriter;
import com.tsc9526.monalisa.plugin.eclipse.jdt.JDTCompiler;

public class SelectRun {
	public final static String RUN_PACKAGE="sql.select";
	public final static String RUN_NAME   ="Run";
	public final static String RUN_CLASS  =RUN_PACKAGE+"."+RUN_NAME;		
	 	
	private SourceUnit unit;
 
	public SelectRun(SourceUnit unit){
		this.unit=unit;
	}
	 
	
	public List<DBExchange> run(List<SelectMethod> methods) {		
		String runCode=createRunJavaCode(methods);
		 
		String[] classPath=compile(runCode);
		
		return runWithClassloader(classPath);		 	                 		 		
	}	
	
	private String[] compile(String runCode){
		String dirRoot=FileHelper.combinePath(unit.getProjectPath(),DBGenerator.PROJECT_TMP_PATH);
		 
		String[] classpath=Helper.combinePaths(unit.getRuntimeClasspath(),unit.getPluginClasspath());
		JDTCompiler compiler=new JDTCompiler(dirRoot,classpath);
		
		compiler.clean();
		
		compiler.compile(unit.getPackageName()+"."+unit.getUnitName(),unit.getJavaCode());
		
		compiler.compile(RUN_CLASS,runCode);		
		
		String[] classPath=Helper.combinePaths(compiler.getClasspaths(), unit.getRuntimeClasspath(),unit.getPluginClasspath());
		
		return classPath;
	}
	
	private String createRunJavaCode(List<SelectMethod> methods){
		JavaWriter runWriter=JavaWriter.getBufferedWriter();
		runWriter.println("package "+RUN_PACKAGE+";");		
		runWriter.println("import "+unit.getPackageName()+".*;");
		runWriter.println("import "+DBExchange.class.getName()+";");
		for(String i:unit.getImports().values()){
			runWriter.println("import "+i+";");
		}		
		runWriter.println("public class "+RUN_NAME+"{");
		
		String path=unit.getProjectPath();
		runWriter.println("static{");
		runWriter.println(DBConfig.class.getName()+".DEFAULT_PATH=\""+path+"\";");
		runWriter.println("}");
		
		for(SelectMethod sm:methods){
			sm.writeRunMethod(runWriter);
		}
		runWriter.println("}");
		
		return runWriter.getContent();
	}

	private List<DBExchange> runWithClassloader(String[] classPath) {
		URLClassLoader loader=null;
		try {
			List<DBExchange> exchanges = new ArrayList<DBExchange>();
			List<URL> urls = new ArrayList<URL>();
			 
			for (String x : classPath) {
				File file = new File(x);
				if(file.exists()){
					urls.add(file.toURI().toURL());
				}
			}

			loader = new URLClassLoader(urls.toArray(new URL[0]));
			Class<?> runClass = loader.loadClass(RUN_CLASS);
			Object run = runClass.newInstance();
			for (Method m : runClass.getMethods()) {
				if (OBJECT_METHODS.contains(m.getName()) == false) {
					m.invoke(run);

					DBExchange exchange = DBExchange.getExchange();
					
					exchanges.add(exchange);
				}
			}						
			return exchanges;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(loader);
		}
	}
 
	
	
	public static Set<String> OBJECT_METHODS=new HashSet<String>(){		 
		private static final long serialVersionUID = -4949935939426517392L;
		{
			add("equals");
			add("getClass");
			add("hashCode");
			add("notify");
			add("notifyAll");
			add("toString");
			add("wait");
		}
	};

	
}
