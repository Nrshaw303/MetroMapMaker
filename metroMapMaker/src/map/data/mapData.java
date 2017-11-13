package map.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static map.data.mapState.SIZING_SHAPE;
import map.gui.mapWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import static map.data.mapState.SELECTING_NODE;
//import map.transactions.AddNode_Transaction;
import javafx.scene.text.Text;
import jtps.jTPS;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // THESE ARE THE NODES IN THE LOGO
    ObservableList<Node> logoNodes;
        
    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Shape newShape;

    // THIS IS THE NODE CURRENTLY SELECTED
    Node selectedNode;

    // CURRENT STATE OF THE APP
    mapState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE NODE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public mapData(AppTemplate initApp) {
	// KEEP THE APP FOR LATER
	app = initApp;
        
	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedNode = null;

	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
    }
    
    public ObservableList<Node> getLogoNodes() {
	return logoNodes;
    }
    
    public void setLogoNodes(ObservableList<Node> initLogoNodes) {
	logoNodes = initLogoNodes;
    }
    
    public void removeSelectedNode() {
	if (selectedNode != null) {
	    logoNodes.remove(selectedNode);
	    selectedNode = null;
	}
    }
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
	setState(SELECTING_NODE);
	newShape = null;
	selectedNode = null;
	
	//logoNodes.clear();
	((mapWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        ((mapWorkspace)app.getWorkspaceComponent()).initDebugText();
    }
    
    public Color getBackgroundColor() {
        return (Color)((mapWorkspace)app.getWorkspaceComponent()).getCanvas().getBackground().getFills().get(0).getFill();
    }
    
    public void setBackgroundColor(Color color) {
        Pane canvas = ((mapWorkspace)app.getWorkspaceComponent()).getCanvas();        
        BackgroundFill fill = new BackgroundFill(color, null, null);
	Background background = new Background(fill);
        canvas.setBackground(background);
    }

    public void selectSizedShape() {
	if (selectedNode != null)
	    unhighlightNode(selectedNode);
	selectedNode = newShape;
	highlightNode(selectedNode);
	newShape = null;
	if (state == SIZING_SHAPE) {
	    state = ((Draggable)selectedNode).getStartingState();
	}
    }
    
    public void unhighlightNode(Node node) {
	node.setEffect(null);
    }
    
    public void highlightNode(Node node) {
	node.setEffect(highlightedEffect);
    }

    public void startNewRectangle(int x, int y) {
	DraggableRectangle newRectangle = new DraggableRectangle();
	newRectangle.start(x, y);
	newShape = newRectangle;
	initNewShape();
    }

    public void startNewEllipse(int x, int y) {
	DraggableEllipse newEllipse = new DraggableEllipse();
	newEllipse.start(x, y);
	newShape = newEllipse;
	initNewShape();
    }

    public void initNewShape() {
	// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedNode != null) {
	    unhighlightNode(selectedNode);
	    selectedNode = null;
	}

	// USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//	newShape.setFill(workspace.getFillColorPicker().getValue());
//	newShape.setStroke(workspace.getOutlineColorPicker().getValue());
//	newShape.setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
	
	// GO INTO SHAPE SIZING MODE
	state = mapState.SIZING_SHAPE;
	
	// FINALLY, ADD A TRANSACTION FOR ADDING THE NEW SHAPE
        jTPS tps = app.getTPS();
        mapData data = (mapData)app.getDataComponent();
//        AddNode_Transaction newTransaction = new AddNode_Transaction(data, newShape);
//        tps.addTransaction(newTransaction);
    }

    public Shape getNewShape() {
	return newShape;
    }

    public Node getSelectedNode() {
	return selectedNode;
    }

    public void setSelectedNode(Node initSelectedNode) {
	selectedNode = initSelectedNode;
    }

    public Node selectTopNode(int x, int y) {
	Node node = getTopNode(x, y);
	if (node == selectedNode)
	    return node;
	
	if (selectedNode != null) {
	    unhighlightNode(selectedNode);
	}
	if (node != null) {
	    highlightNode(node);
	    mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
	    workspace.loadSelectedNodeSettings(node);
	}
	selectedNode = node;
	if (node != null) {
            ((Draggable)node).setStart(x, y);
	}
	return node;
    }
    
    public boolean isShape(Draggable node) {
        return ((node.getNodeType() == Draggable.ELLIPSE) 
                || (node.getNodeType() == Draggable.RECTANGLE)
                || (node.getNodeType() == Draggable.TEXT));
    }
    
    public Draggable getSelectedDraggableNode() {
        if (selectedNode == null)
            return null;
        else
            return (Draggable)selectedNode;
    }

    public Node getTopNode(int x, int y) {
	for (int i = logoNodes.size() - 1; i >= 0; i--) {
	    Node node = (Node)logoNodes.get(i);
	    if (node.contains(x, y)) {
		return node;
	    }
	}
	return null;
    }

    public mapState getState() {
	return state;
    }

    public void setState(mapState initState) {
	state = initState;
    }

    public boolean isInState(mapState testState) {
	return state == testState;
    }
    
    // METHODS NEEDED BY TRANSACTIONS
    
    public void moveNodeToFront(Node nodeToMove) {
        int currentIndex = logoNodes.indexOf(nodeToMove);
        if (currentIndex >= 0) {
            logoNodes.remove(currentIndex);
	    if (logoNodes.isEmpty()) {
		logoNodes.add(nodeToMove);
	    }
	    else {
		ArrayList<Node> temp = new ArrayList();
		temp.add(nodeToMove);
		for (Node node : logoNodes)
		    temp.add(node);
		logoNodes.clear();
		for (Node node : temp)
                    logoNodes.add(node);
	    }            
        }
    }
    
    public void moveNodeToBack(Node nodeToMove) {
        int currentIndex = logoNodes.indexOf(nodeToMove);
        if (currentIndex >= 0) {
	    logoNodes.remove(currentIndex);
	    logoNodes.add(nodeToMove);
        }
    }
    
    public void moveNodeToIndex(Node nodeToMove, int index) {
        int currentIndex = logoNodes.indexOf(nodeToMove);
        int numberOfNodes = logoNodes.size();
        if ((currentIndex >= 0) && (index >= 0) && (index < numberOfNodes)) {
            // IS IT SUPPOSED TO BE THE LAST ONE?
            if (index == (numberOfNodes-1)) {
                logoNodes.remove(currentIndex);
                logoNodes.add(nodeToMove);
            }
            else {
                logoNodes.remove(currentIndex);
                logoNodes.add(index, nodeToMove);
            }
        }
    }
    
    public void removeNode(Node nodeToRemove) {
        int currentIndex = logoNodes.indexOf(nodeToRemove);
        if (currentIndex >= 0) {
	    logoNodes.remove(currentIndex);
        }
    }    
    
    public void addNode(Node nodeToAdd) {
        int currentIndex = logoNodes.indexOf(nodeToAdd);
        if (currentIndex < 0) {
	    logoNodes.add(nodeToAdd);
        }
    }

    public int getIndexOfNode(Node node) {
        return logoNodes.indexOf(node);
    }

    public void addNodeAtIndex(Node node, int nodeIndex) {
        logoNodes.add(nodeIndex, node);    
    }

    public boolean isTextSelected() {
        if (selectedNode == null)
            return false;
        else
            return (selectedNode instanceof Text);
    }
}
