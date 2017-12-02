package map.transactions;

import map.data.mapData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class AddNode_Transaction implements jTPS_Transaction {
    private mapData data;
    private Node node;
    
    public AddNode_Transaction(mapData initData, Node initNode) {
        data = initData;
        node = initNode;
    }

    @Override
    public void doTransaction() {
        data.addNode(node);
    }

    @Override
    public void undoTransaction() {
        data.removeNode(node);    
    }
}