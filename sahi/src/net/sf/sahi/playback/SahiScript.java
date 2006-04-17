/**
 * User: nraman
 * Date: May 18, 2005
 * Time: 10:19:59 PM
 */
package net.sf.sahi.playback;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.sahi.config.Configuration;
import net.sf.sahi.util.Utils;

public abstract class SahiScript {
	private static Logger logger = Configuration
	.getLogger("net.sf.sahi.playback.SahiScript");
	private static ArrayList actionKeywords;
	private static ArrayList normalKeywords;
	protected String script;
	private static final String PREFIX = "sahiAdd(\"";
	private static final String CONJUNCTION = "\", \"";
	private static final String SUFFIX = "\")";
	protected String scriptName;
	protected String fqn;
	protected String base = "";
	protected ArrayList parents;
	private String original = "";
	private static final String REG_EXP_FOR_ADDING = getRegExp(false);
	private static final String REG_EXP_FOR_STRIPPING = getRegExp(true);
	private static final String REG_EXP_FOR_ACTIONS = getActionRegExp();

	public SahiScript() {
	}

	public SahiScript(String fileName) {
		this(fileName, new ArrayList());
	}

	public SahiScript(String fileName, ArrayList parents) {
		this.parents = parents;
		init(fileName);
	}

	protected void setScript(String s) {
		original  = s;
		script = modify(s);
	}

	public String modifiedScript() {
		return script == null ? "" : script;
	}

	String modify(String s) {
		StringBuffer sb = new StringBuffer();
		Iterator tokens = Utils.getTokens(s).iterator();
		int lineNumber = 0;
		while (tokens.hasNext()) {
			lineNumber++;
			String line = ((String)tokens.next()).trim();
			if ("".equals(line))
				continue;
			if (line.startsWith("_include")) {
				sb.append(processInclude(line));
			} else if (line.startsWith("_") && lineStartsWithActionKeyword(line)) {
				sb.append(PREFIX);
				sb.append(modifyFunctionNames(separateVariables(Utils
						.escapeDoubleQuotesAndBackSlashes(line))));
				sb.append(CONJUNCTION);
				sb.append(scriptName);
				sb.append("?n=");
				sb.append(lineNumber);
				sb.append(SUFFIX);
				sb.append("\r\n");
			} else {
				sb.append(modifyFunctionNames(line));
				sb.append("\r\n");
			}
		}
		String toString = sb.toString();
		logger.fine(toString);
		return toString;
	}

	static boolean lineStartsWithActionKeyword(String line) {
		return line.matches(REG_EXP_FOR_ACTIONS);
	}

	private String processInclude(String line) {
		final String include = getInclude(line);
		if (include != null && !isRecursed(include)) {
			ArrayList clone = (ArrayList) parents.clone();
			clone.add(this.fqn);
			return new ScriptFactory().getScript(getFQN(include), clone).modifiedScript();
		}
		return "";
	}

	private boolean isRecursed(final String include) {
		return parents.contains(getFQN(include));
	}

	String getFQN(String scriptName) {
		if (scriptName.indexOf("http://") != 0 && scriptName.indexOf("https://") != 0) {
			scriptName = base + scriptName;
		}
		return scriptName;
	}

	static String getInclude(String line) {
		final String re = ".*_include\\([\"|'](.*)[\"|']\\).*";
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(line.trim());
		if (m.matches()) {
			return line.substring(m.start(1), m.end(1));
		}
		return null;
	}


	static String getActionRegExp() {
		ArrayList keywords = getActionKeyWords();
		return getActionRegExp(keywords);
	}

	static String getActionRegExp(ArrayList keywords) {
		StringBuffer sb = new StringBuffer();
		int size = keywords.size();
		sb.append("^(?:");
		for (int i = 0; i < size; i++) {
			String keyword = (String) keywords.get(i);
			sb.append(keyword);
			if (i != size - 1)
				sb.append("|");
		}
		sb.append(")\\s*\\(.*");
		return sb.toString();
	}

	public static String modifyFunctionNames(String unmodified) {
		unmodified = stripSahiFromFunctionNames(unmodified);
		String modified = unmodified.replaceAll(REG_EXP_FOR_ADDING, "sahi$1$2");
		return modified;
	}

	public static String stripSahiFromFunctionNames(String unmodified) {
		String modified = unmodified.replaceAll(REG_EXP_FOR_STRIPPING, "$1$2");
		return modified;
	}

	static String getRegExp(boolean isForStripping) {
		ArrayList keywords = getKeyWords();
		return getRegExp(isForStripping, keywords);
	}

	static String getRegExp(boolean isForStripping, ArrayList keywords) {
		StringBuffer sb = new StringBuffer();
		int size = keywords.size();
		if (isForStripping)
			sb.append("sahi");
		sb.append("_?(");
		for (int i = 0; i < size; i++) {
			String keyword = (String) keywords.get(i);
			sb.append(keyword);
			if (i != size - 1)
				sb.append("|");
		}
		sb.append(")(\\s*\\()");
		return sb.toString();
	}

	static ArrayList getActionKeyWords() {
		if (actionKeywords == null) actionKeywords = loadActionKeyWords();
		return actionKeywords;
	}
	
	static ArrayList loadActionKeyWords() {
		ArrayList keywords = new ArrayList();
		keywords.addAll(loadKeyWords("scheduler"));
		return keywords;
	}
	
	static ArrayList getKeyWords() {
		if (normalKeywords == null) normalKeywords = loadKeyWords();
		return normalKeywords;
	}
	
	static ArrayList loadKeyWords() {
		ArrayList keywords = new ArrayList();
		keywords.addAll(loadKeyWords("scheduler"));
		keywords.addAll(loadKeyWords("normal"));
		return keywords;
	}

	static ArrayList loadKeyWords(String type) {
		ArrayList keywords = new ArrayList();
		Properties fns = new Properties();
		try {
			fns.load(new FileInputStream("../config/"+type+"_functions.txt"));
		}catch(Exception e) {
			
		}
		keywords.addAll(fns.keySet());
		return keywords;				
	}
	
	String separateVariables(String s) {
		StringBuffer sb = new StringBuffer();
		char c = ' ';
		char prev = ' ';
		boolean isVar = false;
		int len = s.length();
		for (int i = 0; i < len; i++) {
			c = s.charAt(i);
			if (c == '$' && prev != '\\' && i + 1 < len
					&& Character.isJavaIdentifierStart(s.charAt(i + 1))) {
				isVar = true;
				sb.append("\"+s_v(");
			}
			if (isVar && !(Character.isJavaIdentifierPart(c) || c == '.')) {
				sb.append(")+\"");
				isVar = false;
			}
			sb.append(c);
			prev = c;
		}
		return sb.toString();
	}

	protected String read(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int c = ' ';
		try {
			while ((c = in.read()) != -1) {
				out.write(c);
			}
		} catch (IOException ste) {
			ste.printStackTrace();
		}
		return new String(out.toByteArray());
	}

	public String getScriptName() {
		return scriptName;
	}

	protected void setNameAndBase(String url) {
		this.fqn = url;
		int lastIndexOfSlash = url.lastIndexOf("/");
		if (lastIndexOfSlash != -1) {
			this.scriptName = url.substring(lastIndexOfSlash + 1);
			this.base = url.substring(0, lastIndexOfSlash + 1);
		}
	}

	protected void init(String url) {
		setNameAndBase(url);
		loadScript(url);
	}

	protected abstract void loadScript(String url);

	public String getOriginal() {
		return original;
	}

	public String getOriginalInHTML() {
		return SahiScriptHTMLAdapter.createHTML(original);
	}
}