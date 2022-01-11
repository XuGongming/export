package com.example.demo.demo;


import com.example.demo.demo.common.PageResponse;
import com.example.demo.demo.common.Response;
import com.example.demo.demo.dto.TestVO;
import com.example.demo.demo.mapper.TestShardPageMapper;
import com.example.demo.demo.query.TestQuery;
import com.example.demo.demo.util.BeanUtil;
import com.example.demo.demo.util.ShardPageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @GetMapping("/test/shard")
    public Response<PageResponse<TestVO>> queryByPrimaryKey(TestQuery query) {
        PageResponse<TestVO> page = shardPageUtil.queryForListShardingPage(testShardPageMapper, BeanUtil.objectToMap(query), 10);
        return Response.ok(page);

    }
}
