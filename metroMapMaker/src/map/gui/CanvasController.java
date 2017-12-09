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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import jtps.jTPS;
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
            
            if (node instanceof MetroLine){
                mapWorkspace.getLineColorPicker().setValue((Color)((MetroLine) node).getColor());
            }
            if (mouseClicks > 1 && node instanceof MetroLine) {
                Dialog dialog = new Dialog();
                dialog.setHeaderText("Choose a name and a color for new line:");
                dialog.setTitle("New Line");

                VBox dialogVBox = new VBox();
                HBox nameHBox = new HBox();
                HBox colorHBox = new HBox();

                ColorPicker color = new ColorPicker((Color)((MetroLine) node).getColor());
                Label label1 = new Label("Line Name:");
                TextField name = new TextField(((MetroLine) node).getAssociatedStartLabel().getText());
                Label label2 = new Label("Line Color");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Region spacer2 = new Region();
                HBox.setHgrow(spacer2, Priority.ALWAYS);

                nameHBox.getChildren().addAll(label1, spacer, name);
                colorHBox.getChildren().addAll(label2, spacer2, color);
                dialogVBox.getChildren().addAll(nameHBox, colorHBox);
                dialogVBox.setSpacing(15);
                dialog.getDialogPane().setContent(dialogVBox);

                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, ButtonType.OK);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.get() == ButtonType.OK) {
                    String lineName = name.getText();
                    Color lineColor = color.getValue();
                    ((MetroLine) node).setColor(lineColor);
                    ((MetroLine) node).editName(lineName);
                }
            }
            // AND START DRAGGING IT
            else if (node != null) {
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(mapState.DRAGGING_NODE);

                // STORE CURRENT POSSITION FOR UNDO/REDO
                if (node instanceof DraggableText){
                    currentOldX = ((DraggableText) node).getX();
                    currentOldY = ((DraggableText) node).getY();
                }
                
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        }
//        } else if (dataManager.isInState(mapState.STARTING_RECTANGLE)) {
//            dataManager.startNewImage(x, y);
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
            
                LineStationAndLabelUpdater labelUpdater = new LineStationAndLabelUpdater();
                labelUpdater.updateLabelAssociations(label);
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
        zoomCounter = zoomCounter + 0.25;
        zoomGroup.setScaleX(zoomCounter);
        zoomGroup.setScaleY(zoomCounter);
    }
    
    public void processZoomOutRequest(Group zoomGroup){
        if (zoomCounter == 0.25)
            return;
        zoomCounter = zoomCounter - 0.25;        
        zoomGroup.setScaleX(zoomCounter);
        zoomGroup.setScaleY(zoomCounter);
    }
    
    public void processIncreaseMapSizeRequest(Pane canvas){
        canvas.setMaxSize(canvas.getWidth() * 1.25, canvas.getHeight() * 1.25);
    }
    
    public void processDecreaseMapSizeRequest(Pane canvas){
        if (canvas.getHeight() * 0.75 < canvas.getMinHeight() || canvas.getWidth() * 0.75 < canvas.getMinWidth())
            return;
        canvas.setMaxSize(canvas.getWidth() * 0.75, canvas.getHeight() * 0.75);
    }
}
