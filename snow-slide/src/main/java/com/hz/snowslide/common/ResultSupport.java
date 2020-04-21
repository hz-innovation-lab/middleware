package com.hz.snowslide.common;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Package:com.hz.snowslide.common</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:35
 */
public class ResultSupport<T> implements Result<T> {
    private static final long serialVersionUID = 4661096805690919752L;
    private boolean success = true;
    private String resultCode;
    private String message;
    private T model;
    private int totalRecord;
    private List<T> models = new ArrayList();

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getModel() {
        return this.model;
    }

    public int getTotalRecord() {
        return this.totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public ResultSupport() {
    }

    public ResultSupport(boolean success) {
        this.success = success;
    }

    public ResultSupport(boolean success, String resultCode, String message) {
        this.success = success;
        this.resultCode = resultCode;
        this.message = message;
    }

    public ResultSupport(String resultCode, String message) {
        this.success = Boolean.FALSE;
        this.resultCode = resultCode;
        this.message = message;
    }

    public ResultSupport(ResultCode resultCode) {
        this.success = false;
        this.resultCode = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public String getResultCode() {
        return !this.isSuccess() && isBlank(this.resultCode) ? BaseResultCode.SYSTEM_DEFAULT.getCode() : this.resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<T> getModels() {
        return this.models;
    }

    public void setModels(List<T> models) {
        this.models = models;
    }

    public void setModel(T model) {
        this.model = model;
    }
}

