package com.github.lovepoem.hbase.client;

/**
 * 默认配置
 *
 * @author
 */
public interface HbaseConfig {

    /**
     * DEFAULT_HOST
     */
    public static final String DEFAULT_HOST = "localhost";
    /**
     * DEFAULT_PORT
     */
    public static final String DEFAULT_PORT = "2181";
    /**
     * DEFAULT_MASTER
     */
    public static final String DEFAULT_MASTER = null;
    /**
     * DEFAULT_ROOTDIR
     */
    public static final String DEFAULT_ROOTDIR = null;

    /**
     * ZOOKEEPER_QUORUM_PROPERTY
     */
    public static final String ZOOKEEPER_QUORUM_PROPERTY = "xxxx.hbase.zookeeper.quorum";
    /**
     * ZOOKEEPER_CLIENTPORT_PROPERTY
     */
    public static final String ZOOKEEPER_CLIENTPORT_PROPERTY = "xxxx.hbase.zookeeper.property.clientPort";
    /**
     * MASTER_PROPERTY
     */
    public static final String MASTER_PROPERTY = "xxxx.hbase.master";
    /**
     * ROOTDIR_PROPERTY
     */
    public static final String ROOTDIR_PROPERTY = "xxxx.hbase.rootdir";

}
