package flowchart;

import java.util.Optional;

import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import shapes.Ellips;
import shapes.Form;
import shapes.Rektangel;
import shapes.Romb;
import shapes.TextToAdd;

/** Här sköts det mesta av logiken
 * 
 * @author Mattias Larsson
 *
 */
public class Model {
	
	private Object selObject;
	
	/** Skapar en text-etikett för formen
	 * 
	 * @return Textobjekt
	 */
	public String getLabel() {
		TextInputDialog textDialog = new TextInputDialog();
		textDialog.setTitle("Enter label");
		textDialog.setHeaderText("Enter label for shape");
		Optional<String> text = textDialog.showAndWait();
		if (text.isPresent()) {
			return text.get();
		}
		return null;
	}
	
	/** Returnerar ett nytt rektangel-objekt
	 * 
	 * @return Rektangel
	 */
	public Form addRect() {
		return new Rektangel(getLabel());
	}
	
	/** Returnerar ett nytt ellips-objekt
	 * 
	 * @return Ellips
	 */
	public Form addEllipse() {
		return new Ellips(getLabel());
	}
	
	/** Returnerar ett nytt romb-objekt
	 * 
	 * @return Romb
	 */
	public Form addRomb() {
		return new Romb(getLabel());
	}
	
	/** Returnerar ett nytt text-objekt
	 * 
	 * @return TextToAdd
	 */
	public Form addText() {
		return new TextToAdd(getLabel());
	}

	/** Returnerar ett nytt pil-objekt
	 * 
	 * @param Form form1
	 * @param Form form2
	 * @return Path
	 */
	public Path addArrow(Form form1, Form form2) {
		Path arrow = new Path();
		MoveTo startPoint = new MoveTo();
		
		// Binder startpunkten till första formens centrumpunkt
		startPoint.xProperty().bind(form1.translateXProperty().add(form1.getWidth()/2));
		startPoint.yProperty().bind(form1.translateYProperty().add(form1.getHeight()/2));
		
		LineTo firstLine = new LineTo();
		
		// Binder första linjens slutpunkt till startpunktens x
		// och andra formens y
		firstLine.xProperty().bind(startPoint.xProperty());
		firstLine.yProperty().bind(form2.translateYProperty().add(form2.getHeight()/2));
		
		LineTo secondLine = new LineTo();
		
		// Binder andra linjens slutpunkt till andra formens centrumpunkt
		secondLine.xProperty().bind(form2.translateXProperty().add(form2.getWidth()/2));
		secondLine.yProperty().bind(form2.translateYProperty().add(form2.getHeight()/2));
		
		// Pilspetsar
		// Kollar vilken sida av formen pilen skall befinna sig på
		NumberBinding xStart = new When(firstLine.xProperty().greaterThan(
				form2.translateXProperty().add(form2.widthProperty()))).then(
						form2.translateXProperty().add(form2.widthProperty())).otherwise(
								form2.translateXProperty());
		
		// Ritar ut pilspetsar 
		MoveTo arrowStart = new MoveTo();
		arrowStart.xProperty().bind(xStart);
		arrowStart.yProperty().bind(secondLine.yProperty());
		
		
		LineTo firstArrowHead = new LineTo();
		// Sätter korrekta koordinater för första vinkeln beroende på vilken sida
		// den skall befinna sig på
		firstArrowHead.xProperty().bind(new When(
				xStart.isEqualTo(form2.translateXProperty().add(form2.widthProperty()))).then(
						arrowStart.xProperty().add(Math.cos(50)*10)).otherwise(
								arrowStart.xProperty().subtract(Math.cos(-50)*10)));
		
		firstArrowHead.yProperty().bind(new When(
				xStart.isEqualTo(form2.translateXProperty().add(form2.widthProperty()))).then(
						arrowStart.yProperty().add(Math.sin(50)*10)).otherwise(
								arrowStart.yProperty().add(Math.sin(-50)*10)));
		
		MoveTo backToStart = new MoveTo();
		backToStart.xProperty().bind(arrowStart.xProperty());
		backToStart.yProperty().bind(arrowStart.yProperty());
		
		LineTo secondArrowHead = new LineTo();
		// Sätter korrekta koordinater för andra vinkeln beroende på vilken sida
		// den skall befinna sig på
		secondArrowHead.xProperty().bind(new When(
				xStart.isEqualTo(form2.translateXProperty().add(form2.widthProperty()))).then(
						arrowStart.xProperty().add(Math.cos(-50)*10)).otherwise(
								arrowStart.xProperty().subtract(Math.cos(50)*10)));
		
		secondArrowHead.yProperty().bind(new When(
				xStart.isEqualTo(form2.translateXProperty().add(form2.widthProperty()))).then(
						arrowStart.yProperty().add(Math.sin(-50)*10)).otherwise(
								arrowStart.yProperty().add(Math.sin(50)*10)));
		
		arrow.getElements().addAll(startPoint, firstLine, secondLine, arrowStart, firstArrowHead, backToStart, secondArrowHead);
		arrow.setStrokeWidth(2);
		
		return arrow;
	}
	
	
	/** Lyssnare för pilarna
	 * 
	 * @param ObservableList<Shape> arrowList
	 * @param formsList<Form> formsList
	 */
	public void setArrowListeners(ObservableList<Path> arrowList, ObservableList<Form> formsList) {
		arrowList.forEach(arrow -> {
			try {
				arrow.setOnMouseClicked(mouseClicked -> {
					// Kollar om det är höger-klick
					if (mouseClicked.getButton().equals(MouseButton.SECONDARY)) {
						// Är pilen inte det objekt som är valt sedan innan
						if (!(arrow==getSelectedObject())) {
							
							// Avmarkerar den pilen eller formen som är vald
							arrowList.forEach(a -> {
								if (a==getSelectedObject()) {
									a.setEffect(null);
								}
							});
							formsList.forEach(f -> {
								if (f == getSelectedObject()) {
									f.setEffect(null);
								}
							});
							
							// Markerar pilen som vald
							arrow.setEffect(new PulsatingShadow());
							setSelectedObject(arrow);
						} else {
							// Är pilen vald sedan innan så avmarkera den
							arrow.setEffect(null);
							setSelectedObject(null);
						}
					}
				});
			} catch (Exception e) {}
		});
	}
	
	/** Returnerar det objekt som är valt för närvarande
	 * 
	 * @return Object
	 */
	public Object getSelectedObject() {
		return selObject;
	}
	
	/** Sätter valt objekt
	 * 
	 * @param Object selObj
	 */
	public void setSelectedObject(Object selObj) {
		selObject = selObj;
	}
	
	/** Tar bort ett objekt från skärmen
	 * 
	 * @param ObservableList<Form> formsList
	 * @param ObservableList<Shape> arrowList
	 */
	public void deleteObject(ObservableList<Form> formsList, ObservableList<Path> arrowList) {
		// Kollar i vilken lista objektet som skall raderas finns i
		if (arrowList.contains(selObject)) {
			// Lägger till en dummy
			arrowList.add(null); 
			// Raderar valt objekt
			arrowList.remove(selObject); 
			// Tar bort dummyn
			arrowList.remove(arrowList.size()-1);
			// Nollställer valt objekt
			selObject = null; 
		} else if (formsList.contains(selObject)) {
			formsList.add(null);
			formsList.remove(selObject);
			formsList.remove(formsList.size()-1);
			selObject=null;
		}
	}
}
