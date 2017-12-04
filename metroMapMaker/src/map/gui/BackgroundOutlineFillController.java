package map.gui;

import djf.AppTemplate;
import map.data.Draggable;
import map.data.mapData;
import map.transactions.ChangeBackgroundColor_Transaction;
import map.transactions.ChangeShapeFillColor_Transaction;
import map.transactions.ChangeShapeOutlineColor_Transaction;
import map.transactions.ChangeShapeOutlineThickness_Transaction;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class BackgroundOutlineFillController {
    AppTemplate app;
    
    public BackgroundOutlineFillController(AppTemplate initApp) {
        app = initApp;
    }

    /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectBackgroundColor() {
//	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
//	if (selectedColor != null) {
//            Pane canvas = workspace.getCanvas();
//	    ChangeBackgroundColor_Transaction transaction = new ChangeBackgroundColor_Transaction(canvas, selectedColor);
//            jTPS tps = app.getTPS();
//            tps.addTransaction(transaction);
//	    app.getGUI().updateToolbarControls(false);
//	}
    }
            
    /**
     * This method processes a user request to select a fill color for
     * a shape.
     */
    public void processSelectFillColor() {
//	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//	Color selectedColor = workspace.getFillColorPicker().getValue();
//	if (selectedColor != null) {
//            mapData data = (mapData)app.getDataComponent();
//            Node node = data.getSelectedNode();
//            if (node != null) {
//                ChangeShapeFillColor_Transaction transaction = new ChangeShapeFillColor_Transaction((Shape)node, selectedColor);
//                jTPS tps = app.getTPS();
//                tps.addTransaction(transaction);
//                app.getGUI().updateToolbarControls(false);
//            }
//	}
    }
    
    /**
     * This method processes a user request to select the outline
     * color for a shape.
     */
    public void processSelectOutlineColor() {
//	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//	Color selectedColor = workspace.getOutlineColorPicker().getValue();
//	if (selectedColor != null) {
//            mapData data = (mapData)app.getDataComponent();
//            Draggable node = data.getSelectedDraggableNode();
//            if ((node != null) && data.isShape(node)) {
//                ChangeShapeOutlineColor_Transaction transaction = new ChangeShapeOutlineColor_Transaction((Shape)node, selectedColor);
//                jTPS tps = app.getTPS();
//                tps.addTransaction(transaction);
//                app.getGUI().updateToolbarControls(false);
//            }
//	}    
    }
    
    /**
     * This method processes a user request to select the outline
     * thickness for shape drawing.
     */
    public void processSelectOutlineThickness() {
//	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
//        mapData data = (mapData)app.getDataComponent();
//        Draggable draggableNode = data.getSelectedDraggableNode();
//        if ((draggableNode != null) && (data.isShape(draggableNode))) {
//            ChangeShapeOutlineThickness_Transaction transaction = new ChangeShapeOutlineThickness_Transaction((Shape)draggableNode, outlineThickness);
//            jTPS tps = app.getTPS();
//            tps.addTransaction(transaction);
//            app.getGUI().updateToolbarControls(false);
//        }
    }            
}
