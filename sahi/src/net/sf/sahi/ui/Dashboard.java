package net.sf.sahi.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import net.sf.sahi.Proxy;
import net.sf.sahi.config.Configuration;
import net.sf.sahi.test.BrowserLauncher;
import net.sf.sahi.util.BrowserType;
import net.sf.sahi.util.BrowserTypesLoader;
import net.sf.sahi.util.ProxySwitcher;
import net.sf.sahi.util.Utils;

/**
 * Dashboard exposes various Sahi components from a convenient place.
 */

public class Dashboard extends JFrame {
	// Useful link: http://download.oracle.com/javase/tutorial/uiswing/layout/box.html#filler
	
	
	private static final long serialVersionUID = 8348788972744726483L;
	
	private Map<String, BrowserType> browserTypes;

	private Proxy currentInstance;

	private JPanel masterPanel;

	private int browserTypesHeight;
	
	private JLabel trafficLabel;

	private boolean isTrafficEnabled;
	
	private int rigidAreaWidth;

	private int columns = 1;

	private int dashboardWidth;

	private int dashboardHeight;

	public Dashboard() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Sahi Dashboard");
        startProxy();
        loadBrowserTypes();
		buildUI();
		addOnExit();
		final Dimension dashboardSize = new Dimension(dashboardWidth, dashboardHeight);
//		final Dimension dashboardSize = new Dimension(200, 325 + 50 + browserTypesHeight);
		setSize(dashboardSize);
		setPreferredSize(dashboardSize);
		refreshTrafficLink();
		setVisible(true);
		proxyConfigAlert();
	}

	private void startProxy() {
		final Proxy proxy = new Proxy(Configuration.getPort());
        currentInstance = proxy;
    	proxy.start(true);
	}

	private void buildUI() {
		masterPanel = new JPanel();
		masterPanel.setBackground(new Color(255, 255, 255));
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(getBrowserButtons());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(getLogo());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(getLinksPanel1());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(getLinksPanel2());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(getLinksPanel3());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(showVersionNo());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(trySahiProPanel());
		masterPanel.add(Box.createRigidArea(new Dimension(rigidAreaWidth, 5)));
		masterPanel.add(showSocialPlugins());
		add(masterPanel);
	}

	private Component showSocialPlugins() {
		JPanel buttonPane = new JPanel();
		
		LinkButton facebook = new LinkButton("", "https://www.facebook.com/sahi.software", null);
		facebook.setIcon(new ImageIcon(getImageBytes("facebook24.png"), ""));
		buttonPane.add(facebook);
		
		LinkButton twitter = new LinkButton("", "https://twitter.com/_sahi", null);
		twitter.setIcon(new ImageIcon(getImageBytes("twitter24.png"), ""));
		buttonPane.add(twitter);

		LinkButton linkedin = new LinkButton("", "http://www.linkedin.com/company/tyto-software-pvt.-ltd.", null);
		linkedin.setIcon(new ImageIcon(getImageBytes("linkedin24.png"), ""));
		buttonPane.add(linkedin);
		
		LinkButton google = new LinkButton("", "https://plus.google.com/+Sahiproautomation/posts", null);
		google.setIcon(new ImageIcon(getImageBytes("google24.png"), ""));
		buttonPane.add(google);
		
		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		return buttonPane;
	}

	private void loadBrowserTypes() {
		browserTypes = BrowserTypesLoader.getAvailableBrowserTypes(false);
		setDashboardDimensions(browserTypes.size());
	}
	
	private void setDashboardDimensions(int size) {
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Dimension dim = toolkit.getScreenSize();
		final double screenHeight = dim.getHeight();
		int maxButtonsInColumn = (int) ((screenHeight - 300) / 60);
		columns = (browserTypes.size() / maxButtonsInColumn) + 1;
		dashboardWidth = columns * 120 + 80;
		browserTypesHeight = (browserTypes.size() == 0) ? 120 : ((browserTypes.size() + (columns > 1 ? 1 : 0)) * 50 / columns);
		dashboardHeight = 360 + 50 + browserTypesHeight;
		rigidAreaWidth = columns * 120 + 40; 
	}

	private Component getLinksPanel1() {
		JPanel buttonPane = new JPanel();
		String scriptsPath = Configuration.getScriptRoots()[0];
		
		LinkButton link = new LinkButton("Configure", "http://localhost:9999/_s_/dyn/ConfigureUI", "Edit configurable files");
		buttonPane.add(link);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		LinkButton link2;
		if (Utils.isWindows()){
			if(Configuration.isSahiExpressFlagOn()){
				link2 = new LinkButton("Scripts Dir", "explorer /e,\"" + scriptsPath + "\"", "Opens the script folder");
			}
			else
				link2 = new LinkButton("Scripts", "explorer /e,\"" + scriptsPath + "\"", "Opens the script folder");
		} else {
			if(Configuration.isSahiExpressFlagOn()){
				link2 = new LinkButton("Scripts Dir", "\"" + scriptsPath + "\"", "Opens the script folder");
			}
			else
				link2 = new LinkButton("Scripts", "\"" + scriptsPath + "\"", "Opens the script folder");
		}
		buttonPane.add(link2);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		LinkButton webrunner = new LinkButton("Editor", "http://sahipro.com/docs/using-sahi/sahi-script-editor.html", "Documentation for Web Based Editor (Sahi Pro feature)");
		buttonPane.add(webrunner);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		return buttonPane;
	}

	private Component getLinksPanel2() {
		JPanel buttonPane = new JPanel();

		LinkButton link6 = new LinkButton("Docs", "http://sahipro.com/docs/introduction/index.html", "Open Sahi Pro documentation");
		buttonPane.add(link6);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		LinkButton link3 = new LinkButton("Logs", "http://localhost:9999/logs/", "Open playback logs");
		buttonPane.add(link3);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		if (Utils.isWindows()){
			LinkButton link5 = new LinkButton("Bin", "cmd.exe /K cd " + Configuration.getAbsoluteUserPath("bin"), "Open command prompt at userdata/bin folder");
			buttonPane.add(link5);		
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		} else if (Utils.isMac()) {
			LinkButton link5 = new LinkButton("Bin", "osascript -e 'tell application \"Terminal\" to do script \"cd "+ Configuration.getAbsoluteUserPath("bin") +"\"'", "Open command prompt at userdata/bin folder");
			buttonPane.add(link5);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		} else {
			// some blank space in UI?
		}
		
		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		return buttonPane;
	}
	
	private Component getLinksPanel3() {		
		trafficLabel = new JLabel();
		trafficLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		trafficLabel.setHorizontalAlignment(JButton.LEADING); 
		trafficLabel.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				setTrafficLink(!isTrafficEnabled);
				setTrafficLog((isTrafficEnabled) ? false : true);
			}
		});
		trafficLabel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent arg0) {
				trafficLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			public void mouseDragged(MouseEvent arg0) {}
		});
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(trafficLabel);
		
		LinkButton link2;
		String logsPath = Configuration.getLogsRoot();
		final String text = "[&#8599;]";
		if (Utils.isWindows()){
			link2 = new LinkButton(text, "explorer /e,\"" + logsPath + "\"", "Go to the Traffic Logs directory");
		} else {
			link2 = new LinkButton(text, "\"" + logsPath + "\"", "Go to the Traffic Logs directory");
		}
		buttonPane.add(link2);
		
		return buttonPane;
	}
	
	private Component trySahiProPanel() {		
		JPanel buttonPane = new JPanel();
		
		LinkButton toSahiProLink = new LinkButton("Try Sahi Pro", "http://sahipro.com/sahi-open-source/", "TRY THE LATEST SAHI PRO!");
		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.add(toSahiProLink);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
//		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		return buttonPane;
	}
	
	private void setTrafficLink(boolean isTrafficEnabled){
		this.isTrafficEnabled = isTrafficEnabled;
		trafficLabel.setText(((isTrafficEnabled) ? "<html><a href=''><font color='blue'>Enable Traffic Logs</font></a></html>" : 
			                                       "<html><a href=''><font color='red'>Disable Traffic Logs</font></a></html>"));
	}
	
	private void refreshTrafficLink(){
		setTrafficLink(!(Configuration.isModifiedTrafficLoggingOn() || Configuration.isUnmodifiedTrafficLoggingOn()));
	}

	private Component getLogo() {
		JLabel picLabel = new JLabel();
		picLabel.setIcon(new ImageIcon(getImageBytes("sahi_os_logo2.png"), ""));
		return picLabel;
	}

	private JPanel getBrowserButtons() {
		JPanel panel = new JPanel();
		final GridLayout layout = new GridLayout(0, columns, 0, 10);
		panel.setLayout(layout);
		final Border innerBorder = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		final Border outerBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		final CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
		final Border titleBorder = BorderFactory.createTitledBorder(compoundBorder, "Launch Browser");
		panel.setBorder(titleBorder);
		add(panel);
		if (browserTypes.size() == 0) {
			final JLabel label = new JLabel("<html><b>No usable browsers<br>found in<br>browser_types.xml</b>.<br><br> Click on the<br><u>Configure</u> link below<br>to add/edit browsers.</html>");
			label.setSize(120, 100);
			panel.add(label);
		} else {
		for (Iterator<String> iterator = browserTypes.keySet().iterator(); iterator.hasNext();) {
			String name = iterator.next();
			BrowserType browserType = browserTypes.get(name);
			addButton(browserType, panel);
		}
		}
		panel.setBackground(new Color(255, 255, 255));
		return panel;
	}
	
	private Component showVersionNo() {		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 255, 255));
		buttonPane.setLayout(new FlowLayout());
