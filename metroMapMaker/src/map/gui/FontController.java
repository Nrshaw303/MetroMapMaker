package map.gui;

import djf.AppTemplate;
import map.data.mapData;
import map.transactions.ChangeTextFont_Transaction;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class FontController {
    private AppTemplate app;
    
    public FontController(AppTemplate initApp) {
        app = initApp;
    }

//    public void processChangeFont() {
//        mapData data = (mapData)app.getDataComponent();
//        if (data.isTextSelected()) {
//            Text selectedText = (Text)data.getSelectedNode();
//            mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
//            Font currentFont = workspace.getCurrentFontSettings();
//            ChangeTextFont_Transaction transaction = new ChangeTextFont_Transaction(selectedText, currentFont);
//            jTPS tps = app.getTPS();
//            tps.addTransaction(transaction);
//        }
//    }
}