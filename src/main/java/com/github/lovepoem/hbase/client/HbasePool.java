package com.github.lovepoem.hbase.client;
import com.github.lovepoem.hbase.client.pool.ConnectionPool;
import com.github.lovepoem.hbase.client.pool.HbasePoolConfig;
import com.github.lovepoem.hbase.client.pool.ConnectionPoolBase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.util.Properties;
/**
 * 连接池
 * @author lovepoem
 */
public class HbasePool extends ConnectionPoolBase<Connection> implements ConnectionPool<Connection> {
    /**
     * 添加连接池的构造函数
     */
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9126420905798370243L;

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 默认构造方法</p>
     */
    public HbasePool() {
        this(HbaseConfig.DEFAULT_HOST, HbaseConfig.DEFAULT_PORT);
    }

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host 地址
     * @param port 端口
     */
    public HbasePool(final String host, final String port) {
        this(new HbasePoolConfig(), host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    地址
     * @param port    端口
     * @param master  hbase主机
     * @param rootdir hdfs目录
     */
    public HbasePool(final String host, final String port, final String master, final String rootdir) {
        this(new HbasePoolConfig(), host, port, master, rootdir);
    }

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HbasePool(final Configuration hadoopConfiguration) {
        this(new HbasePoolConfig(), hadoopConfiguration);
    }

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     */
    public HbasePool(final HbasePoolConfig poolConfig, final String host, final String port) {
        this(poolConfig, host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig          池配置
     * @param hadoopConfiguration hbase配置
     */
    public HbasePool(final HbasePoolConfig poolConfig, final Configuration hadoopConfiguration) {
        super(poolConfig, new HbaseConnectionFactory(hadoopConfiguration));
    }


    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig          池配置
     */
    public HbasePool(final String zkquorum, final String zkClientPort, final HbasePoolConfig poolConfig) {
        super(poolConfig, new HbaseConnectionFactory(zkquorum,zkClientPort));
    }
    /**
     * <p>Title: HbasePool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     * @param master     hbase主机
     * @param rootdir    hdfs目录
     */
    public HbasePool(final HbasePoolConfig poolConfig, final String host, final String port, final String master, final String rootdir) {
        super(poolConfig, new HbaseConnectionFactory(host, port, master, rootdir));
    }

    /**
     * @param poolConfig 池配置
     * @param properties 参数配置
     * @since 1.2.1
     */
    public HbasePool(final HbasePoolConfig poolConfig, final Properties properties) {
        super(poolConfig, new HbaseConnectionFactory(properties));
    }

    @Override
    public Connection getConnection() {
        return super.getResource();
    }

    @Override
    public void returnConnection(Connection conn) {
        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Connection conn) {
        super.invalidateResource(conn);
    }
}
