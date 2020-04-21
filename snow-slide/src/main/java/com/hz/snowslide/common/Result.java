package com.hz.snowslide.common;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Package:com.hz.snowslide.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:02
 */
public interface Result<T> extends Serializable {
    void setSuccess(boolean var1);

    boolean isSuccess();

    String getResultCode();

    void setResultCode(String var1);

    T getModel();

    void setModel(T var1);

    /**
     * @deprecated
     */
    @Deprecated
    void setModels(List<T> var1);

    /**
     * @deprecated
     */
    @Deprecated
    List<T> getModels();

    String getMessage();

    void setMessage(String var1);

    /**
     * @deprecated
     */
    @Deprecated
    void setTotalRecord(int var1);

    /**
     * @deprecated
     */
    @Deprecated
    int getTotalRecord();
}

