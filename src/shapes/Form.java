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
	private double dX, dY;
	
	public Form() {
	// Drag'n'drop lyssnare
		this.setOnMousePressed(press -> {
			dX = this.getTranslateX() - press.getSceneX();
			dY = this.getTranslateY() - press.getSceneY();
		})
		this.setOnMouseDragged(drag -> {
			this.setTranslateX(drag.getSceneX() + dX);
			this.setTranslateY(drag.getSceneY() + dY);
		});
	}
	
	/** Skapar ett textlager som l�ggs ovanp� grundformen
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
