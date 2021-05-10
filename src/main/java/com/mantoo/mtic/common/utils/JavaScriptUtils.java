package com.mantoo.mtic.common.utils;

import com.mantoo.mtic.exception.ErrorInfo;
import com.mantoo.mtic.exception.MticException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author keith
 * @version 1.0
 * @date 2021-02-08
 */
public class JavaScriptUtils {

    public static final ConcurrentMap<String, ScriptEngine> scriptEngineMap = new ConcurrentHashMap<>();

    /**
     * 添加/更新ScriptEngine
     *
     * @param key
     * @param script
     */
    public static void put(String key, String script) {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(script);
            scriptEngineMap.put(key, engine);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除传感器对应的脚本
     *
     * @param key 传感器编号
     */
    public static void delete(String key) {
        scriptEngineMap.remove(key);
    }

    /**
     * 执行脚本
     *
     * @param key 脚本编号
     * @param functionName
     * @param args
     * @return
     */
    public static Object execute(String key, String script, String functionName, Object... args){
        if (!scriptEngineMap.containsKey(key)) {
            JavaScriptUtils.put(key,script);
        }
        Invocable invocable = (Invocable) scriptEngineMap.get(key);
        try {
            return invocable.invokeFunction(functionName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new MticException("脚本解析异常", ErrorInfo.SCRIPT_ERROR.getCode());
        }
    }
}
