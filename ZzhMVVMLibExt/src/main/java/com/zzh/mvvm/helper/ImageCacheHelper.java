package com.zzh.mvvm.helper;

import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
/**
 * @date: 2018/4/16 下午2:13
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ImageCacheHelper.java
 * @version 1
 */
public class ImageCacheHelper {
	private static final String TAG = "ImageCacheHelper";
	private Map<String, SoftReference<Bitmap>> softCaches = new LinkedHashMap<String, SoftReference<Bitmap>>();
	private LruCache<String, Bitmap> lruCache = null;

	public interface OnImageDownloadListener {
		void onImageDownload(Bitmap bitmap);
	}

	public ImageCacheHelper() {
		int memoryAmount = (int) Runtime.getRuntime().maxMemory();
		// 获取剩余内存的8分之一作为缓存
		if (lruCache == null) {
			// Log.i(TAG, "==强引用空间大小：" + memoryAmount / 8);
			lruCache = new MyLruCache(memoryAmount / 8);
		}
	}

	public void imageDownload(String url, ImageView imageView,
			OnImageDownloadListener downloadListener) {
		Bitmap bitmap = null;
		// 先从强引用中拿数据
		if (lruCache != null) {
			bitmap = lruCache.get(url);
		}
		if (bitmap != null && url.equals(imageView.getTag())) {
			// Log.i(TAG, "==从强引用中找到数据");
			imageView.setImageBitmap(bitmap);
		} else {
			SoftReference<Bitmap> softReference = softCaches.get(url);
			if (softReference != null) {
				bitmap = softReference.get();
			}

			// 从软引用中拿数据
			if (bitmap != null && url.equals(imageView.getTag())) {
				// Log.i(TAG, "==从软引用中找到数据");
				imageView.setImageBitmap(bitmap);

				// 添加到强引用中
				lruCache.put(url, bitmap);
				// Log.i(TAG, "==添加到强引用中");
				// 从软引用集合中移除
				softCaches.remove(url);
				// Log.i(TAG, "==从软引用集合中移除");
			} else {
				new MyAsyncTask(downloadListener).execute(url);
			}
		}
	}

	// 异步下载图片类
	class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private OnImageDownloadListener downloadListener;

		public MyAsyncTask(OnImageDownloadListener downloadListener) {
			this.downloadListener = downloadListener;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bm = null;
			try {
				// Log.i(TAG, "==访问网络加载图片");
				URL urlObj = new URL(params[0]);
				// Log.i(TAG, "==图片url:" + params[0]);
				HttpURLConnection httpConn = (HttpURLConnection) urlObj
						.openConnection();
				httpConn.setDoInput(true);
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				// Log.i(TAG, "==responsecode" + httpConn.getResponseCode());
				if (httpConn.getResponseCode() == 200) {
					bm = BitmapFactory.decodeStream(httpConn.getInputStream());
					// Log.i(TAG, "==bitmap" + bm.toString());
				}
				// 放入强缓存
				lruCache.put(params[0], bm);
				// Log.i(TAG, "==放入强缓存ok");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			// 回调设置图片
			if (result != null) {
				downloadListener.onImageDownload(result);
			}
		}
	}

	// 强引用缓存类
	class MyLruCache extends LruCache<String, Bitmap> {
		public MyLruCache(int maxSize) {
			super(maxSize);
		}

		@Override
		protected int sizeOf(String key, Bitmap value) {
			// return value.getHeight() * value.getWidth() * 4;
			// Bitmap图片的一个像素是4个字节
			return value.getByteCount();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			if (evicted) {
				SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(
						oldValue);
				softCaches.put(key, softReference);
			}
		}
	}
}
