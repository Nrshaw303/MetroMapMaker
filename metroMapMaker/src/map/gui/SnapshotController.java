package map.gui;

import djf.AppTemplate;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;

/**
 *
 * @author McKillaGorilla
 */
public class SnapshotController {
    private AppTemplate app;
    
    public SnapshotController(AppTemplate initApp) {
        app = initApp;
    }
    
    /**
     * This method processes a user request to take a snapshot of the
     * current scene.
     */
    public void processSnapshot() {
	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("Logo.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }    
}
