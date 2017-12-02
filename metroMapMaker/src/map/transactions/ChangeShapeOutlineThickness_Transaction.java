package map.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeShapeOutlineThickness_Transaction implements jTPS_Transaction {
    private Shape shape;
    private double outlineThickness;
    private double oldOutlineThickness;
    
    public ChangeShapeOutlineThickness_Transaction(Shape initShape, double initOutlineThickness) {
        shape = initShape;
        outlineThickness = initOutlineThickness;
        oldOutlineThickness = shape.getStrokeWidth();
    }

    @Override
    public void doTransaction() {
        shape.setStrokeWidth(outlineThickness);
    }

    @Override
    public void undoTransaction() {
        shape.setStrokeWidth(oldOutlineThickness);
    }    
}