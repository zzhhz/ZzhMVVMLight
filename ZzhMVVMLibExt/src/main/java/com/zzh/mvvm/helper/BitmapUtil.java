package com.zzh.mvvm.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	/**
	 * 对图片进行二次采样，生成缩略图。放置加载过大图片出现内存溢出
	 */
	private Bitmap createThumbnail(String filePath, int newWidth, int newHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int originalWidth = options.outWidth;
		int originalHeight = options.outHeight;

		int ratioWidth = originalWidth / newWidth;
		int ratioHeight = originalHeight / newHeight;

		options.inSampleSize = ratioHeight > ratioWidth ? ratioHeight
				: ratioWidth;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
}
