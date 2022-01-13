package com.example.demo.demo.util;

import com.example.demo.demo.common.PageResponse;
import com.example.demo.demo.dto.InsertDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private TransactionTemplate transactionTemplate ;
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
        for (int tableFix = 0; tableFix < shardingQryCounter.getShardingDbNum(); tableFix++) {
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

    @Transactional(rollbackFor = Exception.class)
    public int batchInsertSharding(ShardPageMapper mapper, List<? extends InsertDTO> insertList, int libNo) {
        ExecutorService es = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
        CompletionService<Integer> cs = new ExecutorCompletionService<>(es);
        int batchNum = 0;
        int tableNum = 0;
        String uuid = UUID.randomUUID().toString();
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (int i = 0; i < libNo; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("uuid", uuid);
            param.put("tableFix", i);
            final int j = i;
            List list = insertList.stream().filter(e -> Integer.parseInt(e.getOrgCode().substring(e.getOrgCode().length() - 1)) % libNo == j)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            tableNum++;
            param.put("insertList", list);
            paramList.add(param);
        }
        for (Map<String, Object> param : paramList) {
            param.put("tableNum", paramList.size());
            ShardingQryExecutor executor = new ShardingQryExecutor(redisTemplate,transactionTemplate);
            cs.submit(executor.batchInsertShardingTx(mapper, param));
        }
        es.shutdown();
        for (int tableFix = 0; tableFix < tableNum; tableFix++) {
            Future<Integer> future = null;
            try {
                future = cs.take();
                batchNum += future.get();
            } catch (Exception e) {
                throw new RuntimeException(ExceptionUtils.getFullStackTrace(e));
            }

        }
        return batchNum;
    }


}
