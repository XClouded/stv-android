package cn.thinkit.libtmfe.test;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import msg.*;

public class RecordThread implements Runnable {
	private int mFrequency;
	private int mChannelConfiguration;
	private volatile boolean mIsRecording = false;;

	private Context mContext;
	Handler mHander;

	private JNI mVREngine;
	public volatile int mVolume = 0;
	
	//������
	private boolean mIfData = false;
	private boolean mIfLog= false;
	private String mFileName = null;
	private DataOutputStream mDataOutputStream;

	long mStartTime = 0, mEndTime = 0;

	// Changing the sample resolution changes sample type. byte vs. short.
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static long mStartRecordTm = 0;

	/**
	 * Handler is passed to pass messages to main screen Recording is done
	 * 8000Hz MONO 16 bit
	 */
	public RecordThread(Context context, Handler h, JNI j) {
		super();

		mContext = context;
		this.setFrequency(8000);
		this.setChannelConfiguration(AudioFormat.CHANNEL_CONFIGURATION_MONO);
		this.mHander = h;

		mVREngine = j;

		Calendar cal = Calendar.getInstance();
		mStartTime = cal.getTimeInMillis();
		mEndTime = cal.getTimeInMillis();
		
		mIfData = MFE.mCfg.getIfDataKeep();
		mIfLog = MFE.mCfg.getIfLogKeep();
		if (!MFE.mCfg.logDirExists()) {
			mIfData = false;
			mIfLog = false;
		}
	}

	public int ReceiveData(Socket socket, byte[] buf, int len)
			throws UnknownHostException, IOException {
		int readLen = 0;

		InputStream in = socket.getInputStream();

		while (true) {
			readLen += in.read(buf, readLen, len - readLen);
			if (readLen == len || readLen < 0)
				break;
		}

		return readLen;
	}

	/* Recording THREAD */
	public void run() {

		JNI j = mVREngine;

		System.out.println("MFE started.");

		//ǰ�˿�ʼ
		int ret = j.mfeStart();
		if (ret != JNI.MFE_SUCCESS) {
			System.out.println("MFE Engine Start failed. Error code is " + ret);
			j.mfeStop();
			return;
		}

		// Wait until we're recording...
		AudioRecord recordInstance = null;

		// We're important...
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		// Allocate Recorder and Start Recording...
		int bufferRead = 0;
		int sizeInBytes = AudioRecord.getMinBufferSize(this.getFrequency(), this
				.getChannelConfiguration(), this.getAudioEncoding());

		recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, this
				.getFrequency(), this.getChannelConfiguration(), this
				.getAudioEncoding(), sizeInBytes);

		int sizeInShorts = sizeInBytes/2;
		short[] realBuffer = new short[sizeInShorts];
		byte[] datBuffer = new byte[sizeInBytes];

		//��������¼���ļ�
		if (mIfData) {
			createDataFile();
		}	
				
		//��ʼ¼��
		recordInstance.startRecording();
		
		if(mIfLog)
			log(1);

