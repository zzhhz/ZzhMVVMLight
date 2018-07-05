package com.zzh.mvvm.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.LruCache;
import android.widget.ImageView;
/**
 * @date: 2018/4/16 下午2:13
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ImageDownloadSDCardCacheHelper.java
 * @version 1
 */
public class ImageDownloadSDCardCacheHelper {
	public final String TAG = "ImageDownloadSDCardCacheHelper";
	public Map<String, SoftReference<Bitmap>> softCaches = new LinkedHashMap<String, SoftReference<Bitmap>>();
	public LruCache<String, Bitmap> lruCache = null;

	public interface OnImageDownloadSDCardCacheListener {
		void onImageDownload(Bitmap bitmap, String imgUrl);
	}

	// 异步加载图片方法
	public void myDownloadImageSDCardCache(Context context, String url,
			ImageView imageView,
			OnImageDownloadSDCardCacheListener downloadListener) {
		Bitmap bitmap = null;

		if (url != null) {
			String imageName = "";
			imageName = getImageName(url);
			String cachePath = SDCardHelper
					.getInstance().getSDCardCachePath(context);
			bitmap = SDCardHelper.getInstance()
					.loadBitmapFromSDCard(
							cachePath + File.separator + imageName);

			if (bitmap != null && url.equals(imageView.getTag())) {
				imageView.setImageBitmap(bitmap);
			} else {
				new MyAsyncTask(context, url, imageView, downloadListener)
						.execute(url);
			}
		}
	}

	// 异步任务类
	class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		public Context context;
		public ImageView mImageView;
		public String url;
		public OnImageDownloadSDCardCacheListener downloadListener;

		public MyAsyncTask(Context context, String url, ImageView mImageView,
				OnImageDownloadSDCardCacheListener downloadListener) {
			this.context = context;
			this.url = url;
			this.mImageView = mImageView;
			this.downloadListener = downloadListener;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bm = null;
			try {
				String urlString = params[0];
				URL urlObj = new URL(urlString);
				HttpURLConnection httpConn = (HttpURLConnection) urlObj
						.openConnection();
				httpConn.setDoInput(true);
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				if (httpConn.getResponseCode() == 200) {
					InputStream is = httpConn.getInputStream();
					bm = BitmapFactory.decodeStream(is);
				}
				if (bm != null) {
					String imageName = getImageName(urlString);
					boolean flag = SDCardHelper
							.getInstance().saveBitmapToSDCardPrivateCacheDir(
									bm, imageName, context);
					return bm;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			// 回调设置图片
			if (downloadListener != null && result != null) {
				downloadListener.onImageDownload(result, url);
			}
		}
	}

	// SDCard工具类
	static class SDCardHelper {
		public static SDCardHelper sdCardHelper;

		public static SDCardHelper getInstance() {
			if (sdCardHelper == null) {
				sdCardHelper = new SDCardHelper();
			}
			return sdCardHelper;
		}

		// 判断SDCard是否挂载
		public boolean isSDCardMounted() {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		}

		// 获取SDCard的根目录路径
		public String getSDCardBasePath() {
			if (isSDCardMounted()) {
				return Environment.getExternalStorageDirectory()
						.getAbsolutePath();
			} else {
				return null;
			}
		}

		// 获取SDCard的完整空间大小
		public long getSDCardTotalSize() {
			long size = 0;
			if (isSDCardMounted()) {
				StatFs statFs = new StatFs(getSDCardBasePath());
				if (Build.VERSION.SDK_INT >= 18) {
					size = statFs.getTotalBytes();
				} else {
					size = statFs.getBlockCount() * statFs.getBlockSize();
				}
				return size / 1024 / 1024;
			} else {
				return 0;
			}
		}

		// 获取SDCard的可用空间大小
		public long getSDCardAvailableSize() {
			long size = 0;
			if (isSDCardMounted()) {
				StatFs statFs = new StatFs(getSDCardBasePath());
				if (Build.VERSION.SDK_INT >= 18) {
					size = statFs.getAvailableBytes();
				} else {
					size = statFs.getAvailableBlocks() * statFs.getBlockSize();
				}
				return size / 1024 / 1024;
			} else {
				return 0;
			}
		}

		// 获取SDCard的剩余空间大小
		public long getSDCardFreeSize() {
			long size = 0;
			if (isSDCardMounted()) {
				StatFs statFs = new StatFs(getSDCardBasePath());
				if (Build.VERSION.SDK_INT >= 18) {
					size = statFs.getFreeBytes();
				} else {
					size = statFs.getFreeBlocks() * statFs.getBlockSize();
				}
				return size / 1024 / 1024;
			} else {
				return 0;
			}
		}

		// 保存byte[]文件到SDCard的指定公有目录
		public boolean saveFileToSDCardPublicDir(byte[] data, String type,
				String fileName) {
			if (isSDCardMounted()) {
				BufferedOutputStream bos = null;
				File file = Environment.getExternalStoragePublicDirectory(type);

				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(file, fileName)));
					bos.write(data);
					bos.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				return false;
			}
		}

