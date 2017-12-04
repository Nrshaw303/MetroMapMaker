package map.file;

import djf.AppTemplate;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.util.ArrayList;
import javafx.scene.image.Image;
import static javafx.scene.paint.Color.rgb;
import map.data.mapData;
import map.data.DraggableCircle;
import map.data.MetroLine;
import map.data.MetroStation;
import map.data.DraggableImage;
import map.data.Draggable;
import static map.data.Draggable.*;
import map.data.DraggableText;
import map.gui.mapWorkspace;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapFiles implements AppFileComponent {
    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_NODES = "nodes";
    static final String JSON_NAME = "name";
    static final String JSON_STATION = "station";
    static final String JSON_LINES = "lines";
    static final String JSON_STATIONS = "stations";
    static final String JSON_STATION_NAMES = "station_names";
    static final String JSON_LINE = "line";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_START_X = "start_x";
    static final String JSON_START_Y = "start_y";
    static final String JSON_END_X = "end_x";
    static final String JSON_END_Y = "end_y";
    static final String JSON_LABEL_X = "label_x";
    static final String JSON_LABEL_Y = "label_y";
    static final String JSON_LABEL_ROTATION = "label_rotation";
    static final String JSON_RADIUS = "radius";
    static final String JSON_THICKNESS = "thickness";
    static final String JSON_COLOR = "color";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    AppTemplate app;
    mapWorkspace mapWorkspace;
    
 
    public mapFiles(AppTemplate app){
        this.app = app;
    }
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that together draws the logo.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	mapData dataManager = (mapData)data;
	
	// FIRST THE BACKGROUND COLOR
	Color bgColor = dataManager.getBackgroundColor();
	JsonObject bgColorJson = makeJsonColorObject(bgColor);

	// NOW BUILD THE JSON OBJCTS TO SAVE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
	ObservableList<Node> nodes = dataManager.getMapNodes();
	for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i) instanceof MetroLine){
	        MetroLine line = (MetroLine) nodes.get(i);
                String type = line.getNodeType();
                String name = line.getAssociatedStartLabel().getText();
                double startX = line.getPoints().get(0);
                double startY = line.getPoints().get(1);
                double endX = line.getPoints().get(line.getPoints().size() - 2);
                double endY = line.getPoints().get(line.getPoints().size() - 1);
                JsonObject lineColorJson = makeJsonColorObject((Color) line.getStroke());
                double width = line.getStrokeWidth();
                
                JsonArrayBuilder stationNameBuilder = Json.createArrayBuilder();
                for (String stationName : line.getStationNames()){
                    stationNameBuilder.add(stationName);
                }

                JsonObject lineJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_START_X, startX)
                        .add(JSON_START_Y, startY)
                        .add(JSON_END_X, endX)
                        .add(JSON_END_Y, endY)
                        .add(JSON_THICKNESS, width)
                        .add(JSON_COLOR, lineColorJson)
                        .add(JSON_STATION_NAMES, stationNameBuilder.build()).build();
                lineBuilder.add(lineJson);
            }
            else if (nodes.get(i) instanceof MetroStation){
	        MetroStation station = (MetroStation) nodes.get(i);
                String type = station.getNodeType();
                String name = station.getAssociatedLabel().getText();
                double X = station.getCenterX();
                double Y = station.getCenterY();
                double labelX = station.getAssociatedLabel().getX();
                double labelY = station.getAssociatedLabel().getY();
                JsonObject stationColorJson = makeJsonColorObject((Color) station.getFill());
                double radius = station.getRadius();

                JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_X, X)
                        .add(JSON_Y, Y)
                        .add(JSON_RADIUS, radius)
                        .add(JSON_LABEL_X, labelX)
                        .add(JSON_LABEL_Y, labelY)
                        .add(JSON_COLOR, stationColorJson).build();
                stationBuilder.add(stationJson);
            }
        }
        JsonArray lineArray = lineBuilder.build();
	JsonArray stationArray = stationBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_LINES, lineArray)
		.add(JSON_STATIONS, stationArray).build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private JsonObject makeJsonColorObject(Color color) {
	JsonObject colorJson = Json.createObjectBuilder()
		.add(JSON_RED, color.getRed())
		.add(JSON_GREEN, color.getGreen())
		.add(JSON_BLUE, color.getBlue())
		.add(JSON_ALPHA, color.getOpacity()).build();
	return colorJson;
    }
      
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	mapData dataManager = (mapData)data;
	dataManager.resetData();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
