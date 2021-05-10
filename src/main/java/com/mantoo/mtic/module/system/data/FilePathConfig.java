package com.mantoo.mtic.module.system.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "filepath")
@PropertySource(value = "classpath:filepath.properties", encoding = "utf-8")
@Setter
@Getter
public class FilePathConfig {
    /**
     * windows静态资源保存路径
     */
    private String winsPath;
    /**
     * linux静态资源保存路径
     */
    private String linuxPath;
    /**
     * windows用户头像路径
     */
    private String winsUserImgPath;
    /**
     * linux用户头像图片路径
     */
    private String linuxUserImgPath;
    /**
     * windows现场图片路径
     */
    private String winsStructureImgPath;
    /**
     * linux现场图片路径
     */
    private String linuxStructureImgPath;
	/**
	 * windows网关图片路径
	 */
	private String winsGatewayImgPath;
	/**
	 * linux网关图片路径
	 */
    private String linuxGatewayImgPath;
    /**
     * windows传感器图片路径
     */
    private String winsSensorImgPath;
    /**
     * linux传感器图片路径
     */
    private String linuxSensorImgPath;
    /**
     * windows开关量传感器图片路径
     */
    private String winsSwitchSensorImgPath;
    /**
     * linux开关量传感器图片路径
     */
    private String linuxSwitchSensorImgPath;
    /**
     * windows传感器图片路径
     */
    private String winsReverseEquipmentImgPath;
    /**
     * linux传感器图片路径
     */
    private String linuxReverseEquipmentImgPath;
    /**
     * windows协议文件路径
     */
    private String winsAgreementFilePath;
    /**
     * linux协议文件路径
     */
    private String linuxAgreementFilePath;
    /**
     * windows传感器协议测值图片相对路径
     */
    private String winsMeasureImgPath;
    /**
     * linux传感器协议测值图片相对路径
     */
    private String linuxMeasureImgPath;

    /**
     * windows算法文件路径
     */
    private String winsAlgorithmPath;

    /**
     * linux算法文件路径
     */
    private String linuxAlgorithmPath;

    /**
     * windows原始测值历史数据相对路径
     */
    private String winsMeasureValuePath;

    /**
     * linux原始测值历史数据相对路径
     */
    private String linuxMeasureValuePath;

    /**
     * windows变量历史数据相对路径
     */
    private String winsVariablePath;

    /**
     * linux变量历史数据相对路径
     */
    private String linuxVariablePath;

    private String winsMaterialPath;

    private String linuxMaterialPath;

    private String winsFirmwarePath;

    private String linuxFirmwarePath;
}
