package set;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

public class Config {

	private String mFilePath = "";

	private String mIP = "124.16.130.176";
	private String mUserName = "jjwang";
	private boolean mIfDataKeep = false;
	private boolean mIfLogKeep = false;
	private int mOncePostLen = 4 * 1024;
	private int mTimeOut = 20 * 1000;
	
	private String mLogFile = "";
	private Context mContext;

	private void init(){
		
		File fileCfg = new File(mFilePath);
		
		//如果配置文件不存在，从asset中导入
		if (!fileCfg.exists())
			writeFromAssets();
		else {
			//如果系统文件读取失败，则重新从asset导入配置文件
			if (!read())
				writeFromAssets();}
		
	}
	
	public Config(Context context) {
		mContext = context;
		String filesPath = mContext.getFilesDir().getPath();
		mFilePath = filesPath + "/" + "config.xml";
		//mFilePath = "/data/data/cn.thinkit.libtmfe.test/files/config.xml";
		init();
	}

	public Config(String path, Context context) {
		mContext = context;
		mFilePath = path;
		init();
	}

	private void writeFromAssets() {
		String cfgPath = mFilePath;

		File fileCfg = new File(cfgPath);
		
		if (fileCfg.exists()) fileCfg.delete();		 
		if (!fileCfg.exists()) {

			try {
				InputStream in = mContext.getAssets().open("config.xml");

				byte[] b = new byte[in.available()];
				in.read(b);

				fileCfg.createNewFile();
				BufferedOutputStream bufferedStream = new BufferedOutputStream(
						new FileOutputStream(fileCfg));
				DataOutputStream dataOutputStream = new DataOutputStream(
						bufferedStream);
				dataOutputStream.write(b);
				dataOutputStream.close();

			} catch (FileNotFoundException e) {
				throw new IllegalStateException("Cannot Open File", e);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void write(){

		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			// serializer.startDocument("UTF-8",true);
			serializer.startDocument("utf-8", true);

			// <MfeOnlineDemo>
			serializer.startTag("", "MfeOnlineDemo");

			// <HttpPost> ... </HttpPost>
			writeHttpPost(serializer);

			// <Debug> ... </Debug>
			writeDebug(serializer);
			
			// </MfeOnlineDemo>
			serializer.endTag("", "MfeOnlineDemo");
			serializer.endDocument();

			String str = writer.toString();
			File txt = new File(mFilePath);
			if (!txt.exists()) {
				txt.createNewFile();
			}
			byte bytes[] = new byte[1024];
			bytes = str.getBytes(); // 新加的
			FileOutputStream fos = new FileOutputStream(txt);
			fos.write(bytes, 0, bytes.length);
			fos.close();

			// return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void writeHttpPost(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

		// <HttpPost>
		serializer.startTag("", "HttpPost");

		// <IP>124.16.130.176</IP>
		serializer.startTag("", "IP");
		serializer.text(mIP);
		serializer.endTag("", "IP");
		// <USERNAME>jjwang</USERNAME>
		serializer.startTag("", "USERNAME");
		serializer.text(mUserName);
		serializer.endTag("", "USERNAME");
		// <ONCEPOST_LEN>4096</ONCEPOST_LEN>
		serializer.startTag("", "ONCEPOST_LEN");
		String onceLen = Integer.toString(mOncePostLen);
		serializer.text(onceLen);
		serializer.endTag("", "ONCEPOST_LEN");
		// <CONNECT_TIMEOUT>100000</CONNECT_TIMEOUT>
		serializer.startTag("", "CONNECT_TIMEOUT");
		String timeOut = Integer.toString(mTimeOut);
		serializer.text(timeOut);
		serializer.endTag("", "CONNECT_TIMEOUT");

		// </HttpPost>
		serializer.endTag("", "HttpPost");
	}

	private void writeDebug(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

		String ifKeep = "";
		if (mIfDataKeep) {
			ifKeep = "true";
		} else {
			ifKeep = "false";
		}

		String ifTrace= "";
		if (mIfLogKeep) {
			ifTrace = "true";
		} else {
			ifTrace = "false";
		}

		// <Debug>
		serializer.startTag("", "Debug");

		// <SYSDATA_KEEP>true</SYSDATA_KEEP>
		serializer.startTag("", "SYSDATA_KEEP");
		serializer.text(ifKeep);
		serializer.endTag("", "SYSDATA_KEEP");
		// <PROCESS_TRACE>true</PROCESS_TRACE>
		serializer.startTag("", "PROCESS_TRACE");
		serializer.text(ifTrace);
		serializer.endTag("", "PROCESS_TRACE");

		// </Debug>
		serializer.endTag("", "Debug");
	}

	// ReadXML
	public boolean read() {
		boolean result = false;
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream is = new FileInputStream(mFilePath);
			doc = docBuilder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList books = root.getElementsByTagName("MfeOnlineDemo");
			//NodeList books = root.getElementsByTagName("HttpPost");
			if (books != null && books.getLength()>0) {
				Node book = books.item(0);

				for (Node node = book.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String name = node.getNodeName();
						if (name.compareToIgnoreCase("HttpPost") == 0) {
							readHttpPost(node);
						} else if (name.compareToIgnoreCase("Debug") == 0) {
							//readTemporaryPermission(node);
							readDebug(node);
						}
					}
					if (node == book.getLastChild())
						break;
				}
				result = true;
			}

		} catch (IOException e) {
			System.out.println("IOException " + e.toString());
			result = false;
		} catch (SAXException e) {
			System.out.println("SAXException " + e.toString());
			result = false;
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException " + e.toString());
			result = false;
		} finally {
			doc = null;
			docBuilder = null;
			docBuilderFactory = null;
		}
		return result;
	}

	public void readHttpPost(Node HttpPost) {
		for (Node node = HttpPost.getFirstChild(); node != null; node = node
				.getNextSibling()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String name = node.getNodeName();
				String value = node.getFirstChild().getNodeValue();
				// System.out.println("name: " + name + " value=" + value);

				if (name.compareToIgnoreCase("IP") == 0) {
					mIP = value;
				} else if (name.compareToIgnoreCase("USERNAME") == 0) {
					mUserName = value;
				} else if (name.compareToIgnoreCase("ONCEPOST_LEN") == 0) {
					mOncePostLen = Integer.parseInt(value);
				} else if (name.compareToIgnoreCase("CONNECT_TIMEOUT") == 0) {
					mTimeOut = Integer.parseInt(value);
				}
			}
			if (node == HttpPost.getLastChild())
				break;
		}
		if(mIP == null)
			mIP = "";
		if(mUserName == null)
			mUserName = "";	
	}

