package com.zzh.mvvm.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
/**
 * @date: 2018/4/16 下午2:16
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: SDCardHelper.java
 * @version 1
 */
public class SDCardHelper {

	// 判断SD卡是否被挂载
	public static boolean isSDCardMounted() {
		// return Environment.getExternalStorageState().equals("mounted");
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// 获取SD卡的根目录
	public static String getSDCardBaseDir() {
		if (isSDCardMounted()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	// 获取SD卡的完整空间大小，返回MB
	public static long getSDCardSize() {
		if (isSDCardMounted()) {
			StatFs fs = new StatFs(getSDCardBaseDir());
			int count = fs.getBlockCount();
			int size = fs.getBlockSize();
			return count * size / 1024 / 1024;
		}
		return 0;
	}

	// 获取SD卡的剩余空间大小
	public static long getSDCardFreeSize() {
		if (isSDCardMounted()) {
			StatFs fs = new StatFs(getSDCardBaseDir());
			int count = fs.getFreeBlocks();
			int size = fs.getBlockSize();
			return count * size / 1024 / 1024;
		}
		return 0;
	}

	// 获取SD卡的可用空间大小
	public static long getSDCardAvailableSize() {
		if (isSDCardMounted()) {
			StatFs fs = new StatFs(getSDCardBaseDir());
			int count = fs.getAvailableBlocks();
			int size = fs.getBlockSize();
			return count * size / 1024 / 1024;
		}
		return 0;
	}

	// 往SD卡的公有目录下保存文件
	public static boolean saveFileToSDCardPublicDir(byte[] data, String type,
			String fileName) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = Environment.getExternalStoragePublicDirectory(type);
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 往SD卡的自定义目录下保存文件
	public static boolean saveFileToSDCardCustomDir(byte[] data, String dir,
			String fileName) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = new File(getSDCardBaseDir() + File.separator + dir);
			if (!file.exists()) {
				file.mkdirs();// 递归创建自定义目录
			}
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 往SD卡的私有Files目录下保存文件
	public static boolean saveFileToSDCardPrivateFilesDir(byte[] data,
			String type, String fileName, Context context) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = context.getExternalFilesDir(type);
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 往SD卡的私有Cache目录下保存文件
	public static boolean saveFileToSDCardPrivateCacheDir(byte[] data,
			String fileName, Context context) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = context.getExternalCacheDir();
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 从SD卡获取文件
	public static byte[] loadFileFromSDCard(String fileDir) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			bis = new BufferedInputStream(
					new FileInputStream(new File(fileDir)));
			byte[] buffer = new byte[8 * 1024];
			int c = 0;
			while ((c = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 获取SD卡公有目录的路径
	public static String getSDCardPublicDir(String type) {
		return Environment.getExternalStoragePublicDirectory(type).toString();
	}

	// 获取SD卡私有Cache目录的路径
	public static String getSDCardPrivateCacheDir(Context context) {
		return context.getExternalCacheDir().getAbsolutePath();
	}

	// 获取SD卡私有Files目录的路径
	public static String getSDCardPrivateFilesDir(Context context, String type) {
		return context.getExternalFilesDir(type).getAbsolutePath();
	}

	/* 外存卡中的操作清除缓存*/
	/**
	 * 清除本应用内部缓存：(/data/data/com.xxx.xxx/cache)
	 * @param context
	 */
	public static void clearInternalCache(Context context) {
		delelteDirAllFiles(context.getCacheDir());
	}

	// 清除本应用私有数据库：(/data/data/com.xxx.xxx/databases)
	public static void clearDatabases(Context context) {
		//改进
		deleteFilesOfDirectory(new File(context.getCacheDir() + "/databases"));
//		deleteFilesOfDirectory(new File("/data/data/"
//				+ context.getPackageName() + "/databases"));
	}

	// 清除本应用SharedPreference：(/data/data/com.xxx.xxx/shared_prefs)
	public static void clearSharedPreference(Context context) {
//		改进
//		deleteFilesOfDirectory(new File("/data/data/"
//				+ context.getPackageName() + "/shared_prefs"));
		deleteFilesOfDirectory(new File(context.getCacheDir() + "/shared_prefs"));
		
	}
	/*----------------------------------------------------------------*/
	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}
	
	
	public static String getCacheSize(File file) throws Exception {
		return getFormatSize(getFolderSize(file));
	}
	
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
	/*----------------------------------------------------------------*/
	
	

	// 按名字清除本应用数据库：
	public static void clearDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	// 清除内部存储中的数据：（/data/data/com.xxx.xxx/files下的内容）
	public static void clearFiles(Context context) {
		deleteFilesOfDirectory(context.getFilesDir());
	}

	// 清除SD卡中cache目录中的数据：(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	public static void clearExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesOfDirectory(context.getExternalCacheDir());
		}
	}

	// 清除自定义路径下的文件
	public static void clearCustomCache(String filePath) {
		deleteFilesOfDirectory(new File(filePath));
	}

	// 清除本应用所有的cache
	public static void clearApplicationCache(Context context) {
		clearInternalCache(context);
		clearExternalCache(context);
	}

	// 清除本应用所有的数据
	public static void clearApplicationData(Context context, String... filepath) {
		clearInternalCache(context);
		clearExternalCache(context);
		clearDatabases(context);
		clearSharedPreference(context);
		clearFiles(context);
		for (String filePath : filepath) {
			clearCustomCache(filePath);
		}
	}

	// 该方法 只删除某目录下的文件，如果传入的directory是个文件，将不做处理
	private static void deleteFilesOfDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				// Log.i("Helper", "---" + item.toString());
				if (item.isDirectory()) {
					deleteDir(item);
				} else {
					item.delete();
				}
			}
		}
	}

	// File.delete()用于删除“某个文件或者空目录”！所以要删除某个目录及其中的所有文件和子目录，要进行递归删除
	// 递归删除目录下的所有文件及子目录下所有文件
	public static void delelteDirAllFiles(File dir) {
		if (dir.isDirectory()) {
			// 若文件夹非空。枚举、递归删除里面内容
			File[] subFiles = dir.listFiles();
			for (int i = 0; i <= subFiles.length - 1; i++) {
				if (subFiles[i].isDirectory()) {
					delelteDirAllFiles(subFiles[i]);// 递归删除子文件夹内容
				}
				subFiles[i].delete();// 删除子文件夹本身
			}
			dir.delete();// 删除此文件夹本身
		}
	}

	// 递归删除目录下的所有文件及子目录下所有文件写法2
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
	
}
