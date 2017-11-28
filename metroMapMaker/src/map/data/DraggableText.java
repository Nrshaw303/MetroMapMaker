package map.data;

import javafx.scene.text.Text;
import map.data.Draggable;
import map.data.mapState;

/**
 * This is a draggable rectangle for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author Nicholas Shaw
 * @version 1.0
 */
public class DraggableText extends Text implements Draggable {
    double startX;
    double startY;
    boolean isForLine;
    boolean isStartLabel;
    boolean isForStation;
    MetroLine associatedLine = null;
    MetroStation associatedStation = null;
    
    public DraggableText() {
	setX(0.0);
	setY(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    public DraggableText(String text) {
        setX(0.0);
        setY(0.0);
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;
        setText(text);
    }
    
    @Override
    public mapState getStartingState() {
	return null;
    }
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
        setX(x);
        setY(y);
    }
    
    public void startDrag(int x, int y) {
	startX = x;
	startY = y;
    }
    
    @Override
    public void drag(int x, int y) {
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
	double height = y - getY();	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
    }
    
    public String getShapeType() {
	return TEXT;
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DraggableText duplicateText(){
        DraggableText newText = new DraggableText();
        newText.setText(this.getText());
        newText.setX(0);
        newText.setY(0);
        newText.setFont(this.getFont());
        newText.setFill(this.getFill());
        newText.setStroke(this.getStroke());
        newText.setStrokeWidth(this.getStrokeWidth());
        return newText;
    }

    @Override
    public String getNodeType() {
        return TEXT;
    }

    @Override
    public void setStart(int initX, int initY) {
        startX = initX;
        startY = initY;
    }
    
    public void setIsForLine(boolean isForLine){
        this.isForLine = isForLine;
    }
    
    public void setIsForStation(boolean isForStation){
        this.isForStation = isForStation;
    }
    
    public void setIsStart(boolean isStartLabel){
        this.isStartLabel = isStartLabel;
    }
    
    public void setAssociatedLine(MetroLine associatedLine){
        this.associatedLine = associatedLine;
    }
    
    public void setAssociatedStation(MetroStation associatedStation){
        this.associatedStation = associatedStation;
    }
    
    public boolean getIsForLine(){
        return isForLine;
    }
    
    public boolean getIsForStation(){
        return isForStation;
    }
    
    public boolean getIsStartLabel(){
        return isStartLabel;
    }
    
    public MetroStation getAssociatedStation(){
        return associatedStation;
    }
    
    public MetroLine getAssociatedLine(){
        return associatedLine;
    }
}
