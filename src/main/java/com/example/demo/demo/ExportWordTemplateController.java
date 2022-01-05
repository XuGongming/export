/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.demo;

import com.example.demo.demo.service.ExportWordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@RestController
public class ExportWordTemplateController {

    @Resource
    private ExportWordService exportWordService;


    @GetMapping("/export/inventory/record/info/{id}")
    public void exportInventoryRecordInfo(HttpServletResponse response, @PathVariable Long id) {
        exportWordService.exportInventoryRecordFile(response,id);
    }
}
