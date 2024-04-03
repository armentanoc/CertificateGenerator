package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class SelectForm {
	
    private Image icon;
    private FXMLLoader loader;
	
	public void selectz(String icons, String base, String css) {
		try {
            icon = new Image(getClass().getResource(icons).toExternalForm());
            loader = new FXMLLoader(getClass().getResource(base));
            css = getClass().getResource(css).toExternalForm();
            
		} catch (Exception e) {e.printStackTrace();} 
	}
	
    public Image getIcon() {
        return icon;
    }

    public FXMLLoader getLoader() {
        return loader;
    }
    
}
