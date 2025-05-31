package com.yuralil.domain.service;

import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.entities.Exhibit;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReportWordService {

    public byte[] generate() {
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFParagraph title = doc.createParagraph();
            title.createRun().setText("Опис усіх експонатів");
            title.setSpacingAfter(300);

            List<Exhibit> exhibits = ExhibitDao.getInstance().findAll();
            for (Exhibit exhibit : exhibits) {
                XWPFParagraph p = doc.createParagraph();
                p.setSpacingAfter(200);
                p.createRun().setText("Назва: " + exhibit.getName());
                p.createRun().addBreak();
                p.createRun().setText("Опис: " + exhibit.getDescription());
                p.createRun().addBreak();
                p.createRun().setText("Категорія: " + exhibit.getCategory().getName());
                p.createRun().addBreak();
                p.createRun().setText("Дата надходження: " + exhibit.getAcquisitionDate().toString());
                p.createRun().addBreak();
            }

            doc.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Помилка при генерації Word-звіту", e);
        }
    }
}
