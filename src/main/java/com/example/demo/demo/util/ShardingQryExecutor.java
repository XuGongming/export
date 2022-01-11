package com.example.demo.demo.util;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xugm
 * @create 2022/1/10 14:15
 */
public class ShardingQryExecutor {
    public <T> Callable<List<T>> queryForList(final ShardPageMapper mapper, final Map<String, Object> paramMap) {

        return () -> mapper.queryForListShardingPage(paramMap);
    }

    public Callable<Map<String, Object>> queryForListCount(final ShardPageMapper mapper, final Map<String, Object> paramMap) {

        return () -> {
            Map<String, Object> map=new HashMap<>();
            map.put("tableFix",MapUtils.getInteger(paramMap, "tableFix"));
            map.put("tableCount",mapper.queryForListShardingCount(paramMap));
            return map;
        };
    }
}
