package com.yuralil.domain.service;

import com.yuralil.domain.dao.ExhibitDao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Сервіс для генерації Excel-звіту зі статистикою по категоріях експонатів.
 * <p>
 * Створює файл .xlsx з двома стовпцями:
 * <ul>
 *     <li>Категорія</li>
 *     <li>Кількість експонатів</li>
 * </ul>
 * <p>
 * Дані отримуються з {@link ExhibitDao#countByCategory()}.
 */
public class ReportExcelService {

    /**
     * Генерує Excel-звіт і повертає його вміст у вигляді байтового масиву.
     *
     * @return масив байтів .xlsx-файлу
     */
    public byte[] generate() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Статистика по категоріях");

            // Стиль заголовка
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Заголовок
            Row header = sheet.createRow(0);
            Cell catCell = header.createCell(0);
            catCell.setCellValue("Категорія");
            catCell.setCellStyle(headerStyle);

            Cell countCell = header.createCell(1);
            countCell.setCellValue("Кількість експонатів");
            countCell.setCellStyle(headerStyle);

            // Дані
            Map<String, Integer> stats = ExhibitDao.getInstance().countByCategory();
            int rowIndex = 1;
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Помилка при генерації Excel-звіту", e);
        }
    }
}
