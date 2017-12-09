/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.data.MetroLine;
import map.data.MetroStation;
import map.data.mapData;

/**
 *
 * @author Nick
 */
public class LineController {
    
    AppTemplate app;
    mapData mapManager;
    mapWorkspace mapWorkspace;
    
    public LineController(AppTemplate app){
        this.app = app;
        mapManager = (mapData)app.getDataComponent();
        mapWorkspace = (mapWorkspace)app.getWorkspaceComponent();
    }
    
    public void addLine(){
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Choose a name and a color for new line:");
        dialog.setTitle("New Line");
        
        VBox dialogVBox = new VBox();
        HBox nameHBox = new HBox();
        HBox colorHBox = new HBox();
        
        ColorPicker color = new ColorPicker();
        Label label1 = new Label("Line Name:");
        TextField name = new TextField();
        Label label2 = new Label("Line Color: ");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        nameHBox.getChildren().addAll(label1, spacer, name);
        colorHBox.getChildren().addAll(label2, spacer2, color);
        dialogVBox.getChildren().addAll(nameHBox, colorHBox);
        dialogVBox.setSpacing(15);
        dialog.getDialogPane().setContent(dialogVBox);
        
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, ButtonType.OK);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.get() == ButtonType.OK){
            String lineName = name.getText();
            Color lineColor = color.getValue();
            mapManager.startNewLine(100, 100, 200, 100, lineName, lineColor);
        }
    }
    
    public void removeLine(){
        Node lineToRemove = mapManager.getSelectedNode();
        mapWorkspace = (mapWorkspace) app.getWorkspaceComponent();
        mapManager = (mapData) app.getDataComponent();
        
        if (lineToRemove instanceof MetroLine){
            for (String station : ((MetroLine) lineToRemove).getStationNames()){
                for (Node node : mapManager.getMapNodes()){
                    if (node instanceof MetroStation){
                        if (((MetroStation) node).getAssociatedLabel().equals(station)){
                            ((MetroStation) node).removeLine((MetroLine) lineToRemove);
                        }
                    }
                }
            }
            mapWorkspace.removeLineFromList((MetroLine) lineToRemove);
            mapManager.removeNode(((MetroLine) lineToRemove).getAssociatedStartLabel());
            mapManager.removeNode(((MetroLine) lineToRemove).getAssociatedEndLabel());
            mapManager.removeSelectedNode();
        }
    }
}
