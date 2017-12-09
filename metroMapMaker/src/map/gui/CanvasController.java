package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import map.data.Draggable;
import map.data.mapData;
import map.data.mapState;
import static map.data.mapState.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import jtps.jTPS;
import map.data.DraggableImage;
import map.data.DraggableText;
import map.data.MetroLine;
import map.data.MetroStation;
import map.transactions.Move_Label_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class CanvasController {
    AppTemplate app;
    mapData dataManager;
    double zoomCounter = 1;
    double currentOldX;
    double currentOldY;

    public CanvasController(AppTemplate initApp) {
	app = initApp;
	dataManager = (mapData)app.getDataComponent();
    }
    
    /**
     * This method handles the response for selecting either the
     * selection or removal tool.
     */
    public void processSelectSelectionTool() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(mapState.SELECTING_NODE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * This method handles a user request to remove the selected node.
     */
    public void processRemoveSelectedNode() {
	// REMOVE THE SELECTED NODE IF THERE IS ONE
	dataManager.removeSelectedNode();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }   
    
    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y, int mouseClicks) {
        mapData dataManager = (mapData) app.getDataComponent();
        mapWorkspace mapWorkspace = (mapWorkspace) app.getWorkspaceComponent();
        
        if (dataManager.getTopNode(x, y) instanceof Line){
            return;
        }
        if (dataManager.isInState(SELECTING_STATIONS)){
            Node node = dataManager.selectTopNode(x, y);
            if (node instanceof MetroStation)
                mapWorkspace.getSelectedLine().addStation((MetroStation) node);
            else{
                dataManager.setState(SELECTING_NODE);
            }
        }
        if (dataManager.isInState(REMOVING_STATIONS)){
            Node node = dataManager.selectTopNode(x, y);
            if (node instanceof MetroStation)
                mapWorkspace.getSelectedLine().removeStation((MetroStation) node);
            else{
                dataManager.setState(SELECTING_NODE);
            }
        }
        else if (dataManager.isInState(SELECTING_NODE)) {
            // SELECT THE TOP NODE
            Node node = dataManager.selectTopNode(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
            
            if (mouseClicks > 1 && node instanceof MetroLine) {
                MetroLine line = (MetroLine) node;
                line.getEditLineDialog();
            }
            else if (node instanceof MetroLine){
                mapWorkspace.getMetroLinesComboBox().getSelectionModel().select(((MetroLine) node).getAssociatedStartLabel().getText());
            }
            else if (node instanceof MetroStation){
                mapWorkspace.getMetroStationsComboBox().getSelectionModel().select(((MetroStation) node).getAssociatedLabel().getText());
            }
            // AND START DRAGGING IT
            else if (node != null) {
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(mapState.DRAGGING_NODE);

                // STORE CURRENT POSSITION FOR UNDO/REDO
                if (node instanceof DraggableText || node instanceof DraggableImage){
                    currentOldX = ((Draggable) node).getX();
                    currentOldY = ((Draggable) node).getY();
                }
                
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        }
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        mapData dataManager = (mapData) app.getDataComponent();
        if (dataManager.isInState(DRAGGING_NODE)) {
            Node selectedNode = dataManager.getSelectedNode();
            if (selectedNode instanceof DraggableText){
                Draggable selectedDraggableNode = (Draggable) dataManager.getSelectedNode();
                DraggableText label = (DraggableText) selectedNode;
                selectedDraggableNode.drag(x, y);
            
                LineStationAndLabelUpdater updater = new LineStationAndLabelUpdater();
                updater.updateLabelAssociations(label);
            }
            if (selectedNode instanceof DraggableImage){
                Draggable selectedDraggableNode = (Draggable) dataManager.getSelectedNode();
                selectedDraggableNode.drag(x, y);
            }
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        mapData dataManager = (mapData) app.getDataComponent();
        if (dataManager.isInState(mapState.DRAGGING_NODE)) {
            dataManager.setState(SELECTING_NODE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
            
            // NOW ADD MOVE NODE TRANSACTION
            if (((mapData) app.getDataComponent()).getSelectedNode() instanceof DraggableText) {
                jTPS jtps = app.getTPS();
                DraggableText node = (DraggableText) ((mapData) app.getDataComponent()).getSelectedNode();
                Move_Label_Transaction transaction = new Move_Label_Transaction(node, node.getX(), node.getY(), currentOldX, currentOldY);
                jtps.addTransaction(transaction);
            }
        } else if (dataManager.isInState(mapState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_NODE);
        }
    }

    public void processZoomInRequest(Group zoomGroup){
        zoomCounter = zoomCounter + 0.1;
        zoomGroup.setScaleX(zoomCounter);
        zoomGroup.setScaleY(zoomCounter);
    }
    
    public void processZoomOutRequest(Group zoomGroup){
        if (zoomCounter == 0.1)
            return;
        zoomCounter = zoomCounter - 0.1;        
        zoomGroup.setScaleX(zoomCounter);
        zoomGroup.setScaleY(zoomCounter);
    }
    
    public void processIncreaseMapSizeRequest(Pane canvas){
        canvas.setMaxSize(canvas.getWidth() * 1.1, canvas.getHeight() * 1.1);
    }
    
    public void processDecreaseMapSizeRequest(Pane canvas){
        if (canvas.getHeight() * 0.9 < 200 || canvas.getWidth() * 0.9 < 200)
            return;
        canvas.setMaxSize(canvas.getWidth() * 0.9, canvas.getHeight() * 0.9);
    }
}
