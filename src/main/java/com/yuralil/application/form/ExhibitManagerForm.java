    package com.yuralil.application.form;

    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.control.*;
    import javafx.scene.layout.*;
    import javafx.stage.Stage;

    public class ExhibitManagerForm extends VBox {

        private final ExhibitListView exhibitListView;
        private final TextField searchField;

        public ExhibitManagerForm() {
            setSpacing(20);
            setPadding(new Insets(30));
            setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 24;");

            Label title = new Label("Manage Exhibits");
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a3e2b;");

            searchField = new TextField();
            searchField.setPromptText("Search...");
            searchField.setPrefWidth(300);
            searchField.setStyle("""
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-color: #ccc;
                -fx-padding: 6 10;
            """);

            Button searchBtn = new Button("Search");
            searchBtn.setStyle("""
                -fx-background-color: #e6efe9;
                -fx-text-fill: #1a3e2b;
                -fx-background-radius: 8;
                -fx-font-weight: bold;
                -fx-padding: 6 14;
            """);

            HBox searchBox = new HBox(10, searchField, searchBtn);
            searchBox.setAlignment(Pos.CENTER_LEFT);
            searchBox.setPadding(new Insets(0, 0, 10, 0));

            exhibitListView = new ExhibitListView();

            ScrollPane scrollPane = new ScrollPane(exhibitListView);
            scrollPane.setFitToWidth(true); // розтягує контент по ширині scrollpane
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // ❗ прибирає нижню смугу

            scrollPane.setMaxHeight(500);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            VBox contentBox = new VBox(10, title, searchBox, scrollPane);
            VBox.setVgrow(contentBox, Priority.ALWAYS);

            Button addBtn = new Button("Add");
            addBtn.setOnAction(e -> AddExhibitForm.showForm((Stage) getScene().getWindow(), exhibitListView));


            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("Delete");

            for (Button btn : new Button[]{addBtn, editBtn, deleteBtn}) {
                btn.setStyle("""
                    -fx-background-color: #e6efe9;
                    -fx-text-fill: #1a3e2b;
                    -fx-background-radius: 8;
                    -fx-font-weight: bold;
                    -fx-padding: 6 14;
                """);
            }

            HBox buttonBox = new HBox(10, addBtn, editBtn, deleteBtn);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            buttonBox.setPadding(new Insets(10, 0, 0, 0));

            getChildren().addAll(contentBox, buttonBox);
        }
    }