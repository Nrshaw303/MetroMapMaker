/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import map.data.DraggableText;
import map.data.MetroLine;
import map.data.MetroStation;

/**
 *
 * @author Nick
 */
public class LineStationAndLabelUpdater {
    
    public void updateLabelAssociations(DraggableText label) {
        if (label.getIsForLine()) {
            if (label.getIsStartLabel()) {
                label.getAssociatedLine().getPoints().set(0, label.getX());
                label.getAssociatedLine().getPoints().set(1, label.getY());
            } else {
                label.getAssociatedLine().getPoints().set(
                        label.getAssociatedLine().getPoints().size() - 2, label.getX());
                label.getAssociatedLine().getPoints().set(
                        label.getAssociatedLine().getPoints().size() - 1, label.getY());
            }
        } 
        else if (label.getIsForStation()) {
            double xOffset = 0;
            double yOffset = 0;
            switch (label.getMovedProperty()) {
                case 0:
                    xOffset = label.getAssociatedStation().getRadius() * - 1 - 5;
                    yOffset = -label.getHeight() / 2;
                    break;
                case 1:
                    xOffset = label.getWidth() / 2;
                    yOffset = -label.getAssociatedStation().getRadius() - 5 - label.getHeight();
                    break;
                case 2:
                    xOffset = label.getAssociatedStation().getRadius() + 5 + label.getWidth();
                    yOffset = -label.getHeight() / 2;
                    break;
                case 3:
                    xOffset = label.getWidth() / 2;
                    yOffset = label.getAssociatedStation().getRadius() + 5;
                    break;
            }
            label.getAssociatedStation().setCenterX(label.getX() + xOffset);
            label.getAssociatedStation().setCenterY(label.getY() + yOffset);
            if (!(label.getAssociatedStation().getLines().isEmpty())) {
                for (MetroLine m : label.getAssociatedStation().getLines()) {
                    m.getPoints().set(m.getStationNames().indexOf(label.getText()) * 2 + 2,
                            label.getAssociatedStation().getX() + label.getAssociatedStation().getRadius());
                    m.getPoints().set(m.getStationNames().indexOf(label.getText()) * 2 + 3,
                            label.getAssociatedStation().getY() + label.getAssociatedStation().getRadius());

                }
            }
        }
    }    
    
    public void updateStationAssociations(MetroStation station) {
        DraggableText label = station.getAssociatedLabel();
        switch (label.getMovedProperty()) {
            case 0:
                label.setX(label.getAssociatedStation().getCenterX() + label.getAssociatedStation().getRadius() + 5);
                label.setY(label.getAssociatedStation().getCenterY() + label.getHeight() / 2);
                break;
            case 1:
                label.setX(label.getAssociatedStation().getCenterX() - label.getWidth() / 2);
                label.setY(label.getAssociatedStation().getCenterY() + label.getAssociatedStation().getRadius() + 5 + label.getHeight());
                break;
            case 2:
                label.setX(label.getAssociatedStation().getCenterX() - label.getAssociatedStation().getRadius() - 5 - label.getWidth());
                label.setY(label.getAssociatedStation().getCenterY() + label.getHeight() / 2);
                break;
            case 3:
                label.setX(label.getAssociatedStation().getCenterX() - label.getWidth() / 2);
                label.setY(label.getAssociatedStation().getCenterY() - label.getAssociatedStation().getRadius() - 5);
                break;
        }
    }
    
    public void updateLineStartAssociations(DraggableText label){
        label.setX(label.getAssociatedLine().getPoints().get(0));
        label.setY(label.getAssociatedLine().getPoints().get(1));
    }
    
    public void updateLineEndAssociations(DraggableText label){
        label.setX(label.getAssociatedLine().getPoints().get(label.getAssociatedLine().getPoints().size() - 2));
        label.setY(label.getAssociatedLine().getPoints().get(label.getAssociatedLine().getPoints().size() - 1));
    }
    
    public void updateStationLines(MetroStation station) {
        if (!(station.getLines().isEmpty())) {
            for (MetroLine m : station.getLines()) {
                m.getPoints().set(m.getStationNames().indexOf(station.getAssociatedLabel().getText()) * 2 + 2,
                        station.getX() + station.getRadius());
                m.getPoints().set(m.getStationNames().indexOf(station.getAssociatedLabel().getText()) * 2 + 3,
                        station.getY() + station.getRadius());

            }
        }
    }
}
