package shapes;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** Skapar en text-form
 * 
 * @author Mattias Larsson
 *
 */
public class TextToAdd extends Form {
	private Text textObj;
	private Color textColor;
	private double x, y;
	
	public TextToAdd(String text) {
		x = 400;
		y = 300;
		textObj = super.setText(text);
		textColor = Color.BLACK;
		getChildren().add(textObj);
		setTranslateX(x);
		setTranslateY(y);
		textObj.setFont(Font.font(20));
		textObj.setFill(textColor);
	}

	@Override
	public String getText() {
		return textObj.getText();
	}


	@Override
	public Color getColor() {
		return (Color) textObj.getFill();
	}


	@Override
	public Color getTextColor() {
		return (Color) textObj.getFill();
	}
}
