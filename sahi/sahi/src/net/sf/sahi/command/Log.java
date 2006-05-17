package net.sf.sahi.command;

import net.sf.sahi.request.HttpRequest;
import net.sf.sahi.response.HttpFileResponse;
import net.sf.sahi.response.HttpResponse;
import net.sf.sahi.session.Session;
import net.sf.sahi.util.URLParser;

public class Log {
	public HttpResponse highlight(HttpRequest requestFromBrowser) {
		int lineNumber = getLineNumber(requestFromBrowser);
		String fileName = URLParser.scriptFileNamefromURI(
				requestFromBrowser.uri(), "/Log_highlight/");
		final HttpFileResponse response = new HttpFileResponse(fileName, null, false, false);
		if (lineNumber != -1) {
			response
					.setData(("<html><body><style>b{color:brown}</style><pre>"
							+ Log.highlight(new String(
									response.data()), lineNumber) + "</pre></body></html>")
							.getBytes());
		}
		response.addHeader("Content-type", "text/html");
		response.resetRawHeaders();
		// System.out.println(new String(response.data()));
		return response;
	}


	public void execute(HttpRequest request) {
		Session session = request.session();
		if (session.getScript() != null) {
			session.logPlayBack(request.getParameter("msg"),
					request.getParameter("type"),
					request.getParameter("debugInfo"));
		}
	}

	private int getLineNumber(HttpRequest req) {
		String p = req.getParameter("n");
		int i = -1;
		try {
			i = Integer.parseInt(p);
		} catch (Exception e) {
		}
		return i;
	}

	public static String highlight(String s, int lineNumber) {
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		int startIx = 0;
		int endIx = -1;
		int len = s.length();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<lineNumber; i++) {
			startIx = endIx+1;
			endIx = s.indexOf("\n", startIx);
			if (endIx == -1) break;
		}
		if (endIx==-1) endIx = len;
		sb.append(s.substring(0, startIx));
		sb.append("<b>");
		sb.append(s.substring(startIx, endIx));
		sb.append("</b>");
		sb.append(s.substring(endIx, len));
		return sb.toString();
	}

}