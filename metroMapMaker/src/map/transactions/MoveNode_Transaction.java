package map.transactions;

import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class MoveNode_Transaction implements jTPS_Transaction {
    private Node node;
    private int newX;
    private int newY;
    private int oldX;
    private int oldY;
    
    public MoveNode_Transaction(Node initNode, int initNewX, int initNewY, int initOldX, int initOldY) {
        node = initNode;
        newX = initNewX;
        newY = initNewY;
        oldX = initOldX;
        oldY = initOldY;
    }

    @Override
    public void doTransaction() {
        node.translateXProperty().set(newX);
        node.translateYProperty().set(newY);
    }

    @Override
    public void undoTransaction() {
        node.translateXProperty().set(oldX);
        node.translateYProperty().set(oldY);
    }    
}