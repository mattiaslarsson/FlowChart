package gui;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shapes.Form;

/** Innehåller metoder för att visa och ta bort objekt från skärmen
 * 
 * @author Mattias Larsson
 *
 */
public class GUI {
	private Stage stage;
	private BorderPane root;
	private AnchorPane centerPane;
	private Scene scene;
	private Color cpBgColor, cpFgColor;
	
	/** Konstruktor
	 * visar fönstret
	 * 
	 * @param Stage stage 
	 * @param double ScreenWidth
	 * @param double ScreenHeight
	 */
	public GUI(Stage primaryStage, double ScreenWidth, double ScreenHeight) {
		stage = primaryStage;
		root = new BorderPane();
		scene = new Scene(root, ScreenWidth, ScreenHeight);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle("FX-FlowChart v1.0");
		stage.show();
	}
	
	/** Initierar arbetsytan
	 * 
	 * @param AnchorPane pane
	 */
	public void setWorkArea(AnchorPane pane) {
		centerPane = pane;
		centerPane.setBackground(new Background(
				new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		root.setCenter(centerPane);
	}
	
	/** Lägger till en meny till fönstret
	 * 
	 * @param MenuBar menuBar
	 */
	public void addMenu(MenuBar menuBar) {
		root.setTop(menuBar);
	}
	
	/** Uppdaterar arbetsytan när en ny form läggs till
	 * 
	 * @param ObservableList<Node> formsList
	 */
	public void updateView(ObservableList<Form> formsList, ObservableList<Path> arrowList) {
		try {
			centerPane.getChildren().removeAll(formsList);
			centerPane.getChildren().removeAll(arrowList);
			centerPane.getChildren().addAll(formsList);
			centerPane.getChildren().addAll(arrowList);
			
			// Lägger alla pilar bakom de övriga objekten
			arrowList.forEach(arrow -> {
				arrow.toBack();
			});
			
		} catch (Exception e) {}
	}
	
	
	/** Sätter en tangentbordslyssnare till scenen
	 * 
	 * @param EventHandler<KeyEvent> eventHandler
	 */
	public void setKeyboardListener(EventHandler eventHandler) {
		scene.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
	}

	/** Visar en ny panel där användaren kan ändra färg
	 * på former
	 * 
	 * @param Shape shape
	 * @param Text text
	 */
	public void changeColor(Shape shape, Text text) {
		ColorPicker bgCp = new ColorPicker();
		bgCp.setValue((Color) shape.getFill());
		
		// När man väljer färg ändras formens färg genast
		bgCp.setOnAction(e -> {
			shape.setFill(bgCp.getValue());
		});
		
		Text bgText = new Text("Background Color");
		Text fgText = new Text("Text Color");
		
		ColorPicker fgCp = new ColorPicker();
		fgCp.setValue((Color) text.getFill());
		
		// När man väljer färg ändras textens färg genast
		fgCp.setOnAction(e -> {
			text.setFill(fgCp.getValue());
		});
		
		// Knapp för att kopiera färger
		Button copyColors = new Button("Copy colorset");
		copyColors.setOnAction(e -> {
			cpBgColor = (Color) bgCp.getValue();
			cpFgColor = (Color) fgCp.getValue();
		});
		
		// Knapp för att klistra in färger
		Button pasteColors = new Button("Paste colorset");
		pasteColors.setOnAction(e -> {
			shape.setFill(cpBgColor);
			text.setFill(cpFgColor);
			bgCp.setValue(cpBgColor);
			fgCp.setValue(cpFgColor);
		});
		
		HBox cpPaste = new HBox();
		cpPaste.getChildren().addAll(copyColors, pasteColors);
		
		Button colorClose = new Button("Close this panel");
		colorClose.setOnAction(e -> {
			// Döljer panelen
			root.setRight(null);
		});
		
		VBox colorColumn = new VBox();
		colorColumn.getChildren().addAll(bgText, bgCp, fgText, fgCp, cpPaste, colorClose);
		root.setRight(colorColumn);
	}

	/** Visar olika hjälp-rutor
	 * 
	 * @param String string
	 */
	public void showHelp(String string) {
		Alert helpDialog = new Alert(AlertType.INFORMATION);
		String helpTitle="", helpHeader="", helpContent="";
		switch(string) {
		case "addShape":
			helpTitle = "How to add a Shape";
			helpHeader = "How to add a Shape";
			helpContent = "Select 'Add Shape' in the menu bar\n";
			helpContent += "Select the desired shape\n";
			helpContent += "Enter the label for the shape\n";
			helpContent += "You can move the shape with drag and drop.";
			break;
		case "addLine":
			helpTitle = "How to add a Line";
			helpHeader = "How to add a Line";
			helpContent = "Left-click on the first shape.\n";
			helpContent += "While pressing <CTRL>, left-click on the second shape.";
			break;
		case "delete":
			helpTitle = "How to delete a shape";
			helpHeader = "How to delete a shape";
			helpContent = "Right-click on an object.\n";
			helpContent += "Press <DELETE> on the keyboard.";
			break;
		case "changeColor":
			helpTitle = "How to change color of a shape";
			helpHeader = "How to change color of a shape";
			helpContent = "Double-click on a shape.\n";
			helpContent += "Select background color and text color.\n";
			helpContent += "You can copy this set of colors and paste it to another shape ";
			helpContent += "simply by clicking \'Copy Colorset\' and 'Paste Colorset' respectively\n";
			helpContent += "Press the button \'Close this panel\' to close the color picker.";
		}
		helpDialog.setTitle(helpTitle);
		helpDialog.setHeaderText(helpHeader);
		helpDialog.setContentText(helpContent);
		helpDialog.showAndWait();
	}

	public AnchorPane getWorkArea() {
		return centerPane;
	}
	
	/** Visar information om programmet
	 * 
	 */
	public void showAbout() {
		Alert aboutAlert = new Alert(AlertType.INFORMATION);
		aboutAlert.setTitle("FX-FlowChart");
		aboutAlert.setHeaderText("About FX-FlowChart");
		aboutAlert.setContentText("FX-FlowChart is a flowchart-maker written entirely in Java FX 8. "
				+ "This piece of software was developed in Jan-Feb 2016 as a school-assignment. "
				+ "I wish to thank\nJohan Lindström <jolindse@hotmail.com>\nRobin Listerberg\nJonas Sunnari <jsunnari@gmail.com>\nfor valuable "
				+ "feedback and input.\n\n"
				+ "This software was coded in its' entirety by Mattias Larsson\n"
				+ "mattias.larsson75@outlook.com\n\n"
				+ "Todo:\n"
				+ "Implement save and load\n"
				+ "Arrowheads");
		aboutAlert.showAndWait();
		
	}


	
}
