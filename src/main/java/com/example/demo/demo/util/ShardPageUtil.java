package com.example.demo.demo.util;

import com.example.demo.demo.common.PageResponse;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author xugm
 * @create 2022/1/10 10:17
 */
@Component
public class ShardPageUtil {
    private static final String PAGESIZE = "pageSize";
    private static final String PAGE = "page";
    public static final String MAP_KEY_MIN_ROW = "minRow";
    public static final String MAP_KEY_FETCH_NUM = "fetchNum";

    public <T> PageResponse<T> queryForListShardingPage(ShardPageMapper mapper, Map<String, Object> paramMap, int libNo) {
        ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
        CompletionService<List<T>> cs = new ExecutorCompletionService<>(executorService);
        ShardingQryCounter shardingQryCounter = this.queryForListCountSharding(mapper, paramMap, libNo);
        int page = MapUtils.getIntValue(paramMap, PAGE, 1);
        int pageSize = MapUtils.getIntValue(paramMap, PAGESIZE, 15);
        int minRow = (page - 1) * pageSize;
        int fetchNum = pageSize;
        int dbSeq = 0;
        List<T> returnList = new ArrayList<>();
        for (int tableFix = 0; tableFix < libNo; tableFix++) {
            Map<String, Object> query = new HashMap<>(paramMap);
            query.put("tableFix", tableFix);
            Integer dataRange[] = shardingQryCounter.getDataRangeForShardingDb(tableFix, minRow, fetchNum);
            query.put(MAP_KEY_MIN_ROW, dataRange[0]);
            query.put(MAP_KEY_FETCH_NUM, dataRange[1]);
            if (dataRange[1] == 0) {
                continue;
            }
            dbSeq++;
            ShardingQryExecutor executor = new ShardingQryExecutor();
            cs.submit(executor.queryForList(mapper, query));
        }
        executorService.shutdown();
        for (int tableFix = 0; tableFix < dbSeq; tableFix++) {
            Future<List<T>> future = null;
            try {
                future = cs.take();
                returnList.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long num = shardingQryCounter.getTotalNum();
        return PageResponse.apply(num, returnList);
    }

    public <T> List<T> queryForListSharding(ShardPageMapper mapper, Map<String, Object> paramMap, int libNo) {
        ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
        CompletionService<List<T>> cs = new ExecutorCompletionService<>(executorService);
        List<T> returnList = new ArrayList<>();
        for (int tableFix = 0; tableFix < libNo; tableFix++) {
            Map<String, Object> query = new HashMap<>(paramMap);
            query.put("tableFix", tableFix);
            ShardingQryExecutor executor = new ShardingQryExecutor();
            cs.submit(executor.queryForList(mapper, query));
        }
        executorService.shutdown();
        for (int tableFix = 0; tableFix < libNo; tableFix++) {
            Future<List<T>> future = null;
            try {
                future = cs.take();
                returnList.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

    public ShardingQryCounter queryForListCountSharding(ShardPageMapper mapper, Map<String, Object> paramMap, int libNo) {
        ShardingQryCounter shardingQryCounter = new ShardingQryCounter(libNo);
        Integer[] shardingDbDataCount = new Integer[libNo];
        Long sum = 0l;
        // 并行查询
        ExecutorService es = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
        CompletionService<Map<String, Object>> cs = new ExecutorCompletionService<>(es);


        for (int i = 0; i < shardingQryCounter.getShardingDbNum(); i++) {
            Map<String, Object> query = new HashMap<>(paramMap);
            query.put("tableFix", i);
            ShardingQryExecutor executor = new ShardingQryExecutor();
            cs.submit(executor.queryForListCount(mapper, query));
        }
        es.shutdown();
        for (int tableFix = 0; tableFix < libNo; tableFix++) {
            Future<Map<String, Object>> future = null;
            try {
                future = cs.take();
                int dataCnt = MapUtils.getInteger(future.get(), "tableCount");
                shardingDbDataCount[MapUtils.getInteger(future.get(), "tableFix")] = dataCnt;
                sum += dataCnt;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 汇总结果
        shardingQryCounter.setShardingResultSetCount(shardingDbDataCount);
        shardingQryCounter.setTotalNum(sum);
        return shardingQryCounter;
    }
}
