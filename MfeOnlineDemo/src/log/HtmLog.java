package log;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.thinkit.libtmfe.test.MFE;

public class HtmLog {
	private boolean mTabFlag = false;
	//log����·��
	public String mFileName =  MFE.mCfg.getLogDir()+ "/trace.htm";
	private final String tHead = "<table width=\"100%\" frame=\"void\">";
	private final String tEnd = "</table><br>";
	private final String tCaptionStytle = " align=\"left\"";
	private final String thStyleLeft = " width=\"30%\" style= \"border:1px solid #000000; border-width:1 1 1 0\"";
	private final String thStyleRight = " width=\"70%\"  style= \"border:1px solid #000000; border-width:1 0 1 1\"";
	private final String tdStyleLeft = " align=\"left\" style= \"border:1px solid #000000; border-width:1 1 1 0\"";
	private final String tdStyleRight = " align=\"left\" style= \"border:1px solid #000000; border-width:1 0 1 1\"";
	
	public HtmLog(String fileName){
		mFileName = fileName;
	}
	
	public void start(String title) {

		//���⼰��ͷ	
		mTabFlag = true;

		writeFile(tHead);

		String  caption= "<caption " + tCaptionStytle + ">" + title + "</caption>";
		writeFile(caption);
		String th = "<tr>" + "<th " + thStyleLeft + ">ʱ��</th>"
		 	+ "<th "+ thStyleRight + ">����</th>" + "</tr>";
		writeFile(th);		
	}

	public void stop() {

		//��β
		writeFile(tEnd);
		
		mTabFlag = false;
	}

	//log��Ϣд���ļ�
	public void writeFile(String data) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			synchronized (this) {
				try {
					FileWriter out = new FileWriter(mFileName, true);
					String logLine = data;
					out.write(logLine);
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//ʱ��ͳ��+logInfo
	public void write1(long timeInterval, String logInfo){
	
		String time = Long.toString(timeInterval);
		time += " ms";
		String td = "<tr>" + "<td "+ tdStyleLeft+ ">" + time + "</td>"
			+ "<td " + tdStyleRight + ">" + logInfo + "</td>" + "</tr>";
		if(mTabFlag)
			writeFile(td);		
	}

	//ʱ���+logInfo
	public void write(long timePoint, String logInfo){
		
		//<tr width="60%"><th>Time</th><th>Description</th>	
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
		String time = formatter.format(timePoint);
		String td ="<tr>" + "<td "+ tdStyleLeft+ ">" + time + "</td>"
			+ "<td " + tdStyleRight + ">" + logInfo + "</td>" + "</tr>";
		if(mTabFlag)
			writeFile(td);
	}
	
	public String getLogPath() {
		return mFileName;		
	}

}
