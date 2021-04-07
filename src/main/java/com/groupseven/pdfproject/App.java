package com.groupseven.pdfproject;

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
import javafx.stage.Stage;

public class App extends Application {


    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 790;

    private MainCanvas canvas;
    private static DrawingToolbar _drawingToolBar;

    private DocumentModel doc;
    int currentPage;
    private Scene mainScene;

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
            doc = new DocumentModel("src/main/resources/test_pdf.pdf");
//            doc = new DocumentModel();
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

//        vbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                canvas.getEventHandler().Event(event);
//            }
//        });

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
            DocumentModel newDoc = new DocumentModel();
            setDisplayDoc(newDoc, 0);
        });

        fileMenu.getItems().add(newDocument);

        return fileMenu;
    }

    /// \brief create drawing menu element
    /// \return
    ///
    /// \ref t14_2_2 "task 14.2.2"
    /// \ref t9_1_1 "task 9.1.1"
    /// \ref t9_1_2 "task 9.1.2"
    private Menu createDrawingMenu() {
        Label drawLabel = new Label("Drawing");
        drawLabel.setOnMouseClicked(
                action -> {
                    if (_drawingToolBar == null)
                        _drawingToolBar = new DrawingToolbar(canvas);

                    canvas.getChildren().add(_drawingToolBar);
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

        menuBar.getMenus().addAll(fileMenu, drawingMenu, helpMenu);

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
        //root BorderPane allows for more versatile alignment than HBox or VBox
        BorderPane root = new BorderPane();
        GridPane ToolBox = createToolBox();
        MenuBar menuBar = createMenuBar();
        Button undobutton = createUndoButton();
        Button redobutton = createRedoButton();


        GridPane.setConstraints(undobutton, 0, 0);
        GridPane.setConstraints(redobutton, 1, 0);

        root.setTop(menuBar);
        root.setLeft(ToolBox);

        ToolBox.getChildren().addAll(undobutton,redobutton);

        mainScene = new Scene(root);
        primaryStage.setScene(mainScene);

        setDisplayDoc(doc, 0);

        primaryStage.show();

        canvas = doc.getPage(currentPage).getCanvas();
//        mainScene.setOnKeyPressed((KeyEvent event) -> {
//            canvas.getEventHandler().Event(event);
//        });
    }

    public void setDisplayDoc(DocumentModel document, int pageNum) {
        currentPage = 0;
        doc = document;
        PageModel page = document.getPage(pageNum);
        BorderPane root = (BorderPane) mainScene.getRoot();

        MainCanvas canvas = page.getCanvas();
        root.setCenter(createViewbox(page));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
