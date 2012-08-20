package com.stv.demo.vo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends View{
	private ImageView img;

	public ImageView getImg() {
		return img;
	}

	public void setImg(ImageView img) {
		this.img = img;
	}

	public TextView getTv() {
		return tv;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}

	private TextView tv;

	public ViewHolder(Context ctt) {
		super(ctt);
		img = new ImageView(ctt);
		tv = new TextView(ctt);
	}

}
