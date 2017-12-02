package map.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeShapeOutlineColor_Transaction implements jTPS_Transaction {
    private Shape shape;
    private Color color;
    private Color oldColor;
    
    public ChangeShapeOutlineColor_Transaction(Shape initShape, Color initColor) {
        shape = initShape;
        color = initColor;
        oldColor = (Color)shape.getStroke();
    }

    @Override
    public void doTransaction() {
        shape.setStroke(color);
    }

    @Override
    public void undoTransaction() {
        shape.setStroke(oldColor);
    }    
}