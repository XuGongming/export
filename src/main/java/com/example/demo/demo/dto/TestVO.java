/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.demo.dto;

import lombok.Data;

/**
 * @author zhaoxj@tsingyun.net
 * @date 2020/2/24 4:39 下午
 */
@Data
public class TestVO {

    private Long id;

    /**
     * 审批状态
     */
    private String name;


    private String orgCode;
}
