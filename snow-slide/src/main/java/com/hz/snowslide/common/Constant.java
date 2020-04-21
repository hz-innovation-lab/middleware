package com.hz.snowslide.common;

public class Constant {

    public static final int ZK_SEQUENTIAL_LENGTH = 10;

    /**
     * 存储机器workId的路径
     */
    public static final String STORE_PATH = "/flame/node/store";

    /**
     * 持久顺序节点路径，存储游标
     */
    public static final String SEQ_PATH = "/flame/node/sequence";

    /**
     * 临时节点，存储运行的节点host
     */
    public static final String HOST_TMP_PATH = "/flame/node/host";

    public static final String SEPARATE = "/";

    public final static Integer DEFAULT_PORT = 9088;

    public final static String WORK_ID_KEY = "workId";

    public final static String CURSOR_KEY = "cursor";

    public final static long FLAME_SYN_PERIOD = 5;

    public final static int MAX_BATCH_SIZE = 10000;

}
