package map.transactions;

import javafx.scene.Node;
import jtps.jTPS_Transaction;
import map.data.DraggableText;
import map.gui.CanvasController;
import map.gui.LineStationAndLabelUpdater;

/**
 *
 * @author McKillaGorilla
 */
public class Move_Label_Transaction implements jTPS_Transaction {
    private DraggableText label;
    private double newX;
    private double newY;
    private double oldX;
    private double oldY;
    
    public Move_Label_Transaction(DraggableText initNode, double initNewX, double initNewY, double initOldX, double initOldY) {
        label = initNode;
        newX = initNewX;
        newY = initNewY;
        oldX = initOldX;
        oldY = initOldY;
    }

    @Override
    public void doTransaction() {
        label.setX(newX);
        label.setY(newY);
        
        LineStationAndLabelUpdater labelUpdater = new LineStationAndLabelUpdater();
        labelUpdater.updateLabelAssociations(label);
    }

    @Override
    public void undoTransaction() {
        label.setX(oldX);
        label.setY(oldY);
        
        LineStationAndLabelUpdater labelUpdater = new LineStationAndLabelUpdater();
        labelUpdater.updateLabelAssociations(label);
    }    
}