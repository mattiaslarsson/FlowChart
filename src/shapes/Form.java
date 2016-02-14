package shapes;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** Abstrakt superklass till alla former
 * 
 * @author Mattias Larsson
 *
 */
public abstract class Form extends StackPane {
	
	
	public Form() {
	// Drag'n'drop lyssnare
		this.setOnMouseDragged(e -> {
			double newX = this.getTranslateX()+e.getX();
			double newY = this.getTranslateY()+e.getY();
			this.setTranslateX(newX);
			this.setTranslateY(newY);
		});
	}
	
	/** Skapar ett textlager som läggs ovanpå grundformen
	 * 
	 * @param String text
	 * @return Text
	 */
	public Text setText(String text) {
		Text textLayer = new Text(text);
		textLayer.setFont(Font.font(20));
		textLayer.setFill(Color.WHITE);
		
		return textLayer;
	}
	
	public abstract String getText();
	public abstract Color getColor();
	public abstract Color getTextColor();
	
}
