package cn.thinkit.libtmfe.test;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Calendar;

import org.xml.sax.InputSource;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.util.Log;

import msg.*;
import xml.*;

class SendStatus {
	public final int INITIAL = 0;
	public final int ISSENDING = 1;
	public final int ISOVER = 2;
	public final int QUIT = 2;
};

public class PostThread implements Runnable {

	private long mSessionId = 12345678;
	private Handler mHandler;
	private int mPostMaxLen;
	private String mIp;
	private String mUser;

	private Context mContext;
	public volatile boolean isAlived = false;
	public volatile int sendStatus = 0;
	// 0 ��ʾ����ǰ�� 1��ʾ�������ڷ��ͣ� 2��ʾ������ϣ� 3��ʾ�������Ͳ��˳�

	private boolean mIfData;
	private String mFileName = null;
	private DataOutputStream mDataOutputStream;

	private boolean mIfLog;

	public PostThread(Handler h, Context context) {
		super();

		this.mHandler = h;
		mContext = context;
		
		//�������ļ��л����������
		mPostMaxLen = MFE.mCfg.getOncePostLen();
		mIfData = MFE.mCfg.getIfDataKeep();
		mIfLog = MFE.mCfg.getIfLogKeep();
		if (!MFE.mCfg.logDirExists()) {
			mIfData = false;
			mIfLog = false;
		}
		mIp = MFE.mCfg.getIP();
		mUser = MFE.mCfg.getUserName();
	}

	@Override
	public void run() {
		isAlived = true;
		Long runTime = SystemClock.elapsedRealtime();
		mSessionId = runTime;

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		Log.e("--CustomDialog", "-start-");
		String strHttpResponse = "";

		if (mIfData) {
			createDataFile();
		}

		int postIdx = 1;
		while (isAlived) {
			byte[] postBuffer = new byte[12];
			int postLen = 0;
			int postFlag = 0;

			synchronized (RecordActivity.backDataList) {

				int size = RecordActivity.backDataList.size();
				int postSize = size;// ȡlistǰpostSize���ݷ���
				if (size > 0) {
					int i;
					int j;
					int totallen = 0;// ��ȡbuffer�ĳ��ȣ�totallen <= mPostMaxLen
					for (i = 0; i < RecordActivity.backDataList.size(); i++) {
						CallbackData temp = RecordActivity.backDataList.get(i);
						if (totallen + temp.len <= mPostMaxLen) {
							totallen += temp.len;
							if (temp.flag >= 2) {
								postFlag++;
							}
						} else {
							if (i != 0) {
								postSize = i;
							} else {
								// ���ٷ���һ������
								totallen += temp.len;
								if (temp.flag >= 2) {
									postFlag++;
								}
								postSize = 1;
							}

							break;
						}
					}

					// ����Ϊtotallen������ ����postBuffer
					postBuffer = new byte[totallen];
					int bufferIdx = 0;
					for (i = 0; i < postSize; i++) {
						CallbackData temp = RecordActivity.backDataList.get(i);

						for (j = 0; j < temp.len; j++) {
							postBuffer[bufferIdx++] = temp.buffer[j];
						}
					}
					// ����Ƿ������һ�����ݣ���������һ�����ݣ�postFlag=2
					if (totallen > 0) {
						postLen = totallen;
						postFlag = (postFlag > 0) ? 2 : 1;
					}

					// �ӻ����������Ҫ���͵�����
					for (i = postSize - 1; i >= 0; i--) {
						RecordActivity.backDataList.remove(i);
					}
				}
			}// end of synchronized

			if (postLen > 0 && isAlived) {
				// IfDataSaveģʽ����ǰ�˷��ص����ݵ��ļ�
				if (mIfData)
					saveDataFile(postBuffer, postLen);

				if (mIfLog && postIdx == 1) {
					log(1, null);
				}

				System.out.println("Send out idx = " + postIdx + " len = "
						+ postLen);
				this.sendStatus = 1;// ���ڷ���
				strHttpResponse = postRecordData(mSessionId, postIdx,
						postBuffer, postLen, postFlag);

				if (mIfLog) {
					if (postIdx == 1) {
						if (strHttpResponse.startsWith("###")) {
							log(2, "ʧ��");
						} else {
							log(2, "�ɹ�");
						}
					}
					if (postFlag == 2) {
						if (strHttpResponse.startsWith("###")) {
							log(3, "ʧ�ܣ�������������");
						} else {
							log(3, "��" + strHttpResponse);
						}
					}
					if ((postIdx != 1) && (postFlag != 2)
							&& (strHttpResponse.startsWith("###"))) {
						log(4, "��" + postIdx + "��������ʧ�ܣ�������������");
					}

				}
				postIdx++;
				if (this.sendStatus == 3) {
					break;
				} else {
					this.sendStatus = 2;
				}

				Message msg;
				if (strHttpResponse.startsWith("###")) {
					// ��###��ͷ����ʾpostʧ��
					isAlived = false;
					msg = mHandler.obtainMessage(
							MsgDefinition.CONNECT_FAILED_MSG, strHttpResponse);
					mHandler.sendMessage(msg);
					break;
				} else if (postFlag == 2) {
					isAlived = false;

					if (mIfData)
						closeDataFile();

					// ��ʾ�������һ�����ݣ����ص��ַ���������ʶ��Ľ��
					msg = mHandler
							.obtainMessage(MsgDefinition.RES_MSG, strHttpResponse);
					mHandler.sendMessage(msg);
					break;
				}
			}// end of if (postData.len > 0)
		}// end of while(true)
		isAlived = false;
	}

