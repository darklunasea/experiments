package com.nxiao.service.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParser 
{
	SAXParser parser;
	CusipHandler cusipHandler;

	public XmlParser() throws ParserConfigurationException, SAXException 
	{
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		parser = parserFactor.newSAXParser();
		cusipHandler = new CusipHandler();
	}

	public String getCusip(String xml) throws SAXException, IOException
	{
		parser.parse(new InputSource(new StringReader(xml)), cusipHandler);
		return cusipHandler.cusip;
	}
}

class CusipHandler extends DefaultHandler 
{
	String cusip;	
	private String content = null;

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch (qName) {
		case "cusip":
			cusip = content;
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
