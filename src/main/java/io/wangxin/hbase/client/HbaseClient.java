package io.wangxin.hbase.client;


import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * hbase基础增删改查操作
 *
 * @author lovepoem
 */
public abstract class HbaseClient {
    private static Logger logger = LoggerFactory.getLogger(HbaseClient.class);

    /**
     * 取得hbase 数据源连接池
     * @return
     */
    public abstract HbasePool getHbasePool();

    /**
     * 得到表信息
     *
     * @param tableName hbase表名
     * @return
     */
    private Table getTable(String tableName) {
        Table table = null;
        Connection conn = null;
        try {
            conn = getHbasePool().getConnection();
            table = conn.getTable(TableName.valueOf(tableName));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            getHbasePool().returnConnection(conn);
        }

        return table;
    }

    /**
     * 删除数据
     *
     * @param tableName hbase表名
     * @param delete    要删除的行
     */
    public void delete(String tableName, Delete delete) {
        Connection conn = null;
        try {
            Table table = null;
            conn = getHbasePool().getConnection();
            table = conn.getTable(TableName.valueOf(tableName));
            table.delete(delete);
            table.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            getHbasePool().returnConnection(conn);
        }
    }

    /**
     * 插入数据
     *
     * @param tableName hbase表名
     * @param put
     */
    public void put(String tableName, Put put) {
        Connection conn = null;
        try {
            Table table = null;
            conn = getHbasePool().getConnection();
            table = conn.getTable(TableName.valueOf(tableName));
            table.put(put);
            table.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            getHbasePool().returnConnection(conn);
        }
    }


    /**
     * 查询单条记录
     *
     * @param tableName hbase表名
     * @param get       get参数
     * @return
     */
    public Result getRecord(String tableName, Get get) {
        return getRecord(tableName, get, Integer.MAX_VALUE);
    }

    /**
     * 取得多条记录
     *
     * @param tableName 表明
     * @return
     */
    public Result[] getRecordList(String tableName, List<Get> getList) {
        Table table = null;
        Result[] resultList = null;
        try {
            table = getTable(tableName);
            if (null == table) {
                logger.error("无法创建hbase表：" + tableName + "的连接，请查看后台!");
                return null;
            }
            resultList = table.get(getList);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return resultList;
    }

    /**
     * 得到记录
     *
     * @param tableName   hbase表名
     * @param get
     * @param maxVersions
     * @return
     */
    private Result getRecord(String tableName, Get get, int maxVersions) {
        Table table = null;
        Result result = null;
        try {
            table = getTable(tableName);
            if (null == table) {
                logger.error("无法创建hbase表：" + tableName + "的连接，请查看后台!");
                return null;
            }
            get.setMaxVersions(maxVersions);
            result = table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * hbase 某rowKey的按照列表的分页方法
     *
     * @return
     */
    public Result columnPaginationOfRow(String tableName, String rowKey, int limit, int offset, Long beginTime, Long endTime) {
        Filter filter = new ColumnPaginationFilter(limit, offset);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.setFilter(filter);
        long begin = beginTime == null ? 0 : beginTime.longValue();
        long end = endTime == null ? Long.MAX_VALUE : endTime.longValue();
        try {
            get = get.setTimeRange(begin, end);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Result result = this.getRecord(tableName, get);
        return result;
    }

}

