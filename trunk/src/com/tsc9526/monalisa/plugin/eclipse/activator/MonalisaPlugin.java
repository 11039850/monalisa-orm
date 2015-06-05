package com.tsc9526.monalisa.plugin.eclipse.activator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.tsc9526.monalisa.core.datasource.DataSourceManager;

public class MonalisaPlugin extends AbstractUIPlugin {
	public final static String PLUGIN_ID = "com.tsc9526.monalisa";
  
	private static MonalisaPlugin plugin;

	public static MonalisaPlugin getDefault() {
		return plugin;
	}

	private BundleContext context;

	public void start(BundleContext bundleContext) throws Exception {
		this.context = bundleContext;

		plugin = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		try {
			DataSourceManager.getInstance().shutdown();
		} finally {
			plugin = null;
		}
	}

	public BundleContext getContext() {
		return context;
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {

	}
    
}
