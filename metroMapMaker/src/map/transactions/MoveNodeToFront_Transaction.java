package map.transactions;

import map.data.mapData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class MoveNodeToFront_Transaction implements jTPS_Transaction {
    private mapData data;
    private Node node;
    private int originalIndex;
        
    public MoveNodeToFront_Transaction(mapData initData, Node initNode) {
        data = initData;
        node = initNode;
        originalIndex = data.getIndexOfNode(node);        
    }

    @Override
    public void doTransaction() {
        data.moveNodeToFront(node);
    }

    @Override
    public void undoTransaction() {
        data.moveNodeToIndex(node, originalIndex);
    }
}