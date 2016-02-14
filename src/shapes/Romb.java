package shapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/** Skapar en romb-form
 * 
 * @author Mattias Larsson
 *
 */
public class Romb extends Form {
	private Path romb;
	private Text textLayer;
	private MoveTo start;
	private LineTo punkt1, punkt2, punkt3, punkt4;
	private Color bg, textColor;
	
	
	public Romb(String text) {
		double x = 400;
		double y = 300;
		romb = new Path();
		textLayer = super.setText(text);
		bg = Color.BLUE;
		textColor = Color.WHITE;
		getChildren().addAll(romb, textLayer);
		drawRomb(x, y);
	}

	/** Skapar en romb
	 * 
	 * @param double x
	 * @param double y
	 */
	private void drawRomb(double x, double y) {
		// Anpassar storleken på romben efter hur stpr texten är
		start = new MoveTo(x+(textLayer.getBoundsInLocal().getWidth()/2), y-15);
		punkt1 = new LineTo(start.getX()-(textLayer.getBoundsInLocal().getWidth()/2+15),
				y+(textLayer.getBoundsInLocal().getHeight()/2));
		punkt2 = new LineTo(start.getX(), punkt1.getY()+(textLayer.getBoundsInLocal()
				.getHeight()/2+15));
		punkt3 = new LineTo(punkt2.getX()+(textLayer.getBoundsInLocal().getWidth()/2+15),
				punkt1.getY());
		punkt4 = new LineTo(start.getX(), start.getY());
		
		this.setWidth(textLayer.getBoundsInLocal().getWidth()>50 ? textLayer.getBoundsInLocal().getWidth()+15 : 50);
		
		romb.getElements().addAll(start, punkt1, punkt2, punkt3, punkt4);
		romb.setFill(bg);
		romb.setStroke(Color.DARKBLUE);
		textLayer.setFill(textColor);
		setTranslateX(x);
		setTranslateY(y);
	}
	
	@Override
	public Color getColor() {
		return (Color) romb.getFill();
	}

	@Override
	public String getText() {
		return textLayer.getText();
	}
	
	@Override
	public Color getTextColor() {
		return (Color) textLayer.getFill();
	}
	

}
