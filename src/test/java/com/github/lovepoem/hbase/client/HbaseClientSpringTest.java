package com.github.lovepoem.hbase.client;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * spring 方式配置数据源测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-hbase-client.xml")
public class HbaseClientSpringTest extends HbaseClient {
    @Resource(name = "hbasePool")
    private HbasePool hbasePool;

    @Override
    public HbasePool getHbasePool() {
        return hbasePool;
    }

    private static Logger logger = LoggerFactory.getLogger(HbaseClient.class);
    private String TABLE_TEST1 = "test:test1";
    private String TABLE_TEST2 = "test:test2";

    @Test
    public void testGetRecord() {
        Result result = this.getRecord(TABLE_TEST1, new Get(Bytes.toBytes("0000:000xxxxxxxxx")));
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
    public void testPut() {
        String rowName = getRowKey(115143989233L);
        System.out.println("rowName-------" + rowName);
        String COLUMN_NAME_TOTAL = "total";
        String COLUMN_FAMILY = "data";
        Put put = new Put(Bytes.toBytes(rowName));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_NAME_TOTAL), Bytes.toBytes("4444"));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(System.currentTimeMillis()), Bytes.toBytes("{itemname=hhaahaha}"));
        super.put("test:test1", put);

        Result result = super.getRecord("test:test1", new Get(Bytes.toBytes(rowName)));
        System.out.println(result);
    }

    /**
     * 通过多个row key返回数据
     */
    @Test
    public void testGetPageRecords() {
        Result result = super.columnPaginationOfRow(TABLE_TEST2, getRowKey(1151426366L), 1, 10, 0L, System.currentTimeMillis());
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

}
