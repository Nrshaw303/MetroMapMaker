package map.transactions;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeBackgroundColor_Transaction implements jTPS_Transaction {
    private Pane pane;
    private Color color;
    private Color oldColor;
    
    public ChangeBackgroundColor_Transaction(Pane initPane, Color initColor) {
        pane = initPane;
        color = initColor;
        oldColor = (Color)pane.getBackground().getFills().get(0).getFill();
    }

    @Override
    public void doTransaction() {
        BackgroundFill fill = new BackgroundFill(color, null, null);
	Background background = new Background(fill);
        pane.setBackground(background);
    }

    @Override
    public void undoTransaction() {
        BackgroundFill fill = new BackgroundFill(oldColor, null, null);
	Background background = new Background(fill);
        pane.setBackground(background);    
    }    
}