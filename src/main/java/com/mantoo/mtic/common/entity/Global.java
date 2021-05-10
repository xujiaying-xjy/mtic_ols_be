package com.mantoo.mtic.common.entity;

/**
* 全局配置类
* @Author: mjh
* @Date: 2018-03-27 21:25:49
*/
public class Global {
	
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 是/否
	 */
	public static final boolean YES = true;
	public static final boolean NO = false;

	/**
	 * 用户类型（0：管理端；1：业主端；）
	 */
	public static final Integer MANAGER = 0;
	public static final Integer OWNER = 1;

	/**
	 * 删除标记（0：未删除；1：删除；）
	 */
	public static final Integer NORMAL = 0;
	public static final Integer DELETED = 1;
	
	/**
	 * 启用状态（0：未启用；1：启用；）
	 */
	public static final Integer ENABLE = 1;
	public static final Integer DISABLE = 0;
	
	/**
	 * 显示状态（0：隐藏；1：显示；）
	 */
	public static final byte SHOW = 1;
	public static final byte HIDE = 0;

    /**
     * 附件上传异常提示信息
     */
    public static final String OUTOFMAXSIZE = "上传文件不能超过";

	/**
	 * 日志分割符号
	 */
	public static final String SEPARATOR = "|";
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

	
}
