package com.tsc9526.monalisa.plugin.eclipse.generator;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.swt.SWT;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

import com.tsc9526.monalisa.core.annotation.Select;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.plugin.eclipse.console.HyperLink;
import com.tsc9526.monalisa.plugin.eclipse.console.MMC;

/**
 * xxx {@value #X_STRING}
 */	
public class SelectGenerator implements SourceGenerator{		
	private SourceUnit unit;	 
	private MultiTextEdit edit=new MultiTextEdit();
	
	private List<SelectMethod> methods=new ArrayList<SelectMethod>();
	private Set<String> referTypes=new HashSet<String>();
	 
	public SelectGenerator(){		
	}
 		
	public void generate(SourceUnit unit,MultiTextEdit edit){
		this.unit=unit;
		this.edit=edit;
		 
		//org.jboss.tools.ws.jaxrs.core.jdt.Annotation db=this.unit.findClassAnnotation(DB.class.getName());
		//if(db!=null){
		//	dbClass=db.getValues("url").get(0);
			
			doGenerate();
		//};  		 	 
 	}
	
	private void doGenerate(){
		try{				
			//Get @Select methods
			findSelectMethods();
			
			if(methods.size()>0){
				//Run select methods
				SelectRun run=new SelectRun(unit);
				List<DBExchange> exchanges=run.run(methods);
				
				IResource r=unit.getUnit().getJavaElement().getResource();
				String filePath=unit.getProjectPath()+"/"+r.getProjectRelativePath();
				
				MMC mmc=MMC.getConsole();	
				mmc.print(Helper.getTime()+" [I] Processing java unit: ", SWT.COLOR_BLACK);
				mmc.print(new HyperLink("file://"+filePath, unit.getPackageName()+"."+unit.getUnitName()), SWT.COLOR_DARK_BLUE);
				mmc.print("\r\n", SWT.COLOR_BLACK);
				
				delayGenerate(exchanges);
			}
		}catch(Exception e){				
			MMC.getConsole().error(e);
		}
	}
	
	private void delayGenerate(final List<DBExchange> exchanges) {
		if(exchanges.size()>0){
			Job job=new WorkspaceJob("Monalisa building ..."){			    
			    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {			    	
			    	try{
			    		writeResultClasses(exchanges);
					}catch(Exception e){
						MMC.getConsole().error(e);
					}	
			    	return Status.OK_STATUS;
			    }
			};
			job.setRule(ResourcesPlugin.getWorkspace().getRoot());
			job.schedule();	
		}
	}
	
	private void writeResultClasses(List<DBExchange> exchanges)throws JavaModelException{
		for(DBExchange exchange:exchanges){
			writeResultClass(exchange);
		}
	}	
	
	
	private void writeResultClass(DBExchange exchange)throws JavaModelException{
		SelectMethod method=methods.get(exchange.getIndex());		
		String className=unit.getSubPackageName()+"."+method.getResultClassName();
		
		IPackageFragmentRoot pfr=unit.getPackageFragmentRoot();
		
		IResource r=unit.getUnit().getJavaElement().getResource();
		String linkMethodUrl  ="file://"+unit.getProjectPath()+"/"+r.getProjectRelativePath()+"#"+method.getMd().getName();
		String linkMethodText ="";
	 	
		String linkClassUrl   ="file://"+unit.getProjectPath()+"/"+pfr.getResource().getProjectRelativePath();
		linkClassUrl+="/"+className.replace(".", "/")+".java";
		
		String linkClassText  =className;
		
		List<?> params=method.getMd().parameters();
		for(Object p:params){
			SingleVariableDeclaration svd=(SingleVariableDeclaration)p;
			String ptype=svd.getType().toString();
			linkMethodUrl +=","+ptype;
			
			if(linkMethodText.length()>0){
				linkMethodText+=", ";
			}
			linkMethodText+=ptype;
		}
		
		linkMethodText=method.getMd().getName()+"("+linkMethodText+")";
		
		
		MMC mmc=MMC.getConsole();
		mmc.print(Helper.getTime()+" [I] ", SWT.COLOR_BLACK);		
		mmc.print("Create class: ",SWT.COLOR_BLACK);
		mmc.print(new HyperLink(linkClassUrl,linkClassText),SWT.COLOR_DARK_BLUE);
		mmc.print(", method: [",SWT.COLOR_BLACK);
		mmc.print(new HyperLink(linkMethodUrl, linkMethodText), SWT.COLOR_DARK_BLUE);
		mmc.print("]\r\n",SWT.COLOR_BLACK);
		mmc.sql(exchange.getSql());
		 
		if(exchange.getErrorString()==null){
			IPackageFragment pf = pfr.createPackageFragment(unit.getSubPackageName(), true, null);
						
			String java=method.createResultJavaCode(exchange);
			
	        pf.createCompilationUnit(method.getResultClassName()+".java", java,  true, new NullProgressMonitor());
	    }else{
	    	mmc.error(exchange.getErrorString());
		}
	}
	  
	
	private void addSelectMethod(SelectMethod sm){
		sm.setIndex(methods.size());
		methods.add(sm);
	}
	
