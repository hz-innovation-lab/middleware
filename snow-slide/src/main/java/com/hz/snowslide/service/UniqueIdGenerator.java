package com.hz.snowslide.service;


import com.hz.snowslide.common.Result;

import java.util.List;

public interface UniqueIdGenerator {

    /**
     * 返回全局唯一id，18位或者19位
     *
     * @return
     */
    Result<Long> nextId();


    /**
     * 返回批量的全局唯一id，18位或者19位
     *
     * @return
     */
    Result<List<Long>> nextBatchId(int size);

    /**
     * 只能业务订正数据时使用，
     * 保证不会与现有主键冲突
     *
     * @return
     */
    Result<Long> fixId();

}
