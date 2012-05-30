/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package org.springframework.roo.shell.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.osgi.service.startlevel.StartLevel;

import com.springsource.sts.roo.core.RooCoreActivator;

/**
 * Supports {@link Main}.
 * <p>
 * This class is based on Apache Felix's org.apache.felix.main.AutoProcessor class.
 * <p>
 * For maximum compatibility with Felix, this class has no changes from the Felix original.
 * @author Christian Dupuis
 * @author Steffen Pingel
 */
public class AutoProcessor {
	/**
	 * The property name used to specify auto-deploy actions.
	 **/
	public static final String AUTO_DEPLOY_ACTION_PROPERY = "felix.auto.deploy.action";

	/**
	 * The property name used for the bundle directory.
	 **/
	public static final String AUTO_DEPLOY_DIR_PROPERY = "felix.auto.deploy.dir";

	/**
	 * The default name used for the bundle directory.
	 **/
	public static final String AUTO_DEPLOY_DIR_VALUE = "bundle";

	/**
	 * The name used for the auto-deploy install action.
	 **/
	public static final String AUTO_DEPLOY_INSTALL_VALUE = "install";

	/**
	 * The name used for the auto-deploy start action.
	 **/
	public static final String AUTO_DEPLOY_START_VALUE = "start";

	/**
	 * The name used for the auto-deploy uninstall action.
	 **/
	public static final String AUTO_DEPLOY_UNINSTALL_VALUE = "uninstall";

	/**
	 * The name used for the auto-deploy update action.
	 **/
	public static final String AUTO_DEPLOY_UPDATE_VALUE = "update";

	/**
	 * The property name prefix for the launcher's auto-install property.
	 **/
	public static final String AUTO_INSTALL_PROP = "felix.auto.install";

	/**
	 * The property name prefix for the launcher's auto-start property.
	 **/
	public static final String AUTO_START_PROP = "felix.auto.start";

	/**
	 * Used to instigate auto-deploy directory process and auto-install/auto-start configuration property processing
	 * during.
	 * @param configMap Map of configuration properties.
	 * @param context The system bundle context.
	 * @param rooVersion The version of the roo runtime
	 **/
	public static void process(Map configMap, BundleContext context, String rooVersion) {
		configMap = (configMap == null) ? new HashMap() : configMap;
		processAutoDeploy(configMap, context, rooVersion);
		processAutoProperties(configMap, context);
	}

	private static boolean isFragment(Bundle bundle) {
		return bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
	}

	private static String nextLocation(StringTokenizer st) {
		String retVal = null;

		if (st.countTokens() > 0) {
			String tokenList = "\" ";
			StringBuffer tokBuf = new StringBuffer(10);
			String tok = null;
			boolean inQuote = false;
			boolean tokStarted = false;
			boolean exit = false;
			while ((st.hasMoreTokens()) && (!exit)) {
				tok = st.nextToken(tokenList);
				if (tok.equals("\"")) {
					inQuote = !inQuote;
					if (inQuote) {
						tokenList = "\"";
					}
					else {
						tokenList = "\" ";
					}

				}
				else if (tok.equals(" ")) {
					if (tokStarted) {
						retVal = tokBuf.toString();
						tokStarted = false;
						tokBuf = new StringBuffer(10);
						exit = true;
					}
				}
				else {
					tokStarted = true;
					tokBuf.append(tok.trim());
				}
			}

			// Handle case where end of token stream and
			// still got data
			if ((!exit) && (tokStarted)) {
				retVal = tokBuf.toString();
			}
		}

		return retVal;
	}

