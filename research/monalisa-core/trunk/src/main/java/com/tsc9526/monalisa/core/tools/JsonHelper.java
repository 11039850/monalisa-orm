package com.tsc9526.monalisa.core.tools;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
 

/**
 * JSON Parser and Generator.
 * <p />
 * This class provides some static methods to convert POJOs to and from JSON
 * notation. The mapping from JSON to java is:
 *
 * <pre>
 *   object ==> Map
 *   array  ==> Object[]
 *   number ==> Double or Long
 *   string ==> String
 *   null   ==> null
 *   bool   ==> Boolean
 * </pre>
 * 
 * The java to JSON mapping is:
 *
 * <pre>
 *   String --> string
 *   Number --> number
 *   Map    --> object
 *   List   --> array
 *   Array  --> array
 *   null   --> null
 *   Boolean--> boolean
 *   Object --> string (dubious!)
 * </pre>
 *
 * The interface {@link JsonHelper.Convertible} may be implemented by classes that
 * wish to externalize and initialize specific fields to and from JSON objects.
 * Only directed acyclic graphs of objects are supported.
 * <p />
 * The interface {@link JsonHelper.Generator} may be implemented by classes that know
 * how to render themselves as JSON and the {@link #toString(Object)} method
 * will use {@link JsonHelper.Generator#addJSON(Appendable)} to generate the JSON. The
 * class {@link JsonHelper.Literal} may be used to hold pre-generated JSON object.
 * <p />
 * The interface {@link JsonHelper.Convertor} may be implemented to provide static
 * converters for objects that may be registered with
 * {@link #registerConvertor(Class, Convertor)}. These converters are looked up
 * by class, interface and super class by {@link #getConvertor(Class)}.
 * <p />
 * If a JSON object has a "class" field, then a java class for that name is
 * loaded and the method {@link #convertTo(Class,Map)} is used to find a
 * {@link JsonHelper.Convertor} for that class.
 * <p />
 * If a JSON object has a "x-class" field then a direct lookup for a
 * {@link JsonHelper.Convertor} for that class name is done (without loading the
 * class).
 */
public class JsonHelper {

	public final static JsonHelper DEFAULT = new JsonHelper();

	private Map<String, Convertor> _convertors = new ConcurrentHashMap<String, Convertor>();
	private int _stringBufferSize = 1024;

	public JsonHelper() {
	}

	/**
	 * @return the initial stringBuffer size to use when creating JSON strings
	 *         (default 1024)
	 */
	public int getStringBufferSize() {
		return _stringBufferSize;
	}

	/**
	 * @param stringBufferSize
	 *            the initial stringBuffer size to use when creating JSON
	 *            strings (default 1024)
	 */
	public void setStringBufferSize(int stringBufferSize) {
		_stringBufferSize = stringBufferSize;
	}

	/**
	 * Register a {@link Convertor} for a class or interface.
	 *
	 * @param forClass
	 *            The class or interface that the convertor applies to
	 * @param convertor
	 *            the convertor
	 */
	public static void registerConvertor(Class forClass, Convertor convertor) {
		DEFAULT.addConvertor(forClass, convertor);
	}

	public static JsonHelper getDefault() {
		return DEFAULT;
	}

	@Deprecated
	public static void setDefault(JsonHelper json) {
	}

	public static String toString(Object object) {
		StringBuilder buffer = new StringBuilder(DEFAULT.getStringBufferSize());
		DEFAULT.append(buffer, object);
		return buffer.toString();
	}

	public static String toString(Map object) {
		StringBuilder buffer = new StringBuilder(DEFAULT.getStringBufferSize());
		DEFAULT.appendMap(buffer, object);
		return buffer.toString();
	}

	public static String toString(Object[] array) {
		StringBuilder buffer = new StringBuilder(DEFAULT.getStringBufferSize());
		DEFAULT.appendArray(buffer, array);
		return buffer.toString();
	}

	/**
	 * @param s
	 *            String containing JSON object or array.
	 * @return A Map, Object array or primitive array parsed from the JSON.
	 */
	public static Object parse(String s) {
		return DEFAULT.parse(new StringSource(s), false);
	}

	/**
	 * @param s
	 *            String containing JSON object or array.
	 * @param stripOuterComment
	 *            If true, an outer comment around the JSON is ignored.
	 * @return A Map, Object array or primitive array parsed from the JSON.
	 */
	public static Object parse(String s, boolean stripOuterComment) {
		return DEFAULT.parse(new StringSource(s), stripOuterComment);
	}

	/**
	 * @param in
	 *            Reader containing JSON object or array.
	 * @return A Map, Object array or primitive array parsed from the JSON.
	 */
	public static Object parse(Reader in) throws IOException {
		return DEFAULT.parse(new ReaderSource(in), false);
	}

	/**
	 * @param in
	 *            Reader containing JSON object or array.
	 * @param stripOuterComment
	 *            If true, an outer comment around the JSON is ignored.
	 * @return A Map, Object array or primitive array parsed from the JSON.
	 */
	public static Object parse(Reader in, boolean stripOuterComment) throws IOException {
		return DEFAULT.parse(new ReaderSource(in), stripOuterComment);
	}

	/**
	 * Convert Object to JSON
	 *
	 * @param object
	 *            The object to convert
	 * @return The JSON String
	 */
	public String toJSON(Object object) {
		StringBuilder buffer = new StringBuilder(getStringBufferSize());
		append(buffer, object);
		return buffer.toString();
	}

	/**
	 * Convert JSON to Object
	 *
	 * @param json
	 *            The json to convert
	 * @return The object
	 */
	public Object fromJSON(String json) {
		Source source = new StringSource(json);
		return parse(source);
	}

	@Deprecated
	public void append(StringBuffer buffer, Object object) {
		append((Appendable) buffer, object);
	}

