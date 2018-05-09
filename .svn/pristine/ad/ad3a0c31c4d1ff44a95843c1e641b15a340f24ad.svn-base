package com.yw.platform.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.yw.platform.R;
import com.yw.platform.model.ZipContentItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AttachAdapter extends BaseAdapter {
	private ArrayList<ZipContentItem> list;
	public Context context;
	public LayoutInflater factory;

	public AttachAdapter(Context context, ArrayList<ZipContentItem> list) {
		this.list = list;
		this.context = context;
		factory = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = factory.inflate(R.layout.attach_list_item, null);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
		TextView tv = (TextView) convertView.findViewById(R.id.appname);
		ZipContentItem item = list.get(position);

		if (!item.type.equalsIgnoreCase("")) {
			if (item.type.equalsIgnoreCase("xls")
					|| item.type.equalsIgnoreCase("xlsx")) {
				iv.setBackgroundResource(R.drawable.xls);
			} else if (item.type.equalsIgnoreCase("pdf")) {
				iv.setBackgroundResource(R.drawable.pdf);
			} else if (item.type.equalsIgnoreCase("doc")
					|| item.type.equalsIgnoreCase("docx")) {
				iv.setBackgroundResource(R.drawable.doc);
			} else if (item.type.equalsIgnoreCase("ceb")) {
				iv.setBackgroundResource(R.drawable.ceb);
			} else if (item.type.equalsIgnoreCase("cebx")) {
				iv.setBackgroundResource(R.drawable.cebx);
			} else if (item.type.equalsIgnoreCase("dps")
					|| item.type.equalsIgnoreCase("docx")) {
				iv.setBackgroundResource(R.drawable.wpt);
			} else if(item.type.equalsIgnoreCase("wps")){
				iv.setBackgroundResource(R.drawable.wps);
			}else if(item.type.equalsIgnoreCase("ett")||item.type.equalsIgnoreCase("et")){
					iv.setBackgroundResource(R.drawable.ett);
			} else if (item.type.equalsIgnoreCase("zip")) {
				iv.setBackgroundResource(R.drawable.zip);
			} else if (item.type.equalsIgnoreCase("rar")) {
				iv.setBackgroundResource(R.drawable.rar);
			} else if (item.type.equalsIgnoreCase("ppt")
					|| item.type.equalsIgnoreCase("pptx")) {
				iv.setBackgroundResource(R.drawable.ppt);
			} else if (item.type.equalsIgnoreCase("txt")
					|| item.type.equalsIgnoreCase("log")) {
				iv.setBackgroundResource(R.drawable.txt);
			} else if (item.type.equalsIgnoreCase("jpg")
					|| item.type.equalsIgnoreCase("png")) {
				iv.setBackgroundResource(R.drawable.pic);
			} else {
				iv.setImageResource(R.drawable.unknow);
			}
		} else {
			iv.setImageResource(R.drawable.unknow);
		}
		double size_f = Float.parseFloat(item.size);
		DecimalFormat df = new DecimalFormat("#.0");
		double size_d = size_f / 1024;
		String size = df.format(size_d);
		String text = item.name + "(" + size + "K)";
		tv.setText(text);
		return convertView;
	}

}
