package com.stv.supervod.widget_extended;

import com.stv.supervod.activity.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SearchBarWidget extends LinearLayout {
	
	private Button mSearchOkButton;
	private Button mClearButton;
	private ImageView mSearchRightImageView;
	private EditText mSearchEditText;
	private Context context;
	
	private onSearchListener mOnSearchListener = null; 
	public interface onSearchListener{
		public void onSearchChange(String search);
	}
	
	private OnClearBtnClickListener mOnClearListener = null;
	public interface OnClearBtnClickListener {
		public void onClearBtnClick();
	}
	
	public SearchBarWidget(Context context) {
		super(context);
		viewInit(context);
		logicInit();
	}
	
	public SearchBarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		viewInit(context);
		logicInit();
	}
	
	/*** 界面初始化 **/
	private void viewInit(Context context) {
		this.context = context;
		inflate(context, R.layout.search_bar_widget, this);
		mSearchOkButton = (Button)findViewById(R.id.search_btn);
		mClearButton = (Button)findViewById(R.id.clear_btn);
		mSearchRightImageView = (ImageView)findViewById(R.id.search_right);
		mSearchEditText = (EditText)findViewById(R.id.search_text);
	}
	
	/*** 逻辑初始化 **/
	private void logicInit() {
		if(mSearchOkButton != null)
		{
			mSearchOkButton.setOnClickListener(mSearchBtnClickListener);
		}
		
		if(mSearchEditText != null)
		{
			mSearchEditText.setOnTouchListener(mSearchEditTextOnClickListener);
			mSearchEditText.addTextChangedListener(mSearchTextWatcher);
		}
		
		if (mClearButton != null) {
			mClearButton.setOnClickListener(mClearBtnClickListener);
		}
		setTextEditable(false);
	}
	
	/** 搜索按键点击事件处理 **/
	private View.OnClickListener mSearchBtnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mOnSearchListener != null && mSearchEditText.getText().length() != 0) {
				//隐藏输入框
				@SuppressWarnings("static-access")
				InputMethodManager imm = ((InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE));  
				if (imm.isActive()) {
					if (mSearchEditText.getWindowToken() != null) {
						imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				mOnSearchListener.onSearchChange(mSearchEditText.getText().toString());
			}
		}
	};
	
	/** 清除按键点击事件处理 **/
	private View.OnClickListener mClearBtnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mSearchEditText.getText().length() != 0) {
				mSearchEditText.getText().delete(0, mSearchEditText.getText().length());
				mClearButton.setVisibility(View.GONE);
				mSearchEditText.setHint("输入搜索关键字");
			}
			
			if (mOnClearListener != null) {
				mOnClearListener.onClearBtnClick();
			}
		}
	};
	
	/** EditText Touch事件处理 **/
	private View.OnTouchListener mSearchEditTextOnClickListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			/*if(mSearchOkButton != null && mSearchOkButton.getVisibility() != View.VISIBLE)
			{
				setSearchBarState(LAYOUT_STATE_EDIT);
			}*/
			setTextEditable(true);
			return false;
		}
	};
	
	/** 搜索条文字变化监听器 ***/
	private TextWatcher mSearchTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (mSearchEditText.getText().length() == 0
					&& mClearButton != null 
					&& mClearButton.getVisibility() == View.VISIBLE) {
				mClearButton.setVisibility(View.GONE);
				mSearchEditText.setHint("输入搜索关键字");
				if (mOnClearListener != null) {
					mOnClearListener.onClearBtnClick();
				}
			} else if (count != 0 && mClearButton != null 
					&& mClearButton.getVisibility() != View.VISIBLE) {
				mClearButton.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			Log.d("####beforeTextChanged", String.valueOf(count));
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub	
			Log.d("####afterTextChanged", "####afterTextChanged");
		}
	};
	
	/***
	 * 设置搜索框是否可以编辑
	 * @param isEditable
	 */
	private void setTextEditable(boolean isEditable) {
		if(isEditable)
		{
			mSearchEditText.setFocusableInTouchMode(true);
			mSearchEditText.setFocusable(true);
			mSearchEditText.requestFocus();
		}
		else
		{
			mSearchEditText.clearFocus();
			mSearchEditText.setFocusable(false);
		}
	}
	
	private static final int LAYOUT_STATE_VIEW = 1;
	private static final int LAYOUT_STATE_EDIT = 2;
	
	/**
	 * 设置搜索条的状态
	 * <p>浏览状态 LAYOUT_STATE_VIEW 只显示搜索条 同时失去焦点</p>
	 * <p>编辑状态 LAYOUT_STATE_EDIT 显示搜索条和取消按钮 获取焦点</p>
	 * @param state
	 */
	private void setSearchBarState(int state) {
		switch (state) {
		case LAYOUT_STATE_VIEW:
			mSearchEditText.setText("");
			mSearchOkButton.startAnimation(getButtonTranslateAnimation(false, 90));
			mSearchRightImageView.startAnimation(getImageTranslateAnimation(false, 90));
			setTextEditable(false);
			mSearchOkButton.setVisibility(View.GONE);
			break;
		case LAYOUT_STATE_EDIT:
			mSearchOkButton.startAnimation(getButtonTranslateAnimation(true, 90));
			mSearchRightImageView.startAnimation(getImageTranslateAnimation(true, 90));
			setTextEditable(true);
			mSearchOkButton.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/*** 搜索按鈕位移動畫**/
	private TranslateAnimation getButtonTranslateAnimation(boolean in ,int distance) {
		TranslateAnimation animation = null;
		if(in)
		{
			animation =  new TranslateAnimation(distance, 0, 0, 0);
		}
		else
		{
			animation =  new TranslateAnimation(0,distance, 0, 0);
		}
		animation.setDuration(300);
		animation.setFillAfter(true);
		return animation;
	}
	
	/*** 背景存图位移动画 **/
	private TranslateAnimation getImageTranslateAnimation(boolean in,int distance) {
		TranslateAnimation animation = null;
		if(in)
		{
			animation =  new TranslateAnimation(0, -distance, 0, 0);
		}
		else
		{
			animation =  new TranslateAnimation(-distance, 0, 0, 0);
		}
		animation.setDuration(300);
		animation.setFillAfter(true);
		return animation;
	}
	
	public void setOnSearchListener(onSearchListener listener) {
		if(listener != null)
		{
			mOnSearchListener = listener;
		}
	}
	
	public void setOnClearBtnClickListener(OnClearBtnClickListener listener) {
		if(listener != null)
		{
			mOnClearListener = listener;
		}
	}
	
	public void setEditText(String text) {
		mSearchEditText.setText(text);
	}
	
	public String getEditText() {
		return mSearchEditText.getText().toString();
	}

}
