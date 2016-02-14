package shapes;


import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

/** Skapar en ellips-form
 * 
 * @author Mattias Larsson
 *
 */
public class Ellips extends Form  {
	private Ellipse ellipse;
	private Text textLayer;
	private double x, y;
	private Color bg, textColor;
	
	public Ellips(String text) {
		x = 400;
		y = 300;
		bg = Color.BLUE;
		textColor = Color.WHITE;
		drawEllipse(text, x, y);
		ellipse.setStroke(Color.DARKBLUE);
	}
	
	/** Skapar en ellips
	 * 
	 * @param String text
	 * @param double x 
	 * @param double y
	 */
	private void drawEllipse(String text, double x, double y) {
		ellipse = new Ellipse();
		textLayer = super.setText(text);
		getChildren().addAll(ellipse, textLayer);
		
		// Anpassar storleken på ellipsen till hur stor texten är
		
		ellipse.setRadiusX((textLayer.getBoundsInLocal().getWidth()>50 ? textLayer.getBoundsInLocal().getWidth() : 50));
		ellipse.setRadiusY(textLayer.getBoundsInLocal().getHeight());
		
		
		setTranslateX(x);
		setTranslateY(y);
		textLayer.setFill(textColor);
		ellipse.setFill(bg);
	}
	
	@Override
	public String getText() {
		return textLayer.getText();
	}

	@Override
	public Color getColor() {
		return (Color) ellipse.getFill();
	}

	@Override
	public Color getTextColor() {
		return (Color) textLayer.getFill();
	}
}
