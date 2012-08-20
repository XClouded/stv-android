package com.stv.demo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stv.R;

public class SearchActivity extends BaseActivity {
	private static final int DIALOG1_KEY = 0;
	private static final int DIALOG2_KEY = 1;
	//private TextView tv;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_search);
		//tv = (TextView) this.findViewById(R.id.widget28);

	}

	public void search(View view) {
		showDialog(DIALOG2_KEY);
		new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dismissDialog(DIALOG2_KEY);
				}
			}
		}.start();

		listview = (ListView) this.findViewById(R.id.widget31);
		SimpleAdapter adapter = new SimpleAdapter(this, new ListActivityDemo().getData(), R.layout.study_vertical_item, new String[] { "name", "type", "score", "img" },
				new int[] { R.id.name, R.id.type, R.id.score, R.id.img });
		listview.setAdapter(adapter);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Indeterminate");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		case DIALOG2_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

}
