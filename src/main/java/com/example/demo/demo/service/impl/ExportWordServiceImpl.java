/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.demo.service.impl;

import com.example.demo.demo.dto.UserVO;
import com.example.demo.demo.mapper.UserMapper;
import com.example.demo.demo.service.DetailService;
import com.example.demo.demo.service.ExportWordService;
import com.example.demo.demo.util.CreateWordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@Service
public class ExportWordServiceImpl extends DetailService<UserVO, UserMapper> implements ExportWordService {

    @Resource
    private CreateWordUtil createWordUtil;

    @Override
    public void exportInventoryRecordFile(HttpServletResponse response, Long id) {
        UserVO info = this.getByIdForExportDetail(id);
        String fileName = "test";
        try {
            OutputStream out = response.getOutputStream();
            createWordUtil.exportWord(response, info, out, fileName);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
