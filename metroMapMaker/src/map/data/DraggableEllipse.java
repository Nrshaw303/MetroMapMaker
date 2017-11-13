package map.data;

import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import javafx.scene.shape.Ellipse;
import properties_manager.PropertiesManager;


/**
 * This is a draggable ellipse for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class DraggableEllipse extends Ellipse implements Draggable {
    double startCenterX;
    double startCenterY;
    
    public DraggableEllipse() {
	setCenterX(0.0);
	setCenterY(0.0);
	setRadiusX(0.0);
	setRadiusY(0.0);
	setOpacity(1.0);
	startCenterX = 0.0;
	startCenterY = 0.0;
    }
    
    @Override
    public DraggableEllipse makeClone() {
        DraggableEllipse cloneEllipse = new DraggableEllipse();
        cloneEllipse.setRadiusX(getRadiusX());
        cloneEllipse.setRadiusY(getRadiusY());
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        cloneEllipse.setCenterX(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
        cloneEllipse.setCenterY(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
        cloneEllipse.setOpacity(getOpacity());
        cloneEllipse.setFill(getFill());
        cloneEllipse.setStroke(getStroke());
        cloneEllipse.setStrokeWidth(getStrokeWidth());
        return cloneEllipse;
    }
    
    @Override
    public mapState getStartingState() {
	return mapState.STARTING_ELLIPSE;
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
	setRadiusX(width / 2);
	setRadiusY(height / 2);	
	
    }
        
    @Override
    public double getX() {
	return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
	return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
	return getRadiusY() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadiusX(initWidth/2);
	setRadiusY(initHeight/2);
    }
    
    @Override
    public String getNodeType() {
	return ELLIPSE;
    }
}
