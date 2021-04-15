package com.groupseven.pdfproject;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.groupseven.pdfproject.utilities.DrawingMode;
import com.groupseven.pdfproject.utilities.DrawingTool;
import com.groupseven.pdfproject.view.DrawingToolbar;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;


public class App extends Application {

    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 790;

    private MainCanvas canvas;

    private DocumentModel doc;
    int currentPage;
    private Scene mainScene;
    private static DrawingToolbar _drawingToolBar;

    /// \ref t8_3 "task 8.3"
    private EventHandler handleImportAsset = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

        }
    };

    /// \brief creates a new instance of the Document Model to be displayed
    /// \return void
    ///
    /// \ref t14_1 "task 14.1"
    private void initializeDocument() {
        try {
            File file = new File("src/main/resources/test_pdf.pdf");

            doc = new DocumentModel(file);
            // doc = new DocumentModel();
            currentPage = 0;
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// \brief creates a viewbox element containing a pdf document
    /// \return VBox displaying the pdf and canvas element
    ///
    /// \ref t14_1 "task 14.1"
    private VBox createViewbox(PageModel page) {
        VBox vbox = new VBox(0);
        vbox.setAlignment(Pos.BASELINE_RIGHT);
        vbox.getChildren().add(page.getNode());
        page.clear();

        // vbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
        // @Override
        // public void handle(KeyEvent event) {
        // canvas.getEventHandler().Event(event);
        // }
        // });

        return vbox;
    }

    /// \brief create file menu element
    /// \return Menu containing all relevant dropdown elements
    ///
    /// \ref t14_2_1 "task 14.2.1"
    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newDocument = new MenuItem("New Document");
        newDocument.setOnAction(e -> {
            newDocumentWindow();
        });

        MenuItem openDocument = new MenuItem("Open Document");

        openDocument.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);

            try {
                DocumentModel newDoc = new DocumentModel(selectedFile);
                setDisplayDoc(newDoc, 0);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        MenuItem saveDocument = new MenuItem("Save Document");

        /// ref t8_8 "task 8.8"
        saveDocument.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(null);

            try {
                doc.export(selectedFile);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        fileMenu.getItems().add(newDocument);
        fileMenu.getItems().add(openDocument);
        fileMenu.getItems().add(saveDocument);

        return fileMenu;
    }

    /// \brief display new window for creating new documents
    ///
    /// \ref t8_2 "task 8.2"
    private void newDocumentWindow(){
        BorderPane newdocPane = new BorderPane();
        Scene newdocScene = new Scene(newdocPane, 300, 250);
        Stage newdocWindow = new Stage();

        Button createdocButton = new Button("Create New Document");
        RadioButton defaultButton = new RadioButton("Default");
        RadioButton customButton = new RadioButton("Custom");
        ToggleGroup newdocGroup = new ToggleGroup();
        defaultButton.setToggleGroup(newdocGroup);
        defaultButton.setSelected(true);
        customButton.setToggleGroup(newdocGroup);

        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField("595");
        widthField.setMaxWidth(100);
        TextField heightField = new TextField("842");
        heightField.setMaxWidth(100);

        widthField.disableProperty().bind(defaultButton.selectedProperty());
        heightField.disableProperty().bind(defaultButton.selectedProperty());

        defaultButton.setOnAction(f -> {
            widthField.setText("595");
            heightField.setText("842");
        });

        widthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if(!newValue.matches("\\d*")){
                    widthField.setText("595");
                }
            }
        });

        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if(!newValue.matches("\\d*")){
                    heightField.setText("842");
                }
            }
        });

        VBox dimensionBox = new VBox(5);
        dimensionBox.setPadding(new Insets(25, 25, 25, 50));
        dimensionBox.getChildren().addAll(defaultButton, customButton, widthLabel, widthField, heightLabel, heightField, createdocButton);

        createdocButton.setOnAction(f -> {
            if(defaultButton.isSelected()){
                DocumentModel newDoc = new DocumentModel();
                setDisplayDoc(newDoc, 0);
            }else{
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                DocumentModel newDoc = new DocumentModel(width, height);
                setDisplayDoc(newDoc, 0);
            }
            newdocWindow.close();
        });

        newdocPane.setCenter(dimensionBox);
        newdocWindow.setTitle("New Document");
        newdocWindow.setScene(newdocScene);
        newdocWindow.show();
    }

    /// \brief create edit menu element
    /// \return Menu containing all relevant dropdown elements
    ///
    /// \ref t8_2 "task 8.2"
    private Menu createEditMenu(){
        Menu editMenu = new Menu("Edit");

        MenuItem resize = new MenuItem("Resize...");
        resize.setOnAction(e -> {
            resizeWindow();
        });

        editMenu.getItems().add(resize);

        return editMenu;
    }

    /// \brief display new window for resizing existing documents
    ///
    /// \ref t8.2 "task 8.2"
    private void resizeWindow(){
        BorderPane resizePane = new BorderPane();
        Scene resizeScene = new Scene(resizePane, 300, 250);
        Stage resizeStage = new Stage();

        Button resizeButton = new Button("Resize");
        Button cancelButton = new Button("Cancel");
        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField("" + (int) doc.getPage(currentPage).getCanvas().getCanvas().getWidth());
        widthField.setMaxWidth(100);
        TextField heightField = new TextField("" + (int) doc.getPage(currentPage).getCanvas().getCanvas().getHeight());
        heightField.setMaxWidth(100);

        widthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if(!newValue.matches("\\d*")){
                    widthField.setText("" + (int) doc.getPage(currentPage).getCanvas().getCanvas().getWidth());
                }
            }
        });

        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if(!newValue.matches("\\d*")){
                    heightField.setText("" + (int) doc.getPage(currentPage).getCanvas().getCanvas().getHeight());
                }
            }
        });

        cancelButton.setOnAction(e ->{
            resizeStage.close();
        });

        resizeButton.setOnAction(e ->{
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());
            doc.setDimensions(width, height, currentPage);
            setDisplayDoc(doc, currentPage);
            doc.getPage(currentPage).getCanvas().refresh();
            resizeStage.close();
        });

        HBox resizeHBox = new HBox(5);
        resizeHBox.setPadding(new Insets(25, 25, 25, 50));
        resizeHBox.getChildren().addAll(resizeButton, cancelButton);
        VBox resizeVBox = new VBox(5);
        resizeVBox.setPadding(new Insets(25, 25, 25, 50));
        resizeVBox.getChildren().addAll(widthLabel, widthField, heightLabel, heightField, resizeHBox);

        resizePane.setCenter(resizeVBox);
        resizeStage.setTitle("Resize");
        resizeStage.setScene(resizeScene);
        resizeStage.show();
    }

    /// \brief create drawing menu element
    /// \return
    ///
    /// \ref t14_2_2 "task 14.2.2"
    /// \ref t9_1_1 "task 9.1.1"
    /// \ref t9_1_2 "task 9.1.2"
    private Menu createDrawingMenu() {
        Label drawLabel = new Label("Drawing");
        drawLabel.setOnMouseClicked(action -> {
            if (_drawingToolBar != null && canvas.getChildren().contains(_drawingToolBar)) {
                canvas.getChildren().remove(_drawingToolBar);
            } else {
                _drawingToolBar = new DrawingToolbar(canvas);
                canvas.getChildren().add(_drawingToolBar);
            }

        });

        Menu drawingMenu = new Menu();
        drawingMenu.setGraphic(drawLabel);
        return drawingMenu;
    }

    /// \brief create help menu element
    /// \return Menu containing all relevant dropdown elements
    ///
    /// \ref t14_2_3 "task 14.2.3"
    private Menu createHelpMenu() {
        Menu helpMenu = new Menu("Help");

        return helpMenu;
    }

    /// \brief create menu bar element
    /// \return Menu containing all relevant dropdown elements
    ///
    /// \ref t14.2 "task 14.2"
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = createFileMenu();
        Menu drawingMenu = createDrawingMenu();
        Menu helpMenu = createHelpMenu();
        Menu editMenu = createEditMenu();

        menuBar.getMenus().addAll(fileMenu, editMenu, drawingMenu, helpMenu);

        return menuBar;
    }

    /// \brief Create Toolbox elements and populate with buttons
    /// \return GridPane element containing all relevant buttons
    ///
    /// \ref t14_3 "task 14.3"
    private GridPane createToolBox() {
        GridPane ToolWindow = new GridPane();

        Button doNothing = new Button();
        Button doNothing2 = new Button();

        ToolWindow.setMinWidth(300);
        ToolWindow.setAlignment(Pos.BASELINE_LEFT);
        ToolWindow.setStyle("-fx-background-color: Gray;");

        ToolWindow.getChildren().add(doNothing);
        ToolWindow.getChildren().add(doNothing2);

        return ToolWindow;
    }

    /// \brief create undo button element
    /// \return Button for activating undo feature
    ///
    /// \ref t10_1 "task 10.1"
    private Button createUndoButton() {
        Image undoimg = new Image("undoarrow.png");
        ImageView undoview = new ImageView(undoimg);

        Button undobutton = new Button();
        undobutton.setGraphic(undoview);
        undobutton.setOnAction(event -> canvas.undo());
        Tooltip undotip = new Tooltip("Undo");
        Tooltip.install(undobutton, undotip);
        return undobutton;
    }

    /// \brief create redo button element
    /// \return Button for activating redo feature
    ///
    /// \ref t10_1 "task 10.1"
    private Button createRedoButton() {
        Image redoimg = new Image("redoarrow.png");
        ImageView redoview = new ImageView(redoimg);

        Button redobutton = new Button();
        redobutton.setGraphic(redoview);
        redobutton.setOnAction(event -> canvas.redo());
        Tooltip redotip = new Tooltip("Redo");
        Tooltip.install(redobutton, redotip);
        return redobutton;
    }

    /// \brief starts javafx GUI
    ///
    /// \return void
    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeDocument();
        primaryStage.setTitle("PDF Project");
        // root BorderPane allows for more versatile alignment than HBox or VBox
        BorderPane root = new BorderPane();
        GridPane ToolBox = createToolBox();
        MenuBar menuBar = createMenuBar();
        Button undobutton = createUndoButton();
        Button redobutton = createRedoButton();

        GridPane.setConstraints(undobutton, 0, 0);
        GridPane.setConstraints(redobutton, 1, 0);

        root.setTop(menuBar);
        root.setLeft(ToolBox);

        ToolBox.getChildren().addAll(undobutton, redobutton);

        mainScene = new Scene(root);
        primaryStage.setScene(mainScene);

        setDisplayDoc(doc, 0);

        primaryStage.show();

        canvas = doc.getPage(currentPage).getCanvas();
        // mainScene.setOnKeyPressed((KeyEvent event) -> {
        // canvas.getEventHandler().Event(event);
        // });
    }

    public void setDisplayDoc(DocumentModel document, int pageNum) {
        currentPage = 0;
        doc = document;
        PageModel page = document.getPage(pageNum);
        BorderPane root = (BorderPane) mainScene.getRoot();

        canvas = page.getCanvas();
        root.setCenter(createViewbox(page));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
