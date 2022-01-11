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

    public <T> PageResponse<T> queryForListShardingPage(ShardPageMapper mapper, Map<String, Object> paramMap, int libNo){
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
        int page = MapUtils.getIntValue(paramMap, "page", 1);
        int size = MapUtils.getIntValue(paramMap, "size", 15);
        int from = (page - 1) * size;
        int to = Math.min(returnList.size(), page * size);
        return PageResponse.apply(returnList.size(), returnList.subList(from, to));
    }


}
