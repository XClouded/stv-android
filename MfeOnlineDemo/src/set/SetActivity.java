package set;

import cn.thinkit.libtmfe.test.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import set.Config;
import msg.MsgDefinition;

/**
 * @author nxlu
 * @version 0.1
 * @date 2011-06-10
 * @function MfeOnlineDemo���ý���
 * 
 */

public class SetActivity extends PreferenceActivity {

	// �ؼ�
	private EditTextPreference mEditIP;
	private EditTextPreference mEditUserName;
	private EditTextPreference mEditOncePostLen;
	private EditTextPreference mEditTimeOut;

	private CheckBoxPreference mCheckSysDataKeep;
	private CheckBoxPreference mCheckProcessTrace;

	// ���ò���
	public static Config mCfg;
	private String mCfgIP = "";
	private String mCfgUserName = "";
	private int mCfgTimeOut = 100000;
	private int mCfgOncePostLen = 4096;
	private boolean mCfgSysDataKeep = false;
	private boolean mCfgProcessTrace = false;

	// �˳���Ϣ
	public static Handler msgHandle; // ��������Ϣ���

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.config);

		// ��ȡ�����ļ�
		mCfgIP = mCfg.getIP();
		mCfgUserName = mCfg.getUserName();
		mCfgOncePostLen = mCfg.getOncePostLen();
		mCfgTimeOut = mCfg.getTimeOut();

		mCfgSysDataKeep = mCfg.getIfDataKeep();
		mCfgProcessTrace = mCfg.getIfLogKeep();


		// ��ʼ���ؼ�
		
		// ����IP�Ŀؼ�
		mEditIP = (EditTextPreference)getPreferenceScreen()
		.findPreference("edittext_ip");
		mEditIP.setEnabled(true);
		mEditIP.setSummary(mCfgIP);
		mEditIP.setText(mCfgIP);
		mEditIP.setOnPreferenceChangeListener(ChangeListener_IP);
		mEditIP.setOnPreferenceClickListener(ClickListener_IP);		

		// ����UserName�Ŀؼ�
		mEditUserName = (EditTextPreference)getPreferenceScreen()
		.findPreference("edittext_username");
		mEditUserName.setEnabled(true);
		mEditUserName.setSummary(mCfgUserName);
		mEditUserName.setText(mCfgUserName);			
		mEditUserName.setOnPreferenceChangeListener(ChangeListener_UserName);
		mEditUserName.setOnPreferenceClickListener(ClickListener_UserName);		

		// ����OncePostLen�Ŀؼ�
		mEditOncePostLen = (EditTextPreference)getPreferenceScreen()
		.findPreference("edittext_oncepostlen");
		mEditOncePostLen.setEnabled(true);
		String onceLen = Integer.toString(mCfgOncePostLen);
		mEditOncePostLen.setSummary(onceLen + " bytes");
		mEditOncePostLen.setText(onceLen);			
		mEditOncePostLen.setOnPreferenceChangeListener(ChangeListener_OncePostLen);
		mEditOncePostLen.setOnPreferenceClickListener(ClickListener_OncePostLen);		

		// ����TimeOut�Ŀؼ�
		mEditTimeOut = (EditTextPreference)getPreferenceScreen()
		.findPreference("edittext_timeout");
		mEditTimeOut.setEnabled(true);
		String timeOut = Integer.toString(mCfgTimeOut);
		mEditTimeOut.setSummary(timeOut + " ms");
		mEditTimeOut.setText(timeOut);			
		mEditTimeOut.setOnPreferenceChangeListener(ChangeListener_TimeOut);
		mEditTimeOut.setOnPreferenceClickListener(ClickListener_TimeOut);		

		// ����SysDataKeep�Ŀؼ�
		mCheckSysDataKeep = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("checkbox_sysdatakeep");
		mCheckSysDataKeep.setChecked(mCfgSysDataKeep);
		mCheckSysDataKeep
				.setOnPreferenceClickListener(ClickListener_SysDataKeep);

		// ����ProcessTrace�Ŀؼ�
		mCheckProcessTrace = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("checkbox_processtrace");
		mCheckProcessTrace.setChecked(mCfgProcessTrace);
		mCheckProcessTrace
				.setOnPreferenceClickListener(ClickListener_ProcessTrace);

	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		System.out.println("Settting onStop");
		mCfg.write();

		//mCfg.read();
		super.onStop();
	}

	protected void onDestroy() {
		exit();
		super.onDestroy();
	}

	private OnPreferenceChangeListener ChangeListener_IP = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			mCfgIP = newValue.toString();
			mCfg.setIP(mCfgIP);
			mEditIP.setSummary(mCfgIP);
			return true;
		}
	};

	private OnPreferenceClickListener ClickListener_IP = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {			 
			return true;
		}
	};
	
	private OnPreferenceChangeListener ChangeListener_UserName = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			mCfgUserName = newValue.toString();
			mCfg.setUserName(mCfgUserName);
			mEditUserName.setSummary(mCfgUserName);
			return true;
		}
	};
	
	private OnPreferenceClickListener ClickListener_UserName = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {			 
			return true;
		}
	};

	private OnPreferenceChangeListener ChangeListener_OncePostLen = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if (canString2Int(newValue.toString())) {
				int nLen = Integer.parseInt(newValue.toString());
				if (nLen >= 0) {
					mCfgOncePostLen = nLen;
					mCfg.setOncePostLen(mCfgOncePostLen);
					mEditOncePostLen.setSummary("" + mCfgOncePostLen + " bytes");
				} else {
					String text = getText(R.string.title_edittext_oncepostlen)+"����Ϊ����";
					Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
				}
			} else {
				String text = "���������";
				Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};

	private OnPreferenceClickListener ClickListener_OncePostLen = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {	

			return true;
		}
	};
	
	private boolean canString2Int(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	private OnPreferenceChangeListener ChangeListener_TimeOut = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			if (canString2Int(newValue.toString())) {
				int nTimeOut = Integer.parseInt(newValue.toString());
				if (nTimeOut >= 0) {
					mCfgTimeOut = nTimeOut;
					mCfg.setTimeOut(mCfgTimeOut);
					mEditTimeOut.setSummary("" + mCfgTimeOut + " ms");
				} else {
					String text = getText(R.string.title_edittext_timeout)+"����Ϊ����";
					Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
				}
			} else {
				String text = "���������";
				Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
			}
			String timeOut = Integer.toString(mCfgTimeOut);
			mEditTimeOut.setText(timeOut);
			return true;
		}
	};

	private OnPreferenceClickListener ClickListener_TimeOut = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			return true;
		}
	};

	private OnPreferenceClickListener ClickListener_SysDataKeep = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {

			mCfgSysDataKeep = !mCfgSysDataKeep;
			mCfg.setIfSysDataKeep(mCfgSysDataKeep);
			if (mCfgSysDataKeep && !mCfg.logDirExists()) {
				String text = "û��sd�������ܱ�������";
				Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
			}
			return true;
		}

	};

	private OnPreferenceClickListener ClickListener_ProcessTrace = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {

			mCfgProcessTrace = !mCfgProcessTrace;
			mCfg.setIfProcessTrace(mCfgProcessTrace);
			if (mCfgProcessTrace && !mCfg.logDirExists()) {
				String text = "û��sd�������ܱ�����־";
				Toast.makeText(SetActivity.this, text, Toast.LENGTH_SHORT).show();
			}
			return true;
		}

	};

	void exit() {
		Message msg = msgHandle.obtainMessage(MsgDefinition.SETTING_EXIT_MSG, null);
		if (msgHandle.sendMessage(msg)) {
			System.out.println("About exit.");
		}
		setResult(RESULT_OK, (new Intent()).setAction("Corky!"));

		finish();
	}
}

