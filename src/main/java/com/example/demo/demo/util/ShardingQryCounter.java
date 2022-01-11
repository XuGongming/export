package com.example.demo.demo.util;


public class ShardingQryCounter {
    // 总记录数
    private Long totalNum;
    // 分库数量
    private int shardingDbNum;
    // 每个分库查询结果集的数据计数
    private Integer[] shardingResultSetCount;

    public ShardingQryCounter(int shardingDbNum) {
        this.shardingDbNum = shardingDbNum;
        // 初始化每个分库结果条数为0
        this.shardingResultSetCount = new Integer[shardingDbNum];
        for (int i = 0; i < shardingDbNum; i++) {
            this.shardingResultSetCount[i] = 0;
        }
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public int getShardingDbNum() {
        return shardingDbNum;
    }

    public void setShardingDbNum(int shardingDbNum) {
        this.shardingDbNum = shardingDbNum;
    }

    public Integer[] getShardingResultSetCount() {
        return shardingResultSetCount;
    }

    public void setShardingResultSetCount(Integer[] shardingResultSetCount) {
        this.shardingResultSetCount = shardingResultSetCount;
    }

    /**
     * 获取指定分库的数据范围 <br>
     *
     * @param TableSeq    分库序号
     * @param minRow   开始记录
     * @param fetchNum 总数据条数
     * @return 一个二维数组, 第一位是指定分库开始记录, 第二位是指定分库需要获取的记录数
     */
    public Integer[] getDataRangeForShardingDb(int TableSeq, Integer minRow, Integer fetchNum) {
        int lastShardingTotalSum = 0;

        // 从分库开始获取记录的起始行
        int sdMinRow = 0;
        // 要从分库上获取的条数
        int sdFetchNum = 0;

        Integer shardingDbRange[] = getShardingDataDbRange(minRow, fetchNum);
        int fromTableFix = shardingDbRange[0];
        int toTableFix = shardingDbRange[1];

        for (int i = 0; i < TableSeq; i++) {
            int thisShardingDbTototal = getShardingResultSetCount()[i];
            lastShardingTotalSum += thisShardingDbTototal;
        }

        if (fromTableFix == TableSeq && toTableFix == TableSeq) {
            // 只在一个分库上取就可以了
            sdMinRow = minRow - lastShardingTotalSum;
            sdFetchNum = fetchNum;
        }
        if (fromTableFix == TableSeq && toTableFix > TableSeq) {
            // 从当前库开始取, 从起始记录到结束所有数据
            sdMinRow = minRow - lastShardingTotalSum;
            // -1 表示取后面所有记录
            sdFetchNum = getShardingResultSetCount()[TableSeq] - sdMinRow + 1;
        }
        if (fromTableFix < TableSeq && toTableFix > TableSeq) {
            // 起始,结束位置都不在这个分库上, 这个库上的数据全部取
            sdMinRow = 0;
            sdFetchNum = -1;
        }
        if (fromTableFix < TableSeq && toTableFix == TableSeq) {
            // 在这个分库上结束
            sdMinRow = 0;
            sdFetchNum = minRow + fetchNum - lastShardingTotalSum;
        }

        return new Integer[]{sdMinRow, sdFetchNum};
    }

    /**
     * 获取分库数据分布范围 <br>
     *
     * @param minRow   起始记录
     * @param fetchNum 结束记录
     * @return 一个二维数组表示范围, 第一位是开始的分库序号,第二位是结束的分库序号
     */
    public Integer[] getShardingDataDbRange(Integer minRow, Integer fetchNum) {

        int shardingTotalSum = 0;

        // 起始分库
        int fromTableFix = -1;
        int toTableFix = -1;
        for (int i = 0; i < this.getShardingDbNum(); i++) {
            int thisShardingDbTototal = this.getShardingResultSetCount()[i];
            shardingTotalSum += thisShardingDbTototal;

            if ((minRow + 1) <= shardingTotalSum && fromTableFix == -1) {
                fromTableFix = i;
            }
            if (((minRow + fetchNum) <= shardingTotalSum || totalNum == shardingTotalSum) && toTableFix == -1) {
                toTableFix = i;
                break;
            }
        }

        return new Integer[]{fromTableFix, toTableFix};
    }
}
