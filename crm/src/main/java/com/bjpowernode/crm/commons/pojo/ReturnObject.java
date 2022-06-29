package com.bjpowernode.crm.commons.pojo;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.commons.pojo
 * @Author: dushicheng
 * @CreateTime: 2022-06-14  00:18
 * @Description: TODO
 * @Version: 1.0
 */

public class ReturnObject {
    private String code;//是否处理成功1--成功 0--失败

    private String message;//提示信息

    private Object retData;//其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", retData=" + retData +
                '}';
    }
}
