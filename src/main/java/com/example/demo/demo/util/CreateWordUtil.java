package com.example.demo.demo.util;

import com.example.demo.demo.common.ExportField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CreateWordUtil {

    public <T> void exportWord(HttpServletResponse response, T info, OutputStream out, String fileName) {
        XWPFDocument document = new XWPFDocument();
        try {
            response.setCharacterEncoding(Charset.defaultCharset().name());
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, Charset.defaultCharset().name()) + ".docx");
            Field[] infoFields = info.getClass().getDeclaredFields();
            List<Field> fieldList = Arrays.asList(infoFields).stream().filter(e ->
                    Objects.nonNull(e.getAnnotation(ExportField.class))).collect(Collectors.toList());

            //工作经历表格
            XWPFTable infoTable = document.createTable();
            //去表格边框
            infoTable.getCTTbl().getTblPr().unsetTblBorders();
            CTTblWidth comTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
            comTableWidth.setType(STTblWidth.DXA);
            comTableWidth.setW(BigInteger.valueOf(7000));

            //表格第一行
            XWPFTableRow comTableRow = infoTable.getRow(0);
            comTableRow.addNewTableCell();
            comTableRow.addNewTableCell();
            comTableRow.addNewTableCell();
            int i = 0;
            for (Field field : fieldList) {
                field.setAccessible(true);
                comTableRow.getCell(i++).setText(field.getAnnotation(ExportField.class).fieldName() + ":");
                comTableRow.getCell(i - 1).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
                comTableRow.getCell(i++).setText(field.get(info).toString());
                comTableRow.getCell(i - 1).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
                if (i == 4) {
                    comTableRow = infoTable.createRow();
                    i = 0;
                }
            }
//
//
//            comTableRowOne.getCell(0).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
//            comTableRowOne.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//            comTableRowOne.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//            comTableRowOne.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//            comTableRowOne.getCell(0).setText(inventoryRecordVO.getTemplateTitle());
//            comTableRowOne.getCell(0).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
//            comTableRowOne.setHeight(600);
//            int i = 0;
//            int j = 0;
//            XWPFTableRow comTableRow = ComTable.createRow();
//            for (InventoryRecordFieldInfoVO info : list) {
//                if (info.getFieldLength() == 2) {
//                    if (i != 0) {
//                        comTableRow = ComTable.createRow();
//                    }
//                    comTableRow.getCell(0).setText(info.getFieldName());
//                    comTableRow.getCell(0).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
//                    comTableRow.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
//                    comTableRow.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//                    comTableRow.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//                    if (info.getFieldId() == 11L) {
//                        XWPFTableCell imageCell = comTableRow.getCell(1);
//                        List<XWPFParagraph> paragraphs = imageCell.getParagraphs();
//                        XWPFParagraph newPara = paragraphs.get(0);
//                        XWPFRun imageCellRunn = newPara.createRun();
//                        imageCellRunn.addPicture(new FileInputStream("d:/1.png"), XWPFDocument.PICTURE_TYPE_PNG, "1.png", 1200000, 400000);
//                        //imageCellRunn.addBreak();
//                    } else {
//                        comTableRow.getCell(1).setText(info.getFieldId() == 2L ? bizOrgService.names(info.getFieldValue()) : info.getFieldValue());
//                    }
//                    comTableRow.getCell(1).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
//                    comTableRow.setHeight(info.getFieldHeight() * 400);
//                    i = 4;
//                } else {
//                    comTableRow.getCell(i++).setText(info.getFieldName());
//                    comTableRow.getCell(i - 1).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
//                    comTableRow.getCell(i++).setText(info.getFieldId() == 2L ? bizOrgService.names(info.getFieldValue()) : info.getFieldValue());
//                    comTableRow.getCell(i - 1).getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
//                    comTableRow.setHeight(info.getFieldHeight() * 400);
//                }
//                j++;
//                if (i == 4 && j < list.size()) {
//                    comTableRow = ComTable.createRow();
//                    i = 0;
//                }
//            }
            document.write(out);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }

    }
}
