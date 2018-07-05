package com.zzh.mvvm.helper;

import android.os.AsyncTask;

public class AsyncTaskHelper {
	private static final String TAG = "AsynTaskHelper";

	public interface OnDataDownloadListener {
		void onDataDownload(byte[] result);
	}

	public void downloadData(String url, OnDataDownloadListener downloadListener) {
		new MyTask(downloadListener).execute(url);
	}

	class MyTask extends AsyncTask<String, Void, byte[]> {
		private OnDataDownloadListener downloadListener;

		public MyTask(OnDataDownloadListener downloadListener) {
			this.downloadListener = downloadListener;
		}

		@Override
		protected byte[] doInBackground(String... params) {
			byte[] jsonData = HttpURLConnHelper.loadByteFromURL(params[0]);
			return jsonData;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			super.onPostExecute(result);
			// 通过回调接口来传递数据
			if (result != null) {
				downloadListener.onDataDownload(result);
			}
		}
	}
}
