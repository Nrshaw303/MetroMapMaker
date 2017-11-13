/*
 * Nicholas Shaw
 * 110381707
 * Fill with more info
 */

package map.gui;

import static djf.AppPropertyType.LOAD_ERROR_CONTENT;
import static djf.AppPropertyType.LOAD_ERROR_TITLE;
import djf.AppTemplate;
import djf.ui.AppDialogs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class mapWelcome {
    
    // WELCOME DIALOG ELEMENTS
    AppTemplate app;
    Stage welcomeStage;
    BorderPane welcomePane;
    VBox leftVBox;
    Button newWorkButton;
    Button loadPreviousButton;
    Button closeButton;
    Image MMMIcon;
    ImageView LogoImage;
    
    // SLIDE OUT MENU
    Pane slideMenu;
    VBox slideVBox;
    TranslateTransition menuTranslation;
    Button slideClose;
    
    // RECENT WORKS
    File recents;
    FileReader fileReader;
    BufferedReader bufferedReader;
    
    // HANDLE REQUESTS
    String requestType = "";
    public static final String NEW_MAP_REQUEST = "new_map_request";
    public static final String LOAD_MAP_REQUEST = "load_map_request";
    
    public mapWelcome(AppTemplate app){
        this.app = app;
    }

    public String initWelcome(){
        // INIT MAIN STAGE
        recents = new File("recent_files.txt");
        welcomeStage = new Stage();
        welcomeStage.setTitle("Welcome");
        MMMIcon = new Image("file:images/MMMIcon.png");
        welcomeStage.getIcons().add(MMMIcon);
        welcomePane = new BorderPane();
        welcomeStage.setScene(new Scene(welcomePane, 700, 400));
        
        // MAIN BUTTONS
        newWorkButton = new Button("Create New Work");
        loadPreviousButton = new Button("Load Previous Work");
        closeButton = new Button("Close");
        
        // LOGO IMAGE
        LogoImage = new ImageView(MMMIcon);
        
        initLayout();
        initStyle();
        initControllers();
        return requestType;
    }
    
    public void initLayout(){
        // 
        leftVBox = new VBox();
        slideMenu = new Pane();
        slideVBox = new VBox();
        
        // SLIDE MENU
        slideVBox.prefHeightProperty().bind(welcomePane.heightProperty());
        slideVBox.setPrefWidth(200);
        slideClose = new Button("Close");
        slideVBox.getChildren().addAll(slideClose, new Separator());
        slideMenu.getChildren().add(slideVBox);
        slideMenu.setTranslateX(-200);
        loadRecents(slideVBox);
        
        // SLIDE TRANSITION
        menuTranslation = new TranslateTransition(Duration.millis(150), slideMenu);
        menuTranslation.setFromX(-200);
        menuTranslation.setToX(0);
        
        // ADDING BUTTONS TO MAIN PANE
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.getChildren().addAll(newWorkButton, loadPreviousButton);
        welcomePane.setLeft(leftVBox);
        welcomePane.setRight(closeButton);
        welcomePane.setCenter(LogoImage);
        welcomePane.getChildren().add(slideMenu);
    }
    
    public void initStyle(){
        welcomePane.getStylesheets().add("map/css/map_welcome_style.css");
        closeButton.setMaxSize(150, 50);
        newWorkButton.setMaxSize(250, 50);
        loadPreviousButton.setMaxSize(250, 50);
        slideClose.setMaxSize(200, 50);
        LogoImage.setFitHeight(250);
        LogoImage.setFitWidth(250);
        
        slideMenu.setId("slide-menu");
        slideVBox.setStyle("-fx-background-color: #93CEFF;");
        leftVBox.setId("side-menu");
        welcomePane.setId("main");
    }
    
    public void initControllers(){
        
        closeButton.setOnAction(i -> {
            requestType = NEW_MAP_REQUEST;
            welcomeStage.close(); 
        });
        newWorkButton.setOnAction(i -> {
            requestType = NEW_MAP_REQUEST;
            welcomeStage.close();
        });
        loadPreviousButton.setOnAction(i ->{
            menuTranslation.setRate(1);
            menuTranslation.play();
        });
        slideClose.setOnAction(i ->{
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });
        welcomeStage.showAndWait();
        if (requestType == "")
            requestType = NEW_MAP_REQUEST;
    }
    
    public void loadRecents(Pane menu){
        try{
            fileReader = new FileReader(recents);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            
            while ((line = bufferedReader.readLine()) != null){
                File file = new File(line);
                if (file.exists()){
                    String[] lineArray = line.split("//");
                    Button recentFile = new Button(lineArray[lineArray.length - 1]);
                    recentFile.setMaxSize(200, 50);
                    recentFile.setOnAction(j -> {
                        requestType = LOAD_MAP_REQUEST;
                        welcomeStage.close();
                    });
                    menu.getChildren().add(recentFile);
                }
            }
        }catch(Exception e){
            AppDialogs.showMessageDialog(app.getGUI().getWindow(), LOAD_ERROR_TITLE, LOAD_ERROR_CONTENT);
        }
    }
}
