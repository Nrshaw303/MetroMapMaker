/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import javax.script.*;

import djf.AppTemplate;
import static java.lang.Double.MAX_VALUE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
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
import map.data.DraggableText;
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
        dialog.setHeaderText("Choose a name for the new station:");
        dialog.setTitle("New Station");
        
        VBox dialogVBox = new VBox();
        HBox nameHBox = new HBox();
        HBox colorHBox = new HBox();
        
        Label label1 = new Label("Station Name:");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField name = new TextField();
        
        nameHBox.getChildren().addAll(label1, spacer, name);
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

    public void moveLabel(Node node) {
        if (node instanceof MetroStation){
            MetroStation station = (MetroStation) node;
            station.getAssociatedLabel().setMovedProperty();
            
            LineStationAndLabelUpdater updater = new LineStationAndLabelUpdater();
            updater.updateStationAssociations(station);
        }
    }

    public void rotateLabel(Node node) {
        if (node instanceof MetroStation){
            if (((MetroStation) node).getAssociatedLabel().getRotate() == 0)
                ((MetroStation) node).getAssociatedLabel().setRotate(90);
            else
                ((MetroStation) node).getAssociatedLabel().setRotate(0);
        }
        else{
            if (((DraggableText) node).getRotate() == 0)
                ((DraggableText) node).setRotate(90);
            else
                ((DraggableText) node).setRotate(0);
        }
    }
    
    public void snapToGrid(Node node){
        if (node instanceof MetroStation){
            MetroStation station = (MetroStation) node;
            station.setCenterX(Math.round(station.getCenterX() / 30) * 30);
            station.setCenterY(Math.round(station.getCenterY() / 30) * 30);

            LineStationAndLabelUpdater updater = new LineStationAndLabelUpdater();
            updater.updateStationAssociations(station);
            updater.updateStationLines(station);
        }
        else if (node instanceof DraggableText){
            if (((DraggableText) node).getIsForLine()){
                if (((DraggableText) node).getIsStartLabel()){
                    DraggableText label = (DraggableText) node;
                    label.getAssociatedLine().getPoints().set(0, (double) Math.round(
                            label.getAssociatedLine().getPoints().get(0) / 30) * 30);
                    label.getAssociatedLine().getPoints().set(1, (double) Math.round(
                            label.getAssociatedLine().getPoints().get(1) / 30) * 30);

                    LineStationAndLabelUpdater updater = new LineStationAndLabelUpdater();
                    updater.updateLineStartAssociations(label);
                }
                else{
                    DraggableText label = (DraggableText) node;
                    label.getAssociatedLine().getPoints().set(label.getAssociatedLine().getPoints().size() - 2, 
                            (double) Math.round(label.getAssociatedLine().getPoints().get(
                                    label.getAssociatedLine().getPoints().size() - 2) / 30) * 30);
                    label.getAssociatedLine().getPoints().set(label.getAssociatedLine().getPoints().size() - 1, 
                            (double) Math.round(label.getAssociatedLine().getPoints().get(
                                    label.getAssociatedLine().getPoints().size() - 1) / 30) * 30);
                    
                    LineStationAndLabelUpdater updater = new LineStationAndLabelUpdater();
                    updater.updateLineEndAssociations(label);
                }
            }
        }
    }
    
    public ArrayList<MetroStation> findRoute(MetroStation startStation, MetroStation endStation){
        
        ArrayList<MetroStation> stations = ((mapWorkspace) app.getWorkspaceComponent()).getListOfStations();
        int[][] adjMatrix = new int[stations.size()][stations.size()];
        int[][] solMatrix = new int[stations.size()][stations.size()];
        for (int i = 0; i < stations.size(); i++){
            for (int j = 0; j < stations.size(); j++){
                adjMatrix[i][j] = 10000;
                solMatrix[i][j] = 10000;
            }
        }
        for (int i = 0; i < stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
                if (stations.get(i).getLines().size() != 0 && stations.get(j).getLines().size() != 0) {
                    for (MetroLine line : stations.get(i).getLines()) {
                        for (MetroLine line2 : stations.get(j).getLines()) {
                            if (line == line2) {
                                String stationName1 = stations.get(i).getAssociatedLabel().getText();
                                String stationName2 = stations.get(j).getAssociatedLabel().getText();
                                for (int k = 0; k < line.getStationNames().size() - 1; k++) {
                                    String temp1 = line.getStationNames().get(k);
                                    String temp2 = line.getStationNames().get(k + 1);
                                    if ((temp1.equals(stationName1) || temp1.equals(stationName2)) 
                                            && (temp2.equals(stationName1) || temp2.equals(stationName2))) {
                                        adjMatrix[i][j] = 1;
                                        solMatrix[i][j] = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int k = 0; k < stations.size(); k++){
            for (int i = 0; i < stations.size(); i++){
                for (int j = 0; j < stations.size(); j++){
                    if (solMatrix[i][k] + solMatrix[k][j] < solMatrix[i][j]){
                        solMatrix[i][j] = solMatrix[i][k] + solMatrix[k][j];
                    }
                }
            }
        }
        for (int i = 0; i < stations.size(); i++){
            solMatrix[i][i] = 0;
        }
        int[] visited = new int[stations.size()];
        for (int i = 0; i < visited.length; i++){
            visited[i] = -1;
        }
        int start = stations.indexOf(startStation);
        int end = stations.indexOf(endStation);
        if (solMatrix[start][end] == 0)
            return new ArrayList();
        return BFS(adjMatrix, start, end, stations, visited);
    }
    
    public ArrayList<MetroStation> BFS(int[][] adj, int start, int end, ArrayList<MetroStation> stations, int[] visited){
        visited[start] = 100;
        ArrayList<Integer> arr = new ArrayList();
        ArrayList<MetroStation> solution = new ArrayList();
        arr.add(start);
        while (!arr.isEmpty()){
            int e = arr.get(arr.size() - 1);
            if (arr.contains(end)){
                break;
            }
            int temp = 0;
            while (temp < stations.size()){
                if ((visited[temp] == -1)  && (adj[e][temp] == 1)){
                    arr.add(temp);
                    visited[temp] = e;
                }
                temp++;
            }
        }
        int i = end;
        while(i != 100){
            solution.add(stations.get(i));
            i = visited[i];
        }
        Collections.reverse(solution);
        return solution;
    }
    
    public void addLabel(){
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Choose content of new label:");
        dialog.setTitle("New Label");
        
        VBox dialogVBox = new VBox();
        HBox nameHBox = new HBox();
        
        Label label1 = new Label("Text:");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField name = new TextField();
        
        nameHBox.getChildren().addAll(label1, spacer, name);
        dialogVBox.getChildren().addAll(nameHBox);
        dialog.getDialogPane().setContent(dialogVBox);
        
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, ButtonType.OK);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.get() == ButtonType.OK){
            String text = name.getText();
            mapManager.startNewLabel(100, 100, text);
        }
    }
}
