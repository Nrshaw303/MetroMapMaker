package map.data;

import java.util.ArrayList;
import static javafx.scene.paint.Color.rgb;
import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import javafx.scene.shape.Circle;
import properties_manager.PropertiesManager;


/**
 * This is a draggable ellipse for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroStation extends DraggableCircle{
    double startCenterX;
    double startCenterY;
    DraggableText associatedLabel;
    ArrayList<MetroLine> lines;
    
    public MetroStation() {
	setCenterX(0.0);
	setCenterY(0.0);
	setRadius(0.0);
	setOpacity(1.0);
	startCenterX = 0.0;
	startCenterY = 0.0;
        associatedLabel = new DraggableText();
        lines = new ArrayList<>();
    }
    
    public MetroStation(double x, double y, int radius){
	setCenterX(x);
	setCenterY(y);
	setRadius(radius);
	setOpacity(1.0);
	startCenterX = 0.0;
	startCenterY = 0.0;
        associatedLabel = new DraggableText();
        lines = new ArrayList<>();
    }
    
    @Override
    public mapState getStartingState() {
	return mapState.STARTING_STATION;
    }
    
    @Override
    public void start(int x, int y) {
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void setStart(int initStartX, int initStartY) {
        startCenterX = initStartX;
        startCenterY = initStartY;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startCenterX;
	double diffY = y - startCenterY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - startCenterX;
	double height = y - startCenterY;
	double centerX = startCenterX + (width / 2);
	double centerY = startCenterY + (height / 2);
	setCenterX(centerX);
	setCenterY(centerY);
	setRadius(width/2);
	setRadius(height/2);	
	
    }
        
    @Override
    public double getX() {
	return getCenterX() - getRadius();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadius();
    }

    @Override
    public double getWidth() {
	return getRadius() * 2;
    }

    @Override
    public double getHeight() {
	return getRadius() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initWidth/2));
	setRadius(initWidth/2);
    }
    
    @Override
    public String getNodeType() {
	return STATION;
    }
    
    public DraggableText getAssociatedLabel(){
        return associatedLabel;
    }
    
    public void setAssociatedLabel(DraggableText associatedLabel){
        this.associatedLabel = associatedLabel;
    }
    
    public void addLine(MetroLine line){
        lines.add(line);
    }
    
    public void removeLine(MetroLine line){
        lines.remove(line);
    }
    
    public ArrayList<MetroLine> getLines(){
        return lines;
    }
}
