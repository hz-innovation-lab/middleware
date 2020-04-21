package com.hz.snowslide.component.flake;

/**
 * <p>Package:com.dfire.flame.domain</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:06
 */
public class Cursor {

    private long cursor;

    private long lastTimestamp = -1L;

    private long sequence = 0L;

    public Cursor(long cursor) {
        this.cursor = cursor;
    }

    public Cursor(long cursor, long lastTimestamp, long sequence) {
        this.cursor = cursor;
        this.lastTimestamp = lastTimestamp;
        this.sequence = sequence;
    }

    public long getCursor() {
        return cursor;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public long getSequence() {
        return sequence;
    }
}
