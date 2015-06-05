package com.tsc9526.monalisa.plugin.eclipse.console;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;

public class HyperLink implements IHyperlink {
	private String text;
	private URL url;

	public HyperLink(String urlStr) {
		this(urlStr, urlStr);
	}

	public HyperLink(String urlStr, String text) {
		this.text = text;
		
		if(urlStr!=null && urlStr.length()>0){
			try {
				this.url = new URL(urlStr);				
			} catch (MalformedURLException e) {
				e.printStackTrace();				 
			}
		}
	}
	
	public URL getUrl(){
		return this.url;
	}

	public String getText() {
		return text;
	}
 
	@Override
	public void linkActivated() {
		if (url != null) {
			try {
				String protocol=url.getProtocol();
				if(protocol.equals("file")){
					String ref=url.getRef();
					
					IFile[] files= ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(url.toURI());
					if (files.length > 0) {
						for (int i = 0; i < files.length; i++) {
							IFile curr= files[0];
							IJavaElement element= JavaCore.create(curr);
							if (element != null && element.exists() ) {															
								if(ref!=null && ref.length()>0 && element instanceof ICompilationUnit){											 
									String[] sv=ref.split(",");
									String name=sv[0];
									
									String[] signatures=null;
									if(sv.length>1){
										signatures=new String[sv.length-1];
										for(int x=1;x<sv.length;x++){
											signatures[x-1]= Signature.createTypeSignature(sv[x],false);
										}
									}
									
									IType type=((ICompilationUnit)element).getTypes()[0];
									IMethod method=type.getMethod(name, signatures);
									if(method!=null && method.exists()){
										JavaUI.openInEditor(method, true, true);	
										return;
									}
								}
								
								JavaUI.openInEditor(element, true, true);								 
								return;
							}
						}
					}
				}
				
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser(text).openURL(url);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void linkEntered() {
		 
	}

	@Override
	public void linkExited() {
		 
	}
}