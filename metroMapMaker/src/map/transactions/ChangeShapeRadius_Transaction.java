package map.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;
import map.data.MetroStation;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeShapeRadius_Transaction implements jTPS_Transaction {
    private MetroStation shape;
    private double radius;
    private double oldRadius;
    
    public ChangeShapeRadius_Transaction(MetroStation initShape, double initRadius) {
        shape = initShape;
        radius = initRadius;
        oldRadius = initShape.getRadius();
    }

    @Override
    public void doTransaction() {
        shape.setRadius(radius);
    }

    @Override
    public void undoTransaction() {
        shape.setRadius(oldRadius);
    }    
}