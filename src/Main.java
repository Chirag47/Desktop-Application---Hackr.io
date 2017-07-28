import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

import java.sql.*;

public class Main extends Application{


	Stage window;
	Scene scene;
	boolean hasNext = true;
	GridPane grid = new GridPane();
	Button button;
	LinkedList<Button> list = new LinkedList<>();
	ScrollBar sc = new ScrollBar();
	VBox leftPane = new VBox();
	Group root = new Group();
	Connection con = null;
	DatabseConnection sql = new DatabseConnection();
	
	
	public static void main(String args[]){
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		window.setTitle("Hackr.io");
		window.getIcons().add(new Image("icon.png"));
		window.setMaxHeight(600);
		window.setMaxWidth(800);
		
		scene= new Scene(root,800,600);
		scene.getStylesheets().add("Zypher.css");
		window.setScene(scene);
		window.setResizable(false);
		root.getChildren().addAll(leftPane,sc);
		
		//SCROLLBAR SETTINGS
		sc.setMin(0);
		sc.setMax(500);
		sc.setPrefWidth(20);
		sc.setPrefHeight(565);
		sc.setOrientation(Orientation.VERTICAL);
		
		Button _block = new Button();
		_block.setStyle("-fx-background-color: white");
		_block.setMinSize(150, 76);
		
		ImageView img = new ImageView(new Image("hackr.jpg"));
		
		//LEFT PANE IS THE MAIN VBOX WHERE ALL THE BUTTONS WILL BE PLACED.
		leftPane.getChildren().add(new HBox(_block,img));
		leftPane.setPadding(new Insets(50));
		leftPane.setAlignment(Pos.BASELINE_CENTER);
		leftPane.setSpacing(50);
		
		//BUTTONS ALIGNED IN A GRID
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(20);
		grid.setHgap(20);
				
		HBox centre = new HBox(grid);
		leftPane.getChildren().add(centre);
	    
		//CONNECTION TO THE DATABASE
		CreateConnection();
		
		//GEGTTING THE LIST OF ALL THE LANGUAGES FOR WHICH TUTORIALS ARE AVAILABLE
		ResultSet r  = sql.getAllLanguages();
		
		//NO OF ROWS RETURNED
		r.beforeFirst();
		r.last();
		int temp = r.getRow() /4;
		r.beforeFirst();

		//DISPLAY FORMATTING
		for(int i=0; i<=temp;i++){
			for(int j=0; j<4; j++){
				if(r.next()){
					button = new Button(r.getObject(2).toString());
					button.setPrefSize(140, 30);
					Tooltip buttonTip = new Tooltip(button.getText());
					buttonTip.setStyle("-fx-font: 12pt Lato-Bold");
					button.setTooltip(buttonTip);
					list.add(button);
					GridPane.setConstraints(button,j,i);
					grid.getChildren().add(button);
					
				}
				else{
					hasNext = false;
					break;
				}
			}
		}

		//HYPERLINK TO HACKR.IO WEBSITE ON THE LOGO
		img.setOnMouseClicked(e -> {
			
			hostServices("https://hackr.io/?ref=chiragagrawal");
		});
	
		/*
		 * EVENT HANDLERS FOR EACH BUTTON
		 * SINCE ALL BUTTONS HAVE SIMILAR EVENTS, THEREFORE A LIST OF BUTTONS AND SAME EVENT HANLDER FOR ALL OF THEM.
		 */
		int i = list.size() - 1;
		while(i>=0){
			Button b = list.get(i);
			b.setOnAction(e -> {
				@SuppressWarnings("unused")
				Language object = new Language(b.getText(), window);
			});
			i--;
		}
	
/*		//MOUSE SCROLL EVENT
		root.setOnScroll(e -> {
			root.setTranslateX(root.getTranslateX() + e.getDeltaX());
			root.setTranslateY(root.getTranslateY() + e.getDeltaY());
		});
*/		
		
		//THIS CHANGE LISTENER CHANGES THE DISPLAY WITH CHANGE IN THE VALUE OF SCROLLBAR
		sc.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				leftPane.setLayoutY(-newValue.doubleValue());
				
			}
		});

		window.show();
		
	}

	public void CreateConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/qrious","root","A@#$lll123");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void hostServices(String url){
		@SuppressWarnings("unused")
		HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
		getHostServices().showDocument(url);
	}
}