	/**
	 * Append object as JSON to string buffer.
	 *
	 * @param buffer
	 *            the buffer to append to
	 * @param object
	 *            the object to append
	 */
	public void append(Appendable buffer, Object object) {
		try {
			if (object == null) {
				buffer.append("null");
			}
			// Most likely first
			else if (object instanceof Map) {
				appendMap(buffer, (Map) object);
			} else if (object instanceof String) {
				appendString(buffer, (String) object);
			} else if (object instanceof Number) {
				appendNumber(buffer, (Number) object);
			} else if (object instanceof Boolean) {
				appendBoolean(buffer, (Boolean) object);
			} else if (object.getClass().isArray()) {
				appendArray(buffer, object);
			} else if (object instanceof Character) {
				appendString(buffer, object.toString());
			} else if (object instanceof Convertible) {
				appendJSON(buffer, (Convertible) object);
			} else if (object instanceof Generator) {
				appendJSON(buffer, (Generator) object);
			} else {
				// Check Convertor before Collection to support
				// JSONCollectionConvertor
				Convertor convertor = getConvertor(object.getClass());
				if (convertor != null) {
					appendJSON(buffer, convertor, object);
				} else if (object instanceof Collection) {
					appendArray(buffer, (Collection) object);
				} else {
					appendString(buffer, object.toString());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendNull(StringBuffer buffer) {
		appendNull((Appendable) buffer);
	}

	public void appendNull(Appendable buffer) {
		try {
			buffer.append("null");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendJSON(final StringBuffer buffer, final Convertor convertor, final Object object) {
		appendJSON((Appendable) buffer, convertor, object);
	}

	public void appendJSON(final Appendable buffer, final Convertor convertor, final Object object) {
		appendJSON(buffer, new Convertible() {
			public void fromJSON(Map object) {
			}

			public void toJSON(Output out) {
				convertor.toJSON(object, out);
			}
		});
	}

	@Deprecated
	public void appendJSON(final StringBuffer buffer, Convertible converter) {
		appendJSON((Appendable) buffer, converter);
	}

	public void appendJSON(final Appendable buffer, Convertible converter) {
		ConvertableOutput out = new ConvertableOutput(buffer);
		converter.toJSON(out);
		out.complete();
	}

	@Deprecated
	public void appendJSON(StringBuffer buffer, Generator generator) {
		generator.addJSON(buffer);
	}

	public void appendJSON(Appendable buffer, Generator generator) {
		generator.addJSON(buffer);
	}

	@Deprecated
	public void appendMap(StringBuffer buffer, Map<?, ?> map) {
		appendMap((Appendable) buffer, map);
	}

	public void appendMap(Appendable buffer, Map<?, ?> map) {
		try {
			if (map == null) {
				appendNull(buffer);
				return;
			}

			buffer.append('{');
			Iterator<?> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
				QuotedStringTokenizer.quote(buffer, entry.getKey().toString());
				buffer.append(':');
				append(buffer, entry.getValue());
				if (iter.hasNext())
					buffer.append(',');
			}

			buffer.append('}');
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendArray(StringBuffer buffer, Collection collection) {
		appendArray((Appendable) buffer, collection);
	}

	public void appendArray(Appendable buffer, Collection collection) {
		try {
			if (collection == null) {
				appendNull(buffer);
				return;
			}

			buffer.append('[');
			Iterator iter = collection.iterator();
			boolean first = true;
			while (iter.hasNext()) {
				if (!first)
					buffer.append(',');

				first = false;
				append(buffer, iter.next());
			}

			buffer.append(']');
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendArray(StringBuffer buffer, Object array) {
		appendArray((Appendable) buffer, array);
	}

	public void appendArray(Appendable buffer, Object array) {
		try {
			if (array == null) {
				appendNull(buffer);
				return;
			}

			buffer.append('[');
			int length = Array.getLength(array);

			for (int i = 0; i < length; i++) {
				if (i != 0)
					buffer.append(',');
				append(buffer, Array.get(array, i));
			}

			buffer.append(']');
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendBoolean(StringBuffer buffer, Boolean b) {
		appendBoolean((Appendable) buffer, b);
	}

	public void appendBoolean(Appendable buffer, Boolean b) {
		try {
			if (b == null) {
				appendNull(buffer);
				return;
			}
			buffer.append(b ? "true" : "false");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void appendNumber(StringBuffer buffer, Number number) {
		appendNumber((Appendable) buffer, number);
	}

	public void appendNumber(Appendable buffer, Number number) {
		try {
			if (number == null) {
				appendNull(buffer);
				return;
			}
			buffer.append(String.valueOf(number));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void appendString(Appendable buffer, String string) {
		if (string == null) {
			appendNull(buffer);
			return;
		}

		QuotedStringTokenizer.quote(buffer, string);
	}

	// Parsing utilities

	protected String toString(char[] buffer, int offset, int length) {
		return new String(buffer, offset, length);
	}

	protected Map<String, Object> newMap() {
		return new HashMap<String, Object>();
	}

	protected Object[] newArray(int size) {
		return new Object[size];
	}

	protected JsonHelper contextForArray() {
		return this;
	}

	protected JsonHelper contextFor(String field) {
		return this;
	}

	protected Object convertTo(Class type, Map map) {
		if (type != null && Convertible.class.isAssignableFrom(type)) {
			try {
				Convertible conv = (Convertible) type.newInstance();
				conv.fromJSON(map);
				return conv;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		Convertor convertor = getConvertor(type);
		if (convertor != null) {
			return convertor.fromJSON(map);
		}
		return map;
	}

	/**
	 * Register a {@link Convertor} for a class or interface.
	 *
	 * @param forClass
	 *            The class or interface that the convertor applies to
	 * @param convertor
	 *            the convertor
	 */
	public void addConvertor(Class forClass, Convertor convertor) {
		_convertors.put(forClass.getName(), convertor);
	}

	/**
	 * Lookup a convertor for a class.
	 * <p>
	 * If no match is found for the class, then the interfaces for the class are
	 * tried. If still no match is found, then the super class and it's
	 * interfaces are tried recursively.
	 *
	 * @param forClass
	 *            The class
	 * @return a {@link JsonHelper.Convertor} or null if none were found.
	 */
	protected Convertor getConvertor(Class forClass) {
		Class cls = forClass;
		Convertor convertor = _convertors.get(cls.getName());
		if (convertor == null && this != DEFAULT)
			convertor = DEFAULT.getConvertor(cls);

		while (convertor == null && cls != Object.class) {
			Class[] ifs = cls.getInterfaces();
			int i = 0;
			while (convertor == null && ifs != null && i < ifs.length)
				convertor = _convertors.get(ifs[i++].getName());
			if (convertor == null) {
				cls = cls.getSuperclass();
				convertor = _convertors.get(cls.getName());
			}
		}
		return convertor;
	}

	/**
	 * Register a {@link JsonHelper.Convertor} for a named class or interface.
	 *
	 * @param name
	 *            name of a class or an interface that the convertor applies to
	 * @param convertor
	 *            the convertor
	 */
	public void addConvertorFor(String name, Convertor convertor) {
		_convertors.put(name, convertor);
	}

	/**
	 * Lookup a convertor for a named class.
	 *
	 * @param name
	 *            name of the class
	 * @return a {@link JsonHelper.Convertor} or null if none were found.
	 */
	public Convertor getConvertorFor(String name) {
		Convertor convertor = _convertors.get(name);
		if (convertor == null && this != DEFAULT)
			convertor = DEFAULT.getConvertorFor(name);
		return convertor;
	}

	public Object parse(Source source, boolean stripOuterComment) {
		int comment_state = 0; // 0=no comment, 1="/", 2="/*", 3="/* *" -1="//"
		if (!stripOuterComment)
			return parse(source);

		int strip_state = 1; // 0=no strip, 1=wait for /*, 2= wait for */

		Object o = null;
		while (source.hasNext()) {
			char c = source.peek();

			// handle // or /* comment
			if (comment_state == 1) {
				switch (c) {
				case '/':
					comment_state = -1;
					break;
				case '*':
					comment_state = 2;
					if (strip_state == 1) {
						comment_state = 0;
						strip_state = 2;
					}
				}
			}
			// handle /* */ comment
			else if (comment_state > 1) {
				switch (c) {
				case '*':
					comment_state = 3;
					break;
				case '/':
					if (comment_state == 3) {
						comment_state = 0;
						if (strip_state == 2)
							return o;
					} else
						comment_state = 2;
					break;
				default:
					comment_state = 2;
				}
			}
			// handle // comment
			else if (comment_state < 0) {
				switch (c) {
				case '\r':
				case '\n':
					comment_state = 0;
				default:
					break;
				}
			}
			// handle unknown
			else {
				if (!Character.isWhitespace(c)) {
					if (c == '/')
						comment_state = 1;
					else if (c == '*')
						comment_state = 3;
					else if (o == null) {
						o = parse(source);
						continue;
					}
				}
			}

			source.next();
		}

		return o;
	}

	public Object parse(Source source) {
		int comment_state = 0; // 0=no comment, 1="/", 2="/*", 3="/* *" -1="//"

		while (source.hasNext()) {
			char c = source.peek();

			// handle // or /* comment
			if (comment_state == 1) {
				switch (c) {
				case '/':
					comment_state = -1;
					break;
				case '*':
					comment_state = 2;
				}
			}
			// handle /* */ comment
			else if (comment_state > 1) {
				switch (c) {
				case '*':
					comment_state = 3;
					break;
				case '/':
					if (comment_state == 3)
						comment_state = 0;
					else
						comment_state = 2;
					break;
				default:
					comment_state = 2;
				}
			}
			// handle // comment
			else if (comment_state < 0) {
				switch (c) {
				case '\r':
				case '\n':
					comment_state = 0;
					break;
				default:
					break;
				}
			}
			// handle unknown
			else {
				switch (c) {
				case '{':
					return parseObject(source);
				case '[':
					return parseArray(source);
				case '"':
					return parseString(source);
				case '-':
					return parseNumber(source);

				case 'n':
					complete("null", source);
					return null;
				case 't':
					complete("true", source);
					return Boolean.TRUE;
				case 'f':
					complete("false", source);
					return Boolean.FALSE;
				case 'u':
					complete("undefined", source);
					return null;
				case 'N':
					complete("NaN", source);
					return null;

				case '/':
					comment_state = 1;
					break;

				default:
					if (Character.isDigit(c))
						return parseNumber(source);
					else if (Character.isWhitespace(c))
						break;
					return handleUnknown(source, c);
				}
			}
			source.next();
		}

		return null;
	}

	protected Object handleUnknown(Source source, char c) {
		throw new IllegalStateException("unknown char '" + c + "'(" + (int) c + ") in " + source);
	}

	protected Object parseObject(Source source) {
		if (source.next() != '{')
			throw new IllegalStateException();
		Map<String, Object> map = newMap();

		char next = seekTo("\"}", source);

		while (source.hasNext()) {
			if (next == '}') {
				source.next();
				break;
			}

			String name = parseString(source);
			seekTo(':', source);
			source.next();

			Object value = contextFor(name).parse(source);
			map.put(name, value);

			seekTo(",}", source);
			next = source.next();
			if (next == '}')
				break;
			else
				next = seekTo("\"}", source);
		}

		String xclassname = (String) map.get("x-class");
		if (xclassname != null) {
			Convertor c = getConvertorFor(xclassname);
			if (c != null)
				return c.fromJSON(map);

		}

		return map;
	}

	protected Object parseArray(Source source) {
		if (source.next() != '[')
			throw new IllegalStateException();

		int size = 0;
		ArrayList list = null;
		Object item = null;
		boolean coma = true;

		while (source.hasNext()) {
			char c = source.peek();
			switch (c) {
			case ']':
				source.next();
				switch (size) {
				case 0:
					return newArray(0);
				case 1:
					Object array = newArray(1);
					Array.set(array, 0, item);
					return array;
				default:
					return list.toArray(newArray(list.size()));
				}

			case ',':
				if (coma)
					throw new IllegalStateException();
				coma = true;
				source.next();
				break;

			default:
				if (Character.isWhitespace(c))
					source.next();
				else {
					coma = false;
					if (size++ == 0)
						item = contextForArray().parse(source);
					else if (list == null) {
						list = new ArrayList();
						list.add(item);
						item = contextForArray().parse(source);
						list.add(item);
						item = null;
					} else {
						item = contextForArray().parse(source);
						list.add(item);
						item = null;
					}
				}
			}

		}

		throw new IllegalStateException("unexpected end of array");
	}

	protected String parseString(Source source) {
		if (source.next() != '"')
			throw new IllegalStateException();

		boolean escape = false;

		StringBuilder b = null;
		final char[] scratch = source.scratchBuffer();

		if (scratch != null) {
			int i = 0;
			while (source.hasNext()) {
				if (i >= scratch.length) {
					// we have filled the scratch buffer, so we must
					// use the StringBuffer for a large string
					b = new StringBuilder(scratch.length * 2);
					b.append(scratch, 0, i);
					break;
				}

				char c = source.next();

				if (escape) {
					escape = false;
					switch (c) {
					case '"':
						scratch[i++] = '"';
						break;
					case '\\':
						scratch[i++] = '\\';
						break;
					case '/':
						scratch[i++] = '/';
						break;
					case 'b':
						scratch[i++] = '\b';
						break;
					case 'f':
						scratch[i++] = '\f';
						break;
					case 'n':
						scratch[i++] = '\n';
						break;
					case 'r':
						scratch[i++] = '\r';
						break;
					case 't':
						scratch[i++] = '\t';
						break;
					case 'u':
						char uc = (char) ((TypeUtil.convertHexDigit((byte) source.next()) << 12) + (TypeUtil.convertHexDigit((byte) source.next()) << 8)
								+ (TypeUtil.convertHexDigit((byte) source.next()) << 4) + (TypeUtil.convertHexDigit((byte) source.next())));
						scratch[i++] = uc;
						break;
					default:
						scratch[i++] = c;
					}
				} else if (c == '\\') {
					escape = true;
				} else if (c == '\"') {
					// Return string that fits within scratch buffer
					return toString(scratch, 0, i);
				} else {
					scratch[i++] = c;
				}
			}

			// Missing end quote, but return string anyway ?
			if (b == null)
				return toString(scratch, 0, i);
		} else
			b = new StringBuilder(getStringBufferSize());

		// parse large string into string buffer
		final StringBuilder builder = b;
		while (source.hasNext()) {
			char c = source.next();

			if (escape) {
				escape = false;
				switch (c) {
				case '"':
					builder.append('"');
					break;
				case '\\':
					builder.append('\\');
					break;
				case '/':
					builder.append('/');
					break;
				case 'b':
					builder.append('\b');
					break;
				case 'f':
					builder.append('\f');
					break;
				case 'n':
					builder.append('\n');
					break;
				case 'r':
					builder.append('\r');
					break;
				case 't':
					builder.append('\t');
					break;
				case 'u':
					char uc = (char) ((TypeUtil.convertHexDigit((byte) source.next()) << 12) + (TypeUtil.convertHexDigit((byte) source.next()) << 8)
							+ (TypeUtil.convertHexDigit((byte) source.next()) << 4) + (TypeUtil.convertHexDigit((byte) source.next())));
					builder.append(uc);
					break;
				default:
					builder.append(c);
				}
			} else if (c == '\\') {
				escape = true;
			} else if (c == '\"') {
				break;
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	public Number parseNumber(Source source) {
		boolean minus = false;
		long number = 0;
		StringBuilder buffer = null;

		longLoop: while (source.hasNext()) {
			char c = source.peek();
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				number = number * 10 + (c - '0');
				source.next();
				break;

			case '-':
			case '+':
				if (number != 0)
					throw new IllegalStateException("bad number");
				minus = true;
				source.next();
				break;

			case '.':
			case 'e':
			case 'E':
				buffer = new StringBuilder(16);
				if (minus)
					buffer.append('-');
				buffer.append(number);
				buffer.append(c);
				source.next();
				break longLoop;

			default:
				break longLoop;
			}
		}

		if (buffer == null)
			return minus ? -1 * number : number;

		doubleLoop: while (source.hasNext()) {
			char c = source.peek();
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '-':
			case '.':
			case '+':
			case 'e':
			case 'E':
				buffer.append(c);
				source.next();
				break;

			default:
				break doubleLoop;
			}
		}
		return new Double(buffer.toString());

	}

	protected void seekTo(char seek, Source source) {
		while (source.hasNext()) {
			char c = source.peek();
			if (c == seek)
				return;

			if (!Character.isWhitespace(c))
				throw new IllegalStateException("Unexpected '" + c + " while seeking '" + seek + "'");
			source.next();
		}

		throw new IllegalStateException("Expected '" + seek + "'");
	}

	protected char seekTo(String seek, Source source) {
		while (source.hasNext()) {
			char c = source.peek();
			if (seek.indexOf(c) >= 0) {
				return c;
			}

			if (!Character.isWhitespace(c))
				throw new IllegalStateException("Unexpected '" + c + "' while seeking one of '" + seek + "'");
			source.next();
		}

		throw new IllegalStateException("Expected one of '" + seek + "'");
	}

	protected static void complete(String seek, Source source) {
		int i = 0;
		while (source.hasNext() && i < seek.length()) {
			char c = source.next();
			if (c != seek.charAt(i++))
				throw new IllegalStateException("Unexpected '" + c + " while seeking  \"" + seek + "\"");
		}

		if (i < seek.length())
			throw new IllegalStateException("Expected \"" + seek + "\"");
	}

	private final class ConvertableOutput implements Output {
		private final Appendable _buffer;
		char c = '{';

		private ConvertableOutput(Appendable buffer) {
			_buffer = buffer;
		}

		public void complete() {
			try {
				if (c == '{')
					_buffer.append("{}");
				else if (c != 0)
					_buffer.append("}");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void add(Object obj) {
			if (c == 0)
				throw new IllegalStateException();
			append(_buffer, obj);
			c = 0;
		}

		public void addClass(Class type) {
			try {
				if (c == 0)
					throw new IllegalStateException();
				_buffer.append(c);
				_buffer.append("\"class\":");
				append(_buffer, type.getName());
				c = ',';
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void add(String name, Object value) {
			try {
				if (c == 0)
					throw new IllegalStateException();
				_buffer.append(c);
				QuotedStringTokenizer.quote(_buffer, name);
				_buffer.append(':');
				append(_buffer, value);
				c = ',';
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void add(String name, double value) {
			try {
				if (c == 0)
					throw new IllegalStateException();
				_buffer.append(c);
				QuotedStringTokenizer.quote(_buffer, name);
				_buffer.append(':');
				appendNumber(_buffer, value);
				c = ',';
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void add(String name, long value) {
			try {
				if (c == 0)
					throw new IllegalStateException();
				_buffer.append(c);
				QuotedStringTokenizer.quote(_buffer, name);
				_buffer.append(':');
				appendNumber(_buffer, value);
				c = ',';
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void add(String name, boolean value) {
			try {
				if (c == 0)
					throw new IllegalStateException();
				_buffer.append(c);
				QuotedStringTokenizer.quote(_buffer, name);
				_buffer.append(':');
				appendBoolean(_buffer, value ? Boolean.TRUE : Boolean.FALSE);
				c = ',';
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public interface Source {
		boolean hasNext();

		char next();

		char peek();

		char[] scratchBuffer();
	}

	public static class StringSource implements Source {
		private final String string;
		private int index;
		private char[] scratch;

		public StringSource(String s) {
			string = s;
		}

		public boolean hasNext() {
			if (index < string.length())
				return true;
			scratch = null;
			return false;
		}

		public char next() {
			return string.charAt(index++);
		}

		public char peek() {
			return string.charAt(index);
		}

		@Override
		public String toString() {
			return string.substring(0, index) + "|||" + string.substring(index);
		}

		public char[] scratchBuffer() {
			if (scratch == null)
				scratch = new char[string.length()];
			return scratch;
		}
	}

	public static class ReaderSource implements Source {
		private Reader _reader;
		private int _next = -1;
		private char[] scratch;

		public ReaderSource(Reader r) {
			_reader = r;
		}

		public void setReader(Reader reader) {
			_reader = reader;
			_next = -1;
		}

		public boolean hasNext() {
			getNext();
			if (_next < 0) {
				scratch = null;
				return false;
			}
			return true;
		}

		public char next() {
			getNext();
			char c = (char) _next;
			_next = -1;
			return c;
		}

		public char peek() {
			getNext();
			return (char) _next;
		}

		private void getNext() {
			if (_next < 0) {
				try {
					_next = _reader.read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		public char[] scratchBuffer() {
			if (scratch == null)
				scratch = new char[1024];
			return scratch;
		}

	}

	/**
	 * JSON Output class for use by {@link Convertible}.
	 */
	public interface Output {
		public void addClass(Class c);

		public void add(Object obj);

		public void add(String name, Object value);

		public void add(String name, double value);

		public void add(String name, long value);

		public void add(String name, boolean value);
	}

	/* ------------------------------------------------------------ */
	/**
	 * JSON Convertible object. Object can implement this interface in a similar
	 * way to the {@link Externalizable} interface is used to allow classes to
	 * provide their own serialization mechanism.
	 * <p>
	 * A JSON.Convertible object may be written to a JSONObject or initialized
	 * from a Map of field names to values.
	 * <p>
	 * If the JSON is to be convertible back to an Object, then the method
	 * {@link Output#addClass(Class)} must be called from within toJSON()
	 *
	 */
	public interface Convertible {
		public void toJSON(Output out);

		public void fromJSON(Map object);
	}

	/**
	 * Static JSON Convertor.
	 * <p>
	 * may be implemented to provide static convertors for objects that may be
	 * registered with
	 * {@link JsonHelper#registerConvertor(Class, org.JsonHelper.jetty.util.ajax.JSON.Convertor)}
	 * . These convertors are looked up by class, interface and super class by
	 * {@link JsonHelper#getConvertor(Class)}. Convertors should be used when the
	 * classes to be converted cannot implement {@link Convertible} or
	 * {@link Generator}.
	 */
	public interface Convertor {
		public void toJSON(Object obj, Output out);

		public Object fromJSON(Map object);
	}

	/**
	 * JSON Generator. A class that can add it's JSON representation directly to
	 * a StringBuffer. This is useful for object instances that are frequently
	 * converted and wish to avoid multiple Conversions
	 */
	public interface Generator {
		public void addJSON(Appendable buffer);
	}

	/**
	 * A Literal JSON generator A utility instance of {@link JsonHelper.Generator}
	 * that holds a pre-generated string on JSON text.
	 */
	public static class Literal implements Generator {
		private String _json;

		/**
		 * Construct a literal JSON instance for use by
		 * {@link JsonHelper#toString(Object)}. If {@link Log#isDebugEnabled()} is
		 * true, the JSON will be parsed to check validity
		 *
		 * @param json
		 *            A literal JSON string.
		 */
		public Literal(String json) {

			_json = json;
		}

		@Override
		public String toString() {
			return _json;
		}

		public void addJSON(Appendable buffer) {
			try {
				buffer.append(_json);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class QuotedStringTokenizer extends StringTokenizer {
		private final static String __delim = "\t\n\r";
		private String _string;
		private String _delim = __delim;
		private boolean _returnQuotes = false;
		private boolean _returnDelimiters = false;
		private StringBuffer _token;
		private boolean _hasToken = false;
		private int _i = 0;
		private int _lastStart = 0;
		private boolean _double = true;
		private boolean _single = true;

		/* ------------------------------------------------------------ */
		public QuotedStringTokenizer(String str, String delim, boolean returnDelimiters, boolean returnQuotes) {
			super("");
			_string = str;
			if (delim != null)
				_delim = delim;
			_returnDelimiters = returnDelimiters;
			_returnQuotes = returnQuotes;

			if (_delim.indexOf('\'') >= 0 || _delim.indexOf('"') >= 0)
				throw new Error("Can't use quotes as delimiters: " + _delim);

			_token = new StringBuffer(_string.length() > 1024 ? 512 : _string.length() / 2);
		}

		/* ------------------------------------------------------------ */
		public QuotedStringTokenizer(String str, String delim, boolean returnDelimiters) {
			this(str, delim, returnDelimiters, false);
		}

		/* ------------------------------------------------------------ */
		public QuotedStringTokenizer(String str, String delim) {
			this(str, delim, false, false);
		}

		/* ------------------------------------------------------------ */
		public QuotedStringTokenizer(String str) {
			this(str, null, false, false);
		}

		/* ------------------------------------------------------------ */
		@Override
		public boolean hasMoreTokens() {
			// Already found a token
			if (_hasToken)
				return true;

			_lastStart = _i;

			int state = 0;
			boolean escape = false;
			while (_i < _string.length()) {
				char c = _string.charAt(_i++);

				switch (state) {
				case 0: // Start
					if (_delim.indexOf(c) >= 0) {
						if (_returnDelimiters) {
							_token.append(c);
							return _hasToken = true;
						}
					} else if (c == '\'' && _single) {
						if (_returnQuotes)
							_token.append(c);
						state = 2;
					} else if (c == '\"' && _double) {
						if (_returnQuotes)
							_token.append(c);
						state = 3;
					} else {
						_token.append(c);
						_hasToken = true;
						state = 1;
					}
					break;

				case 1: // Token
					_hasToken = true;
					if (_delim.indexOf(c) >= 0) {
						if (_returnDelimiters)
							_i--;
						return _hasToken;
					} else if (c == '\'' && _single) {
						if (_returnQuotes)
							_token.append(c);
						state = 2;
					} else if (c == '\"' && _double) {
						if (_returnQuotes)
							_token.append(c);
						state = 3;
					} else {
						_token.append(c);
					}
					break;

				case 2: // Single Quote
					_hasToken = true;
					if (escape) {
						escape = false;
						_token.append(c);
					} else if (c == '\'') {
						if (_returnQuotes)
							_token.append(c);
						state = 1;
					} else if (c == '\\') {
						if (_returnQuotes)
							_token.append(c);
						escape = true;
					} else {
						_token.append(c);
					}
					break;

				case 3: // Double Quote
					_hasToken = true;
					if (escape) {
						escape = false;
						_token.append(c);
					} else if (c == '\"') {
						if (_returnQuotes)
							_token.append(c);
						state = 1;
					} else if (c == '\\') {
						if (_returnQuotes)
							_token.append(c);
						escape = true;
					} else {
						_token.append(c);
					}
					break;
				}
			}

			return _hasToken;
		}

		/* ------------------------------------------------------------ */
		@Override
		public String nextToken() throws NoSuchElementException {
			if (!hasMoreTokens() || _token == null)
				throw new NoSuchElementException();
			String t = _token.toString();
			_token.setLength(0);
			_hasToken = false;
			return t;
		}

		/* ------------------------------------------------------------ */
		@Override
		public String nextToken(String delim) throws NoSuchElementException {
			_delim = delim;
			_i = _lastStart;
			_token.setLength(0);
			_hasToken = false;
			return nextToken();
		}

		/* ------------------------------------------------------------ */
		@Override
		public boolean hasMoreElements() {
			return hasMoreTokens();
		}

		/* ------------------------------------------------------------ */
		@Override
		public Object nextElement() throws NoSuchElementException {
			return nextToken();
		}

		/* ------------------------------------------------------------ */
		/**
		 * Not implemented.
		 */
		@Override
		public int countTokens() {
			return -1;
		}

		/* ------------------------------------------------------------ */
		/**
		 * Quote a string. The string is quoted only if quoting is required due
		 * to embedded delimiters, quote characters or the empty string.
		 * 
		 * @param s
		 *            The string to quote.
		 * @param delim
		 *            the delimiter to use to quote the string
		 * @return quoted string
		 */
		public static String quoteIfNeeded(String s, String delim) {
			if (s == null)
				return null;
			if (s.length() == 0)
				return "\"\"";

			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == '\\' || c == '"' || c == '\'' || Character.isWhitespace(c) || delim.indexOf(c) >= 0) {
					StringBuffer b = new StringBuffer(s.length() + 8);
					quote(b, s);
					return b.toString();
				}
			}

			return s;
		}

		/* ------------------------------------------------------------ */
		/**
		 * Quote a string. The string is quoted only if quoting is required due
		 * to embeded delimiters, quote characters or the empty string.
		 * 
		 * @param s
		 *            The string to quote.
		 * @return quoted string
		 */
		public static String quote(String s) {
			if (s == null)
				return null;
			if (s.length() == 0)
				return "\"\"";

			StringBuffer b = new StringBuffer(s.length() + 8);
			quote(b, s);
			return b.toString();

		}

		private static final char[] escapes = new char[32];
		static {
			Arrays.fill(escapes, (char) 0xFFFF);
			escapes['\b'] = 'b';
			escapes['\t'] = 't';
			escapes['\n'] = 'n';
			escapes['\f'] = 'f';
			escapes['\r'] = 'r';
		}

		/* ------------------------------------------------------------ */
		/**
		 * Quote a string into an Appendable. The characters ", \, \n, \r, \t,
		 * \f and \b are escaped
		 * 
		 * @param buffer
		 *            The Appendable
		 * @param input
		 *            The String to quote.
		 */
		public static void quote(Appendable buffer, String input) {
			try {
				buffer.append('"');
				for (int i = 0; i < input.length(); ++i) {
					char c = input.charAt(i);
					if (c >= 32) {
						if (c == '"' || c == '\\')
							buffer.append('\\');
						buffer.append(c);
					} else {
						char escape = escapes[c];
						if (escape == 0xFFFF) {
							// Unicode escape
							buffer.append('\\').append('u').append('0').append('0');
							if (c < 0x10)
								buffer.append('0');
							buffer.append(Integer.toString(c, 16));
						} else {
							buffer.append('\\').append(escape);
						}
					}
				}
				buffer.append('"');
			} catch (IOException x) {
				throw new RuntimeException(x);
			}
		}

		/* ------------------------------------------------------------ */
		/**
		 * Quote a string into a StringBuffer only if needed. Quotes are forced
		 * if any delim characters are present.
		 *
		 * @param buf
		 *            The StringBuffer
		 * @param s
		 *            The String to quote.
		 * @param delim
		 *            String of characters that must be quoted.
		 * @return true if quoted;
		 */
		public static boolean quoteIfNeeded(Appendable buf, String s, String delim) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (delim.indexOf(c) >= 0) {
					quote(buf, s);
					return true;
				}
			}

			try {
				buf.append(s);
				return false;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/* ------------------------------------------------------------ */
		public static String unquoteOnly(String s) {
			return unquoteOnly(s, false);
		}

		/* ------------------------------------------------------------ */
		/**
		 * Unquote a string, NOT converting unicode sequences
		 * 
		 * @param s
		 *            The string to unquote.
		 * @param lenient
		 *            if true, will leave in backslashes that aren't valid
		 *            escapes
		 * @return quoted string
		 */
		public static String unquoteOnly(String s, boolean lenient) {
			if (s == null)
				return null;
			if (s.length() < 2)
				return s;

			char first = s.charAt(0);
			char last = s.charAt(s.length() - 1);
			if (first != last || (first != '"' && first != '\''))
				return s;

			StringBuilder b = new StringBuilder(s.length() - 2);
			boolean escape = false;
			for (int i = 1; i < s.length() - 1; i++) {
				char c = s.charAt(i);

				if (escape) {
					escape = false;
					if (lenient && !isValidEscaping(c)) {
						b.append('\\');
					}
					b.append(c);
				} else if (c == '\\') {
					escape = true;
				} else {
					b.append(c);
				}
			}

			return b.toString();
		}

		/* ------------------------------------------------------------ */
		public static String unquote(String s) {
			return unquote(s, false);
		}

		/* ------------------------------------------------------------ */
		/**
		 * Unquote a string.
		 * 
		 * @param s
		 *            The string to unquote.
		 * @return quoted string
		 */
		public static String unquote(String s, boolean lenient) {
			if (s == null)
				return null;
			if (s.length() < 2)
				return s;

			char first = s.charAt(0);
			char last = s.charAt(s.length() - 1);
			if (first != last || (first != '"' && first != '\''))
				return s;

			StringBuilder b = new StringBuilder(s.length() - 2);
			boolean escape = false;
			for (int i = 1; i < s.length() - 1; i++) {
				char c = s.charAt(i);

				if (escape) {
					escape = false;
					switch (c) {
					case 'n':
						b.append('\n');
						break;
					case 'r':
						b.append('\r');
						break;
					case 't':
						b.append('\t');
						break;
					case 'f':
						b.append('\f');
						break;
					case 'b':
						b.append('\b');
						break;
					case '\\':
						b.append('\\');
						break;
					case '/':
						b.append('/');
						break;
					case '"':
						b.append('"');
						break;
					case 'u':
						b.append((char) ((TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 24) + (TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 16)
								+ (TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 8) + (TypeUtil.convertHexDigit((byte) s.charAt(i++)))));
						break;
					default:
						if (lenient && !isValidEscaping(c)) {
							b.append('\\');
						}
						b.append(c);
					}
				} else if (c == '\\') {
					escape = true;
				} else {
					b.append(c);
				}
			}

			return b.toString();
		}

		/* ------------------------------------------------------------ */
		/**
		 * Check that char c (which is preceded by a backslash) is a valid
		 * escape sequence.
		 * 
		 * @param c
		 * @return
		 */
		private static boolean isValidEscaping(char c) {
			return ((c == 'n') || (c == 'r') || (c == 't') || (c == 'f') || (c == 'b') || (c == '\\') || (c == '/') || (c == '"') || (c == 'u'));
		}

		/* ------------------------------------------------------------ */
		/**
		 * @return handle double quotes if true
		 */
		public boolean getDouble() {
			return _double;
		}

		/* ------------------------------------------------------------ */
		/**
		 * @param d
		 *            handle double quotes if true
		 */
		public void setDouble(boolean d) {
			_double = d;
		}

		/* ------------------------------------------------------------ */
		/**
		 * @return handle single quotes if true
		 */
		public boolean getSingle() {
			return _single;
		}

		/* ------------------------------------------------------------ */
		/**
		 * @param single
		 *            handle single quotes if true
		 */
		public void setSingle(boolean single) {
			_single = single;
		}
	}
	
	
	public static class TypeUtil
	{
	    
	    public static int CR = '\015';
	    public static int LF = '\012';

	    /* ------------------------------------------------------------ */
	    private static final HashMap<String, Class<?>> name2Class=new HashMap<String, Class<?>>();
	    static
	    {
	        name2Class.put("boolean",java.lang.Boolean.TYPE);
	        name2Class.put("byte",java.lang.Byte.TYPE);
	        name2Class.put("char",java.lang.Character.TYPE);
	        name2Class.put("double",java.lang.Double.TYPE);
	        name2Class.put("float",java.lang.Float.TYPE);
	        name2Class.put("int",java.lang.Integer.TYPE);
	        name2Class.put("long",java.lang.Long.TYPE);
	        name2Class.put("short",java.lang.Short.TYPE);
	        name2Class.put("void",java.lang.Void.TYPE);

	        name2Class.put("java.lang.Boolean.TYPE",java.lang.Boolean.TYPE);
	        name2Class.put("java.lang.Byte.TYPE",java.lang.Byte.TYPE);
	        name2Class.put("java.lang.Character.TYPE",java.lang.Character.TYPE);
	        name2Class.put("java.lang.Double.TYPE",java.lang.Double.TYPE);
	        name2Class.put("java.lang.Float.TYPE",java.lang.Float.TYPE);
	        name2Class.put("java.lang.Integer.TYPE",java.lang.Integer.TYPE);
	        name2Class.put("java.lang.Long.TYPE",java.lang.Long.TYPE);
	        name2Class.put("java.lang.Short.TYPE",java.lang.Short.TYPE);
	        name2Class.put("java.lang.Void.TYPE",java.lang.Void.TYPE);

	        name2Class.put("java.lang.Boolean",java.lang.Boolean.class);
	        name2Class.put("java.lang.Byte",java.lang.Byte.class);
	        name2Class.put("java.lang.Character",java.lang.Character.class);
	        name2Class.put("java.lang.Double",java.lang.Double.class);
	        name2Class.put("java.lang.Float",java.lang.Float.class);
	        name2Class.put("java.lang.Integer",java.lang.Integer.class);
	        name2Class.put("java.lang.Long",java.lang.Long.class);
	        name2Class.put("java.lang.Short",java.lang.Short.class);

	        name2Class.put("Boolean",java.lang.Boolean.class);
	        name2Class.put("Byte",java.lang.Byte.class);
	        name2Class.put("Character",java.lang.Character.class);
	        name2Class.put("Double",java.lang.Double.class);
	        name2Class.put("Float",java.lang.Float.class);
	        name2Class.put("Integer",java.lang.Integer.class);
	        name2Class.put("Long",java.lang.Long.class);
	        name2Class.put("Short",java.lang.Short.class);

	        name2Class.put(null,java.lang.Void.TYPE);
	        name2Class.put("string",java.lang.String.class);
	        name2Class.put("String",java.lang.String.class);
	        name2Class.put("java.lang.String",java.lang.String.class);
	    }

	    /* ------------------------------------------------------------ */
	    private static final HashMap<Class<?>, String> class2Name=new HashMap<Class<?>, String>();
	    static
	    {
	        class2Name.put(java.lang.Boolean.TYPE,"boolean");
	        class2Name.put(java.lang.Byte.TYPE,"byte");
	        class2Name.put(java.lang.Character.TYPE,"char");
	        class2Name.put(java.lang.Double.TYPE,"double");
	        class2Name.put(java.lang.Float.TYPE,"float");
	        class2Name.put(java.lang.Integer.TYPE,"int");
	        class2Name.put(java.lang.Long.TYPE,"long");
	        class2Name.put(java.lang.Short.TYPE,"short");
	        class2Name.put(java.lang.Void.TYPE,"void");

	        class2Name.put(java.lang.Boolean.class,"java.lang.Boolean");
	        class2Name.put(java.lang.Byte.class,"java.lang.Byte");
	        class2Name.put(java.lang.Character.class,"java.lang.Character");
	        class2Name.put(java.lang.Double.class,"java.lang.Double");
	        class2Name.put(java.lang.Float.class,"java.lang.Float");
	        class2Name.put(java.lang.Integer.class,"java.lang.Integer");
	        class2Name.put(java.lang.Long.class,"java.lang.Long");
	        class2Name.put(java.lang.Short.class,"java.lang.Short");

	        class2Name.put(null,"void");
	        class2Name.put(java.lang.String.class,"java.lang.String");
	    }

	    /* ------------------------------------------------------------ */
	    private static final HashMap<Class<?>, Method> class2Value=new HashMap<Class<?>, Method>();
	    static
	    {
	        try
	        {
	            Class<?>[] s ={java.lang.String.class};

	            class2Value.put(java.lang.Boolean.TYPE,
	                           java.lang.Boolean.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Byte.TYPE,
	                           java.lang.Byte.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Double.TYPE,
	                           java.lang.Double.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Float.TYPE,
	                           java.lang.Float.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Integer.TYPE,
	                           java.lang.Integer.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Long.TYPE,
	                           java.lang.Long.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Short.TYPE,
	                           java.lang.Short.class.getMethod("valueOf",s));

	            class2Value.put(java.lang.Boolean.class,
	                           java.lang.Boolean.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Byte.class,
	                           java.lang.Byte.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Double.class,
	                           java.lang.Double.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Float.class,
	                           java.lang.Float.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Integer.class,
	                           java.lang.Integer.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Long.class,
	                           java.lang.Long.class.getMethod("valueOf",s));
	            class2Value.put(java.lang.Short.class,
	                           java.lang.Short.class.getMethod("valueOf",s));
	        }
	        catch(Exception e)
	        {
	            throw new Error(e);
	        }
	    }

	    /* ------------------------------------------------------------ */
	    /** Array to List.
	     * <p>
	     * Works like {@link Arrays#asList(Object...)}, but handles null arrays.
	     * @return a list backed by the array.
	     */
	    public static <T> List<T> asList(T[] a) 
	    {
	        if (a==null)
	            return Collections.emptyList();
	        return Arrays.asList(a);
	    }
	    
	    /* ------------------------------------------------------------ */
	    /** Class from a canonical name for a type.
	     * @param name A class or type name.
	     * @return A class , which may be a primitive TYPE field..
	     */
	    public static Class<?> fromName(String name)
	    {
	        return name2Class.get(name);
	    }

	    /* ------------------------------------------------------------ */
	    /** Canonical name for a type.
	     * @param type A class , which may be a primitive TYPE field.
	     * @return Canonical name.
	     */
	    public static String toName(Class<?> type)
	    {
	        return class2Name.get(type);
	    }

	    /* ------------------------------------------------------------ */
	    /** Convert String value to instance.
	     * @param type The class of the instance, which may be a primitive TYPE field.
	     * @param value The value as a string.
	     * @return The value as an Object.
	     */
	    public static Object valueOf(Class<?> type, String value)
	    {
	        try
	        {
	            if (type.equals(java.lang.String.class))
	                return value;

	            Method m = class2Value.get(type);
	            if (m!=null)
	                return m.invoke(null, value);

	            if (type.equals(java.lang.Character.TYPE) ||
	                type.equals(java.lang.Character.class))
	                return new Character(value.charAt(0));

	            Constructor<?> c = type.getConstructor(java.lang.String.class);
	            return c.newInstance(value);
	        }
	        catch(NoSuchMethodException e)
	        {
	            // LogSupport.ignore(log,e);
	        }
	        catch(IllegalAccessException e)
	        {
	            // LogSupport.ignore(log,e);
	        }
	        catch(InstantiationException e)
	        {
	            // LogSupport.ignore(log,e);
	        }
	        catch(InvocationTargetException e)
	        {
	            if (e.getTargetException() instanceof Error)
	                throw (Error)(e.getTargetException());
	            // LogSupport.ignore(log,e);
	        }
	        return null;
	    }

	    /* ------------------------------------------------------------ */
	    /** Convert String value to instance.
	     * @param type classname or type (eg int)
	     * @param value The value as a string.
	     * @return The value as an Object.
	     */
	    public static Object valueOf(String type, String value)
	    {
	        return valueOf(fromName(type),value);
	    }

	    /* ------------------------------------------------------------ */
	    /** Parse an int from a substring.
	     * Negative numbers are not handled.
	     * @param s String
	     * @param offset Offset within string
	     * @param length Length of integer or -1 for remainder of string
	     * @param base base of the integer
	     * @return the parsed integer
	     * @throws NumberFormatException if the string cannot be parsed
	     */
	    public static int parseInt(String s, int offset, int length, int base)
	        throws NumberFormatException
	    {
	        int value=0;

	        if (length<0)
	            length=s.length()-offset;

	        for (int i=0;i<length;i++)
	        {
	            char c=s.charAt(offset+i);

	            int digit=convertHexDigit((int)c);
	            if (digit<0 || digit>=base)
	                throw new NumberFormatException(s.substring(offset,offset+length));
	            value=value*base+digit;
	        }
	        return value;
	    }

	    /* ------------------------------------------------------------ */
	    /** Parse an int from a byte array of ascii characters.
	     * Negative numbers are not handled.
	     * @param b byte array
	     * @param offset Offset within string
	     * @param length Length of integer or -1 for remainder of string
	     * @param base base of the integer
	     * @return the parsed integer
	     * @throws NumberFormatException if the array cannot be parsed into an integer
	     */
	    public static int parseInt(byte[] b, int offset, int length, int base)
	        throws NumberFormatException
	    {
	        int value=0;

	        if (length<0)
	            length=b.length-offset;

	        for (int i=0;i<length;i++)
	        {
	            char c=(char)(0xff&b[offset+i]);

	            int digit=c-'0';
	            if (digit<0 || digit>=base || digit>=10)
	            {
	                digit=10+c-'A';
	                if (digit<10 || digit>=base)
	                    digit=10+c-'a';
	            }
	            if (digit<0 || digit>=base)
	                throw new NumberFormatException(new String(b,offset,length));
	            value=value*base+digit;
	        }
	        return value;
	    }

	    /* ------------------------------------------------------------ */
	    public static byte[] parseBytes(String s, int base)
	    {
	        byte[] bytes=new byte[s.length()/2];
	        for (int i=0;i<s.length();i+=2)
	            bytes[i/2]=(byte)TypeUtil.parseInt(s,i,2,base);
	        return bytes;
	    }

	    /* ------------------------------------------------------------ */
	    public static String toString(byte[] bytes, int base)
	    {
	        StringBuilder buf = new StringBuilder();
	        for (byte b : bytes)
	        {
	            int bi=0xff&b;
	            int c='0'+(bi/base)%base;
	            if (c>'9')
	                c= 'a'+(c-'0'-10);
	            buf.append((char)c);
	            c='0'+bi%base;
	            if (c>'9')
	                c= 'a'+(c-'0'-10);
	            buf.append((char)c);
	        }
	        return buf.toString();
	    }

	    /* ------------------------------------------------------------ */
	    /**
	     * @param c An ASCII encoded character 0-9 a-f A-F
	     * @return The byte value of the character 0-16.
	     */
	    public static byte convertHexDigit( byte c )
	    {
	        byte b = (byte)((c & 0x1f) + ((c >> 6) * 0x19) - 0x10);
	        if (b<0 || b>15)
	            throw new IllegalArgumentException("!hex "+c);
	        return b;
	    }
	    
	    /* ------------------------------------------------------------ */
	    /**
	     * @param c An ASCII encoded character 0-9 a-f A-F
	     * @return The byte value of the character 0-16.
	     */
	    public static int convertHexDigit( int c )
	    {
	        int d= ((c & 0x1f) + ((c >> 6) * 0x19) - 0x10);
	        if (d<0 || d>15)
	            throw new NumberFormatException("!hex "+c);
	        return d;
	    }

	    /* ------------------------------------------------------------ */
	    public static void toHex(byte b,Appendable buf)
	    {
	        try
	        {
	            int d=0xf&((0xF0&b)>>4);
	            buf.append((char)((d>9?('A'-10):'0')+d));
	            d=0xf&b;
	            buf.append((char)((d>9?('A'-10):'0')+d));
	        }
	        catch(IOException e)
	        {
	            throw new RuntimeException(e);
	        }
	    }

	    /* ------------------------------------------------------------ */
	    public static void toHex(int value,Appendable buf) throws IOException
	    {
	        int d=0xf&((0xF0000000&value)>>28);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x0F000000&value)>>24);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x00F00000&value)>>20);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x000F0000&value)>>16);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x0000F000&value)>>12);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x00000F00&value)>>8);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&((0x000000F0&value)>>4);
	        buf.append((char)((d>9?('A'-10):'0')+d));
	        d=0xf&value;
	        buf.append((char)((d>9?('A'-10):'0')+d));
	    
	        Integer.toString(0,36);
	    }
	    
	    
	    /* ------------------------------------------------------------ */
	    public static void toHex(long value,Appendable buf) throws IOException
	    {
	        toHex((int)(value>>32),buf);
	        toHex((int)value,buf);
	    }

	    /* ------------------------------------------------------------ */
	    public static String toHexString(byte b)
	    {
	        return toHexString(new byte[]{b}, 0, 1);
	    }
	    
	    /* ------------------------------------------------------------ */
	    public static String toHexString(byte[] b)
	    {
	        return toHexString(b, 0, b.length);
	    }

	    /* ------------------------------------------------------------ */
	    public static String toHexString(byte[] b,int offset,int length)
	    {
	        StringBuilder buf = new StringBuilder();
	        for (int i=offset;i<offset+length;i++)
	        {
	            int bi=0xff&b[i];
	            int c='0'+(bi/16)%16;
	            if (c>'9')
	                c= 'A'+(c-'0'-10);
	            buf.append((char)c);
	            c='0'+bi%16;
	            if (c>'9')
	                c= 'a'+(c-'0'-10);
	            buf.append((char)c);
	        }
	        return buf.toString();
	    }

	    /* ------------------------------------------------------------ */
	    public static byte[] fromHexString(String s)
	    {
	        if (s.length()%2!=0)
	            throw new IllegalArgumentException(s);
	        byte[] array = new byte[s.length()/2];
	        for (int i=0;i<array.length;i++)
	        {
	            int b = Integer.parseInt(s.substring(i*2,i*2+2),16);
	            array[i]=(byte)(0xff&b);
	        }
	        return array;
	    }


	    public static void dump(Class<?> c)
	    {
	        System.err.println("Dump: "+c);
	        dump(c.getClassLoader());
	    }

	    public static void dump(ClassLoader cl)
	    {
	        System.err.println("Dump Loaders:");
	        while(cl!=null)
	        {
	            System.err.println("  loader "+cl);
	            cl = cl.getParent();
	        }
	    }


	    /* ------------------------------------------------------------ */
	    /**
	     * @deprecated
	     */
	    public static byte[] readLine(InputStream in) throws IOException
	    {
	        byte[] buf = new byte[256];

	        int i=0;
	        int loops=0;
	        int ch=0;

	        while (true)
	        {
	            ch=in.read();
	            if (ch<0)
	                break;
	            loops++;

	            // skip a leading LF's
	            if (loops==1 && ch==LF)
	                continue;

	            if (ch==CR || ch==LF)
	                break;

	            if (i>=buf.length)
	            {
	                byte[] old_buf=buf;
	                buf=new byte[old_buf.length+256];
	                System.arraycopy(old_buf, 0, buf, 0, old_buf.length);
	            }
	            buf[i++]=(byte)ch;
	        }

	        if (ch==-1 && i==0)
	            return null;

	        // skip a trailing LF if it exists
	        if (ch==CR && in.available()>=1 && in.markSupported())
	        {
	            in.mark(1);
	            ch=in.read();
	            if (ch!=LF)
	                in.reset();
	        }

	        byte[] old_buf=buf;
	        buf=new byte[i];
	        System.arraycopy(old_buf, 0, buf, 0, i);

	        return buf;
	    }

	     
	    
	    public static Object call(Class<?> oClass, String method, Object obj, Object[] arg) 
	       throws InvocationTargetException, NoSuchMethodException
	    {
	        // Lets just try all methods for now
	        Method[] methods = oClass.getMethods();
	        for (int c = 0; methods != null && c < methods.length; c++)
	        {
	            if (!methods[c].getName().equals(method))
	                continue;
	            if (methods[c].getParameterTypes().length != arg.length)
	                continue;
	            if (Modifier.isStatic(methods[c].getModifiers()) != (obj == null))
	                continue;
	            if ((obj == null) && methods[c].getDeclaringClass() != oClass)
	                continue;

	            try
	            {
	                return methods[c].invoke(obj,arg);
	            }
	            catch (IllegalAccessException e)
	            {
	                 
	            }
	            catch (IllegalArgumentException e)
	            {
	                
	            }
	        }

	        throw new NoSuchMethodException(method);
	    }
	}
}
