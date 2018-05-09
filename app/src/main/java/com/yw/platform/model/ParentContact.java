package com.yw.platform.model;

import java.util.ArrayList;

import com.yw.platform.view.Node;

public class ParentContact {
	public String dname;// 所属部门
	private ArrayList<Node> list;// 该部门下的部门列表

	public void setList(ArrayList<Node> list) {
		this.list = list;
	}

	public ArrayList<Node> getList() {
		return list;
	}

}
