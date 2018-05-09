package com.yw.platform.model;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.yw.platform.utils.StringUtils;

import android.util.Xml;


public class ParseBalanceIpXml {

	public static List<BalanceIpItem> parseBalanceIp(String xmlStr)
			throws Exception {

		List<BalanceIpItem> resList = new ArrayList<BalanceIpItem>();
		BalanceIpItem biInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream is = new ByteArrayInputStream(xmlStr.getBytes());
		parser.setInput(is, "UTF-8");
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:

				break;
			case XmlPullParser.START_TAG:
				String nodeName = parser.getName();
				if (nodeName.startsWith("Address")) {
					biInfo = new BalanceIpItem();
				}
				if (nodeName.equals("IP")) {
					if (biInfo != null) {
						biInfo.balanceIp = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("Port")) {
					if (biInfo != null) {
						biInfo.balancePort = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}

				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().startsWith("Address")) {
					resList.add(biInfo);
					biInfo = null;
				}
				break;
			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return resList;
	}
}
