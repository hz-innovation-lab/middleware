package com.hz.snowslide.common;

/**
 * <p>Package:com.hz.snowslide.common</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:34
 */
public class ResultUtil {
    public static final String FAIL_CODE = "0";
    public static final String SUCCESS_CODE = "1";

    public ResultUtil() {
    }

    public static boolean isResultSuccess(Result result) {
        return null != result && result.isSuccess();
    }

    public static boolean isModelNotNull(Result result) {
        return isResultSuccess(result) && null != result.getModel();
    }

    public static Result defaultResult() {
        return new ResultSupport();
    }

    public static <T> Result<T> successResult(T model) {
        Result<T> result = defaultResult();
        result.setModel(model);
        return result;
    }

    public static <T> Result<T> failResult(String errorMessage) {
        return failResult("0", errorMessage);
    }

    public static Result failResult(String errorCode, String errorMessage) {
        Result result = defaultResult();
        result.setSuccess(false);
        result.setResultCode(errorCode);
        result.setMessage(errorMessage);
        return result;
    }
}
