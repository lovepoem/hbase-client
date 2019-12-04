package com.github.lovepoem.hbase.client.pool;


import java.io.Serializable;

/**
 * 连接池接口
 * @author
 */
public interface ConnectionPool<T> extends Serializable {

    /**
     * <p>Title: getConnection</p>
     * <p>Description: 获取连接</p>
     *
     * @return 连接
     */
    public abstract T getConnection();

    /**
     * <p>Title: returnConnection</p>
     * <p>Description: 返回连接</p>
     *
     * @param conn 连接
     */
    public void returnConnection(T conn);

    /**
     * <p>Title: invalidateConnection</p>
     * <p>Description: 废弃连接</p>
     *
     * @param conn 连接
     */
    public void invalidateConnection(T conn);
}