		//ÿ�ζ�ȡ��֡��¼��
		int readLenOnce = 2*(this.getFrequency()*10/1000);
		int idx = 1;
		boolean hasStartPoint = false;
		while (this.mIsRecording) {
			Log.e("recorder", "run");
			// ��ȡ¼������
			// bufferRead = recordInstance.read(realBuffer, 0, bufferSize);
			bufferRead = recordInstance.read(realBuffer, 0, readLenOnce);

			if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_INVALID_OPERATION");
			} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_BAD_VALUE");
			} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_INVALID_OPERATION");
			}

			// ��ǰ�˷���¼������
			j.mfeSendData(realBuffer, bufferRead);
			
			// ����¼�����ݵ��ļ�
			if (mIfData) {
				saveDataFile(realBuffer, bufferRead);
			}

			// ǰ�˶����ݽ��м��
			int detect_flag = j.mfeDetect();

			int noisy_flag = 0, jf_ratio = 0;

			//����ط���Ч
			if(detect_flag >= 0) {
				noisy_flag = detect_flag - (detect_flag & 15);
				detect_flag = detect_flag - noisy_flag;                    

				noisy_flag = noisy_flag >> 4;              

				jf_ratio = noisy_flag - (noisy_flag & 3);
				noisy_flag = noisy_flag - jf_ratio;                   

				jf_ratio = jf_ratio >> 2;
			}
			
			// ��ȡǰ�˴���������
			int readlen = j.mfeGetCallbackData(datBuffer, sizeInBytes);
			
			System.out.println("detect_flag is :" + detect_flag);
			System.out.println("Read len (in bytes) is :" + bufferRead*2);
			System.out.println("Callback len (in bytes) is :" + readlen);
			
			// ������ʾ
			volumeNotify(realBuffer, bufferRead);
			
			// Ϊ������ʾ��������
			if(!hasStartPoint && detect_flag == 1)
				waveformBuild(realBuffer, bufferRead, true, false);
			else if(detect_flag >= 2)
				waveformBuild(realBuffer, bufferRead, false, true);
			else
				waveformBuild(realBuffer, bufferRead, false, false);				
			
			// <-----
			//����ǰ�˷��ص�����
			if (readlen >= 0) {
				addCallbackDataToList(idx, detect_flag, datBuffer, readlen);
				idx++;
			}
			// ----->
			
			//MFE_Detect���������Ϣ��ʽ����
			String strTipInfo = "";
			if (detect_flag >= 1) {
				int msgval = 0;
				if (detect_flag == 1) {
					if (!hasStartPoint) {
						hasStartPoint = true;
						msgval = MsgDefinition.SPD_MSG;
						strTipInfo = "Start point detected.";
						
						if(mIfLog)
							log(2);
					}
				} else if (detect_flag == 2) {
					msgval = MsgDefinition.EPD_MSG;
					strTipInfo = "End point detected.";
					
					if(mIfLog)
						log(3);
				} else {
					msgval = MsgDefinition.NOSPEACH_MSG;
					
					if (noisy_flag == 1) {
						strTipInfo = mContext.getString(R.string.text_noisy);
					} else if (noisy_flag == 2) {
						strTipInfo = mContext.getString(R.string.text_tooloudly);
					} else if (noisy_flag == 3) {
						strTipInfo = mContext.getString(R.string.text_noisy_tooloudly);
					} else {
						//"No speech detected, please speak loudly.";
						strTipInfo = mContext.getString(R.string.text_nospeach);						
					}
				}
				
				if (msgval != 0) {
					Message msg = mHander.obtainMessage(msgval, strTipInfo);
					mHander.sendMessage(msg);
				}
				//��⵽������������ߴ��󣬽���¼��
				if((detect_flag > 1) || (detect_flag < 0)) {
					setRecording(false);
				}
			}
		}

		// ǰ�˽���
		ret = j.mfeStop();
		if (ret != JNI.MFE_SUCCESS) {
			System.out.println("MFE Engine Stop failed. Error code is " + ret);
		}
		
		// �ر�¼���豸
		recordInstance.stop();
		
		if (mIfData) {
			closeDataFile();
		}
		
		Message msg = mHander.obtainMessage(MsgDefinition.RECORDER_STOP_MSG, "");
		mHander.sendMessage(msg);

		System.out.println("Exit from the recording thread");
	}

	//����¼�����ݱ���·��
	public void setFileName(String name) {
		this.mFileName = name;
	}
	
	/**
	 * @param isRecording
	 *            the isRecording to set
	 */
	public void setRecording(boolean isRecording) {
		this.mIsRecording = isRecording;
	}

	/**
	 * @return the isRecording
	 */
	public boolean isRecording() {
		return mIsRecording;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.mFrequency = frequency;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return mFrequency;
	}

	/**
	 * @param channelConfiguration
	 *            the channelConfiguration to set
	 */
	public void setChannelConfiguration(int channelConfiguration) {
		this.mChannelConfiguration = channelConfiguration;
	}

	/**
	 * @return the channelConfiguration
	 */
	public int getChannelConfiguration() {
		return mChannelConfiguration;
	}

	/**
	 * @return the audioEncoding
	 */
	public int getAudioEncoding() {
		return audioEncoding;
	}

	/**
	 * @����������С������������С��������������ͼ��
	 */
	private void volumeNotify(short[] buffer, int len) {		
		double volume = calculateVolume(buffer, len);

		// 6���̶�
		volume = volume*6/100;
		int nVolume = (int)(volume + 0.5);
		this.mVolume = nVolume;
	}
	
	/**
	 * @����������С����Χ������0~100֮��
	 */
	private double calculateVolume(short[] buffer, int len) {
		double volume = 0;
		int temp = 0;

		if (len == 0)
			len = 1;
		for (int j = 0; j < len; j++) {
			temp += ((buffer[j]) * (buffer[j])) / len;
		}
		volume = 20*Math.log10(temp/32767);
		if(volume < 0)
			volume = 0;
		if(volume >100)
			volume = 100;
		return volume;
	}

	/**
	 * @����ͼ����
	 * backlen��Ϊ�˼��㿪ʼ�㼰��β���֡
	 */
	private void waveformBuild(short[] buffer, int len, boolean fist_point, boolean end_point){
		System.out.println("####Len is " + RecordActivity.waveData.len);
		
		//һ֡�����ݣ���ǰȡ10������Ϊһ֡��ǰ�˿��ڲ���λ��16���룩
		int frameLenInShort = this.getFrequency()/1000*10; 
		int valuesLen = len/frameLenInShort;
		short values[] = new short[frameLenInShort];
		short tmpbuf[] = new short[frameLenInShort];
		int offset = 0;
		for (int i = 0; i < valuesLen; i++) {
			System.arraycopy(buffer, offset, tmpbuf, 0, frameLenInShort);
			values[i] = (short)(calculateVolume(tmpbuf, frameLenInShort));		
			
			offset += frameLenInShort;
		}

		int arrayLen = RecordActivity.waveData.len;
		if (RecordActivity.waveData.values.length >= arrayLen + valuesLen) {
			System.arraycopy(values, 0, RecordActivity.waveData.values,
					arrayLen, valuesLen);
			RecordActivity.waveData.len += valuesLen;
		}

		// start_frameǰ������ ����
		if (fist_point) {
			System.out.println("Len is " + RecordActivity.waveData.len);
			
			int start_frame  = mVREngine.mfeGetStartFrame();
			System.out.println("start_frame(16ms) is "+ start_frame);
			start_frame = (int)(start_frame*16/10);
			System.out.println("start_frame is "+ start_frame);

			if (start_frame > 0 && RecordActivity.waveData.len > start_frame) {
				System.arraycopy(RecordActivity.waveData.values, start_frame,
						RecordActivity.waveData.values, 0,
						RecordActivity.waveData.len - start_frame);
				RecordActivity.waveData.len -= start_frame;
			}
		}

		// end_frame������ݶ���
		if (end_point) {
			System.out.println("Len is " + RecordActivity.waveData.len);
			
			int start_frame = mVREngine.mfeGetStartFrame();
			int end_frame = mVREngine.mfeGetEndFrame();

			start_frame = (int)(start_frame*16/10);
			end_frame = (int)(end_frame*16/10)+1;
			
			System.out.println("end_frame is "+ end_frame);
			if (end_frame - start_frame > 0 && RecordActivity.waveData.len > end_frame - start_frame)
				RecordActivity.waveData.len = end_frame - start_frame;
		}
		
		System.out.println("****Len is " + RecordActivity.waveData.len);
	}

	
	/**
	 * @��¼callback����
	 */
	private void addCallbackDataToList(int idx, int detect_flag, byte[] datBuffer,
			int readlen) {
		synchronized (RecordActivity.backDataList) {
			CallbackData data = new CallbackData();
			data.idx = idx;
			data.flag = detect_flag;
			data.len = readlen;
			data.buffer = new byte[readlen];
			for (int i = 0; i < readlen; i++) {
				data.buffer[i] = datBuffer[i];
			}

			RecordActivity.backDataList.add(data);
			int size = RecordActivity.backDataList.size();

			Log.e("--recorder", "write Data:idx=" + data.idx + " falg="
					+ data.flag + " len=" + data.len + " recordData size:"
					+ size);
		}
	}
	
	
	/************************************** Debug **************************************/
	// �������ݵ��ļ��Ľӿ�
	private void createDataFile() {
		String name;
		if (mFileName == null)
			name = "sdcard/MFE_Record.pcm";
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

	// �������ݵ��ļ��Ľӿ�
	private void saveDataFile(short[] datBuffer, int readlen) {
		int idxBuffer;
		try {
			for (idxBuffer = 0; idxBuffer < readlen; ++idxBuffer) {
				mDataOutputStream.writeShort(datBuffer[idxBuffer]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �������ݵ��ļ��Ľӿ�
	private void closeDataFile() {
		try {
			mDataOutputStream.close();
		} catch (IOException e) {
			throw new IllegalStateException("Cannot close writer.");
		}
	}
	
	private void log(int flag){
		Calendar cal = Calendar.getInstance();
		long tmNow = cal.getTimeInMillis();
		long tmInterval = tmNow - RecordThread.mStartRecordTm;	

		String info = "";		
		if (flag == 1) { //��¼��ʼ¼����ʱ���
			info = mContext.getString(R.string.log_record_start);
			mStartRecordTm = tmNow;
			RecordActivity.log.write(tmNow, info);
		} else if (flag == 2) { //��¼ʱ����  �ӿ�ʼ¼����������ʼ��
			info = mContext.getString(R.string.log_start_point);
			RecordActivity.log.write1(tmInterval, info);
		} else if (flag == 3) { //��¼ʱ����  �ӿ�ʼ¼��������������
			info = mContext.getString(R.string.log_end_point);
			RecordActivity.log.write1(tmInterval, info);
		}
	}
}

