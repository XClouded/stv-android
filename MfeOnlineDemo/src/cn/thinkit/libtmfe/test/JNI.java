package cn.thinkit.libtmfe.test;

public class JNI
{
	/** return value */
	public static final int MFE_SUCCESS                 = 0;       
	public static final int MFE_ERROR_UNKNOWN           = -100;
	public static final int MFE_STATE_ERR               = -102;
	public static final int MFE_POINTER_ERR             = -103;
	public static final int MFE_MEMALLOC_ERR            = -107;
	public static final int MFE_PARAMRANGE_ERR          = -109;
	public static final int MFE_SEND_TOOMORE_DATA_ONCE  = -118;
	public static final int MFE_VAD_INIT_ERROR          = -120;
	
	/** parameter type */
	public static final int PARAM_MAX_WAIT_DURATION       = 1;
	public static final int PARAM_MAX_SP_DURATION         = 2;
	public static final int PARAM_MAX_SP_PAUSE            = 3;
	public static final int PARAM_MIN_SP_DURATION         = 4;
	public static final int PARAM_SLEEP_TIMEOUT           = 5;
	public static final int PARAM_ENERGY_THRESHOLD_SP     = 6;
	public static final int PARAM_ENERGY_THRESHOLD_EP     = 7;
	public static final int PARAM_OFFSET                  = 8;
	
	/** MFE API */
	public native int  mfeInit();
	public native int  mfeExit();
	public native int  mfeOpen();
	public native int  mfeClose();
	public native int  mfeStart();
	public native int  mfeStop();
	public native int  mfeSendData(short[] pDataBuf, int iLen);
	public native int  mfeGetCallbackData(byte[] pDataBuf, int iLen);
	public native int  mfeDetect();
	public native int  mfeSetParam(int type, int val);
	public native int  mfeGetParam(int type);
	public native int  mfeEnableNoiseDetection(boolean val);	
	public native void mfeSetLogLevel(int iLevel);
	public native int  mfeGetStartFrame();
	public native int  mfeGetEndFrame();
}

