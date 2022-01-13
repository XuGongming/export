package com.example.demo.demo.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author xugm
 * @create 2022/1/10 14:15
 */

public class ShardingQryExecutor {

    private RedisTemplate redisTemplate;

    private TransactionTemplate transactionTemplate;

    public ShardingQryExecutor(RedisTemplate redisTemplate, TransactionTemplate transactionTemplate) {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate = redisTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public ShardingQryExecutor() {

    }

    public <T> Callable<List<T>> queryForList(final ShardPageMapper mapper, final Map<String, Object> paramMap) {

        return () -> mapper.queryForListShardingPage(paramMap);
    }

    public Callable<Map<String, Object>> queryForListCount(final ShardPageMapper mapper, final Map<String, Object> paramMap) {

        return () -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tableFix", MapUtils.getInteger(paramMap, "tableFix"));
            map.put("tableCount", mapper.queryForListShardingCount(paramMap));
            return map;
        };
    }


    public Callable<Integer> batchInsertSharding(ShardPageMapper mapper, Map<String, Object> param) {
        return () -> {
            String uuid = MapUtils.getString(param, "uuid");
            Long tableNum = MapUtils.getLong(param, "tableNum");
            try {
                int re = mapper.batchInsertSharding(param);
                redisTemplate.opsForValue().increment(uuid + "success", 1);
                redisTemplate.expire(uuid + "success", 10, TimeUnit.MINUTES);
                while (true) {
                    if (redisTemplate.opsForValue().get(uuid + "success") != null && Long.parseLong(redisTemplate.opsForValue().get(uuid + "success").toString()) == tableNum) {
                        return re;
                    } else if (redisTemplate.opsForValue().get(uuid + "fail") != null) {
                        throw new Exception("其他线程存在失败！");
                    }
                    Thread.sleep(500L);
                }
            } catch (Exception e) {
                redisTemplate.opsForValue().increment(uuid + "fail", 1);
                redisTemplate.expire(uuid + "fail", 10, TimeUnit.MINUTES);
                throw new RuntimeException(ExceptionUtils.getFullStackTrace(e));
            }
        };

    }


    public Callable<Integer> batchInsertShardingTx(ShardPageMapper mapper, Map<String, Object> param) {
        return () -> {
            Integer result = transactionTemplate.execute(status -> {
                String uuid = MapUtils.getString(param, "uuid");
                int tableNum = MapUtils.getInteger(param, "tableNum");
                try {
                    int re = mapper.batchInsertSharding(param);
                    redisTemplate.opsForValue().increment(uuid + "success", 1);
                    redisTemplate.expire(uuid + "success", 10, TimeUnit.MINUTES);
                    while (true) {
                        if (redisTemplate.opsForValue().get(uuid + "success") != null && Integer.parseInt(redisTemplate.opsForValue().get(uuid + "success").toString()) == tableNum) {
                            return re;
                        } else if (redisTemplate.opsForValue().get(uuid + "fail") != null) {
                            throw new Exception("其他线程存在失败！");
                        }
                        Thread.sleep(200L);
                    }
                } catch (Exception e) {
                    redisTemplate.opsForValue().increment(uuid + "fail", 1);
                    redisTemplate.expire(uuid + "fail", 10, TimeUnit.MINUTES);
                    status.isRollbackOnly();
                    throw new RuntimeException(e.getMessage());
                }
            });
            return result;
        };

    }
}
