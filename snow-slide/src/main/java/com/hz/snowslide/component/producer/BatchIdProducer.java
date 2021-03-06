package com.hz.snowslide.component.producer;

import com.hz.snowslide.component.flake.Cursor;
import com.hz.snowslide.component.flake.Flake;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Package:com.dfire.flame.domain</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:15
 */
@Slf4j
public class BatchIdProducer extends IdProducer {

    public BatchIdProducer(Cursor cursor, Flake flake) {
        this.cursor = cursor;
        this.flake = flake;
    }

    public long getId() {
        /**
         * 单生产者,不存在线程安全问题
         */
//        游标初始化值是zk同步获得,与当前时间戳大小不一
        Cursor cursor = updateCursor(this.cursor);
        this.cursor = cursor;
        return flake.getIdByCursor(cursor, false);
    }

}
