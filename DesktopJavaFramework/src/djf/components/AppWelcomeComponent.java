/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.components;

import java.io.File;
import javafx.scene.layout.Pane;

/**
 *
 * @author Nick
 */
public interface AppWelcomeComponent {
    
    String initWelcome();
    
    void initLayout();
    
    void initControllers();
    
    void initStyle();

    void loadRecents(Pane pane);
    
    File getRecentFile();
}
