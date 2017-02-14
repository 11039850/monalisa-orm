package com.tsc9526.monalisa.tools.agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.tsc9526.monalisa.tools.clazz.MelpClasspath;
import com.tsc9526.monalisa.tools.clazz.PkgNames;

@SuppressWarnings("unchecked")
public class AgentJar {
	
	private volatile static AgentLoaderInterface agentLoader;
 
	public static void loadAgent(String agentJar, String options) {
		AgentLoaderInterface agentLoader = getAgentLoader(agentJar);
		agentLoader.loadAgent(agentJar, options);
	}

	public static void loadAgentClass(String agentClassName, String options) {
		loadAgentClass(agentClassName, options, null, true, true, false);
	}

	public static void loadAgentClass(String agentClass, String options, String bootClassPath, boolean canRedefineClasses,
			boolean canRetransformClasses, boolean canSetNativeMethodPrefix) {
		try {
			File jarFile = createAgentJar(agentClass, bootClassPath, canRedefineClasses, canRetransformClasses, canSetNativeMethodPrefix);
			loadAgent(jarFile.getPath(), options);
		} catch (IOException ex) {
			throw new RuntimeException("Can't write jar file for agent:" + agentClass, ex);
		}
	}

	private synchronized static AgentLoaderInterface getAgentLoader(String agentJar) {
		if (agentLoader == null) {
			Class<AgentLoaderInterface> agentLoaderClass=loadAgentLoaderClass();
		 	try {
				final Object agentLoaderObject = agentLoaderClass.newInstance();
	  
				agentLoader = new AgentLoaderInterface() {
					public void loadAgent(String agentJar, String options) {
						try {
							Method loadAgentMethod = agentLoaderObject.getClass().getMethod("loadAgent", String.class, String.class);
							loadAgentMethod.invoke(agentLoaderObject, agentJar, options);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				};
			} catch (Exception e) {
				throw new RuntimeException("Error getting agent loader implementation to load: " + agentJar, e);
			}
		}
		return agentLoader;
	}
	
	private static Class<AgentLoaderInterface> loadAgentLoaderClass(){
		try {
			Class.forName(PkgNames.libVirtualMachineClass);
			return (Class<AgentLoaderInterface>) Class.forName(PkgNames.ORM_AgentHotSpotVM);
		} catch (Exception ex) {
			ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
			
			try {
				loadClassFromStream(systemLoader,AgentJar.class.getResourceAsStream("/sun_vm.jar"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			try {
				String agentLoaderClassResource = "/" + PkgNames.ORM_AgentHotSpotVM.replace(".", "/") + ".class";
				return MelpClasspath.defineClass(systemLoader, AgentJar.class.getResourceAsStream(agentLoaderClassResource));
			} catch (Exception e) {
				throw new RuntimeException("Error loading AgentLoader implementation", e);
			}
		}
	}

	public static void loadClassFromStream(ClassLoader loader,InputStream zipStream)throws IOException{  
		ZipInputStream zip=new ZipInputStream(zipStream);
		
		ZipEntry entry;
		byte[] buffer = new byte[16*1024];
		
		Map<String, byte[]> hClasses=new LinkedHashMap<String, byte[]>();
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		while ((entry = zip.getNextEntry()) != null) {
			String pathName=entry.getName();
			  
        	 bos.reset();
             int size;
             while ((size = zip.read(buffer)) != -1) {
                 bos.write(buffer, 0, size);
             }
             zip.closeEntry();
             
             if(!entry.isDirectory() && pathName.endsWith(".class")){
            	 String className=pathName.replace("/",".");
            	 className=className.substring(0,className.length()-6);
            	 hClasses.put(className, bos.toByteArray());
            	
             }
         }
         zip.close();

         MelpClasspath.defineClasses(loader,hClasses);
	}
 
	 
	private static File createAgentJar(String agentClass, String bootClassPath, boolean canRedefineClasses, boolean canRetransformClasses,
			boolean canSetNativeMethodPrefix) throws IOException {
		File jarFile = File.createTempFile("agent." + agentClass, ".jar");

		createAgentJar(new FileOutputStream(jarFile), agentClass, bootClassPath, canRedefineClasses, canRetransformClasses, canSetNativeMethodPrefix);

		return jarFile;
	}

	private static void createAgentJar(OutputStream out, String agentClass, String bootClassPath, boolean canRedefineClasses, boolean canRetransformClasses,
			boolean canSetNativeMethodPrefix) throws IOException {
		Manifest man = new Manifest();
		man.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		man.getMainAttributes().putValue("Premain-Class", agentClass);
		man.getMainAttributes().putValue("Agent-Class", agentClass);
		if (bootClassPath != null) {
			man.getMainAttributes().putValue("Boot-Class-Path", bootClassPath);
		}
		man.getMainAttributes().putValue("Can-Redefine-Classes", Boolean.toString(canRedefineClasses));
		man.getMainAttributes().putValue("Can-Retransform-Classes", Boolean.toString(canRetransformClasses));
		man.getMainAttributes().putValue("Can-Set-Native-Method-Prefix", Boolean.toString(canSetNativeMethodPrefix));

		JarOutputStream jarOut = new JarOutputStream(out, man);
		jarOut.flush();
		jarOut.close();
	}

	interface AgentLoaderInterface {
		void loadAgent(String agentJar, String options);
	}
}
