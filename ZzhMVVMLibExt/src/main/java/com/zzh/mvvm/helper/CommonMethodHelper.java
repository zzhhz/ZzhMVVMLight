package com.zzh.mvvm.helper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CommonMethodHelper {
	protected final static String simplekey = "_TS#@!21";
	
	public  static void gzipString(String content , String zippath) {
	    try {	      
	        BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zippath)));	     
	        OutputStreamWriter writer = new OutputStreamWriter(out,"UTF-8");	
	    	writer.write(content);
	    	writer.flush();
	      	writer.close();	      
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static String getCurrentTimeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		return str;
	}
	
	public static void simpleEnc(byte[] pdata , int len , String aKey) {
		int loop = len/8;	
		int last = len%8;	
		int i=0;	
		int j=0;
		byte buf2[] = aKey. getBytes();	
		for(i=0; i<loop; i++) {	
			for(j=0; j<8; j++) {	
				pdata[i*8 + j] ^= buf2[j];	
				pdata[i*8 + j] ^= 0x4E;		
				pdata[i*8 + j] ^= buf2[(j+1)%8];		
			}		
			pdata[i] ^= buf2[i%8];	
		}
	
		for(j=0; j<last; j++) {	
			pdata[loop*8 + j] ^= buf2[j];	
			pdata[loop*8 + j] ^= 0x4E;	
			pdata[loop*8 + j] ^= buf2[(j+1)%8];	
		}
	}
	
	public static void simpleEncFileStream(String filepath , String keys , String filepath2) {		
		try{
			File file = new File(filepath);
			File file2 = new File(filepath2);
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(file2);
			byte[] buffer = new byte[(int) file.length()];
			fis.read(buffer);						
			simpleEnc(buffer , (int)file.length() , keys );						
			fos.write(buffer);			
			fis.close();
			fos.close();
		 } catch (Exception e) { }
	}
	
	
	public  static void gzipAndEncString(String content , String keys , String zipPath , String encPath) {
	    try {	      
	        BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zipPath)));	     
	        OutputStreamWriter writer = new OutputStreamWriter(out,"UTF-8");
	    	writer.write(content);
	    	writer.flush();
	      	writer.close();
	      	
	      	File zipFile = new File(zipPath);
	      	FileInputStream fis = new FileInputStream(zipFile);
	      	FileOutputStream fos = new FileOutputStream(new File(encPath));
	      	byte[] buffer = new byte[(int) zipFile.length()];
	      	fis.read(buffer);
			
	      	simpleEnc(buffer , (int) zipFile.length() , keys);
			
			fos.write(buffer);
			
			fis.close();
			fos.close();	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public  static boolean decAndUnzipFile(String encpath , String key , String zippath , String unzipPath) {
	    try {
	    	File encfile = new File(encpath);
	      	FileInputStream fis = new FileInputStream(encpath);
	      	FileOutputStream fos = new FileOutputStream(zippath);
	      	long len = encfile.length();
	      	byte[] buffer = new byte[(int)len];
			fis.read(buffer);
			
			simpleEnc(buffer , (int)len, key);
			fos.write(buffer);
			fis.close();
			fos.close();
			
			
			File zipfile = new File(zippath);
			File unzipfile = new File(unzipPath);			
			BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(zipfile)),"UTF-8"));
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(unzipfile),"UTF-8");

            String s;
            while ((s = br.readLine()) != null) {
                writer.write(s+"\n");
            }
            writer.close();
            br.close();
            return true;

	    } catch(Exception e) {return false;}
	}
	
	
	public  static void gzipString2(String content , String zippath) {
	    try {	      
	      BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zippath)));	     
	      int c =content.length();
	      byte[] buffer=new byte[c];
	      buffer = content.getBytes();
	      out.write(buffer);
	      out.close();
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	}

	public static String getUtf8String(byte[] iArr, int j, int k) {
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			DataOutputStream dataoutputstream = new DataOutputStream(
					bytearrayoutputstream);
			dataoutputstream.writeShort(k);
			dataoutputstream.write(iArr, j, k);
			DataInputStream datainputstream = new DataInputStream(
					new ByteArrayInputStream(bytearrayoutputstream
							.toByteArray()));
			return (datainputstream.readUTF());
		} catch (IOException ioexception) {
			return ("");
		}
	}
	

	public static String MD5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if(i<0) i+= 256;
				if(i<16) buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static String getFormatDate(String timestampString , String formatString) {  
		//"yyyyMMdd:hhmmss" 20100113:151902
		SimpleDateFormat format = new SimpleDateFormat(formatString); 
		long unixLong = 0; 
		String date = "";		
		try {
			switch(timestampString.length()){
				case 13:
					unixLong = Long.parseLong(timestampString) ;
					break;
				case 10:
					unixLong = Long.parseLong(timestampString) * 1000 ;
					break;
			}		
			date = format.format(unixLong);
		} catch(Exception ex) { 
			return ""; 
		} 
		return date; 
	}
	
	
	public static String getTimestampDate( String dateString , String formatString) {  
		SimpleDateFormat format = new SimpleDateFormat(formatString);   
		try {
			return String.valueOf(format.parse(dateString).getTime());
		} catch (Exception e) {
			return "";
		}		
	}
	
	public static String qpDecoding(String str){
		if (str == null){ return ""; }
		try {
			str = str.replaceAll(";", "");
			str = str.replaceAll("=\n", "");
			byte[] bytes = str.getBytes("US-ASCII");
			for (int i = 0; i < bytes.length; i++){
				byte b = bytes[i];
				if (b != 95){ bytes[i] = b;}
				else { bytes[i] = 32; }
			}
			if (bytes == null){ return ""; }
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i];
				if (b == '=') {
				try {
					int u = Character.digit((char) bytes[++i], 16);
					int l = Character.digit((char) bytes[++i], 16);
					if (u == -1){ continue; }
					buffer.write((char) ((u << 4) + l));
				} catch (Exception e) {}
				} else {
					buffer.write(b);
				}
			}
			return new String(buffer.toByteArray(), "UTF-8");
		} catch (Exception e){
		   return "";
		}
	}
	
	public static String getRandomNum(){
		String a = String.valueOf(System.currentTimeMillis());		
		String b = a.substring(a.length()-6);		
		return b;
	}
	
}


