package io.wangxin.hbase.client.pool;

import org.apache.commons.pool2.PooledObjectFactory;

import java.io.Serializable;
/**
 * 连接池工厂接口
 * @author
 */
public interface ConnectionFactory<T> extends PooledObjectFactory<T>, Serializable {

    /**
     * <p>Title: createConnection</p>
     * <p>Description: 创建连接</p>
     *
     * @return 连接
     * @throws Exception
     */
    public abstract T createConnection() throws Exception;
}

