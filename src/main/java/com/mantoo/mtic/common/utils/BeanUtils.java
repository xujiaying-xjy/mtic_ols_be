package com.mantoo.mtic.common.utils;

import cn.hutool.core.bean.BeanUtil;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author wyq
 *
 */
public class BeanUtils {
	
	/**
	 * 判断Bean属性是否null或空字符串或空格字符
	 * 
	 * @param obj
	 * @return
	 * @author bawll
	 */
	public static boolean allBlankProp(Object obj) {
		Map<Object, Object> map = BeanUtils.beanToMap(obj);
		for (Entry<Object, Object> entry : map.entrySet()) {
			if(entry.getValue() == null) {
				continue;
			}else if(entry.getValue() instanceof String){
				int strLen;
				String str = String.valueOf(entry.getValue());
				if((strLen = str.length()) == 0) {
					continue;
				}else {
					for (int i = 0; i < strLen; i++) {
						if((Character.isWhitespace(str.charAt(i)))) {
							continue;
						}else {
							return false;
						}
					}
				}
			}else {
				return false;
			}
		}
		return true;
	}

	public static Map beanToMap(Object data) {
		return BeanUtil.beanToMap(data);
	}

	public static void mapClear(Map data) {
		Set<Entry> set = data.entrySet();
		for (Entry entry : set) {
			if (entry.getValue() instanceof Integer && Integer.valueOf(0).equals(entry.getValue())) {
				entry.setValue("0");
			} else if (entry.getValue() instanceof Long && Long.valueOf(0).equals(entry.getValue())) {
				entry.setValue("0");
			} else if (entry.getValue() instanceof Double && Double.valueOf(0).equals(entry.getValue())) {
				entry.setValue("0");
			}
		}

	}
}
