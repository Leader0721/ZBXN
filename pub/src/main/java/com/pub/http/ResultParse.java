package com.pub.http;


import com.pub.utils.JsonUtil;
import com.pub.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 返回结果
 *
 * @author Lenovo
 */
public class ResultParse<T> {
    private String success;//错误代码 0-成功 其他失败
    private String msg;//错误信息
    private T data;//返回结果  error=0时，返回json形式的结果
    private int total;//查询到数据总数

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ResultParse parse(String json, Class<T> clazz) {
        System.out.println("json:" + json);
        if (StringUtils.isEmpty(json))
            return null;

        ResultParse result = new ResultParse();
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("success")) {
                result.setSuccess(jsonObject.getString("success"));
            }
            if (!jsonObject.isNull("msg")) {
                result.setMsg(jsonObject.getString("msg"));
            }
            if (!jsonObject.isNull("data") && JsonUtil.isGoodJson(jsonObject.getString("data"))) {
                result.setData(JsonUtil.fromJsonString(jsonObject.getString("data"), clazz));
            }
            if (!jsonObject.isNull("total")) {
                result.setTotal(jsonObject.getInt("total"));
            }
        } catch (JSONException e) {
            System.out.println("Result解析错误：" + e.toString());
        }

        return result;
    }
}