	private void findSelectMethods(){		
		Set<String> imps=new HashSet<String>();
		
		for(MethodDeclaration md:unit.getUnitType().getMethods()){		
			Type rt=md.getReturnType2();
			
			if(rt==null)continue;
			
			String returnClazz=rt.toString();
			Set<String> returnParameter=new HashSet<String>();
			 
			if(rt.isParameterizedType()){
				ParameterizedType ptype=(ParameterizedType)rt;
				returnClazz=ptype.getType().toString();
				for(Object arg:ptype.typeArguments()){
					returnParameter.add(arg.toString());
					
					referTypes.add(arg.toString());
				}
			}else{
				referTypes.add(returnClazz);
			}			 
			
			Annotation a=unit.getAnnotation(md,Select.class);
			if(a!=null){
				SelectMethod sm=new SelectMethod(unit,md,a);				
				String rcn=sm.getResultClassName();
								 
				String newReturnType=null;
				if(returnClazz.equals("List") || returnClazz.equals("java.util.List") || returnClazz.equals("Page") || returnClazz.equals("com.tsc9526.monalisa.query.Page")){
					String ps=returnParameter.size()>0?returnParameter.iterator().next():"";										
					if(ps.equals("") || ps.equals("Object") || ps.equals("java.lang.Object")){						 
						newReturnType=returnClazz+"<"+rcn+">";
						imps.add(unit.getSubPackageName()+"."+rcn);
					}else{
						if(rcn.equals(ps)==false && sm.isForceRenameResultClass()){
							newReturnType=returnClazz+"<"+rcn+">";
							imps.add(unit.getSubPackageName()+"."+rcn);
						}
					}
				}else if(returnClazz.equals("Object") || returnClazz.equals("java.lang.Object")){
					newReturnType=rcn;
					imps.add(unit.getSubPackageName()+"."+rcn);
				}else{
					if(rcn.equals(returnClazz)==false && sm.isForceRenameResultClass()){
						newReturnType=rcn;
						imps.add(unit.getSubPackageName()+"."+rcn);
					}
				}
				 
				if(newReturnType!=null){				 
					QueryRewriteVisitor rewrite=new QueryRewriteVisitor(rcn);
					md.accept(rewrite);
					
				 	List<ReplaceEdit> changes=rewrite.getChanges();
					changes.add(new ReplaceEdit(rt.getStartPosition(),rt.getLength(),newReturnType));
					
					for(ReplaceEdit re:changes){
						edit.addChild(re);
					}
					sm.calculateFingerprint(changes); 
					
					
					addSelectMethod(sm);
				}else if(sm.isChanged()){
					addSelectMethod(sm);
				}
			} 
		}
		
		if(imps.size()>0){
			TextEdit importEdit=unit.createImports(imps);
			edit.addChild(importEdit);
		}
	}
	
	 private class QueryRewriteVisitor extends ASTVisitor{
		 private String parameterType;
		 
		 private List<ReplaceEdit> changes=new ArrayList<ReplaceEdit>();
		 
		 public QueryRewriteVisitor(String parameterType){
			 this.parameterType=parameterType;
		 }
		 
		 public List<ReplaceEdit> getChanges() {
			return changes;
		 }
		 
		 public boolean visit(VariableDeclarationStatement node) {
			 Type type=node.getType();			 
			 String typeString=""+type;
			  
			 String query=unit.getFullName(typeString);			
			 if(query.equals(Query.class.getName())){
				 List<?> fragments=node.fragments();
				 if(fragments.size()>0){
					 Object object=fragments.get(0);
					 if(object instanceof VariableDeclarationFragment){
						 VariableDeclarationFragment vdf=(VariableDeclarationFragment)object;
						 Expression expr=vdf.getInitializer();
						 if(expr instanceof ClassInstanceCreation){
							 ClassInstanceCreation cic=(ClassInstanceCreation)expr;
							 List<?> args=cic.arguments();
							 if(args.size()==1){
								 String a=args.get(0).toString();
								 if(!a.endsWith(".class") && !a.endsWith("Class")){
									 changes.add(new ReplaceEdit(expr.getStartPosition(),expr.getLength(),"new "+typeString+"("+a+", "+parameterType+".class)"));
								 }
							 }else{
								 changes.add(new ReplaceEdit(expr.getStartPosition(),expr.getLength(),"new "+typeString+"("+parameterType+".class)"));
							 }
						 }						 
					 }
				 }
			 }
			return true;
		 }
	 }
 	
}
