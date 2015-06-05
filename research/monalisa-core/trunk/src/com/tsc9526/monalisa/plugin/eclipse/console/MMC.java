package com.tsc9526.monalisa.plugin.eclipse.console;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.plugin.eclipse.resources.Resource;

public class MMC extends MessageConsole {
	public final static String CONSOLE_NAME = "Monalisa";
	
	public static MMC getConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] curConsoles = manager.getConsoles();
		for (IConsole co : curConsoles) {
			if (co.getName().equals(CONSOLE_NAME)){
				return (MMC) co;
			}
		}
		MMC mmc = new MMC();
		manager.addConsoles(new IConsole[] { mmc });
		return mmc;
	}
	
	
	private int length;
	private boolean clear=false;	

	public MMC() {
		super(CONSOLE_NAME, Resource.getImage("monalisa"));
		length = 0;
	}

	public void clearConsole() {		
		super.clearConsole();
		
		clear=true;
		length = 0;
	}

	public void info(Object message){
		if(message instanceof Throwable){						
			Throwable throwable=(Throwable) message;
			throwable.printStackTrace();
			
			message=Helper.getCause(throwable);
		}
		
		print(Helper.getTime()+" [I] "+message+"\r\n",SWT.COLOR_BLACK);				 
	}
	
	public void error(Object message){
		if(message instanceof Throwable){
			Throwable throwable=(Throwable) message;
			throwable.printStackTrace();
			
			message=Helper.getCause(throwable);
		}
		print(Helper.getTime()+" [E] "+message+"\r\n",SWT.COLOR_RED);		
	}
	
	public void sql(String sql){
		print(sql+"\r\n",SWT.COLOR_BLUE);
	}
	
	public void print(final String msg, final Integer SWTColor) {
		print(new HyperLink(null,msg),SWTColor);
	}

	public void print(final HyperLink link, final Integer SWTColor) {
		Display.getDefault().syncExec(new Runnable() {				
			@Override
			public void run() {
				if(clear){
					clear=false;					 
					activate();			
				}
				
				MessageConsoleStream stream = newMessageStream();
				if (SWTColor != null){
					stream.setColor(Display.getDefault().getSystemColor(SWTColor));
				}
				
				if(link.getUrl()!=null){
					//System.out.println("Lenght: "+length+", Expect: "+(length + link.getText().length())+", text: "+link.getText()+", url: "+link.getUrl());
					getDocument().addDocumentListener(new DocChangeListener(MMC.this, link, length + link.getText().length()));
				}
				
				length += link.getText().length();
				stream.print(link.getText()); 
			}
		});									 		
	}
	 
	static class DocChangeListener implements IDocumentListener {
		int lenBeforeChange;
		int expectedlen;
		final MMC console;
		final HyperLink link;

		public DocChangeListener(MMC console, HyperLink link, int expectedlen) {
			this.console = console;
			this.link = link;
			this.expectedlen = expectedlen;
		}

		public void documentAboutToBeChanged(DocumentEvent event) {
			
		}

		public void documentChanged(final DocumentEvent event) {
			int strLenAfterChange = event.getDocument().getLength();
			if (strLenAfterChange > expectedlen) {
				try{
					//System.out.println("Changed: "+strLenAfterChange+", expect: "+expectedlen+", link: "+link.getText());
					console.addHyperlink(link, expectedlen - link.getText().length(), link.getText().length());
				}catch(Exception e){
					e.printStackTrace();
				}
				event.getDocument().removeDocumentListener(this);
			}
		}
	}
}