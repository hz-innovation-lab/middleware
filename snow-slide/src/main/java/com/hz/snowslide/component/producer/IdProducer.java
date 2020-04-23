package com.hz.snowslide.component.producer;

import com.hz.snowslide.component.flake.Cursor;
import com.hz.snowslide.component.flake.Flake;

/**
 * <p>Package:com.dfire.flame.domain</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:07
 */
public abstract class IdProducer {

    protected Cursor cursor;

    protected Flake flake;

    //毫秒内部的序列号 一秒钟最多多少个
    private static final long sequenceBits = 14L;

    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);


    public Cursor updateCursor(Cursor cursor) {
        long timestamp = timeGen();
        long sequence = cursor.getSequence();
        //sequence是序列号
        long lastTimestamp = cursor.getLastTimestamp();
        long cursorTmp = cursor.getCursor();
        if (lastTimestamp >= timestamp) {
            //时钟回拨或者是同一毫秒
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //sequence归0,需要等时间戳超过过去的秒
                timestamp = tilNextMillis(lastTimestamp);
                cursorTmp = timestamp;
            }
        } else {
            //timestamp > lastTimestamp 正常情况
            if (cursorTmp > timestamp) {
                //可能是当前系统时间戳不太行,落后于zk同步下来的,
                //也可能是时钟回拨。
                //无论什么情况,我们可以以cursorTmp为准.
                //如果以timestamp为准可能会导致获得主键不符合自增规则.
                cursorTmp = cursorTmp + 1;
            } else {
                //新的游标更新为当前时间戳
                cursorTmp = timestamp;
            }
            //是新的一轮了
            sequence = 0L;
        }
        return new Cursor(cursorTmp, timestamp, sequence);
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

}
