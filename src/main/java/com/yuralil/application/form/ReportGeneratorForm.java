package com.yuralil.application.form;

import com.yuralil.domain.dao.ReportDao;
import com.yuralil.domain.entities.Report;
import com.yuralil.domain.enums.ReportType;
import com.yuralil.domain.service.ReportExcelService;
import com.yuralil.domain.service.ReportWordService;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Форма JavaFX для генерації та перегляду звітів у системі.
 * <p>
 * Підтримує:
 * <ul>
 *     <li>Вибір типу звіту (DOCX/Excel)</li>
 *     <li>Генерацію та збереження звіту в базу</li>
 *     <li>Вивід історії звітів з можливістю завантаження</li>
 * </ul>
 */
public class ReportGeneratorForm extends VBox {

    private final ComboBox<ReportType> reportTypeComboBox;
    private final Label statusLabel;
    private final VBox reportList;

    /**
     * Конструктор ініціалізує форму генерації звітів.
     */
    public ReportGeneratorForm() {
        setSpacing(30);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setMaxWidth(Double.MAX_VALUE);

        Text title = new Text("📑 Генерація звіту");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll(ReportType.values());
        reportTypeComboBox.setPromptText("Оберіть тип звіту");

        Button generateBtn = new Button("🛠 Згенерувати");
        statusLabel = new Label();
        reportList = new VBox(10);
        reportList.setPadding(new Insets(10));

        generateBtn.setOnAction(e -> generateReport());

        VBox section = new VBox(10, reportTypeComboBox, generateBtn, statusLabel);
        section.setAlignment(Pos.CENTER);

        Separator separator = new Separator();
        Text historyTitle = new Text("🗂 Історія звітів:");
        historyTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        getChildren().addAll(title, section, separator, historyTitle, reportList);

        loadReportHistory();
    }

    /**
     * Генерує звіт відповідного типу та зберігає його в базу.
     * Використовує відповідний сервіс залежно від {@link ReportType}.
     */
    private void generateReport() {
        ReportType selectedType = reportTypeComboBox.getValue();
        if (selectedType == null) {
            statusLabel.setText("❌ Оберіть тип звіту!");
            return;
        }

        try (Connection conn = new ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);

            byte[] content;
            if (selectedType == ReportType.EXHIBIT_DESCRIPTION) {
                content = new ReportWordService().generate();
            } else {
                content = new ReportExcelService().generate();
            }

            Report report = new Report();
            report.setType(selectedType);
            report.setGeneratedAt(LocalDateTime.now());
            report.setContent(content);

            ReportDao.getInstance().insert(report);
            statusLabel.setText("✅ Звіт збережено в базу!");
            loadReportHistory();

        } catch (Exception ex) {
            statusLabel.setText("❌ Помилка при генерації звіту.");
            ex.printStackTrace();
        } finally {
            ConnectionHolder.clear();
        }
    }

    /**
     * Завантажує список усіх звітів із бази даних та відображає їх.
     * Кожен звіт можна завантажити через кнопку "⬇ Завантажити".
     */
    private void loadReportHistory() {
        reportList.getChildren().clear();

        try (Connection conn = new ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);
            List<Report> reports = ReportDao.getInstance().findAll();

            for (Report report : reports) {
                HBox item = new HBox(10);
                item.setAlignment(Pos.CENTER_LEFT);

                Label info = new Label("📄 " + report.getType() + " | " + report.getGeneratedAt());
                Button downloadBtn = new Button("⬇ Завантажити");

                downloadBtn.setOnAction(e -> {
                    FileChooser chooser = new FileChooser();
                    chooser.setInitialFileName("report_" + report.getType().name().toLowerCase());
                    if (report.getType() == ReportType.EXHIBIT_DESCRIPTION) {
                        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word (*.docx)", "*.docx"));
                    } else {
                        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx"));
                    }

                    var file = chooser.showSaveDialog(getScene().getWindow());
                    if (file != null) {
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            out.write(report.getContent());
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Не вдалося зберегти файл.").showAndWait();
                        }
                    }
                });

                item.getChildren().addAll(info, downloadBtn);
                reportList.getChildren().add(item);
            }

        } catch (Exception e) {
            reportList.getChildren().add(new Label("❌ Помилка при завантаженні історії."));
            e.printStackTrace();
        } finally {
            ConnectionHolder.clear();
        }
    }
}
