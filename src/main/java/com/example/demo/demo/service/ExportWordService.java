/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.demo.service;



import javax.servlet.http.HttpServletResponse;

public interface ExportWordService {


    void exportInventoryRecordFile(HttpServletResponse response, Long id);
}
