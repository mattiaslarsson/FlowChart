package shapes;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/** Skapar en rektangel-form
 * 
 * @author Mattias Larsson
 *
 */
public class Rektangel extends Form {
	private Rectangle rect;
	private Text textLayer;
	private double x, y;
	private Color bg, textColor;
	
	public Rektangel(String text) {
		x = 400;
		y = 300;
		bg = Color.BLUE;
		textColor = Color.WHITE;
		textLayer = super.setText(text);
		drawRectangle(x, y);
	}

	/** Skapar en rektangel
	 * 
	 * @param double x
	 * @param double y
	 *
	 */
	private void drawRectangle(double x, double y) {
		rect = new Rectangle();
		rect.setArcHeight(15);
		rect.setArcWidth(15);
		getChildren().addAll(rect, textLayer);
		
		// Anpassar storleken på rektangeln efter hur stor texten är
		rect.setWidth((textLayer.getBoundsInLocal().getWidth()>50 ? textLayer.getBoundsInLocal().getWidth()+15 : 50));
		rect.setHeight(textLayer.getBoundsInLocal().getHeight());
		
		setTranslateX(x);
		setTranslateY(y);
		rect.setFill(bg);
		textLayer.setFill(textColor);
		rect.setStroke(Color.DARKBLUE);
	}
	
	@Override
	public String getText() {
		return textLayer.getText();
	}

	@Override
	public Color getColor() {
		return (Color) rect.getFill();
	}

	@Override
	public Color getTextColor() {
		return (Color) textLayer.getFill();
	}
}

