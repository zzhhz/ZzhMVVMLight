package com.zzh.mvvm.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
/**
 * @date: 2018/4/16 下午2:16
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: PullParserHelper.java
 * @version 1
 */
public class PullParserHelper {

	public static List<Map<String, Object>> xmlPullParser(String xmlString,
			String tagName, String[] tagNameList) {

		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = factory.newPullParser();

			InputStream is = new ByteArrayInputStream(
					xmlString.getBytes("UTF-8"));

			pullParser.setInput(is, "utf-8");
			int event = pullParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				String nodeName = pullParser.getName();

				switch (event) {
				case 0:
					list = new ArrayList<Map<String, Object>>();
					break;
				case 2:
					if (nodeName.equals(tagName)) {
						map = new HashMap<String, Object>();
					}

					for (int i = 0; i < tagNameList.length; i++) {
						if (nodeName.equals(tagNameList[i])) {
							map.put(tagNameList[i], pullParser.nextText());
						}
					}
					break;
				case 3:
					if (tagName.equals(nodeName)) {
						list.add(map);
					}
					break;
				}
				event = pullParser.next();
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
