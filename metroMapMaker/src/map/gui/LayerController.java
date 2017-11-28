package map.gui;

import djf.AppTemplate;
import map.data.mapData;
import map.transactions.MoveNodeToBack_Transaction;
import map.transactions.MoveNodeToFront_Transaction;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class LayerController {
    AppTemplate app;

    public LayerController(AppTemplate initApp) {
	app = initApp;
    }
    
    /**
     * This method processes a user request to move the selected node
     * down to the back layer.
     */
    public void processMoveSelectedNodeToBack() {
        mapData data = (mapData)app.getDataComponent();
        Node node = data.getSelectedNode();
        if (node != null) {            	
            jTPS tps = app.getTPS();
            MoveNodeToBack_Transaction transaction = new MoveNodeToBack_Transaction(data, node);
            tps.addTransaction(transaction);
        }
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to move the selected node
     * up to the front layer.
     */
    public void processMoveSelectedNodeToFront() {
        mapData data = (mapData)app.getDataComponent();
        Node node = data.getSelectedNode();
        if (node != null) {            	
            jTPS tps = app.getTPS();
            MoveNodeToFront_Transaction transaction = new MoveNodeToFront_Transaction(data, node);
            tps.addTransaction(transaction);
        }
	app.getGUI().updateToolbarControls(false);
    }    
}