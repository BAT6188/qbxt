package com.ushine.store.vo;
import java.util.Comparator;
/**
 * 自定义的比较器
 * @author dh
 *
 */
public class MyComparator implements Comparator{
	//倒序排序输出
	@Override
	public int compare(Object arg0, Object arg1) {
		MyJsonObject object1=(MyJsonObject) arg0;
		MyJsonObject object2=(MyJsonObject) arg1;
		int count1=object1.getDataCount();
		int count2=object2.getDataCount();
		if (count2>count1) {
			return 1;
		}else{
			return -1;
		}
	}
	
}