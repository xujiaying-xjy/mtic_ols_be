package com.mantoo.mtic.exception;

/**
 * 错误信息枚举类
 *
 * @Author: mjh
 * @Date: 2018-03-16 15:55:42
 */
public enum ErrorInfo {


    //1xx：用户登录注册相关信息

    /**
     * 1001
     * 用户不存在
     */
    USER_NOT_EXIST("1001", "用户不存在"),
    /**
     * 1002
     * 用户已存在
     */
    USER_EXIST("1002", "用户已存在"),
    /**
     * 1003
     * 账号已停用
     */
    USER_NOT_AVAILABLE("1003", "账号已停用"),
    /**
     * 1004
     * 用户名或者密码错误
     */
    LOGIN_INFO_ERROR("1004", "用户名或者密码错误"),
    /**
     * 1005
     * 获取用户角色菜单失败
     */
    GET_USER_ROLE_MENU_ERROR("1005", "获取用户角色菜单失败"),
    /**
     * 1006
     * 用户不明确
     */
    USER_INDEFINITE("1006", "用户不明确"),
    /**
     * 1007
     * 运营商服务不可用
     */
    OPERATOR_SERVICE_NOT_AVAILABLE("1007", "运营商服务不可用"),
    /**
     * 1008
     * 密码错误
     */
    ORIGINAL_PASSWORD_ERROR("1008", "密码错误"),
    /**
     * 1009
     * 没有此权限
     */
    AUTH_NOT_EXIST("1009", "没有操作权限"),

    /**
     * 1010
     * 未登录
     */
    UN_AUTH("1010", "未登录"),

    /**
     * 1011
     * token已过期，重定向到到登录页面重新登录
     */
    TOKEN_EXPIRY("1011", "token已过期"),

    //4xx：客户端错误，请求包含语法错误或者请求无法实现

    /**
     * 4001
     * 缺少必要参数
     */
    MISSING_PARAM("4001", "缺少必要参数"),
    /**
     * 4001
     * 缺少必要参数或参数类型不合法
     */
    MISSING_PARAM_OR_MAPPING("4001", "缺少必要参数或参数类型不合法"),
    /**
     * 4002
     * 非法参数
     */
    ILLEGAL_PARAM("4002", "非法参数"),
    /**
     * 4003
     * 类型不匹配
     */
    TYPE_MISMATCH("4003", "类型不匹配"),
    /**
     * 4004
     * HTTP请求方法不支持
     */
    METHOD_NOT_SUPPORT("4004", "HTTP请求方法不支持"),
    /**
     * 4101
     * 数据已存在
     */
    EXIST("4101", "数据已存在"),
    /**
     * 4102
     * 数据不存在
     */
    NOT_EXIST("4102", "数据不存在"),
    /**
     * 4103
     * 存在子数据
     */
    EXIST_CHILDREN("4103", "存在子数据"),
    /**
     * 4104
     * 存在关联用户
     */
    EXIST_RELATED_USER("4104", "存在关联用户"),
    /**
     * 4105
     * 存在关联角色
     */
    EXIST_RELATED_ROLE("4104", "存在关联角色"),
    /**
     * 4106
     * 存在多条数据
     */
    EXIST_MORE("4106", "存在多条数据"),


    //5xx：服务器错误，服务器不能实现一种明显无效的请求

    /**
     * 5001
     * 服务异常
     */
    SERVICE_ERROR("5001", "服务异常"),

    /**
     * 网关离线
     */
    GATEWAY_OFF_LINE("4201", "网关离线"),

    /**
     * 网关超时未回复
     */
    GATEWAY_OVERTIME("4202", "网关超时未回复"),

    /**
     * 指令错误
     */
    CMD_ERROR("4203", "指令错误"),

    /**
     * 定时器操作异常
     */
    QUARTZ_ERROR("5002", "定时器异常"),

    /**
     * 脚本解析异常
     */
    SCRIPT_ERROR("5003", "脚本解析异常");

    String code;

    String msg;

    ErrorInfo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