	public void readDebug(Node Debug) {
		for (Node node = Debug.getFirstChild(); node != null; node = node
				.getNextSibling()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String name = node.getNodeName();
				String value = node.getFirstChild().getNodeValue();
				// System.out.println("name: " + name + " value=" + value);

				if (name.compareToIgnoreCase("SYSDATA_KEEP") == 0) {
					if (value.compareToIgnoreCase("true") == 0)
						mIfDataKeep = true;
					else
						mIfDataKeep = false;
				} else if (name.compareToIgnoreCase("PROCESS_TRACE") == 0) {
					if (value.compareToIgnoreCase("true") == 0)
						mIfLogKeep = true;
					else
						mIfLogKeep = false;
				}
			}
			if (node == Debug.getLastChild())
				break;
		}
	}

	public String getIP() {
		return mIP;
	}

	public String getUserName() {
		return mUserName;
	}

	public int getOncePostLen() {
		return mOncePostLen;
	}

	public int getTimeOut() {
		return mTimeOut;
	}

	public boolean getIfDataKeep() {
		return mIfDataKeep;
	}

	public boolean getIfLogKeep() {
		return mIfLogKeep;
	}

	public void setIP(String IP) {
		mIP = IP;
	}

	public void setUserName(String UserName) {
		mUserName = UserName;
	}

	public void setOncePostLen(int OncePostLen) {
		mOncePostLen = OncePostLen;
	}

	public void setTimeOut(int TimeOut) {
		mTimeOut = TimeOut;
	}

	private boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	} 
	
	// 检查log目录是否存在
	public boolean logDirExists() {		
		if(!checkSDCard())
			return false;
		String sdDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File sdcard = new File(sdDir);
		if (sdcard.exists()) {
			String logDir = sdDir + "/.MfeOnline";
			File logFolder = new File(logDir);
			if (!logFolder.exists()) {
				logFolder.mkdirs();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public String getLogDir() {
		String sdDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String logDir = sdDir + "/.MfeOnline";
		return logDir;
	}
	
	public void setLogFile(String logFile) {
		mLogFile = logFile;
	}

	public String getLogFile() {
		if (mLogFile == null || mLogFile.compareTo("") == 0) 
			mLogFile = getLogDir()+ "/trace.htm";
		
		return mLogFile;
	}
	
	public void setIfSysDataKeep(boolean IfDataKeep) {		
		mIfDataKeep = IfDataKeep;
	}

	public void setIfProcessTrace(boolean IfLogKeep) {
		mIfLogKeep = IfLogKeep;
	}
}
