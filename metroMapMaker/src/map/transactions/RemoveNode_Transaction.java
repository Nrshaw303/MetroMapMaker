package map.transactions;

import map.data.mapData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveNode_Transaction implements jTPS_Transaction {
    private mapData data;
    private Node node;
    private int nodeIndex;
    
    public RemoveNode_Transaction(mapData initData, Node initNode) {
        data = initData;
        node = initNode;
        nodeIndex = data.getIndexOfNode(node);
    }

    @Override
    public void doTransaction() {
        data.removeNode(node);
    }

    @Override
    public void undoTransaction() {
        data.addNodeAtIndex(node, nodeIndex);
    }
}