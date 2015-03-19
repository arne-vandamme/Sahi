package net.sf.sahi.ui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.sf.sahi.util.Utils;

public class LinkButton extends JLabel {
	private static final Logger logger = Logger.getLogger("net.sf.sahi.ui.LinkButton");
	private static final long serialVersionUID = 8273875024682878518L;
	
	public LinkButton(final String text) {
		this(text, null, null);
	}
	public LinkButton(final String text, final String uri, final String tooltip) {
		super();
		setText(text);
		if (tooltip != null) setToolTipText(tooltip.toString());
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(Utils.isMac() && text.equals("Bin")){ 
					runAppleScript(uri); 
					return;
				}
				open(uri);
			}
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));  
				setText(text, true);
			}
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
				setText(text, false);
			}
		});
	}

	@Override
	public void setText(String text) {
		setText(text, false);
	}

	public void setText(String text, boolean highlight) {
		text = "<u>" + text + "</u>";
		super.setText("<html><span style=\"color: " + (highlight ? "#FF0000" : "#000099;") + "\">" + text + "</span></html>");
	}
	
	protected void open(String url) {
		String cmd = null;
		if (Utils.isMac()){
			cmd = "open ";
		} else if (Utils.isWindows()) {
			 cmd = "cmd.exe /C start ";
		} else {
			 cmd = "xdg-open ";
		} 
		cmd += url.toString();
		try {
			Utils.executeAndGetProcess(Utils.getCommandTokens(cmd));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Please navigate to http://localhost:9999/_s_/dyn/ConfigureUI to configure browsers for Sahi",
					"Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
		}
	}
	protected void runAppleScript(String url) { 
		String cmd = url.toString();
		try {
			logger.info(cmd);
//			System.out.println(cmd);
			Utils.executeAndGetProcess(Utils.getCommandTokens(cmd));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Please navigate to http://localhost:9999/_s_/dyn/ConfigureUI to configure browsers for Sahi",
					"Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
		}
	}

}