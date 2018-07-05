package com.zzh.mvvm.helper;

import java.util.Arrays;

public class ArrayHelper {
	public static Object[] converseArray(Object[] arr){
		Object para=null;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static int[] converseArray(int[] arr){
		int para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static char[] converseArray(char[] arr){
		char para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static byte[] converseArray(byte[] arr){
		byte para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static short[] converseArray(short[] arr){
		short para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static long[] converseArray(long[] arr){
		long para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static float[] converseArray(float[] arr){
		float para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static double[] converseArray(double[] arr){
		double para;
		for (int i = 0; i <= arr.length / 2; i++) {
			para = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = para;
		}
		return arr;
	} 
	
	public static String[] copyArray(String[] arr) {
		String[] arrNew = Arrays.copyOf(arr, arr.length);
		return arrNew;
	}
	
	
	public static String[] copyArrayRange(String[] arr , int startIndex , int endIndex) {
		String[] arrNew = Arrays.copyOfRange(arr, startIndex , endIndex);
		return arrNew;
	}
	
//	public static int searchArray(int[] arr , int value){
//		int index = Arrays.binarySearch(arr, value);
//		return index;
//	} 
	
	
	/**
	 * 填充替换数组元素
	 */
	public static int[] replaceArray(int[] arr , int startIndex ,int endIndex,int value){
		Arrays.fill(arr, startIndex,endIndex,value);
		return arr;
	}

}
