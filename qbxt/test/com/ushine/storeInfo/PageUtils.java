package com.ushine.storeInfo;

import java.util.ArrayList;
import java.util.List;
/**
 * 分页类
 * @author Administrator
 * @param <T>
 *
 */
public class PageUtils<T> {
	private int totalCount; // 总行数
	private int totalPage; //
	private int currentPage; // 
	private int lineSize; // 

	public PageUtils() {

	}
	/**
	 * 
	 * @param currentPage 当前页数
	 * @param lineSize 每页几行
	 * @param totalCount  总数
	 * @param totalPage	总页数
	 */
	public PageUtils(int currentPage, int lineSize, int totalCount, int totalPage) {
		this.currentPage = currentPage;
		this.lineSize = lineSize;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	// 设置总页数

	public void setTotalPage() {
		// 如果总行数整除每页的行数
		if (this.getTotalCount() / this.getLineSize() == 0) {
			this.totalPage = this.getTotalCount() / this.getLineSize();
		} else {
			this.totalPage = this.getTotalCount() / this.getLineSize() + 1;
		}
	}
	/**
	 * 获得某页数据
	 * @param list
	 * @return
	 */
	public List<T> getPage(List<T> list) {
		List<T> list1 = new ArrayList<T>();

		// 找出分页后，当前页数的后一叶的最后一行是总共行数的第几行-----当前页数减一，乘以每页几行
		int start = (this.getCurrentPage() - 1) * this.getLineSize();
		System.err.println("start："+start);
		// 找出分页后，最后一页的第一行数据是总共行数的第几行
		int end = this.getCurrentPage() * this.getLineSize();
		System.err.println("end："+end);
		int end1 = (this.getCurrentPage() - 1) * this.getLineSize() + this.getTotalCount() % this.getLineSize();
		System.err.println("end1："+end1);

		// 如果说，当前页数等于总页数 并且 总行数除以每页几行不能整除
		if(this.getCurrentPage()<=this.getTotalPage()){
			if (this.getCurrentPage() == this.getTotalPage() && this.getTotalCount() % this.getLineSize() != 0) {
				for (int i = start; i < end1; i++) {
					list1.add(list.get(i));
				}
				//System.err.println(Arrays.toString(list1.toArray()));
			} else {
				for (int i = start; i < end; i++) {
					list1.add(list.get(i));
				}
				//System.err.println(Arrays.toString(list1.toArray()));
			}
		}

		return list1;
	}
	
	public static void main(String[] args) {
		
		List<String> list=new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			list.add(Long.toString(i));
		}
		
		PageUtils util=new PageUtils(5, 3, 15, 5);
		
		List<String> list2=util.getPage(list);
		for (String object : list2) {
			System.err.println(object);
		}
	}
}
