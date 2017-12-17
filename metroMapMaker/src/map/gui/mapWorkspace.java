package map.gui;

import static djf.AppPropertyType.SAVE_VERIFY_CONTENT;
import static djf.AppPropertyType.SAVE_VERIFY_TITLE;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import map.data.mapData;
import static map.data.mapData.BLACK_HEX;
import static map.data.mapData.WHITE_HEX;
import map.data.mapState;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.language.AppLanguageSettings;
import static djf.language.AppLanguageSettings.FILE_PROTOCOL;
import static djf.language.AppLanguageSettings.PATH_IMAGES;
import djf.ui.AppDialogs;
import static djf.ui.AppGUI.DISABLED;
import static djf.ui.AppGUI.ENABLED;
import djf.ui.FileController;
import static java.lang.Double.MAX_VALUE;
import static map.css.mapStyle.*;
import map.data.Draggable;
import static map.mapPropertyType.*;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jtps.jTPS;
import map.data.DraggableImage;
import map.data.DraggableText;
import map.data.MetroLine;
import map.data.MetroStation;
import map.transactions.ChangeBackgroundColor_Transaction;
import map.transactions.ChangeShapeOutlineThickness_Transaction;
import map.transactions.ChangeShapeRadius_Transaction;
import properties_manager.PropertiesManager;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapWorkspace extends AppWorkspaceComponent {
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    
    // LINES PANE
    VBox linesPane;
    HBox linesTopPane;
    HBox linesMiddlePane;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLineButton;
    Button removeStationToLineButton;
    Button stationsAsListButton;
    Text metroLinesText;
    ComboBox metroLinesComboBox;
    Button editLineButton;
    Slider lineWidthSlider;
    Region linesSpacer;
    
    // STATION PANE
    VBox stationsPane;
    HBox stationsTopPane;
    HBox stationsMiddlePane;
    Button addStationButton;
    Button removeStationButton;
    Button snapToGridButton;
    Button moveLabelButton;
    Button rotateLabelButton;
    Text metroStationsText;
    ComboBox metroStationsComboBox;
    ColorPicker stationColorPicker;
    Slider stationRadiusSlider;
    Region stationSpacer;
    
    // ROUTE PANE
    HBox routePane;
    VBox locationPane;
    ComboBox startLocationComboBox;
    ComboBox endLocationComboBox;
    Button findRouteButton;
    
    // DECORE PANE
    VBox decorePane;
    HBox decoreTopPane;
    HBox decoreBottomPane;
    Button setImageBackgroundButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
    Text decoreText;
    ColorPicker decoreColorPicker;
    Region decoreSpacer;

    // FONT PANE
    VBox fontPane;
    HBox fontTopPane;
    HBox fontBottomPane;
    ToggleButton boldButton;
    ToggleButton italicsButton;
    ComboBox fontSizeComboBox;
    ComboBox fontFamilyComboBox;
    Text fontText;
    ColorPicker fontColorColorPicker;
    Region fontSpacer;

    // NAVIGATION PANE
    VBox navigationPane;
    HBox navigationTopPane;
    HBox navigationBottomPane;
    Button zoomInButton;
    Button zoomOutButton;
    Button increaseMapSizeButton;
    Button decreaseMapSizeButton;
    Text navigationText;
    CheckBox showGridCheckBox;
    Region navigationSpacer;
    
    // FOR NAVIGATION
    Group zoomGroup;
    Group canvasGroup;
    ScrollPane scrollPane;
    double panCounterX;
    double panCounterY;
    
    BackgroundImage backgroundImage;
    
    ObservableList<String> stationNames = FXCollections.observableArrayList();
    ArrayList<MetroStation> listOfStations = new ArrayList();
    
    ObservableList<String> lineNames = FXCollections.observableArrayList();
    ArrayList<MetroLine> listOfLines = new ArrayList();
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;
      
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;
    String requestType;
    
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public mapWorkspace(AppTemplate initApp) {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        // WELCOME DIALOG

        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
    }
    
    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    
    public ColorPicker getStationColorPicker() {
	return stationColorPicker;
    }
    
    public ColorPicker getDecoreColorPicker() {
	return decoreColorPicker;
    }
    
    public Slider getStationRadius() {
	return stationRadiusSlider;
    }
    
    public Slider getLineThicknessSlider() {
       return lineWidthSlider; 
    }

    public Pane getCanvas() {
	return canvas;
    }
    
    public void initDebugText() {
	canvas.getChildren().add(debugText);        
    }
        
    // HELPER SETUP METHOD
    private void initLayout() {
        // WE'LL USE THIS TO GET TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox(45);
        editToolbar.getStyleClass().add("edit-toolbar");
        gui.getTopToolbarPane().getStyleClass().add("top-toolbar");
        gui.getTopToolbarPane().setId("top-toolbar");
        gui.getTopToolbarPane().setPadding(new Insets(20, 20, 20, 20));
        gui.getFileToolbar().getStyleClass().add("top-toolbar-sub");
        gui.getFileToolbar().setId("top-toolbar-sub");
        gui.getSettingsToolbar().getStyleClass().add("top-toolbar-sub");
        gui.getSettingsToolbar().setId("top-toolbar-sub");
        gui.getUndoToolbar().getStyleClass().add("top-toolbar-sub");
        gui.getUndoToolbar().setId("top-toolbar-sub");
        editToolbar.setPadding(new Insets(20, 20, 20, 20));
	
        // LINES PANE
        linesPane = new VBox(10);
        linesTopPane = new HBox(5);
        linesMiddlePane = new HBox(5);
        linesPane.setAlignment(Pos.CENTER);
        linesTopPane.setAlignment(Pos.CENTER);
        linesMiddlePane.setAlignment(Pos.CENTER);
        addLineButton = new Button();
        addLineButton.setGraphic(new ImageView(new Image("file:images/plus.png")));
        removeLineButton = new Button();
        removeLineButton.setGraphic(new ImageView(new Image("file:images/minus.png")));
        addStationToLineButton = new Button("Add Station");
        addStationToLineButton.setMaxHeight(Double.MAX_VALUE);
        removeStationToLineButton = new Button("Remove Station");
        removeStationToLineButton.setMaxHeight(Double.MAX_VALUE);
        stationsAsListButton = new Button();
        stationsAsListButton.setGraphic(new ImageView(new Image("file:images/list.png")));
        metroLinesComboBox = new ComboBox(lineNames);
        metroLinesComboBox.setMaxWidth(100);
        metroLinesText = new Text("Metro Lines");
        metroLinesText.getStyleClass().add("labels");
        editLineButton = new Button("Edit Line");
        editLineButton.setMaxHeight(MAX_VALUE);
        lineWidthSlider = new Slider();
        linesTopPane.setId("metro-line");
        linesSpacer = new Region();
        HBox.setHgrow(linesSpacer, Priority.ALWAYS);
        linesTopPane.getChildren().addAll(metroLinesText, linesSpacer, metroLinesComboBox, editLineButton);
        linesMiddlePane.getChildren().addAll(addLineButton, removeLineButton, addStationToLineButton, removeStationToLineButton, stationsAsListButton);
        linesPane.getChildren().addAll(linesTopPane, linesMiddlePane, lineWidthSlider);
        editToolbar.getChildren().add(linesPane);

        // STATION PANE
        stationsPane = new VBox(10);
        stationsTopPane = new HBox(5);
        stationsMiddlePane = new HBox(5);
        stationsPane.setAlignment(Pos.CENTER);
        stationsTopPane.setAlignment(Pos.CENTER);
        stationsMiddlePane.setAlignment(Pos.CENTER);
        addStationButton = new Button();
        addStationButton.setGraphic(new ImageView(new Image("file:images/plus.png")));
        removeStationButton = new Button();
        removeStationButton.setGraphic(new ImageView(new Image("file:images/minus.png")));
        snapToGridButton = new Button("Snap");
        snapToGridButton.setMaxHeight(Double.MAX_VALUE);
        moveLabelButton = new Button("Move Label");
        moveLabelButton.setMaxHeight(Double.MAX_VALUE);
        rotateLabelButton = new Button();
        rotateLabelButton.setGraphic(new ImageView(new Image("file:images/rotate.png")));
        metroStationsText = new Text("Metro Stations");
        metroStationsText.getStyleClass().add("labels");
        metroStationsComboBox = new ComboBox(stationNames);
        metroStationsComboBox.setMaxWidth(100);
        stationColorPicker = new ColorPicker(BLACK);
        stationRadiusSlider = new Slider();
        stationSpacer = new Region();
        HBox.setHgrow(stationSpacer, Priority.ALWAYS);
        stationsTopPane.getChildren().addAll(metroStationsText, stationSpacer, metroStationsComboBox, stationColorPicker);
        stationsMiddlePane.getChildren().addAll(addStationButton, removeStationButton, snapToGridButton, moveLabelButton, rotateLabelButton);
        stationsPane.getChildren().addAll(stationsTopPane, stationsMiddlePane, stationRadiusSlider);
        editToolbar.getChildren().add(stationsPane);

        // ROUTE PANE
        routePane = new HBox(25);
        locationPane = new VBox(5);
        routePane.setAlignment(Pos.CENTER);
        locationPane.setAlignment(Pos.CENTER);
        startLocationComboBox = new ComboBox(stationNames);
        endLocationComboBox = new ComboBox(stationNames);
        findRouteButton = new Button();
        findRouteButton.setGraphic(new ImageView(new Image("file:images/find_route.png")));
        locationPane.getChildren().addAll(startLocationComboBox, endLocationComboBox);
        routePane.getChildren().addAll(locationPane, findRouteButton);
        editToolbar.getChildren().add(routePane);

        // DECORE PANE
        decorePane = new VBox(10);
        decoreTopPane = new HBox();
        decoreBottomPane = new HBox(5);
        decorePane.setAlignment(Pos.CENTER);
        decoreTopPane.setAlignment(Pos.CENTER);
        decoreBottomPane.setAlignment(Pos.CENTER);
        setImageBackgroundButton = new Button("  Set Image\nBackground");
        addImageButton = new Button("Add Image");
        addImageButton.setMaxHeight(Double.MAX_VALUE);
        addLabelButton = new Button("Add Label");
        addLabelButton.setMaxHeight(Double.MAX_VALUE);
        removeElementButton = new Button("Remove\nElement");
        decoreText = new Text("Decore");
        decoreText.getStyleClass().add("labels");
        decoreColorPicker = new ColorPicker(BLACK);
        decoreSpacer = new Region();
        HBox.setHgrow(decoreSpacer, Priority.ALWAYS);
        decoreTopPane.getChildren().addAll(decoreText, decoreSpacer, decoreColorPicker);
        decoreBottomPane.getChildren().addAll(setImageBackgroundButton, addImageButton, addLabelButton, removeElementButton);
        decorePane.getChildren().addAll(decoreTopPane, decoreBottomPane);
        editToolbar.getChildren().add(decorePane);

        // FONT PANE
        fontPane = new VBox(10);
        fontTopPane = new HBox();
        fontBottomPane = new HBox(10);
        fontPane.setAlignment(Pos.CENTER);
        fontTopPane.setAlignment(Pos.CENTER);
        fontBottomPane.setAlignment(Pos.CENTER);
        boldButton = new ToggleButton();
        boldButton.setGraphic(new ImageView(new Image("file:images/bold.png")));
        italicsButton = new ToggleButton();
        italicsButton.setGraphic(new ImageView(new Image("file:images/italics.png")));
        fontFamilyComboBox = initComboBox(FONT_FAMILY_COMBO_BOX_OPTIONS.toString());
        fontSizeComboBox = initComboBox(FONT_SIZE_COMBO_BOX_OPTIONS.toString());
        fontText = new Text("Font");
        fontText.getStyleClass().add("labels");
        fontColorColorPicker = new ColorPicker(BLACK);
        fontSpacer = new Region();
        HBox.setHgrow(fontSpacer, Priority.ALWAYS);
        fontTopPane.getChildren().addAll(fontText, fontSpacer, fontColorColorPicker);
        fontBottomPane.getChildren().addAll(boldButton, italicsButton, fontSizeComboBox, fontFamilyComboBox);
        fontPane.getChildren().addAll(fontTopPane, fontBottomPane);
        editToolbar.getChildren().add(fontPane);

        // NAVIGATION PANE
        navigationPane = new VBox(10);
        navigationTopPane = new HBox();
        navigationBottomPane = new HBox(12);
        navigationPane.setAlignment(Pos.CENTER);
        navigationTopPane.setAlignment(Pos.CENTER);
        navigationBottomPane.setAlignment(Pos.CENTER);
        zoomInButton = new Button();
        zoomInButton.setGraphic(new ImageView(new Image("file:images/zoom_in.png")));
        zoomOutButton = new Button();
        zoomOutButton.setGraphic(new ImageView(new Image("file:images/zoom_out.png")));
        increaseMapSizeButton = new Button();
        increaseMapSizeButton.setGraphic(new ImageView(new Image("file:images/increase_size.png")));
        decreaseMapSizeButton = new Button();
        decreaseMapSizeButton.setGraphic(new ImageView(new Image("file:images/decrease_size.png")));
        navigationText = new Text("Navigation");
        navigationText.getStyleClass().add("labels");
        showGridCheckBox = new CheckBox();
        navigationSpacer = new Region();
        HBox.setHgrow(navigationSpacer, Priority.ALWAYS);
        navigationTopPane.getChildren().addAll(navigationText, navigationSpacer, showGridCheckBox);
        navigationBottomPane.getChildren().addAll(zoomInButton, zoomOutButton, decreaseMapSizeButton, increaseMapSizeButton);
        navigationPane.getChildren().addAll(navigationTopPane, navigationBottomPane);
        editToolbar.getChildren().add(navigationPane);

        
        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        canvas = new Pane();
        BackgroundFill fill = new BackgroundFill(Color.WHITE, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
        debugText = new Text();
        debugText.setText("");
        debugText.setStroke(Color.BLUE);
        canvas.getChildren().add(debugText);
	debugText.setX(500);
	debugText.setY(500);
        
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
        mapData data = (mapData)app.getDataComponent();
	data.setMapNodes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
        canvas.setMaxSize(700, 500);
        canvas.setPrefSize(MAX_VALUE, MAX_VALUE);
        canvas.setMinSize(140, 100);
	((BorderPane)workspace).setLeft(editToolbar);
        zoomGroup = new Group(canvas);
        canvasGroup = new Group(zoomGroup);
        StackPane canvasPane = new StackPane(canvasGroup);
        scrollPane = new ScrollPane(canvasPane);
        scrollPane.setFitToHeight(ENABLED);
        scrollPane.setFitToWidth(ENABLED);
        scrollPane.setPannable(false);
	((BorderPane)workspace).setCenter(scrollPane);
    }
    
    public ToggleButton initToggleButton(Pane parent, String name, boolean enabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String iconProperty = name + "_ICON";
        String tooltipProperty = name + "_TOOLTIP";
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(iconProperty);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        ToggleButton button = new ToggleButton();
        button.setDisable(!enabled);
        button.setGraphic(new ImageView(buttonImage));
        String tooltipText = props.getProperty(tooltipProperty);
        Tooltip buttonTooltip = new Tooltip(tooltipText);
        button.setTooltip(buttonTooltip);
        
        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        AppLanguageSettings languageSettings = app.getLanguageSettings();
        languageSettings.addLabeledControl(name, button);

        // ADD IT TO THE PANE
        parent.getChildren().add(button);
        
        // AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    private ComboBox initComboBox(String comboPropertyList) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> comboOptions = props.getPropertyOptionsList(comboPropertyList);
        ObservableList oList = FXCollections.observableList(comboOptions);
        ComboBox cBox = new ComboBox(oList);
        cBox.getSelectionModel().selectFirst();
        return cBox;
    }
    
    // HELPER SETUP METHOD
    private void initControllers() {
        LineController lineController = new LineController(app);
        StationController stationController = new StationController(app);
        CanvasController canvasController = new CanvasController(app);
        mapData mapManager = (mapData) app.getDataComponent();
        AddImageController imageController = new AddImageController(app);
        FontController fontController = new FontController(app);
        
        scrollPane.setOnKeyPressed(i ->{
            switch(i.getCode()){
                case W:
                    panCounterY += 25;
                    canvasGroup.translateYProperty().set(panCounterY);
                    break;
                case A:
                    panCounterX += 25;
                    canvasGroup.translateXProperty().set(panCounterX);
                    break;
                case S:
                    panCounterY -= 25;
                    canvasGroup.translateYProperty().set(panCounterY);
                    break;
                case D:
                    panCounterX -= 25;
                    canvasGroup.translateXProperty().set(panCounterX);
                    break;
            }
        });        
	app.getGUI().getWindow().setOnCloseRequest(i ->{
            ButtonType sellection = AppDialogs.showYesNoCancelDialog(app.getGUI().getWindow(), SAVE_VERIFY_TITLE, SAVE_VERIFY_CONTENT);
            if (sellection == ButtonType.YES) {
                try{
                app.getGUI().getFileController().processSaveRequest();
                app.getGUI().getWindow().close();
                }
                catch(Exception e){
                    
                }
            } 
            else if (sellection == ButtonType.NO) {
                app.getGUI().getWindow().close();
            }
            else{
                i.consume();
            }
        });
        addLineButton.setOnAction(i ->{
            lineController.addLine();
        });
        removeLineButton.setOnAction(i -> {
            lineController.removeLine();
        });
        addStationToLineButton.setOnAction(i -> {
            Scene scene = app.getGUI().getPrimaryScene();
            if (metroLinesComboBox.getValue() != null){
                scene.setCursor(Cursor.HAND);
                mapManager.setState(mapState.SELECTING_STATIONS);
            }
        });
        removeStationToLineButton.setOnAction(i ->{
            Scene scene = app.getGUI().getPrimaryScene();
            if (metroLinesComboBox.getValue() != null){
                scene.setCursor(Cursor.HAND);
                mapManager.setState(mapState.REMOVING_STATIONS);
            }
        });
        addStationButton.setOnAction(i ->{
            stationController.addStation();
        });
        editLineButton.setOnAction(i ->{
            if (listOfLines.size() == 0){
                i.consume();
            }
            else{
                MetroLine line = listOfLines.get(metroLinesComboBox.getSelectionModel().getSelectedIndex());
                line.getEditLineDialog();
            }
        });
        stationsAsListButton.setOnAction(i ->{
            if (listOfLines.size() == 0){
                i.consume();
            }
            else{
                if (!metroLinesComboBox.getSelectionModel().isEmpty()){
                    MetroLine line = listOfLines.get(metroLinesComboBox.getSelectionModel().getSelectedIndex());
                    line.getListOfStationsDialog();
                }
            }
        });
        stationColorPicker.setOnAction(i ->{
            if (listOfStations.size() == 0){
                i.consume();
            }
            else{
                MetroStation station = listOfStations.get(metroStationsComboBox.getSelectionModel().getSelectedIndex());
                station.setFill(stationColorPicker.getValue());
            }
        });
        metroLinesComboBox.setOnAction(i ->{
            if (!metroLinesComboBox.getSelectionModel().isEmpty())
                mapManager.setSelectedNode(listOfLines.get(metroLinesComboBox.getSelectionModel().getSelectedIndex()));
        });
        removeStationButton.setOnAction(i -> {
            stationController.removeStation();
        });
        snapToGridButton.setOnAction(i ->{
            stationController.snapToGrid(mapManager.getSelectedNode());
        });
        moveLabelButton.setOnAction(i -> {
            stationController.moveLabel(mapManager.getSelectedNode());
        });
        rotateLabelButton.setOnAction(i ->{
            if (mapManager.getSelectedNode() instanceof MetroStation || mapManager.getSelectedNode() instanceof DraggableText){
                if (mapManager.getSelectedNode() instanceof MetroStation){
                    stationController.rotateLabel(mapManager.getSelectedNode());
                }
                else if (((DraggableText) mapManager.getSelectedNode()).getIsForLine()){
                    stationController.rotateLabel(mapManager.getSelectedNode());
                }
            }
        });
        decoreColorPicker.setOnAction(i -> {
            canvas.setBackground(new Background(new BackgroundFill(decoreColorPicker.getValue(), null, null)));
            jTPS jtps = app.getTPS();
            ChangeBackgroundColor_Transaction transaction = new ChangeBackgroundColor_Transaction(canvas, decoreColorPicker.getValue());
            jtps.addTransaction(transaction);
            
        });
        lineWidthSlider.valueProperty().addListener(i ->{
            if (mapManager.getSelectedNode() instanceof MetroLine){
                ((MetroLine) mapManager.getSelectedNode()).setThickness(lineWidthSlider.getValue());
                jTPS jtps = app.getTPS();
                ChangeShapeOutlineThickness_Transaction transaction = new ChangeShapeOutlineThickness_Transaction((MetroLine) 
                        mapManager.getSelectedNode(), lineWidthSlider.getValue());
                jtps.addTransaction(transaction);
            }
        });
        stationRadiusSlider.valueProperty().addListener(i ->{
            if (mapManager.getSelectedNode() instanceof MetroStation){
                ((MetroStation) mapManager.getSelectedNode()).setRadius(stationRadiusSlider.getValue());
                jTPS jtps = app.getTPS();
                ChangeShapeRadius_Transaction transaction = new ChangeShapeRadius_Transaction((MetroStation) 
                        mapManager.getSelectedNode(), stationRadiusSlider.getValue());
                jtps.addTransaction(transaction);
            }
        });
        fontSizeComboBox.setOnAction(i ->{
            fontController.processChangeFont();
        });
        fontFamilyComboBox.setOnAction(i ->{
            fontController.processChangeFont();
        });
        fontColorColorPicker.setOnAction(i ->{
            fontController.processChangeFont();
        });
        boldButton.setOnAction(i ->{
            fontController.processChangeFont();
        });
        italicsButton.setOnAction(i ->{
            fontController.processChangeFont();
        });
        zoomInButton.setOnAction(i ->{
            canvasController.processZoomInRequest(zoomGroup);
        });
        zoomOutButton.setOnAction(i ->{
            canvasController.processZoomOutRequest(zoomGroup);
        });
        increaseMapSizeButton.setOnAction(i ->{
            canvasController.processIncreaseMapSizeRequest(canvas);
            mapManager.updateGridLines();
        });
        decreaseMapSizeButton.setOnAction(i ->{
            canvasController.processDecreaseMapSizeRequest(canvas);
            mapManager.updateGridLines();
        });
        showGridCheckBox.setOnAction(i ->{
            if (showGridCheckBox.isSelected()){
                mapManager.showGridLines();
            }
            else
                mapManager.hideGridLines();
        });
        findRouteButton.setOnAction(i ->{
            if (startLocationComboBox.getSelectionModel().getSelectedItem() == null 
                    || endLocationComboBox.getSelectionModel().getSelectedItem() == null){
                i.consume();
            }
            else{
                MetroStation startStation = null;
                MetroStation endStation = null;
                for (MetroStation station : listOfStations){
                    if (station.getAssociatedLabel().getText() == startLocationComboBox.getValue()){
                        startStation = station;
                    }
                }
                for (MetroStation station : listOfStations){
                    if (station.getAssociatedLabel().getText() == endLocationComboBox.getValue()){
                        endStation = station;
                    }
                }
                ArrayList<MetroStation> solution = stationController.findRoute(startStation, endStation);
                Dialog dialog = new Dialog();
                
                dialog.setTitle("Shortest Route");
                dialog.setHeaderText("Shortest Route from " + startStation.getAssociatedLabel().getText() + 
                        " and " + endStation.getAssociatedLabel().getText());
                
                String sol = "";
                
                if (solution.isEmpty()){
                    sol += "No Possible Route";
                }
                else{
                    for (int j = 0; j < solution.size(); j++){
                        sol += "• " + solution.get(j).getAssociatedLabel().getText() + "\n";
                    }
                }
                
                VBox dialogVBox = new VBox(new Text(sol));
                dialogVBox.setAlignment(Pos.CENTER);
                dialogVBox.setPadding(new Insets(20, 20, 20, 20));
                
                dialog.getDialogPane().setContent(dialogVBox);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            }
        });
        setImageBackgroundButton.setOnAction(i ->{
            backgroundImage = new BackgroundImage(imageController.promptForImage(), null, null, null, 
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
            canvas.setBackground(new Background(backgroundImage));
        });
        addImageButton.setOnAction(i ->{
            imageController.processAddImage();
        });
        addLabelButton.setOnAction(i ->{
            stationController.addLabel();
        });
        removeElementButton.setOnAction(i ->{
            Node node = mapManager.getSelectedNode();
            if (node instanceof DraggableImage || node instanceof DraggableText){
                if (node instanceof DraggableText){
                    if (!((DraggableText) node).getIsForLine() && !((DraggableText) node).getIsForStation()){
                        mapManager.removeNode(node);
                    }
                }
                else{
                    mapManager.removeNode(node);
                }
            }
        });
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY(), e.getClickCount());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{            
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
	});
        app.getGUI().getExportButton().setOnAction(i -> {
            SnapshotController snapshotController = new SnapshotController(app);
            snapshotController.processSnapshot(app.getGUI().getFileController().processExportRequest());
        });
    }

    // HELPER METHOD
    public void loadSelectedNodeSettings(Node node) {
	if (node != null) {
            mapData data = (mapData)app.getDataComponent();
            if (!(node instanceof MetroLine)) {
                if (data.isShape((Draggable) node)) {
                    Shape shape = (Shape) node;
                    Color lineColor = (Color) shape.getFill();
                    Color stationColor = (Color) shape.getStroke();
                    if (shape instanceof MetroLine) {
                        lineWidthSlider.setValue(((MetroLine) shape).getThickness());
                    }
                }
            }
	}
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
	
	// BUTTON STYLE
    }
    
    /**
     * This function reloads all the controls for editing logos in
     * the workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {
//	mapData dataManager = (mapData)data;
//	if (dataManager.isInState(mapState.STARTING_RECTANGLE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(true);
//	    ellipseButton.setDisable(false);
//	}
//	else if (dataManager.isInState(mapState.STARTING_ELLIPSE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(true);
//	}
//	else if (dataManager.isInState(mapState.SELECTING_NODE) 
//		|| dataManager.isInState(mapState.DRAGGING_NODE)
//		|| dataManager.isInState(mapState.DRAGGING_NOTHING)) {
//	    boolean nodeIsNotSelected = dataManager.getSelectedNode() == null;
//	    selectionToolButton.setDisable(true);
//	    removeButton.setDisable(nodeIsNotSelected);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(false);
//	    moveToFrontButton.setDisable(nodeIsNotSelected);
//	    moveToBackButton.setDisable(nodeIsNotSelected);
//	}
//	
//	removeButton.setDisable(dataManager.getSelectedNode() == null);
//	//backgroundColorPicker.setValue(dataManager.getBackgroundColor());
    }
    
    @Override
    public void resetLanguage() {
        // WE'LL NEED TO RELOAD THE CONTROLS WITH TEXT
        // THAT ARE NOT BUTTONS HERE, LIKE LABELS AND COMBO BOXES
        
    }
    
    public Font getCurrentFontSettings() {
        String fontFamily = fontFamilyComboBox.getSelectionModel().getSelectedItem().toString();
        int fontSize = Integer.valueOf(fontSizeComboBox.getSelectionModel().getSelectedItem().toString());
        FontWeight weight = FontWeight.NORMAL;
        if (boldButton.isSelected())
            weight = FontWeight.BOLD;
        FontPosture posture = FontPosture.REGULAR;
        if (italicsButton.isSelected())
            posture = FontPosture.ITALIC;
        Font newFont = Font.font(fontFamily, weight, posture, fontSize);
        return newFont;
    }
    
    public Paint getFontColor(){
        return fontColorColorPicker.getValue();
    }

    public String getRequestType(){
        return requestType;
    }
    
    public void addStationToList(MetroStation stationToAdd){
        if (!(listOfStations.contains(stationToAdd))){
            listOfStations.add(stationToAdd);
            stationNames.add(stationToAdd.getAssociatedLabel().getText());
        }
    }
    
    public ArrayList<MetroStation> getListOfStations(){
        return listOfStations;
    }
    
    public void removeStationFromList(MetroStation stationToRemove){
        if (listOfStations.contains(stationToRemove)){
            listOfStations.remove(stationToRemove);
            stationNames.remove(stationToRemove.getAssociatedLabel().getText());
        }
    }
    
    public void addLineToList(MetroLine lineToAdd){
        if (!(listOfLines.contains(lineToAdd))){
            listOfLines.add(lineToAdd);
            lineNames.add(lineToAdd.getAssociatedStartLabel().getText());
        }
    }
    
    public ArrayList<MetroLine> getListOfLines(){
        return listOfLines;
    }
    
    public void removeLineFromList(MetroLine lineToRemove){
        if (listOfLines.contains(lineToRemove)){
            listOfLines.remove(lineToRemove);
            lineNames.remove(lineToRemove.getAssociatedStartLabel().getText());
        }
    }
    
    public MetroLine getSelectedLine(){
        for (MetroLine l : listOfLines){
            if ((String) metroLinesComboBox.getValue() == l.getAssociatedStartLabel().getText())
                return l;
        }
        return null;
    }
    
    public MetroStation getSelectedStation(){
        for (MetroStation s : listOfStations){
            if ((String) metroStationsComboBox.getValue() == s.getAssociatedLabel().getText())
                return s;
        }
        return null;
    }
    
    public ObservableList<String> getListOfStationNames(){
        return stationNames;
    }
    
    public ObservableList<String> getListOfLineNames(){
        return lineNames;
    }
    
    public SnapshotController getSnapshotController(){
        return new SnapshotController(app);
    }
    
    public ScrollPane getCanvasScrollPane(){
        return scrollPane;
    }
    
    public ComboBox getMetroLinesComboBox(){
        return metroLinesComboBox;
    }
    
    public ComboBox getMetroStationsComboBox(){
        return metroStationsComboBox;
    }
    
    public Slider getLineWidthSlider(){
        return lineWidthSlider;
    }
    
    public Slider getStationRadiusSlider(){
        return stationRadiusSlider;
    }
}
