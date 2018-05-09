package com.yw.platform.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yw.platform.model.PersonMessage;

public class Node implements Serializable {

	private Node parent;// 父节点
	private List<Node> children = new ArrayList<Node>();// 子节点
	private String text;// 节点显示的文字
	private ArrayList<PersonMessage> list = null;
	private String EmailAdress;
	public String email;// 电子邮件
	public String job;// 职位
	public String mobile;// 手机号
	public String name;// 姓名
	public String roomNo;// 房间号
	public String tel;// 电话

	private int color;// 节点显示的文字颜色
	private boolean isChecked = false;// 是否处于选中状态
	private boolean isExpanded = false;// 是否处于展开状态

	/**
	 * 设置父节点
	 * 
	 * @param node
	 */
	public void setParent(Node node) {
		this.parent = node;
	}

	/**
	 * 获得父节点
	 * 
	 * @return
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * 设置节点携带的人员列表
	 * 
	 * @param ArrayList
	 *            <PersonMessage> list
	 */
	public void setList(ArrayList<PersonMessage> list) {
		this.list = list;
	}

	/**
	 * 获得节点携带的人员列表
	 * 
	 * @return
	 */
	public ArrayList<PersonMessage> getList() {
		return this.list;
	}

	/**
	 * 获得节点人员的邮箱地址
	 * 
	 * @return
	 */
	public String getEmailAdress() {
		return this.EmailAdress;
	}

	/**
	 * 设置节点人员的邮箱地址
	 * 
	 * @param ArrayList
	 *            <PersonMessage> list
	 */
	public void setEmailAdress(String EmailAdress) {
		this.EmailAdress = EmailAdress;
	}

	/**
	 * 设置节点文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 设置节点文本颜色
	 * 
	 * @param text
	 */
	public void setTextColor(int color) {
		this.color = color;
	}

	/**
	 * 获得节点文本颜色
	 * 
	 * @param text
	 */
	public int getTextColor() {
		return this.color;
	}

	/**
	 * 获得节点文本
	 * 
	 * @return
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * 是否根节点
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return parent == null ? true : false;
	}

	/**
	 * 获得子节点
	 * 
	 * @return
	 */
	public List<Node> getChildren() {
		return this.children;
	}

	/**
	 * 添加子节点
	 * 
	 * @param node
	 */
	public void add(Node node) {
		if (!children.contains(node)) {
			children.add(node);
		}
	}

	/**
	 * 清除所有子节点
	 */
	public void clear() {
		children.clear();
	}

	/**
	 * 删除一个子节点
	 * 
	 * @param node
	 */
	public void remove(Node node) {
		if (!children.contains(node)) {
			children.remove(node);
		}
	}

	/**
	 * 删除指定位置的子节点
	 * 
	 * @param location
	 */
	public void remove(int location) {
		children.remove(location);
	}

	/**
	 * 获得节点的级数,根节点为0
	 * 
	 * @return
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置节点选中状态
	 * 
	 * @param isChecked
	 */
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * 获得节点选中状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * 是否叶节点,即没有子节点的节点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return children.size() < 1 ? true : false;
	}

	/**
	 * 当前节点是否处于展开状态
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * 设置节点展开状态
	 * 
	 * @return
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	/**
	 * 递归判断父节点是否处于折叠状态,有一个父节点折叠则认为是折叠状态
	 * 
	 * @return
	 */
	public boolean isParentCollapsed() {
		if (parent == null)
			return !isExpanded;
		if (!parent.isExpanded())
			return true;
		return parent.isParentCollapsed();
	}

	/**
	 * 递归判断所给的节点是否当前节点的父节点
	 * 
	 * @param node
	 *            所给节点
	 * @return
	 */
	public boolean isParent(Node node) {
		if (parent == null)
			return false;
		if (node.equals(parent))
			return true;
		return parent.isParent(node);
	}
}
