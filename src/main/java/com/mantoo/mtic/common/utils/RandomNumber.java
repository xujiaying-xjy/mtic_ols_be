package com.mantoo.mtic.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: RandomNumber
 * @Description: 生成工单编号工具类
 * @Author: renjt
 * @Date: 2019-12-31 09:38
 */
public class RandomNumber {
    // 使用单例模式，不允许直接创建实例
    private RandomNumber() {
    }

    // 创建一个空实例对象，类需要用的时候才赋值
    private static RandomNumber instance = null;

    // 单例模式--懒汉模式
    public static synchronized RandomNumber getInstance() {
        if (instance == null) {
            instance = new RandomNumber();
        }
        return instance;
    }

    // 全局自增数
    private static int count = 1;
    // 格式化的时间字符串
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    // 获取当前时间年月日时分秒毫秒字符串
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }

    // 记录上一次的时间，用来判断是否需要递增全局数
    private static String now = null;
    //定义锁对象
    private final static ReentrantLock lock = new ReentrantLock();

    //调用的方法
    public static String GetRandom(String prefix) {
        String Newnumber = null;
        String dateStr = getNowDateStr();
        lock.lock();//加锁
        //判断是时间是否相同
        if (dateStr.equals(now)) {
            try {
                if (count >= 10000) {
                    count = 1;
                }
                if (count < 10) {
                    Newnumber = prefix + getNowDateStr() + "000" + count;
                } else if (count < 100) {
                    Newnumber = prefix + getNowDateStr() + "00" + count;
                } else if (count < 1000) {
                    Newnumber = prefix + getNowDateStr() + "0" + count;
                } else {
                    Newnumber = prefix + getNowDateStr() + count;
                }
                count++;
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        } else {
            count = 1;
            now = getNowDateStr();
            try {
                if (count >= 10000) {
                    count = 1;
                }
                if (count < 10) {
                    Newnumber = prefix + getNowDateStr() + "000" + count;
                } else if (count < 100) {
                    Newnumber = prefix + getNowDateStr() + "00" + count;
                } else if (count < 1000) {
                    Newnumber = prefix + getNowDateStr() + "0" + count;
                } else {
                    Newnumber = prefix + getNowDateStr() + count;
                }
                count++;
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }
        return Newnumber;//返回的值
    }
}
