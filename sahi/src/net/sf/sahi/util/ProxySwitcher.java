package net.sf.sahi.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.sahi.config.Configuration;

public class ProxySwitcher {
	
	private static final Logger logger = Logger.getLogger("net.sf.sahi.util.ProxySwitcher");
	private static String toolsBasePath = Configuration.getToolsPath();
	private static int counter = 0;
	private static boolean switchDaemonStarted;

    /**
     * Restores System Proxy settings to what was before.
     * Used with configureSahiAsSystemProxy()
     */
	public static void revertSystemProxy() {
		revertSystemProxy(false);
	}

	public synchronized static void revertSystemProxy(boolean force) {
		counter--;
		if (force) counter = 0;
		if (counter == 0) {
			if (Utils.isWindows()) {
				logger.info("Reverting Proxy");
//				System.out.println("Reverting Proxy");
				execCommand(toolsBasePath + "/proxy_config.exe original");
			} else if (Utils.isMac()) {
				logger.info("Reverting Proxy: " + Configuration.getMacProxyResetCommand());
//				System.out.println("Reverting Proxy: " + Configuration.getMacProxyResetCommand());
				execCommand(Configuration.getMacProxyResetCommand());
			} 
			
		}
		long sleeptime = Configuration.getDelayInBrowserLaunchAfterProxyChange();
		Utils.sleep(sleeptime);
	}
	
	public static void setSahiAsProxy() {
		setSahiAsProxy(false);
	}
	/**
	 * Sets System proxy settings to localhost 9999
	 */
	public synchronized static void setSahiAsProxy(boolean force) {
		if (counter == 0 || force) {
			if (Utils.isWindows()) {
				setSahiAsProxyOnWindows();
				switchPeriodically();
			}
			else if (Utils.isMac()) {
				logger.info("Setting Proxy: " + Configuration.getMacProxySetCommand());
//				System.out.println("Setting Proxy: " + Configuration.getMacProxySetCommand());
				execCommand(Configuration.getMacProxySetCommand());
			}
			
		}
		long sleeptime = Configuration.getDelayInBrowserLaunchAfterProxyChange();
		Utils.sleep(sleeptime);
		
		counter++;
	}

	private static void switchPeriodically() {
		final boolean isResetEnabled = Configuration.isWindowsContinuousProxyReset();
		final int resetInterval = Configuration.getWindowsContinuousProxyResetInterval() * 1000;
		if (!switchDaemonStarted && isResetEnabled) {
			switchDaemonStarted = true;
			logger.info("Proxy auto reset enabled: reset interval: " + resetInterval);
//			System.out.println("Proxy auto reset enabled: reset interval: " + resetInterval);
			new Thread(new Runnable(){
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(resetInterval);
							if (counter > 0)
								setSahiAsProxyOnWindows();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}}).start();
		}
	}

	private static void setSahiAsProxyOnWindows() {
//		System.out.println("Setting Proxy");
		logger.info("Setting Proxy");
		execCommand(toolsBasePath + "/backup_proxy_config.exe");
		execCommand(toolsBasePath + "/proxy_config.exe sahi_https");
//		System.out.println("Proxy set");
		logger.info("Proxy set");
	}
	
	private static void execCommand(String cmd) {
        try {
            Utils.executeCommand(Utils.getCommandTokens(cmd));
        } catch (Exception ex) {
        	ex.printStackTrace();
            Logger.getLogger(ProxySwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public static void main(String[] args) {
		//execCommand("sh /Users/narayanraman/Documents/workspace/Sahi_Pro/tools/changeproxy.sh");
	}
	
}
