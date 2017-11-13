package map.data;

import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import javafx.scene.shape.Rectangle;
import properties_manager.PropertiesManager;

/**
 * This is a draggable rectangle for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class DraggableRectangle extends Rectangle implements Draggable {
    double startX;
    double startY;
    
    public DraggableRectangle() {
	setX(0.0);
	setY(0.0);
	setWidth(0.0);
	setHeight(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    @Override
    public DraggableRectangle makeClone() {
        DraggableRectangle cloneRectangle = new DraggableRectangle();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        cloneRectangle.setX(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
        cloneRectangle.setY(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
        cloneRectangle.setWidth(getWidth());
        cloneRectangle.setHeight(getHeight());
        cloneRectangle.setOpacity(getOpacity());
        cloneRectangle.setFill(getFill());
        cloneRectangle.setStroke(getStroke());
        cloneRectangle.setStrokeWidth(getStrokeWidth());
        return cloneRectangle;
    }
    
    @Override
    public mapState getStartingState() {
	return mapState.STARTING_RECTANGLE;
    }
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
	setX(x);
	setY(y);
    }
    
    @Override
    public void setStart(int initStartX, int initStartY) {
        startX = initStartX;
        startY = initStartY;
    }
    
    @Override
    public void drag(int x, int y) {
	//double diffX = x - (getX() + (getWidth()/2));
	//double diffY = y - (getY() + (getHeight()/2));
        double diffX = x - startX;
        double diffY = y - startY;
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	widthProperty().set(width);
	double height = y - getY();
	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	widthProperty().set(initWidth);
	heightProperty().set(initHeight);
    }
    
    @Override
    public String getNodeType() {
	return RECTANGLE;
    }
}
