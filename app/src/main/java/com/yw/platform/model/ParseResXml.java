package com.yw.platform.model;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.yw.platform.utils.StringUtils;

import android.util.Xml;

public class ParseResXml {

	public static List<VirtualAppInfo> parseResources(String xmlStr)
			throws Exception {

		List<VirtualAppInfo> resList = new ArrayList<VirtualAppInfo>();
		VirtualAppInfo vaInfo = null;
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
				if (nodeName.startsWith("Resource")) {
					vaInfo = new VirtualAppInfo();
				}
				if (nodeName.equals("ID")) {
					if (vaInfo != null) {
						vaInfo.resId = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("ApplicationName")) {
					if (vaInfo != null) {
						vaInfo.appName = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("CommandLine")) {
					if (vaInfo != null) {
						vaInfo.commanLine = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("Description")) {
					if (vaInfo != null) {
						vaInfo.description = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("MenuName")) {
					if (vaInfo != null) {
						vaInfo.menuName = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("MenuIconName")) {
					if (vaInfo != null) {
						vaInfo.menuIconName = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				
				if (nodeName.equals("UserName")) {
					if (vaInfo != null) {
						vaInfo.userName = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				if (nodeName.equals("Password")) {
					if (vaInfo != null) {
						vaInfo.password = StringUtils.rmBothSideQuotation(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().startsWith("Resource")) {
					resList.add(vaInfo);
					vaInfo = null;
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