		// 保存byte[]文件到SDCard的自定义目录
		public boolean saveFileToSDCardCustomDir(byte[] data, String dir,
				String fileName) {
			if (isSDCardMounted()) {
				BufferedOutputStream bos = null;
				File file = new File(getSDCardBasePath() + File.separator + dir);
				if (!file.exists()) {
					file.mkdirs();// 递归创建子目录
				}
				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(file, fileName)));
					bos.write(data, 0, data.length);
					bos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			} else {
				return false;
			}
		}

		// 保存byte[]文件到SDCard的指定私有Files目录
		public boolean saveFileToSDCardpublicDir(byte[] data, String type,
				String fileName, Context context) {
			if (isSDCardMounted()) {
				BufferedOutputStream bos = null;
				// 获取私有Files目录
				File file = context.getExternalFilesDir(type);
				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(file, fileName)));
					bos.write(data, 0, data.length);
					bos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			} else {
				return false;
			}
		}

		// 保存byte[]文件到SDCard的私有Cache目录
		public boolean saveFileToSDCardpublicCacheDir(byte[] data,
				String fileName, Context context) {
			if (isSDCardMounted()) {
				BufferedOutputStream bos = null;
				// 获取私有的Cache缓存目录
				File file = context.getExternalCacheDir();
				// Log.i("SDCardHelper", "==" + file);
				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(file, fileName)));
					bos.write(data, 0, data.length);
					bos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			} else {
				return false;
			}
		}

		// 保存bitmap图片到SDCard的私有Cache目录
		public boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap,
				String fileName, Context context) {
			if (isSDCardMounted()) {
				BufferedOutputStream bos = null;
				// 获取私有的Cache缓存目录
				File file = context.getExternalCacheDir();
				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(file, fileName)));
					if (fileName != null
							&& (fileName.contains(".png") || fileName
									.contains(".PNG"))) {
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					} else {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					}
					bos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			} else {
				return false;
			}
		}

		// 从SDCard中寻找指定目录下的文件，返回byte[]
		public byte[] loadFileFromSDCard(String filePath) {
			BufferedInputStream bis = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File file = new File(filePath);
			if (file.exists()) {
				try {
					bis = new BufferedInputStream(new FileInputStream(file));
					byte[] buffer = new byte[1024 * 8];
					int c = 0;
					while ((c = (bis.read(buffer))) != -1) {
						baos.write(buffer, 0, c);
						baos.flush();
					}
					return baos.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (baos != null) {
						try {
							baos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return null;
		}

		// 从SDCard中寻找指定目录下的文件，返回Bitmap
		public Bitmap loadBitmapFromSDCard(String filePath) {
			byte[] data = loadFileFromSDCard(filePath);
			if (data != null) {
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (bm != null) {
					return bm;
				}
			}
			return null;
		}

		// 获取SDCard私有的Cache目录
		public String getSDCardCachePath(Context context) {
			return context.getExternalCacheDir().getAbsolutePath();
		}

		// 获取SDCard私有的Files目录
		public String getSDCardFilePath(Context context, String type) {
			return context.getExternalFilesDir(type).getAbsolutePath();
		}

		// 从sdcard中删除文件
		public boolean removeFileFromSDCard(String filePath) {
			File file = new File(filePath);
			if (file.exists()) {
				try {
					file.delete();
					return true;
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public String getImageName(String url) {
		String imageName = "";
		if (url != null) {
			imageName = url.substring(url.lastIndexOf("/") + 1);
		}
		return imageName;
	}

}
