package com.mantoo.mtic.module.system.entity;

import com.mantoo.mtic.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "sys_user")
public class SysUser extends BaseEntity {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "JDBC")
    private Long userId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 状态（1启用，0禁用）
     */
    @Column(name = "do_use")
    private Integer doUse;

    /**
     * 用户类型（0管理端，1业主端）
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 手机
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 性别（1男，0女）
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 接收报警（1是，0否）
     */
    @Column(name = "receive_warning")
    private Integer receiveWarning;

    /**
     * 头像url
     */
    @Column(name = "user_img_url")
    private String userImgUrl;

    /**
     * openId
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 小程序openId
     */
    @Column(name = "miniapp_open_id")
    private String miniappOpenId;

    /**
     * 删除标识（1是，0否）
     */
    @Column(name = "delete_flag")
    private Integer deleteFlag;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 输入密码错误次数
     */
    @Column(name = "password_error_count")
    private Integer passwordErrorCount;
}