package com.mantoo.mtic.conf.shiro.cache;

import com.mantoo.mtic.common.utils.JwtUtil;
import com.mantoo.mtic.common.utils.PropertiesUtil;
import com.mantoo.mtic.common.utils.SerializableUtil;
import com.mantoo.mtic.conf.shiro.Constant;
import com.mantoo.mtic.conf.redis.RedisService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 重写Shiro的Cache保存读取
 *
 * @author dolyw.com
 * @date 2018/9/4 17:31
 */
@Service
public class CustomCache<K, V> implements Cache<K, V> {

    private static RedisService redisService;

    @Autowired
    public void CustomCache(RedisService redisService) {
        CustomCache.redisService = redisService;
    }

    /**
     * 缓存的key名称获取为shiro:cache:account
     *
     * @param key
     * @return java.lang.String
     * @author dolyw.com
     * @date 2018/9/4 18:33
     */
    private String getKey(Object key) {
        return Constant.PREFIX_SHIRO_CACHE + JwtUtil.getClaim(key.toString(), Constant.ACCOUNT);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        /*if(!JedisUtil.exists(this.getKey(key))){
            return null;
        }
        return JedisUtil.getObject(this.getKey(key));*/

        if (!redisService.exists(this.getKey(key))) {
            return null;
        }
        return redisService.get(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 读取配置文件，获取Redis的Shiro缓存过期时间
        PropertiesUtil.readProperties("shiro.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");
        // 设置Redis的Shiro缓存
//        return JedisUtil.setObject(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
        return redisService.set(this.getKey(key), value, Long.parseLong(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        /*if (!JedisUtil.exists(this.getKey(key))) {
            return null;
        }
        JedisUtil.delKey(this.getKey(key));*/

        if (redisService.exists(this.getKey(key))) {
            return null;
        }
        redisService.remove(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
//        Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
        redisService.clear();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
//        Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();
//        return size.intValue();
        return redisService.keys().size();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
//        Set<byte[]> keys = Objects.requireNonNull(JedisUtil.getJedis()).keys("*".getBytes());
        Set<byte[]> keys = redisService.keys();
        Set<Object> set = new HashSet<Object>();
        for (byte[] bs : keys) {
            set.add(SerializableUtil.unserializable(bs));
        }
        return set;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
//            values.add(JedisUtil.getObject(this.getKey(key)));
            values.add( redisService.get(this.getKey(key)));

        }
        return values;
    }
}
