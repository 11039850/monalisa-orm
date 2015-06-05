package com.tsc9526.monalisa.plugin.eclipse.jdt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.Helper;

@SuppressWarnings("restriction")
public class JDTCompiler {
	private String dirSources;
	private String dirClasses;
 	 
	private String[] classPaths;
	 
	
	private CompilerOptions options= getCompilerOptions();
	private IProblemFactory problemFactory = new DefaultProblemFactory(Locale.ENGLISH);
	private IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.exitOnFirstError();
	
	public JDTCompiler(String dirRoot,String[] classPaths){
		this(dirRoot+"/sources", dirRoot+"/classes",classPaths);
		
	}
	
	public JDTCompiler(String dirSources,String dirClasses,String[] classPaths){				
		this.dirSources=dirSources.replace('\\','/');
		this.dirClasses=dirClasses.replace('\\','/');	
		
		this.classPaths=Helper.combinePaths(new String[]{dirClasses},classPaths);
	}
 		 
	
	public void compile(String clazzName,String source) {
		File file=new File(dirSources+"/"+clazzName.replace(".", "/") + ".java");
		FileHelper.write(file,source.getBytes());
		compile(file);
	}
	
	public void compile(File java) {		 
		INameEnvironment nameEnvironment = new FileSystem(classPaths, new String[]{java.getAbsolutePath()}, "UTF-8");
			 
		Compiler jdtCompiler = new Compiler(nameEnvironment, policy,options, compilerRequestor, problemFactory);
				
		jdtCompiler.compile(new ICompilationUnit[] { new CompilationUnit(java) });				
	}
	
	
	public String[] getClasspaths(){
		return new String[]{dirClasses};
	}
	
	public void clean(){
		FileHelper.delete(new File(dirSources),false);
		FileHelper.delete(new File(dirClasses),false);
	}
	 
	 

	public CompilerOptions getCompilerOptions() {
		Map<String, String> settings = new HashMap<String, String>();
		settings.put(CompilerOptions.OPTION_ReportMissingSerialVersion, CompilerOptions.IGNORE);
		settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
		settings.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.IGNORE);
		settings.put(CompilerOptions.OPTION_Encoding, "UTF-8");
		settings.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
		String javaVersion = CompilerOptions.VERSION_1_6;

		if (System.getProperty("java.version").startsWith("1.5")) {
			javaVersion = CompilerOptions.VERSION_1_5;
		} else if (System.getProperty("java.version").startsWith("1.7")) {
			javaVersion = CompilerOptions.VERSION_1_7;
		}

		settings.put(CompilerOptions.OPTION_Source, javaVersion);
		settings.put(CompilerOptions.OPTION_TargetPlatform, javaVersion);
		settings.put(CompilerOptions.OPTION_PreserveUnusedLocal, CompilerOptions.PRESERVE);
		settings.put(CompilerOptions.OPTION_Compliance, javaVersion);
		return new CompilerOptions(settings);
	}

	private  String readFileToString(File f) throws IOException {
		FileInputStream fin = new FileInputStream(f);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[16 * 1024];
		int len = fin.read(buf);
		while (len > 0) {
			bos.write(buf, 0, len);

			len = fin.read(buf);
		}
		fin.close();

		return new String(bos.toByteArray(), "UTF-8");
	}
	
	private ICompilerRequestor compilerRequestor = new ICompilerRequestor() {
		public void acceptResult(CompilationResult result) {
			// If error
			if (result.hasErrors()) {
				for (IProblem problem : result.getErrors()) {
					String className = new String(problem.getOriginatingFileName()).replace("/", ".");
					className = className.substring(0, className.length() - 5);
					String message = problem.getMessage();
					if (problem.getID() == IProblem.CannotImportPackage) {
						// Non sense !
						message = problem.getArguments()[0] + " cannot be resolved";
					}
					throw new RuntimeException(className + ":" + message);
				}
			}
			// Something has been compiled
			ClassFile[] clazzFiles = result.getClassFiles();
			for (int i = 0; i < clazzFiles.length; i++) {
				String clazzName = join(clazzFiles[i].getCompoundName());
				// save to disk as .class file
				File target = new File(dirClasses+"/"+clazzName.replace(".", "/") + ".class");
				FileHelper.write(target, clazzFiles[i].getBytes());				 
			}
		}
	};
	
	

	private class CompilationUnit implements ICompilationUnit {
		private File file;

		public CompilationUnit(File file) {
			this.file = file;
		}

		@Override
		public char[] getContents() {
			try {
				return readFileToString(file).toCharArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public char[] getMainTypeName() {
			return file.getName().replace(".java", "").toCharArray();
		}

		@Override
		public char[][] getPackageName() {
			String fullPkgName = file.getParentFile().getAbsolutePath().replace("\\", "/").replace(dirSources, "");
			fullPkgName = fullPkgName.replace("/", ".");
			if (fullPkgName.startsWith("."))
				fullPkgName = fullPkgName.substring(1);
			String[] items = fullPkgName.split("[.]");
			char[][] pkgName = new char[items.length][];
			for (int i = 0; i < items.length; i++) {
				pkgName[i] = items[i].toCharArray();
			}
			return pkgName;
		}

		@Override
		public boolean ignoreOptionalProblems() {
			return false;
		}

		@Override
		public char[] getFileName() {
			return this.file.getName().toCharArray();
		}
	}

	private  String join(char[][] chars) {
		StringBuilder sb = new StringBuilder();
		for (char[] item : chars) {
			if (sb.length() > 0) {
				sb.append(".");
			}
			sb.append(item);
		}
		return sb.toString();
	}
}
