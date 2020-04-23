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
        long cursortmp = cursor.getCursor();
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //sequence归0,需要等时间戳超过过去的秒
                timestamp = tilNextMillis(lastTimestamp);
                cursortmp = timestamp;
            }
        } else if (timestamp < lastTimestamp) {
            //时钟回拨.游标自增,sequence作0
            cursortmp = cursortmp + 1;
            sequence = 0L;
        } else {
            //timestamp > lastTimestamp 正常情况
            if (cursortmp > timestamp) {
                cursortmp = cursortmp + 1;
                //游标大于时间戳, 这是由于之前时钟回拨了.游标自增即可
            } else {
                //新的游标更新为当前时间戳
                cursortmp = timestamp;
            }
            //是新的一轮了
            sequence = 0L;
        }
        return new Cursor(cursortmp, timestamp, sequence);
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
