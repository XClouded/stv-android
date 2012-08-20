package xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParseHandler extends DefaultHandler {
	// Used to define what elements we are currently in
	private boolean inError = false;
	private boolean inResult = false;
	private boolean inNo = false;
	private boolean inName = false;
	private boolean inConfidence = false;
	private boolean inErrCode = false;
	private boolean inErrMsg = false;
	
	private NBestResult topOneResult = null;
	private NBestResult result = new NBestResult();
	private int errCode = 1;
	private String errMsg = "";
	
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		if (name.trim().compareToIgnoreCase("errors") == 0) {
			inError = true;
		} else if (name.trim().compareToIgnoreCase("result") == 0) {
			inResult = true;
		} else if(inResult) {
			if (name.trim().compareToIgnoreCase("no") == 0) {
				inNo = true;
			} else if (name.trim().compareToIgnoreCase("name") == 0) {
				inName = true;
			} else if (name.trim().compareToIgnoreCase("confidence") == 0) {
				inConfidence = true;
			}
		} else if(inError) {
			if (name.trim().compareToIgnoreCase("code") == 0) {
				inErrCode = true;
			} else if (name.trim().compareToIgnoreCase("description") == 0) {
				inErrMsg = true;
				errMsg = "";
			}
		}
	}

	public void endElement(String uri, String name, String qName)
			throws SAXException {
		if (name.trim().compareToIgnoreCase("errors") == 0) {
			inError = false;
		} else if (name.trim().compareToIgnoreCase("result") == 0) {
			inResult = false;
			if(result.no == 1) {
				topOneResult = new NBestResult();
				
				topOneResult.no = result.no;
				topOneResult.result = result.result;
				topOneResult.confidence = result.confidence;
			}
		}  else if(inResult) {
			if (name.trim().compareToIgnoreCase("no") == 0) {
				inNo = false;
			} else if (name.trim().compareToIgnoreCase("name") == 0) {
				inName = false;
			} else if (name.trim().compareToIgnoreCase("confidence") == 0) {
				inConfidence = false;
			}
		} else if(inError) {
			if (name.trim().compareToIgnoreCase("code") == 0) {
				inErrCode = false;
			} else if (name.trim().compareToIgnoreCase("description") == 0) {
				inErrMsg = false;
			}
		}
	}
	
	public void characters(char ch[], int start, int length) {
		String chars = (new String(ch).substring(start, start + length));
		
		System.out.println("chars        - " + chars);
		if(inNo) {
			result.no = Integer.parseInt(chars);
		} else if(inName) {
			result.result = chars;
		} else if(inConfidence) {
			result.confidence = Float.parseFloat(chars);
		} else if(inErrCode) {
			errCode = Integer.parseInt(chars);
		} else if(inErrMsg) {
			errMsg += chars;
		}
	}
	
	public void parseResults(InputSource i) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			xr.parse(i);
		} catch (IOException e) {
			System.out.println("XML parsing error: " + e.toString());
		} catch (SAXException e) {
			System.out.println("XML parsing error: " + e.toString());
		} catch (ParserConfigurationException e) {
			System.out.println("XML parsing error: " + e.toString());
		} 
	}
	
	public NBestResult GetResult() {
		return topOneResult;
	}
	
	public int GetErrCode() {
		return errCode;
	}
	public String GetErrMsg(){
		return errMsg;
	}
}