//	// LOAD THE BACKGROUND COLOR
//	Color bgColor = loadColor(json, JSON_BG_COLOR);
//	dataManager.setBackgroundColor(bgColor);

        //LOAD ALL LINES FIRST
        JsonArray jsonLineArray = json.getJsonArray(JSON_LINES);
        for (int i = 0; i < jsonLineArray.size(); i++){
            JsonObject jsonLine = jsonLineArray.getJsonObject(i);
            
            //CREATE THE LINE, START, AND END LABELS
            MetroLine line = loadLine(jsonLine);
            DraggableText start = loadStartLabel(jsonLine, line);
            DraggableText end = loadEndLabel(jsonLine, line);
            
            //ADD THEM, SET ASSOCIATIONS, AND ADD POINTS
            line.setAssociatedStartLabel(start);
            line.setAssociatedEndLabel(end);
            line.getPoints().addAll(new Double[]{start.getX(), start.getY(), end.getX(), end.getY()});
            dataManager.addNode(start);
            dataManager.addNode(end);
            dataManager.addNode(line);
            
            mapWorkspace = (mapWorkspace)app.getWorkspaceComponent();
            mapWorkspace.addLineToList(line);
        }
        
        //NOW WE LOAD THE STATIONS
        JsonArray jsonStationArray = json.getJsonArray(JSON_STATIONS);
        for (int i = 0; i < jsonStationArray.size(); i++){
            JsonObject jsonStation = jsonStationArray.getJsonObject(i);
            
            //CREATE THE LINE AND LABEL
            MetroStation station = loadStation(jsonStation);
            DraggableText label = loadLabel(jsonStation, station);
            
            //ADD THEM, SET ASSOCIATIONS, AND ADD POINTS
            station.setAssociatedLabel(label);
            dataManager.addNode(label);
            dataManager.addNode(station);
            
            mapWorkspace = (mapWorkspace)app.getWorkspaceComponent();
            mapWorkspace.addStationToList(station);
        }
        
        //AND NOW WE ADD THE STATIONS TO THE LINES
        for (MetroLine line : mapWorkspace.getListOfLines()){
            for (MetroStation station : mapWorkspace.getListOfStations()){
                for (String stationName : line.getStationNames()){
                    if (stationName.equals(station.getAssociatedLabel().getText())){
                        line.addStation(station);
                    }
                }
            }
        }
    }
    
    private double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private MetroLine loadLine(JsonObject jsonLine) {	
        //LOAD THE COLOR, NAME, AND THICKNESS PROPERTIES
        Color lineColor = loadColor(jsonLine, JSON_COLOR);
        double width = getDataAsDouble(jsonLine, JSON_THICKNESS);
        
        //NOW MAKE THE LINE
	MetroLine line = new MetroLine(width, lineColor);
        
        //AND NOW IT'S LIST OF STATIONS
        JsonArray stationNamesJsonArray = jsonLine.getJsonArray(JSON_STATION_NAMES);
        for (int i = 0; i < stationNamesJsonArray.size(); i++){
            line.addStationName(stationNamesJsonArray.getString(i));
        }
        
	//ALL DONE, RETURN IT
	return line;
    }
    
    private MetroStation loadStation(JsonObject jsonStation){	
        //LOAD THE COLOR, NAME, RADIUS, AND LOCATION PROPERTIES
        Color stationColor = loadColor(jsonStation, JSON_COLOR);
        double radius = getDataAsDouble(jsonStation, JSON_RADIUS);
        double x = getDataAsDouble(jsonStation, JSON_X);
        double y = getDataAsDouble(jsonStation, JSON_Y);
        
        //NOW MAKE THE LINE
	MetroStation station = new MetroStation(x, y, (int) radius);
        station.setFill(stationColor);
        station.setStroke(rgb(0,0,0));
        
	//ALL DONE, RETURN IT
	return station;
    }
    
    private DraggableText loadStartLabel(JsonObject jsonLine, MetroLine line){
        //LOAD START LABEL PROPERTIES
        double startX = getDataAsDouble(jsonLine, JSON_START_X);
        double startY = getDataAsDouble(jsonLine, JSON_START_Y);
        String name = jsonLine.getString(JSON_NAME);
        
        //CREATE THE LABEL
	DraggableText start = new DraggableText(name);
        start.setAssociatedLine(line);
        start.setIsForLine(true);
        start.setIsStart(true);
        start.start((int)startX, (int)startY);
        
        //AND RETURN
        return start;
    }
    
    private DraggableText loadEndLabel(JsonObject jsonLine, MetroLine line){
        //LOAD START LABEL PROPERTIES
        double endX = getDataAsDouble(jsonLine, JSON_END_X);
        double endY = getDataAsDouble(jsonLine, JSON_END_Y);
        String name = jsonLine.getString(JSON_NAME);
        
        //CREATE THE LABEL
        DraggableText end = new DraggableText(name);
        end.setAssociatedLine(line);
        end.setIsForLine(true);
        end.start((int)endX, (int)endY);
        
        //AND RETURN
        return end;
    }
    
    private DraggableText loadLabel(JsonObject jsonStation, MetroStation station){
        //LOAD LABEL PROPERTIES
        double x = getDataAsDouble(jsonStation, JSON_LABEL_X);
        double y = getDataAsDouble(jsonStation, JSON_LABEL_Y);
        String name = jsonStation.getString(JSON_NAME);
        
        //CREATE THE LABEL
        DraggableText label = new DraggableText(name);
        label.setAssociatedStation(station);
        label.setIsForStation(true);
        label.start((int) x, (int) y);
        
        //AND RETURN
        return label;
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        // WE ARE NOT USING THIS, THOUGH PERHAPS WE COULD FOR EXPORTING
        // IMAGES TO VARIOUS FORMATS, SOMETHING OUT OF THE SCOPE
        // OF THIS ASSIGNMENT
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
}
