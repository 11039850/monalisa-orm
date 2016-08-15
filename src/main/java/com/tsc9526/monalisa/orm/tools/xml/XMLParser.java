/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.tools.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class XMLParser {
	private DocumentBuilder builder;
	private XPath xpath;
	 
	public XMLParser() throws ParserConfigurationException {
		DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
		builder = domfactory.newDocumentBuilder();

		XPathFactory xpfactory = XPathFactory.newInstance();
		xpath = xpfactory.newXPath();
	}
 
	public Document parseDocument(String path) throws IOException, SAXException {
		return builder.parse(path);
	}

	 
	public Document parseDocument(File file) throws IOException, SAXException {
		return builder.parse(file);
	}

	 
	public Document parseDocument(InputStream is) throws IOException, SAXException {
		return builder.parse(is);
	}

	 
	public NodeList selectNodes(Node node, String expression) throws XPathExpressionException {
		// XPath对象编译XPath表达式
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.NODESET);
		return (NodeList) object;
	}

	 
	public Node selectSingleNode(Node node, String expression) throws XPathExpressionException {
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.NODE);
		return (Node) object;
	}
 
	public String getNodeStringValue(Node node, String expression) throws XPathExpressionException {
		XPathExpression xpexpreesion = this.xpath.compile(expression);
		Object object = xpexpreesion.evaluate(node, XPathConstants.STRING);
		return (String) object;
	}
}
