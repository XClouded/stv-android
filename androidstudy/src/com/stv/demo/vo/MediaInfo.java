package com.stv.demo.vo;

import android.graphics.Bitmap;

/**
 * Description: 影片元数据
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-15 上午11:44:13 mustang created
 */
public class MediaInfo {
	private String name;
	private String type;
	private int score;
	private Bitmap bitmap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
