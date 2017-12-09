package map.transactions;

import javafx.scene.paint.Paint;
import map.data.mapData;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeTextFont_Transaction implements jTPS_Transaction {
    private Text text;
    private Font font;
    private Font oldFont;
    private Paint oldColor;
    private Paint color;
    
    public ChangeTextFont_Transaction(Text initText, Font initFont, Paint initColor) {
        text = initText;
        font = initFont;
        color = initColor;
        oldFont = text.getFont();
        oldColor = text.getFill();
    }
 
    @Override
    public void doTransaction() {
        text.setFont(font);
        text.setFill(color);
    }

    @Override
    public void undoTransaction() {
        text.setFont(oldFont);
        text.setFill(oldColor);
    }    
}