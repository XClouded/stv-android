package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.thinkit.libtmfe.test.MFE;
import cn.thinkit.libtmfe.test.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class LogActivity  extends Activity{
	private WebView MyWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.log);
		
		MyWebView = (WebView) findViewById(R.id.WebViewTrace);

		WebSettings webSettings = MyWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		MyWebView.setVisibility(View.INVISIBLE);
		String logFile = MFE.mCfg.getLogFile();
		if ((logFile.compareTo("") == 0)
				|| (!new File(logFile).exists())) {
			String text = "没有日志信息";
			Toast.makeText(LogActivity.this, text, Toast.LENGTH_SHORT).show();
			finish();
		} else {
			localHtml();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	};

	//读取log文件内容
	public String getContent(String file)
			throws IOException {
		
		FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        String s, ret;
        ret = "";
        while ((s = br.readLine() )!=null) {
             ret += s;
        }
		return ret;
	}

	private void localHtml() {
		try {

			// 本地文件处理(如果文件名中有空格需要用+来替代)
			String filePath = MFE.mCfg.getLogFile();	
			String fileContent = getContent(filePath);
			
			if(fileContent.length()<5){
				String text = "日志文件解析错误";
				Toast.makeText(LogActivity.this, text, Toast.LENGTH_SHORT).show();
				finish();
			}
			
			MyWebView.setVisibility(View.VISIBLE);

			MyWebView.loadDataWithBaseURL("file:///android_asset/",
					fileContent, "text/html", "utf-8", "");
		} catch (Exception ex) {
			ex.printStackTrace();
			String text = ex.getMessage();
			Toast.makeText(LogActivity.this, text, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

}
