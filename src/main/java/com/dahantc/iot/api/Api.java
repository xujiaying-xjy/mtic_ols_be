package com.dahantc.iot.api;

import com.alibaba.fastjson.JSON;
import com.dahantc.iot.api.base.BaseApi;
import com.dahantc.iot.dto.RenewDto;
import com.dahantc.iot.dto.SignDto;
import com.dahantc.iot.util.HttpClient2;

import java.util.Arrays;
import java.util.List;

/**
 * 描述
 * @author zdq
 * @create 2018/4/9
 */
public class Api extends BaseApi {

    public Api(String url,String appId,String appKey){
        this.baseUrl = url;
        this.appId = appId;
        this.appKey = appKey;
    }

    /**
     * 单卡卡状态改变 0激活 1停用
     */
    @Override
    public String changeCardStatus(String iccid,int status) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/status/"+iccid+"/"+status);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量卡状态改变 激活 停用
     */
    @Override
    public String batchChangeCardStatus(String iccids,int status) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/status/batch");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\",\"status\":"+status+"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单卡GPRS功能的开停
     */
    @Override
    public String changeGPRSStatus(String iccid,int status) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/gprs/"+iccid+"/"+status);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量卡GPRS功能的开停
     */
    @Override
    public String batchChangeGPRSStatus(String iccids,int status) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/gprs/batch");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\",\"status\":"+status+"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 发送短信，支持批量
     */
    @Override
    public String sendSms(String iccids,String content) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/sms/send");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"numbers\":\""+iccids+"\",\"content\":\""+content+"\"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 卡续费
     */
    @Override
    public String renew(String iccids,int payType,int renewPeriod) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/operate/renew");
            RenewDto renewDto = new RenewDto();
            String [] iccidArr = iccids.split(",");
            List iccidList = Arrays.asList(iccidArr);
            renewDto.setIccidList(iccidList);
            renewDto.setPayType(payType);
            renewDto.setRenewPeriod(renewPeriod);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData(JSON.toJSONString(renewDto))
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //------------------------------------------


    /**
     * 单卡状态查询测试用例
     */
    @Override
    public String cardStatus(String iccid) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/status/"+iccid);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量卡状态查询测试用例
     */
    @Override
    public String batchCardStatus(String iccids) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/status/batch");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单卡信息查询测试用例
     */
    @Override
    public String cardInfo(String iccid) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/info/"+iccid);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量卡状态查询测试用例
     */
    @Override
    public String batchCardInfo(String iccids) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/info/batch");

            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 单卡套餐查询测试用例
     */
    @Override
    public String cardPackage(String iccid) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/package/"+iccid);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量卡套餐查询测试用例
     */
    @Override
    public String batchCardPackage(String iccids) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/package/batch");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单卡查询剩余流量测试用例
     */
    @Override
    public String flowLeft(String iccid) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/flow/left/"+iccid);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量查询剩余流量测试用例
     */
    @Override
    public String batchFlowLef(String iccids) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/flow/left/batch");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"iccids\":\""+iccids+"\"}")
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 短信记录查询
     */
    @Override
    public String smsRecord(String iccid,String date) {
        try {
            SignDto signDto = createSignDto("/api/v1/card/base/sms/record/"+date+"/"+iccid);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .post();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询客户流量池列表
     */
    @Override
    public String queryCustomPool() {
        try {
            SignDto signDto = createSignDto("/api/v1/pool/list");
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .get();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询客户流量池详情
     */
    @Override
    public String queryCustomPoolInfo(String poolNo) {
        try {
            SignDto signDto = createSignDto("/api/v1/pool/info/"+poolNo);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .get();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询客户流量池卡列表
     */
    @Override
    public String queryCustomPoolCard(String poolNo,String pageNum) {
        try {
            SignDto signDto = createSignDto("/api/v1/pool/card/"+poolNo);
            String res = HttpClient2.getinstance()
                    .jsonRequest(baseUrl + signDto.getPath())
                    .addHead(signDto.signData())
                    .setData("{\"pageNum\":\""+pageNum+"\"}")
                    .get();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
