package com.example.demo.demo.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xugm
 * @create 2022/1/10 14:15
 */
public class ShardingQryExecutor {
    public <T> Callable<List<T>> queryForList(final ShardPageMapper mapper, final Map<String, Object> paramMap){

        return new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                List<T> list = mapper.queryForListShardingPage(paramMap);
                return list;
            }
        };
    }
}
