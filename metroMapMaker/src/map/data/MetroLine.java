package map.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import properties_manager.PropertiesManager;

/**
 * Metro Line class for Metro Map Maker.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroLine {
    DraggableCircle start;
    DraggableCircle end;
    double thickness;
    Paint color;
    ObservableList<DraggableCircle> stations;
    ObservableList<Line> lines;
    
    public MetroLine(double thickness, Paint color) {
        start = new DraggableCircle(10, 10, 5);
        end = new DraggableCircle(40, 40, 5);
        lines = FXCollections.observableArrayList();
        this.thickness = thickness;
        this.color = color;
        stations = FXCollections.observableArrayList();
        stations.addAll(start, end);
        connectStations();
    }
    
    public void connectStations(){
        lines.clear();
        ObservableList<DraggableCircle> temp = FXCollections.observableArrayList();
        for (DraggableCircle c : stations){
            temp.add(c);
        }
        for (DraggableCircle c : temp){
            DraggableCircle closest = new DraggableCircle();
            double closestDistance = 10000;
            for (DraggableCircle k : temp){
                double tempDistance = Math.sqrt(Math.pow((c.getCenterX() - k.getCenterX()), 2) + 
                        Math.pow((c.getCenterY() - k.getCenterY()), 2));
                if (tempDistance < closestDistance){
                    closestDistance = tempDistance;
                    closest = k;
                }
            }
            Line line = new Line();
            line.setStroke(color);
            line.setStrokeWidth(thickness);
            line.startXProperty().bind(c.centerXProperty());
            line.startYProperty().bind(c.centerYProperty());
            line.endXProperty().bind(closest.centerXProperty());
            line.endYProperty().bind(closest.centerYProperty());
            lines.add(line);
        }
            
    }
    
    public MetroLine makeClone() {
        MetroLine cloneLine = new MetroLine(thickness, color);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        for (DraggableCircle c : stations)
            cloneLine.addStation(c);
        return cloneLine;
    }
    
    public void setThickness(double thickness){
        this.thickness = thickness;
    }
    
    public double getThickness(){
        return thickness;
    }
    
    public void addStation(DraggableCircle metroStation){
        stations.add(metroStation);
        connectStations();
    }
    
    public void removeStation(DraggableCircle metroStation){
        stations.remove(metroStation);
        connectStations();
    }
}
