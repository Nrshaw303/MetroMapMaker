package map.transactions;

import map.data.mapData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class MoveNodeToBack_Transaction implements jTPS_Transaction {
    private mapData data;
    private Node node;
    private int originalIndex;
        
    public MoveNodeToBack_Transaction(mapData initData, Node initNode) {
        data = initData;
        node = initNode;
        originalIndex = data.getIndexOfNode(node);        
    }

    @Override
    public void doTransaction() {
        data.moveNodeToBack(node);
    }

    @Override
    public void undoTransaction() {
        data.moveNodeToIndex(node, originalIndex);
    }
}