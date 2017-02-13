package com.tsc9526.monalisa.tools.string;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;

public class MelpHtml {

	public static String html(Date date) {
		return html(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String html(Date date, String dateFormat) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			return sdf.format(date);
		}
	}

	public static String html(JsonElement v) {
		if (v == null || v.isJsonNull()) {
			return "";
		} else {
			return v.getAsString();
		}
	}

	public static String html(Object v) {
		if (v == null) {
			return "";
		} else {
			return escapeHtml(v.toString());
		}
	}

	public static String escapeHtml(String source) {
		if (source == null) {
			return "";
		}
  
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				case '&':
					buffer.append("&amp;");
					break;
				case '"':
					buffer.append("&quot;");
					break;
				case 10:
				case 13:
					break;
				default:
					buffer.append(c);
			}
		}

		return buffer.toString();
	}
}