	/**
	 * <p>
	 * Processes bundles in the auto-deploy directory, installing and then starting each one.
	 * </p>
	 */
	private static void processAutoDeploy(Map configMap, BundleContext context, String rooVersion) {
		// Determine if auto deploy actions to perform.
		String action = (String) configMap.get(AUTO_DEPLOY_ACTION_PROPERY);
		action = (action == null) ? "" : action;
		List actionList = new ArrayList();
		StringTokenizer st = new StringTokenizer(action, ",");
		while (st.hasMoreTokens()) {
			String s = st.nextToken().trim().toLowerCase();
			if (s.equals(AUTO_DEPLOY_INSTALL_VALUE) || s.equals(AUTO_DEPLOY_START_VALUE)
					|| s.equals(AUTO_DEPLOY_UPDATE_VALUE) || s.equals(AUTO_DEPLOY_UNINSTALL_VALUE)) {
				actionList.add(s);
			}
		}

		// Perform auto-deploy actions.
		if (actionList.size() > 0) {
			// Get list of already installed bundles as a map.
			Map installedBundleMap = new HashMap();
			Bundle[] bundles = context.getBundles();
			for (int i = 0; i < bundles.length; i++) {
				installedBundleMap.put(bundles[i].getLocation(), bundles[i]);
			}

			// Get the auto deploy directory.
			String autoDir = (String) configMap.get(AUTO_DEPLOY_DIR_PROPERY);
			autoDir = (autoDir == null) ? AUTO_DEPLOY_DIR_VALUE : autoDir;
			// Look in the specified bundle directory to create a list
			// of all JAR files to install.
			File[] files = new File(autoDir).listFiles();
			List jarList = new ArrayList();
			if (files != null) {
				Arrays.sort(files);
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().endsWith(".jar")) {
						jarList.add(files[i]);
					}
				}
			}

