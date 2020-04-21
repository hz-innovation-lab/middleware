package com.hz.snowslide.component.consumer;

import com.hz.snowslide.component.flake.Cursor;
import com.hz.snowslide.component.flake.Flake;

/**
 * <p>Package:com.dfire.flame.domain</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:05
 */
public class SingleIdConsumer extends IdConsumer {

    public SingleIdConsumer(Cursor cursor, Flake flake) {
        this.cursor = cursor;
        this.flake = flake;
    }

    public synchronized long getId() {
        //获取新的序列号
        Cursor cursor = updateCursor(this.cursor);
        this.cursor = cursor;
        //起始时间戳,69年
        return flake.getIdByCursor(cursor);
    }

    public synchronized long fixId() {
        Cursor cursor = updateCursor(this.cursor);
        this.cursor = cursor;
        return flake.getIdByCursor(cursor);
    }


}
