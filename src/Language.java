import java.sql.*;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Language extends Application{
	
	String language;
	static Connection con = null;
	Stage window;
	Scene scene;
	Scene BackUpScene;
	Group root = new Group();
	ScrollBar sc = new ScrollBar();
	VBox leftPane = new VBox();
	DatabseConnection sql = new DatabseConnection();
	int lang_id ;
	
	//A NEW SCENE IS CREATED FOR EACH LANGUAGE AND STARTED MANUALLY BY INVOKING START METHOD 
	public Language(String language, Stage stage){
		this.language = language;
		window = stage;
		BackUpScene = stage.getScene();
		try {
			start(window);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void start(Stage primaryStage) throws Exception {
	
	//SETTING UP THE STAGE AND SCENE
		window.setTitle("Hackr.io : "+ language);
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
		sc.setMax(3000);
		sc.setPrefWidth(20);
		sc.setPrefHeight(565);
		sc.setOrientation(Orientation.VERTICAL);
	
	//COMPANY LOGO SETUP WHICH WORKS AS A REFERENCE TO HOME PAGE
		Button _block = new Button();
		_block.setStyle("-fx-background-color: white");
		_block.setMinSize(150, 76);
		
		ImageView img = new ImageView(new Image("hackr.jpg"));
		Tooltip imgTip = new Tooltip("Back to Home!");
		imgTip.setStyle("-fx-font:15px Limelight");
		Tooltip.install(img, imgTip);
		
	//LEFT PANE IS THE MAIN VBOX WHERE ALL THE BUTTONS WILL BE PLACED.
		leftPane.getChildren().add(new HBox(_block,img));
		leftPane.setPadding(new Insets(50));
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setSpacing(50);
		
	//GET LANGUAGE ID
		lang_id = sql.getLanguageId(language);
		
		Button block = new Button();
		block.setStyle("-fx-background-color: white");
		block.setMinSize(70, 70);
	
	//EACH LANGUAGE'S LOGO AND NAME COMES UNDER THIS HEADER 
		HBox langHeader = new HBox();
		langHeader.setSpacing(20);
		ImageView langImg;
	//IN CASE NO IMAGE FOR THAT LANGUAGE IS FOUND ADD COMPANY'S ICON
		try{
			Image image = new Image("_Languages/"+language+".png");
			langImg = new ImageView(image);
		}catch(Exception e){
			Image image = new Image("_Languages/hackr.png");
			langImg = new ImageView(image);
		}
		
		Label label1 = new Label(language);
		label1.setTooltip(new Tooltip(language));
		label1.setStyle("-fx-font: 25pt Limelight; -fx-text-fill: #0068A8;");
		Label label2 = new Label("Learn "+language+" from the best online "+language+" tutorials & courses");
		Label label3 = new Label("recommended by the programing communtiy.");
		langHeader.getChildren().addAll(block,langImg,new VBox(label1,label2,label3));
		leftPane.getChildren().add(langHeader);
		
	//FILTER ATTRIBUTES
		CheckBox free = new CheckBox("Free");
		free.setSelected(false);
		CheckBox paid = new CheckBox("Paid");
		paid.setSelected(false);
		CheckBox beginner = new CheckBox("Beginner");
		beginner.setSelected(false);
		CheckBox intermediate = new CheckBox("Intermediate");
		intermediate.setSelected(false);
		CheckBox advanced = new CheckBox("Advanced");
		advanced.setSelected(false);
		Button filter = new Button("Filter");
		HBox box = new HBox(free,paid,beginner,intermediate,advanced);
		VBox choices = new VBox(box,filter);
		box.setSpacing(20);
		choices.setSpacing(20);
		filter.setStyle("-fx-background-color: darksalmon");
		leftPane.getChildren().add(choices);
		
	//FIRST LOAD OF THE PAGE GIVES ALL TUTORIAL BY DEFAULT
		ResultSet r = sql.getDefaultTutorialsList(lang_id);
		@SuppressWarnings("unused")
		Tutorials tuts = new Tutorials(r,leftPane,this);
		
	//HYPERLINK TO HACKR.IO WEBSITE ON THE LOGO
		img.setOnMouseClicked(e -> {	
			window.setScene(BackUpScene);
		});
		
	//EVENT HANDLER TO FILTER BUTTON
		filter.setOnAction(e -> {
			try {
				leftPane.getChildren().clear();
				leftPane.getChildren().addAll(new HBox(_block,img),langHeader,choices);
				ResultSet resSet = sql.getFilteredTutorialsList(lang_id,free.isSelected(),paid.isSelected(),beginner.isSelected(),intermediate.isSelected(),advanced.isSelected());
				@SuppressWarnings("unused")
				Tutorials tut = new Tutorials(resSet,leftPane,this);
			} catch (SQLException e1) { e1.printStackTrace(); }
		});
		
	//THIS CHANGE LISTENER CHANGES THE DISPLAY WITH CHANGE IN THE VALUE OF SCROLLBAR
		sc.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				leftPane.setLayoutY(-newValue.doubleValue());
				
			}
		});	
		window.show();
	}
	
	//HOST THE TUTORIALS TO THEIR RESPECTIVE WEBSITES
	public void hostServices(String url){
		@SuppressWarnings("unused")
		HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
		getHostServices().showDocument(url);
	}
	
}