			// Install bundle JAR files and remember the bundle objects.
			final List startBundleList = new ArrayList();
			for (int i = 0; i < jarList.size(); i++) {
				// Look up the bundle by location, removing it from
				// the map of installed bundles so the remaining bundles
				// indicate which bundles may need to be uninstalled.
				Bundle b = (Bundle) installedBundleMap.remove(((File) jarList.get(i)).toURI().toString());
				try {
					// If the bundle is not already installed, then install it
					// if the 'install' action is present.
					if ((b == null) && actionList.contains(AUTO_DEPLOY_INSTALL_VALUE)) {
						// Make sure that we don't install the Jline bundle
						if (!((File) jarList.get(i)).getName().startsWith("org.springframework.roo.shell.jline")) {
							b = context.installBundle(((File) jarList.get(i)).toURI().toString());
						}
					}
					// If the bundle is already installed, then update it
					// if the 'update' action is present.
					else if (actionList.contains(AUTO_DEPLOY_UPDATE_VALUE)) {
						b.update();
					}

					// If we have found and/or successfully installed a bundle,
					// then add it to the list of bundles to potentially start.
					if (b != null) {
						startBundleList.add(b);
					}
				}
				catch (BundleException ex) {
					System.err.println("Auto-deploy install: " + ex
							+ ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
				}
			}
			
			// Install the Eclipse shell bundle
			List<String> bundleLocations = RooCoreActivator.getAutoDeployBundleLocations();
			if (bundleLocations.size() > 0) {
				for (String bundleLocation : bundleLocations) {
					if (shouldAutoDeploy(bundleLocation, rooVersion)) {
						try {
							Bundle b = context.installBundle(bundleLocation, RooCoreActivator.getBundleInputStream(bundleLocation));
							// If we have found and/or successfully installed a bundle,
							// then add it to the list of bundles to potentially start.
							if (b != null) {
								startBundleList.add(b);
							}
						}
						catch (BundleException ex) {
							System.err.println("Auto-deploy install: " + ex
									+ ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
						}
					}
				}
			}

			// Uninstall all bundles not in the auto-deploy directory if
			// the 'uninstall' action is present.
			if (actionList.contains(AUTO_DEPLOY_UNINSTALL_VALUE)) {
				for (Iterator it = installedBundleMap.entrySet().iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry) it.next();
					Bundle b = (Bundle) entry.getValue();
					if (b.getBundleId() != 0) {
						try {
							b.uninstall();
						}
						catch (BundleException ex) {
							System.err.println("Auto-deploy uninstall: " + ex
									+ ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
						}
					}
				}
			}

			// Start all installed and/or updated bundles if the 'start'
			// action is present.
			if (actionList.contains(AUTO_DEPLOY_START_VALUE)) {
				for (int i = 0; i < startBundleList.size(); i++) {
					try {
						if (!isFragment((Bundle) startBundleList.get(i))) {
							((Bundle) startBundleList.get(i)).start();
						}
					}
					catch (BundleException ex) {
						System.err.println("Auto-deploy start: " + ex
								+ ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
					}
				}
			}
		}
	}

	private static boolean shouldAutoDeploy(String filename, String rooVersion) {
		// remove path
		int i = filename.lastIndexOf("/");
		if (i != -1) {
			filename = filename.substring(i + 1);
		}
		if (filename.endsWith(".jar")) {
			if (filename.startsWith("org.springframework.roo.shell.eclipse-1.1.0")) {
				return matchesVersion("[1.1.0, 1.2.0)", rooVersion);
			}
			if (filename.startsWith("org.springframework.roo.shell.eclipse-1.2.0")) {
				return matchesVersion("1.2.0", rooVersion);
			}
			if (filename.startsWith("org.springframework.roo.addon.roobot.eclipse.client-1.1.2")) {
				return matchesVersion("[1.1.2, 1.1.5)", rooVersion);
			}
			if (filename.startsWith("org.springframework.roo.addon.roobot.eclipse.client-1.1.5")) { 				
				return matchesVersion("1.1.5", rooVersion);
			}
			return true;
		}
		return false;
	}

	private static boolean matchesVersion(String range, String rooVersion) {
		try {
			int i = rooVersion.indexOf(' ');
			if (new VersionRange(range).isIncluded(new Version(rooVersion.substring(0, i)))) {
				return true;						
			}
		} catch (RuntimeException e) {
			// ignore
		}
		return false;
	}


	/**
	 * <p>
	 * Processes the auto-install and auto-start properties from the specified configuration properties.
	 * </p>
	 */
	private static void processAutoProperties(Map configMap, BundleContext context) {
		// Retrieve the Start Level service, since it will be needed
		// to set the start level of the installed bundles.
		StartLevel sl = (StartLevel) context.getService(context
				.getServiceReference(org.osgi.service.startlevel.StartLevel.class.getName()));

		// Retrieve all auto-install and auto-start properties and install
		// their associated bundles. The auto-install property specifies a
		// space-delimited list of bundle URLs to be automatically installed
		// into each new profile, while the auto-start property specifies
		// bundles to be installed and started. The start level to which the
		// bundles are assigned is specified by appending a ".n" to the
		// property name, where "n" is the desired start level for the list
		// of bundles. If no start level is specified, the default start
		// level is assumed.
		for (Iterator i = configMap.keySet().iterator(); i.hasNext();) {
			String key = ((String) i.next()).toLowerCase();

			// Ignore all keys that are not an auto property.
			if (!key.startsWith(AUTO_INSTALL_PROP) && !key.startsWith(AUTO_START_PROP)) {
				continue;
			}

			// If the auto property does not have a start level,
			// then assume it is the default bundle start level, otherwise
			// parse the specified start level.
			int startLevel = sl.getInitialBundleStartLevel();
			if (!key.equals(AUTO_INSTALL_PROP) && !key.equals(AUTO_START_PROP)) {
				try {
					startLevel = Integer.parseInt(key.substring(key.lastIndexOf('.') + 1));
				}
				catch (NumberFormatException ex) {
					System.err.println("Invalid property: " + key);
				}
			}

			// Parse and install the bundles associated with the key.
			StringTokenizer st = new StringTokenizer((String) configMap.get(key), "\" ", true);
			for (String location = nextLocation(st); location != null; location = nextLocation(st)) {
				try {
					Bundle b = context.installBundle(location, null);
					sl.setBundleStartLevel(b, startLevel);
				}
				catch (Exception ex) {
					System.err.println("Auto-properties install: " + location + " (" + ex
							+ ((ex.getCause() != null) ? " - " + ex.getCause() : "") + ")");
					if (ex.getCause() != null)
						ex.printStackTrace();
				}
			}
		}

		// Now loop through the auto-start bundles and start them.
		for (Iterator i = configMap.keySet().iterator(); i.hasNext();) {
			String key = ((String) i.next()).toLowerCase();
			if (key.startsWith(AUTO_START_PROP)) {
				StringTokenizer st = new StringTokenizer((String) configMap.get(key), "\" ", true);
				for (String location = nextLocation(st); location != null; location = nextLocation(st)) {
					// Installing twice just returns the same bundle.
					try {
						Bundle b = context.installBundle(location, null);
						if (b != null) {
							b.start();
						}
					}
					catch (Exception ex) {
						System.err.println("Auto-properties start: " + location + " (" + ex
								+ ((ex.getCause() != null) ? " - " + ex.getCause() : "") + ")");
					}
				}
			}
		}
	}
}