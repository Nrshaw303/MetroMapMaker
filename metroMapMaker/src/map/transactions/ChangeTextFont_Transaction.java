package map.transactions;

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
    
    public ChangeTextFont_Transaction(Text initText, Font initFont) {
        text = initText;
        font = initFont;
        oldFont = text.getFont();
    }
 
    @Override
    public void doTransaction() {
        text.setFont(font);
    }

    @Override
    public void undoTransaction() {
        text.setFont(oldFont);
    }    
}