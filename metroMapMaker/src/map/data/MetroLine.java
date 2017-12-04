package map.data;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import static map.data.Draggable.LINE;
import properties_manager.PropertiesManager;

/**
 * Metro Line class for Metro Map Maker.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroLine extends Polyline{
    String name;
    DraggableCircle start;
    DraggableCircle end;
    double thickness;
    Paint color;
    ObservableList<DraggableCircle> stations;
    ArrayList<String> stationNames;
    ObservableList<Line> lines;
    DraggableText startLabel;
    DraggableText endLabel;
    
    public MetroLine() {
        lines = FXCollections.observableArrayList();
        stations = FXCollections.observableArrayList();
        stationNames = new ArrayList<>();
    }
    
    public MetroLine(double thickness, Paint color) {
        lines = FXCollections.observableArrayList();
        setStrokeWidth(thickness);
        setStroke(color);
        stations = FXCollections.observableArrayList();
        stationNames = new ArrayList<>();
    }
    
    public void setThickness(double thickness){
        this.thickness = thickness;
    }
    
    public double getThickness(){
        return thickness;
    }
    
    public void addStation(MetroStation metroStation){
        if (!(stations.contains(metroStation))){
            double distance = 100000;
            double x = metroStation.getX();
            double y = metroStation.getY();
            int shortestind = 0;
            for (int i = 0; i < getPoints().size() - 3; i+=2){
                double midpointX = (getPoints().get(i) + getPoints().get(i + 2)) / 2;
                double midpointY = (getPoints().get(i + 1) + getPoints().get( i + 3)) / 2;
                double temp = Math.sqrt((Math.pow(midpointX - x, 2)) + Math.pow(midpointY - y, 2));
                if (temp < distance){
                    shortestind = i;
                    distance = temp;
                }
            }
            int i = shortestind / 2;
            addStationNameToList(i, metroStation.getAssociatedLabel().getText());
            stations.add(i, metroStation);
            getPoints().add(shortestind + 2, metroStation.getX() + metroStation.getRadius());
            getPoints().add(shortestind + 3, metroStation.getY() + metroStation.getRadius());
            metroStation.addLine(this);
        }
    }
    
    public void removeStation(MetroStation metroStation){
        if (stations.contains(metroStation)) {
            stations.remove(metroStation);
            int i = stationNames.indexOf(metroStation.getAssociatedLabel().getText());
            getPoints().remove(metroStation.getX() + metroStation.getRadius());
            getPoints().remove(metroStation.getY() + metroStation.getRadius());
            stationNames.remove(metroStation.getAssociatedLabel().getText());
            metroStation.removeLine(this);
        }
    }
    
    public String getNodeType(){
        return LINE;
    }
    
    public DraggableText getAssociatedStartLabel(){
        return startLabel;
    }
    
    public DraggableText getAssociatedEndLabel(){
        return endLabel;
    }
    
    public void setAssociatedStartLabel(DraggableText start){
        startLabel = start;
    }
    
    public void setAssociatedEndLabel(DraggableText end){
        endLabel = end;
    }
    
    public void editName(String name){
        endLabel.setText(name);
        startLabel.setText(name);
    }
    
    public void setColor(Paint color){
        setStroke(color);
    }
    
    public Paint getColor(){
        return getStroke();
    }
    
    public ArrayList<String> getStationNames(){
        return stationNames;
    }
    
    //USED FOR FILE LOADING
    public void addStationName(String name){
        stationNames.add(name);
    }
    
    //NEEDED FOR FILE LOADING
    public void addStationNameToList(int i, String stationName){
        for (String name : stationNames){
            //IF THE STATION NAME IS ALREADY IN THE LIST (ONLY POSSIBLE DURING LOADING)
            if (stationName.equals(name)){
                return;
            }
        }
        stationNames.add(i, stationName);
    }
}
