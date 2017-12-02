/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.data.MetroLine;
import map.data.MetroStation;
import map.data.mapData;

/**
 *
 * @author Nick
 */
public class StationController {
    
    AppTemplate app;
    mapData mapManager;
    mapWorkspace mapWorkspace;
    
    public StationController(AppTemplate app){
        this.app = app;
        mapManager = (mapData)app.getDataComponent();
        mapWorkspace = (mapWorkspace)app.getWorkspaceComponent();
    }
    
    public void addStation(){
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Choose a name and a color for new line:");
        dialog.setTitle("New Line");
        
        VBox dialogVBox = new VBox();
        HBox nameHBox = new HBox();
        HBox colorHBox = new HBox();
        
        Label label1 = new Label("Line Name:");
        TextField name = new TextField();
        
        nameHBox.getChildren().addAll(label1, name);
        dialogVBox.getChildren().addAll(nameHBox);
        dialog.getDialogPane().setContent(dialogVBox);
        
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, ButtonType.OK);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.get() == ButtonType.OK){
            String stationName = name.getText();
            for (int i = 0; i < mapManager.getMapNodes().size(); i++){
                if (mapManager.getMapNodes().get(i) instanceof MetroStation){
                    if (((MetroStation) mapManager.getMapNodes().get(i)).getAssociatedLabel().getText().equals(stationName)){
                        Dialog popup = new Dialog();
                        popup.setHeaderText("Station name already exists");
                        popup.setTitle("Station creation error");
                        popup.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        
                        popup.showAndWait();
                        
                        return;
                    }
                }
            }
            mapManager.startNewStation(100, 100, stationName);
        }
    }
    
    public void removeStation(){
        Node stationToRemove = mapManager.getSelectedNode();
        mapWorkspace = (mapWorkspace) app.getWorkspaceComponent();
        if (stationToRemove instanceof MetroStation){
            if (!((MetroStation) stationToRemove).getLines().isEmpty()){
                for (MetroLine l : ((MetroStation) stationToRemove).getLines()) {
                    l.getPoints().remove(((MetroStation) stationToRemove).getX() + ((MetroStation) stationToRemove).getRadius());
                    l.getPoints().remove(((MetroStation) stationToRemove).getY() + ((MetroStation) stationToRemove).getRadius());
                }
                ((MetroStation) stationToRemove).getLines().clear();
            }
            mapWorkspace.removeStationFromList((MetroStation) stationToRemove);
            mapManager.removeNode(((MetroStation) stationToRemove).getAssociatedLabel());
            mapManager.removeSelectedNode();
        }
    }    
}
