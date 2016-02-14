package flowchart;
	
import java.awt.Toolkit;

import gui.GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shapes.Form;

/** Huvudklass
 * Drar ig�ng hela applikationen och fungerar som en controller
 * mellan GUI och klasserna som handhar logiken
 * 
 * @author Mattias Larsson
 *
 */
public class Main extends Application {
	private ObservableList<Form> formsList = FXCollections.observableArrayList();
	private ObservableList<Path> arrowList = FXCollections.observableArrayList();
	private GUI gui;
	private Model model;
	private Form[] arrowArray = new Form[2];
	private Stage stage;
	
	
	@Override
	
	public void start(Stage stage) {
		// H�mtar sk�rmstorlek
		final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		this.stage = stage;
		
		// Instansierar gui och logik
		gui = new GUI(this.stage, SCREEN_WIDTH, SCREEN_HEIGHT);
		model = new Model();
		
		// Skickar meny och arbetsyta till GUI
		gui.addMenu(initMenu());
		gui.setWorkArea(new AnchorPane());
		
		// L�gger till tangentbordslyssnare till GUI f�r ta bort objekt fr�n sk�rmen
		gui.setKeyboardListener(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DELETE) {
					model.deleteObject(formsList, arrowList);
				}
			}
		});
		
		// L�gger till lyssnare p� de observerbara listorna
		// som inneh�ller former och pilar
		arrowList.addListener(new ListChangeListener<Path>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Path> c) {
				// N�r listan �ndras, uppdatera vyn
				// och l�gg till lyssnare p� nya pilar
				gui.updateView(formsList, arrowList);
				model.setArrowListeners(arrowList, formsList);
			}
		});
		
		formsList.addListener(new ListChangeListener<Form>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Form> c) {
				gui.updateView(formsList, arrowList);
				formsList.forEach(node -> {
					try {
						// Varje form i listan skall ha en muslyssnare
						node.setOnMouseClicked(mouseClicked -> {
							
							// Dubbelklick - visa f�rg�ndraren
							if (mouseClicked.getClickCount()==2) {
								// Om objektet inte bara inneh�ller text
								if (node.getChildren().size()>1) { 
									gui.changeColor((Shape)node.getChildren().get(0), (Text)node.getChildren().get(1));
								}
							}
							
							// Vanligt klick (utan CTRL)
							if (!(mouseClicked.isControlDown())) {
								if (mouseClicked.getButton().equals(MouseButton.PRIMARY))
									// L�gg till objektet i array f�r linjedragning
									arrowArray[0] = (Form) node;
								if(mouseClicked.getButton().equals(MouseButton.SECONDARY)) {
									// Kolla om klickat objekt inte �r det som redan �r valt
									if (!(node==model.getSelectedObject())) {
										formsList.forEach(n -> {
											// Tag bort markeringen runt objektet som �r valt
											if (n == model.getSelectedObject()) {
												n.setEffect(null);
											}
										});
										arrowList.forEach(a -> {
											if (a == model.getSelectedObject()) {
												a.setEffect(null);
											}
										});
										// Det nya valda objektet f�r en markering
										node.setEffect(new PulsatingShadow());
										model.setSelectedObject(node);
									} else {
										// Om objektet man klickade p� �r valt s� avmarkera det
										node.setEffect(null);
										model.setSelectedObject(null);
									}
								}
							} else {
								// Har man tryckt ned CTRL s� l�ggs objektet till i array
								// och en linje ritas
								arrowArray[1] = (Form) node;
								arrowList.add(model.addArrow(arrowArray[0], arrowArray[1]));
							}
						});
					} catch (Exception e) {}
				});
			}
		});
	}
	
	/** Initierar en meny
	 * 
	 * @return Meny att visa i GUI
	 */
	public MenuBar initMenu() {
		System.out.println("hejsan");
		MenuBar menuBar = new MenuBar();
		
		Menu mnuFile = new Menu("File");
		MenuItem mnuNew = new MenuItem("New");
		mnuNew.setOnAction(mnuAction -> {
			// T�mmer listorna med objekt och pilar
			// och k�r start-metoden igen
			formsList.clear();
			arrowList.clear();
			start(stage);
		});
		
		MenuItem mnuExit = new MenuItem("Exit");
		mnuExit.setOnAction(mnuAction -> {
			Platform.exit();
		});
		mnuFile.getItems().addAll(mnuNew, new SeparatorMenuItem(), mnuExit);
		
		Menu mnuAdd = new Menu("Add Shape");
		MenuItem mnuRect = new MenuItem("Rectangle");
		
		// L�gger till nytt objekt i listan med former
		mnuRect.setOnAction(mnuAction -> {
			formsList.add(model.addRect());
		});
		MenuItem mnuEllipse = new MenuItem("Ellipse");
		mnuEllipse.setOnAction(mnuAction -> {
			formsList.add(model.addEllipse());
		});
		MenuItem mnuDiamond = new MenuItem("Diamond");
		mnuDiamond.setOnAction(mnuAction -> {
			formsList.add(model.addRomb());
		});
		MenuItem mnuText = new MenuItem("Text");
		mnuText.setOnAction(mnuAction -> {
			formsList.add(model.addText());
		});
		mnuAdd.getItems().addAll(mnuRect, mnuEllipse, mnuDiamond, mnuText);
		
		Menu mnuHelp = new Menu("Help");
		Menu mnuHow = new Menu("How do I...");
		
		MenuItem mnuHowAddShape = new MenuItem("...add a shape");
		
		// Visar olika hj�lpf�nster
		mnuHowAddShape.setOnAction(e -> {
			gui.showHelp("addShape");
		});
		
		MenuItem mnuHowAddLine = new MenuItem("...add a line");
		mnuHowAddLine.setOnAction(e -> {
			gui.showHelp("addLine");
		});
		
		MenuItem mnuHowDelete = new MenuItem("...delete an item");
		mnuHowDelete.setOnAction(e -> {
			gui.showHelp("delete");
		});
		
		MenuItem mnuHowChangeColor = new MenuItem("...change color of a shape");
		mnuHowChangeColor.setOnAction(e -> {
			gui.showHelp("changeColor");
		});
		
		mnuHow.getItems().addAll(mnuHowAddShape, mnuHowAddLine, mnuHowDelete, mnuHowChangeColor);
		
		MenuItem mnuAbout = new MenuItem("About");
		
		// Visar om-ruta
		mnuAbout.setOnAction(e -> {
			gui.showAbout();
		});
		
		mnuHelp.getItems().addAll(mnuHow, mnuAbout);
		
		menuBar.getMenus().addAll(mnuFile, mnuAdd, mnuHelp);
		
		return menuBar;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