	private String postRecordData(long sessionId, int idx, byte[] buffer,
			int len, int lastflag) {
		String strHttpResponse = "";
		String deviceId = Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);

		if (mIp == null)
			mIp = "";
		if (mUser == null)
			mUser = "";
		String url = "http://" + mIp + "/filetest.php?sid=" + sessionId
				+ "&username=" + mUser + "&idx=" + idx + "&islast="
				+ ((lastflag == 2) ? "1" : "0") + "&did=" + deviceId
				+ "&dtype=unknown";
		System.out.println("HTTP: " + url);

		strHttpResponse = strHttpResponse+ postVoiceData(url, "C:\\1.txt", buffer, len);
		return strHttpResponse;
	}

	public String postVoiceData(String actionUrl, String saveFileName,
			byte[] datBuffer, int readlen){

		System.out.println("postVoiceData()");
		try {
		int timeOut = MFE.mCfg.getTimeOut();

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		URL uri = new URL(actionUrl);

		HttpURLConnection conn;

		String proxyHost = android.net.Proxy.getDefaultHost();
		if (proxyHost != null) {// �����wap��ʽ��Ҫ������
			java.net.Proxy myProxy = new java.net.Proxy(
					java.net.Proxy.Type.HTTP, new InetSocketAddress(
							android.net.Proxy.getDefaultHost(),
							android.net.Proxy.getDefaultPort()));
			conn = (HttpURLConnection) uri.openConnection(myProxy);
		} else {
			conn = (HttpURLConnection) uri.openConnection();
		}

		conn.setConnectTimeout(timeOut);
		conn.setReadTimeout(timeOut); // ������ʱ��

		conn.setDoInput(true); // ��������
		conn.setDoOutput(true); // �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");

		// conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Connection", "close");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ BOUNDARY);

		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();

		sb.append(PREFIX);
		sb.append(BOUNDARY);
		sb.append(LINEND);
		sb.append("Content-Disposition: voice-data; name=\"upvoice\"; filename=\"C:\\1.txt\""
						+ LINEND);
		sb.append("Content-Type: application/i7-voice" + LINEND);
		sb.append(LINEND);

		System.out.println("Write data");

		DataOutputStream outStream = new DataOutputStream(conn
				.getOutputStream());
		outStream.write(sb.toString().getBytes());

		// �����ļ�����
		outStream.write(datBuffer, 0, readlen);
		for(int i=0;i<datBuffer.length;i++){
			System.out.print(datBuffer[i]+" ");
		}

		// ���������־
		byte[] end_data = (LINEND + PREFIX + BOUNDARY + PREFIX + LINEND)
				.getBytes();
		outStream.write(end_data);
		outStream.flush();

		// �õ���Ӧ��
		System.out.println("Send out request...");
		int res = conn.getResponseCode();
		System.out.println("Get response code OK.");
		String strResult = "";
		if (res == 200) {
			InputStream in = conn.getInputStream();
			InputStreamReader ir = new InputStreamReader(in, "gbk");

			InputSource is = new InputSource(ir);

			XmlParseHandler parser = new XmlParseHandler();
			parser.parseResults(is);

			if (parser.GetErrCode() == 0) {
				NBestResult r = parser.GetResult();
				if (r != null) {
					strResult = r.result;
				}
			} else {
				strResult = "###���������󣬴�����:" + parser.GetErrCode() + "."
						+ parser.GetErrMsg();
				System.out.println("Get response code failed.");
				System.out.println(strResult);
			}

			outStream.close();
			conn.disconnect();
		}

		return strResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception:" + e.getMessage());
			return ("###" + e.getMessage());
		}
	}

	//���÷������ݵı���·��
	public void setFileName(String name) {
		this.mFileName = name;
	}

	// IfSysDataSaveģʽ���������ݵ��ļ��Ľӿ�
	private void createDataFile() {
		String name;
		if (mFileName == null)
			name = "sdcard/MFE_Post.pcm";
		else
			name = mFileName;

		File fileName = new File(name);
		BufferedOutputStream bufferedStreamInstance = null;
		if (fileName.exists()) {
			fileName.delete();
		}
		if (fileName == null) {
			throw new IllegalStateException("fileName is null");
		}

		try {
			bufferedStreamInstance = new BufferedOutputStream(
					new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Cannot Open File", e);
		}
		mDataOutputStream = new DataOutputStream(bufferedStreamInstance);
	}

	/************************************** Debug **************************************/
	// IfSysDataSaveģʽ���������ݵ��ļ��Ľӿ�
	private void saveDataFile(byte[] datBuffer, int readlen) {
		int idxBuffer;
		try {
			for (idxBuffer = 0; idxBuffer < readlen; ++idxBuffer) {
				mDataOutputStream.writeByte(datBuffer[idxBuffer]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// IfSysDataSaveģʽ���������ݵ��ļ��Ľӿ�
	private void closeDataFile() {
		try {
			mDataOutputStream.close();
		} catch (IOException e) {
			throw new IllegalStateException("Cannot close writer.");
		}
	}

	private void log(int flag, String info) {
		Calendar cal = Calendar.getInstance();
		long tmNow = cal.getTimeInMillis();
		long tmInterval = tmNow - RecordThread.mStartRecordTm;

		String logInfo = "";
		if (flag == 1) { //��¼ʱ���� �ӿ�ʼ¼������ʼ���͵�һ�����ݰ�
			logInfo = mContext.getString(R.string.log_send_first);
			RecordActivity.log.write1(tmInterval, logInfo);
		} else if (flag == 2) {//��¼ʱ���� �ӿ�ʼ¼�������͵�һ�����ݰ����
			logInfo = mContext.getString(R.string.log_send_first_over)
					+ info;
			RecordActivity.log.write1(tmInterval, logInfo);
		} else if (flag == 3) { //��¼ʱ���� �ӿ�ʼ¼�������ʶ����
			logInfo = mContext.getString(R.string.log_get_response)
					+ info;
			RecordActivity.log.write1(tmInterval, logInfo);
		} else { //��¼��������ʧ�ܵ����
			logInfo = info;
			RecordActivity.log.write1(tmInterval, logInfo);
		}
	}

}
