package com.hz.snowslide.component.flake;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Package:com.hz.snowslide.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:03
 */

@Service
@Slf4j
public class Flake implements InitializingBean {

    //起始时间戳
    private static final long twepoch = 1451577600000L;   //2016-01-01 00:00:00

    //机房3个字节
    private static final long roomBits = 3L;

    private static final long roomMask = -1L ^ (-1L << roomBits);

    //机器4个字节
    private static final long workBits = 4L;

    private static final long workMask = -1L ^ (-1L << workBits);

    //正负号
    private static final long typeBits = 1L;

    private static final long typeMask = -1L ^ (-1L << typeBits);

    //毫秒内部的序列号 一秒钟最多多少个
    private static final long sequenceBits = 14L;

    //毫秒序列号
    private static final long typeShift = sequenceBits;

    //机器4个字节
    private static final long workShift = typeShift + typeBits;

    //机房3个字节
    private static final long roomShift = workShift + workBits;

    private static final long timestampShift = roomShift + roomBits;

    private static final long SINGLE_TYPE = 0;

    private static final long BATCH_TYPE = typeMask;

    private Long room;

    private long workId = workMask;

    public long getIdByCursor(Cursor cursor) {
        return ((cursor.getCursor() - twepoch) << timestampShift) | room << roomShift | workId << workShift | SINGLE_TYPE << typeShift | cursor.getSequence();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.room = 1L;
        //机房是配置里面来的
        if (this.room < 0 || this.room > roomMask) {
            throw new RuntimeException(String.format("room room can't be greater than %d or less than 0", 9));
        }
        //机器id
        this.workId = 1L;
        if (this.workId > workMask) {
            throw new RuntimeException(String.format("workId can't be greater than %d", workMask));
        }

        /*this.single = new Single(new Cursor(this.nodeManager.getCursor()));
        this.batch = new Batch(new Cursor(this.nodeManager.getCursor()));*/

        //同步游标到zk的定时任务,每五秒取单机和批量较大值同步到zk
/*        this.cursorService.scheduleAtFixedRate(() -> {
            long cursor = this.single.cursor().getCursor() > this.batch.cursor().getCursor() ?
                    this.single.cursor().getCursor() : this.batch.cursor().getCursor();
            nodeManager.setCursor(cursor);
        }, Constant.FLAME_SYN_PERIOD, Constant.FLAME_SYN_PERIOD, TimeUnit.SECONDS);*/

    }


}
