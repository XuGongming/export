package com.example.demo.demo;


import com.example.demo.demo.common.PageResponse;
import com.example.demo.demo.common.Response;
import com.example.demo.demo.dto.TestInsertDTO;
import com.example.demo.demo.dto.TestVO;
import com.example.demo.demo.mapper.TestShardPageMapper;
import com.example.demo.demo.query.TestQuery;
import com.example.demo.demo.util.BeanUtil;
import com.example.demo.demo.util.ShardPageUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xugm
 * @create 2022/1/10 17:59
 */
@RestController
public class TestController {
    @Resource
    private ShardPageUtil shardPageUtil;

    @Resource
    private TestShardPageMapper testShardPageMapper;

    @GetMapping("/test/shard/page")
    public Response<PageResponse<TestVO>> queryShardingPage(TestQuery query) {
        PageResponse<TestVO> page = shardPageUtil.queryForListShardingPage(testShardPageMapper, BeanUtil.objectToMap(query), 10);
        return Response.ok(page);

    }

    @GetMapping("/test/shard/list")
    public Response<List<TestVO>> queryShardingList(TestQuery query) {
        List<TestVO> page = shardPageUtil.queryForListSharding(testShardPageMapper, BeanUtil.objectToMap(query), 10);
        return Response.ok(page);

    }

    @PostMapping("/test/shard/insert")
    public Response<Integer> insertShardingList(@RequestBody List<TestInsertDTO> list){
        Integer page = shardPageUtil.batchInsertSharding(testShardPageMapper, list, 10);
        return Response.ok(page);

    }
}
