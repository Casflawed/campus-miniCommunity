package com.flameking.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *   用于返回前端json数据的工具类
 *   封装统一反回结果集
 */
@Data
public class ResultBean implements Serializable {

    // 状态码
    private int status;
    // 提示信息
    private String message;

    // 封装有效数据
    private Map<String, Object> data = new HashMap();

    public static ResultBean success() {
        ResultBean result = new ResultBean();
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    public static ResultBean success(String message) {
        ResultBean result = success();
        result.setMessage(message);
        return result;
    }

    public static ResultBean success(Map<String,Object> map) {
        ResultBean result = success();
        result.setData(map);
        return result;
    }

    public static ResultBean fail() {
        ResultBean result = new ResultBean();
        result.setStatus(400);
        result.setMessage("fail");
        return result;
    }

    public static ResultBean fail(String message) {
        ResultBean result = fail();
        result.setMessage(message);
        return result;
    }

    public static ResultBean noPermission() {
        ResultBean result = new ResultBean();
        result.setStatus(401);
        result.setMessage("no permission");
        return result;
    }

    public static ResultBean error() {
        ResultBean result = new ResultBean();
        result.setStatus(500);
        result.setMessage("error");
        return result;
    }
    public static ResultBean error(String msg) {
        ResultBean result = error();
        result.setMessage(msg);
        return result;
    }
    public static ResultBean code(int code){
        ResultBean result = new ResultBean();
        result.setStatus(code);
        result.setMessage("exception");
        return result;
    }
    public ResultBean add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
