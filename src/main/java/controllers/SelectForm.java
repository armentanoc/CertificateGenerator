package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class SelectForm {
	
    private Image icon;
    private FXMLLoader loader;
	
	public void selectz(String icons, String base, String css) {
		try {
            ClassLoader classLoader = getClass().getClassLoader();
            icon = new Image(classLoader.getResource(icons).toExternalForm());
            loader = new FXMLLoader(classLoader.getResource(base));
            css = classLoader.getResource(css).toExternalForm();
            
		} catch (Exception e) {e.printStackTrace();} 
	}
	
    public Image getIcon() {
        return icon;
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
    
