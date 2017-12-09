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
import java.util.Optional;
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
        ArrayList<MetroStation> solution = new ArrayList();
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
//        System.out.println("Solution Matrix");
//        System.out.print("[ ] ");
//        for (int i = 0; i < stations.size(); i++){
//            System.out.print("[" + stations.get(i).getAssociatedLabel().getText() + "] ");
//        }
//        System.out.println();
//        for (int i = 0; i < stations.size(); i++){
//            System.out.print("[" + stations.get(i).getAssociatedLabel().getText() + "] ");
//            for (int j = 0; j < stations.size(); j++){
//                System.out.print("[" + solMatrix[i][j] + "] ");
//            }
//            System.out.println();
//        }
        ArrayList<ArrayList<MetroStation>> possibleSolutions = new ArrayList<>();
        int startIndex = stations.indexOf(startStation);
        int endIndex = stations.indexOf(endStation);
        if (solMatrix[startIndex][endIndex] == 1) {
            solution.add(startStation);
            solution.add(endStation);
            return solution;
        } 
        else if (solMatrix[startIndex][endIndex] < 10000) {
            for (int i = 0; i < stations.size(); i++) {
                solution = new ArrayList();
                solution.add(startStation);
                solution.add(stations.get(i));
                ArrayList<MetroStation> possible = traceShortest(solution, solMatrix, startIndex, endStation, endIndex, stations);
                if (possible != null){
                    possibleSolutions.add(possible);
                }
            }
        } 
        for (ArrayList<MetroStation> list : possibleSolutions){
            if (list.size() < solution.size())
                solution = list;
        }
        return solution;
        
    }

    public ArrayList<MetroStation> traceShortest(ArrayList<MetroStation> solution, int[][] solMatrix, 
            int index, MetroStation endStation, int endIndex, ArrayList<MetroStation> stations) {
        if (solution.size() > 0) {
            if (solution.get(solution.size() - 1) == endStation) {
                return solution;
            }
        }
        else{
            if (solMatrix[index][endIndex] < 10000){
                return null;
            }
            else{
                for (int i = 0; i < solMatrix[index].length; i++){
                    solution.add(stations.get(i));
                    ArrayList<MetroStation> possibleSolution = traceShortest(solution, solMatrix, i, endStation, endIndex, stations);
                    if (possibleSolution.get(possibleSolution.size() - 1) == endStation){
                        return possibleSolution;
                    }
                }
            }
        }
        return null;
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