//		buttonPane.add(new JLabel("Sahi OS " + Configuration.getVersionNumber() + " (" + Configuration.getVersion().split(" ")[0] +")"));
		buttonPane.add(new JLabel("Sahi OS " + Configuration.getVersion() + " (" + Configuration.getVersionTimeStamp().split(" ")[0] +")"));
		return buttonPane;
	}

	private void setIcon(AbstractButton button, String iconFile) {
		if (iconFile == null) return;
		button.setIcon(new ImageIcon(getImageBytes(iconFile), ""));
		button.setHorizontalAlignment(SwingConstants.LEFT);
	}

	private byte[] getImageBytes(String iconFile) {
		try {
			final InputStream resourceAsStream = Dashboard.class.getResourceAsStream("/net/sf/sahi/resources/" + iconFile);
			return Utils.getBytes(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	private void addOnExit() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stopProxy();
				ProxySwitcher.revertSystemProxy(true);
				System.exit(0);
			}
		});
	}

	private void addButton(final BrowserType browserType, JPanel panel) {
		final JButton button = new JButton();
		button.setText(browserType.displayName());
		setIcon(button, browserType.icon());
		Dimension dimension = new Dimension(120, 40);
		button.setSize(dimension);
		button.setPreferredSize(dimension);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    new Thread() {
	
			        @Override
			        public void run() {
			        	final BrowserLauncher launcher = new BrowserLauncher(browserType);
			        	launcher.setMaxTimeToWaitForPIDs(Configuration.getMaxTimeForPIDGatherFromDashboard());
			    		String url = "http://" + Configuration.getCommonDomain() + "/_s_/dyn/Driver_initialized?browserType=" + browserType.name();   	
			        	try {
							launcher.openURL(url);
							launcher.waitTillAlive();
							launcher.kill();
						} catch (Exception e) {
							e.printStackTrace();
						} 
			        }

			    }.start();
			}
		});
		panel.add(button);
		pack();
	}

	private void execCommand(String cmd) {
        try {
            Utils.executeCommand(Utils.getCommandTokens(cmd));
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	private void proxyConfigAlert() {
    	if(Configuration.isProxyAlertOn()){
    		String message=Utils.readFileAsString(Configuration.getProxyAlertMessage());
    		JCheckBox checkbox = new JCheckBox("Do not show this message again.");  
        	String title = "Sahi Proxy Question";
        	UIManager.put("OptionPane.okButtonText", "Continue...");  
        	UIManager.put("OptionPane.cancelButtonText", "Quit Sahi");
        	Object[] params = {message, checkbox};  
        	int n = JOptionPane.showConfirmDialog(null, params, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);  
        	boolean dontShow = checkbox.isSelected();
        	if(n == JOptionPane.OK_OPTION){ // Affirmative
        		if(dontShow){
            		Configuration.setProxyAlertOff(true);
            	}
        		return;
            }
        	else if(n == JOptionPane.CANCEL_OPTION){ // negative
            	System.exit(0);
            }
        	else if(n == JOptionPane.CLOSED_OPTION){ // closed the dialog
            	System.exit(0);
            }
    	}
	}
	
	private void setTrafficLog(boolean flag){
		Configuration.setModifiedTrafficLogging(flag);
		Configuration.setUnmodifiedTrafficLogging(flag);
	}	

	private void stopProxy() {
		currentInstance.stop();
	}

	private void toggleProxy(boolean selected) {
		if (selected){
			ProxySwitcher.setSahiAsProxy();
		} else {
			ProxySwitcher.revertSystemProxy();
		}
	}

	public static void main(String args[]) {
    	if (args.length > 0){
    		Configuration.init(args[0], args[1]);
    	}else{
    		Configuration.init();
    	}
    	new Dashboard();
	}
}
