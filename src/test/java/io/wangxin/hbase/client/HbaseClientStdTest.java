package io.wangxin.hbase.client;

import io.wangxin.hbase.client.pool.HbasePoolConfig;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 标准java读写hbase数据源测试
 */
public class HbaseClientStdTest extends HbaseClient {
    private static Logger logger = LoggerFactory.getLogger(HbaseClient.class);
    private String TABLE_TEST1 = "test:test1";
    private String TABLE_TEST2 = "test:test2";
    private String TEST_COLUMN_FAMILY = "data";
    /**
     * 数据源
     */
    private HbasePool hbasePool;

    /**
     * 在构造函数初始化数据源，可以将配置写在本地配置文件中
     */
    public HbaseClientStdTest() {
        HbasePoolConfig poolConfig = new HbasePoolConfig();
        /*** 最大等待数*/
        poolConfig.setMaxTotal(20);
        /*** 最小等待数*/
        poolConfig.setMaxIdle(5);
        /*** 最大等待数*/
        poolConfig.setMaxWaitMillis(1000);
        /*** 启动时候测试*/
        poolConfig.setTestOnBorrow(true);
        /*** zk集群地址*/
        String zkQuorum = "192.168.1.1,192.168.1.2";
        /*** zk集群端口*/
        String zkClientPort = "2181";
        hbasePool = new HbasePool(zkQuorum, zkClientPort, poolConfig);
    }

    @Override
    public HbasePool getHbasePool() {
        return hbasePool;
    }

    @Test
    public void testGetRecord() {
        Result result = getRecord(TABLE_TEST1, new Get(Bytes.toBytes("0000:000xxxxxxxx")));
        System.out.println(result);
        logger.info(result.toString());
    }

    @Test
    public void testGetRecordList() {
        String tableName = "test3:test4";
        String familyName = "test5";
        String columnName = "test6";
        List<String> rowKeys = new ArrayList();
        rowKeys.add("000033782");
        rowKeys.add("00003379");
        rowKeys.add("0000338341");
        List<Get> getList = new ArrayList<>();
        for (String rowKey : rowKeys) {
            getList.add(new Get(Bytes.toBytes(rowKey)));
        }

        Result[] resultList = super.getRecordList(tableName, getList);
        if (resultList != null) {
            for (Result result : resultList) {
                List<Cell> cells = result.getColumnCells(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
                String rowKey = Bytes.toString(result.getRow());
                Double turnover = Bytes.toDouble(CellUtil.cloneValue(cells.get(0)));
                System.out.println("turnover-----" + turnover);
            }
        }
        logger.info(resultList.toString());
        Assert.assertNotNull(resultList);

    }


    @Test
    public void testGetRecordTwo() {
        String TABLE_TEST4 = "test4:test5";
        String TEST4_COLUMN_FAMILY = "data";

        StringBuilder sb = new StringBuilder();
        Long itemId = 1152894742L;
        sb.append("[");
        Get get = new Get(getRowKey(itemId).getBytes());
        Result result = super.getRecord(TABLE_TEST4, get);
        if (result != null && result.getMap() != null) {
            NavigableMap<byte[], NavigableMap<Long, byte[]>> navigableMap = result.getMap().get(Bytes.toBytes(TEST4_COLUMN_FAMILY));
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> resultMap : navigableMap.entrySet()) {
                NavigableMap<Long, byte[]> columns = resultMap.getValue();
                for (Map.Entry<Long, byte[]> columnMap : columns.entrySet()) {
                    sb.append(Bytes.toString(columnMap.getValue()) + ",");
                }
            }
        }
        String res = sb.toString();
        res = res.substring(0, res.length() - 1);
        System.out.println(res);

    }


    @Test
    public void testPut() {
        String rowName = getRowKey(115143989233L);
        System.out.println("rowName-------" + rowName);
        String COLUMN_NAME_TOTAL = "total";
        String COLUMN_FAMILY = "data";
        Put put = new Put(Bytes.toBytes(rowName));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_NAME_TOTAL), Bytes.toBytes("4444"));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(System.currentTimeMillis()), Bytes.toBytes("{itemname=hhaahaha}"));
        super.put("test4:test5", put);

        Result result = super.getRecord("test4:test5", new Get(Bytes.toBytes(rowName)));
        System.out.println(result);
    }


    @Test
    public void testDelete() {
        String rowName = getRowKey(11529184342L);
        System.out.println("rowName-------" + rowName);
        Delete delete = new Delete(Bytes.toBytes(rowName));
        super.delete("test4:test5", delete);

        Result result = super.getRecord("test4:test5", new Get(Bytes.toBytes(rowName)));
        System.out.println(result);
    }

    /**
     * 通过单个row key返按照列分页
     */
    @Test
    public void testGetPageRecords() {
        Result result = super.columnPaginationOfRow(TABLE_TEST2, getRowKey(1152924997L), 10, 0, 0L, System.currentTimeMillis());
        if (result != null && result.getMap() != null) {
            NavigableMap<byte[], NavigableMap<Long, byte[]>> navigableMap = result.getMap().get(Bytes.toBytes(TEST_COLUMN_FAMILY));
            Map<String, String> values = new HashMap<>();
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> resultMap : navigableMap.entrySet()) {
                String key = Bytes.toString(resultMap.getKey());
                NavigableMap<Long, byte[]> columns = resultMap.getValue();
                values.put(key, key);
                for (Map.Entry<Long, byte[]> columnMap : columns.entrySet()) {
                    System.out.println("column key--" + key + "  timestamp --" + columnMap.getKey() + " value----" + Bytes.toString(columnMap.getValue()));
                }
            }
        }
        Assert.assertNotNull(result);
    }

    /**
     * 得到快照的rowkey
     *
     * @param itemId
     * @return
     */
    private static String getRowKey(Long itemId) {
        String md5 = MD5Util.getMD5(String.valueOf(itemId));
        String prefix = md5.substring(0, 5);
        return prefix + "_" + itemId;
    }

    @Test
    public void testName() {
        System.out.printf(getRowKey(1152944707L));
    }

}
