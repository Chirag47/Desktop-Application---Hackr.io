import java.sql.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Tutorials {

	int noOfTuts;
	Tutorials(ResultSet _resSet,VBox _vbox,Language l){
	
		//NO OF ROWS RETURNED
		try {
			_resSet.beforeFirst();
			_resSet.last();
			noOfTuts = _resSet.getRow();
			_resSet.beforeFirst();
		//ADDS THE DETAILS OF RETURNED TUTORIALS
			while(_resSet.next()){
			
				VBox vbox1 = new VBox();
		
		//mainFrame HBOX DEFINES THE ENTIRITY OF EACH TUTORIAL WHICH IS THEN COVERED BY VBOX vbox1
				HBox mainFrame = new HBox();
				HBox hbox = new HBox();
		
		//URL IS THE LINK OF EACH TUTORIAL ON THE WEBSITE
				String url = _resSet.getObject(3).toString();
				Label tutName = new Label(_resSet.getObject(2).toString());
				tutName.setStyle("-fx-font: 20 Lato-Bold ");
				tutName.setOnMouseClicked(e -> l.hostServices(url));
				Label refSite = new Label("("+_resSet.getObject(10).toString()+")");
				Label submitterName = new Label("Submitted by " + _resSet.getObject(12).toString());
				submitterName.setStyle("-fx-text-fill: green");
				hbox.getChildren().addAll(refSite,submitterName);
		
		//ADDING THE ADDITIONAL INFO ON EACH TUTORIAL (FREE/PAID OR LEVEL OF TUTORIAL)
				if(_resSet.getObject(5).toString() == "true"){
					Button costButton = new Button("Free");
					hbox.getChildren().add(costButton);
					costButton.setStyle("-fx-background-color: darksalmon");
				}
				else if(_resSet.getObject(6).toString() == "true"){
					Button costButton = new Button("Paid");
					hbox.getChildren().add(costButton);
					costButton.setStyle("-fx-background-color: darksalmon");
				}
				if(_resSet.getObject(7).toString() == "true"){
					Button levelButton = new Button("Beginner");
					hbox.getChildren().add(levelButton);
					levelButton.setStyle("-fx-background-color: darksalmon");
				}
				else if(_resSet.getObject(8).toString() == "true"){
					Button levelButton = new Button("Intermediate");
					hbox.getChildren().add(levelButton);
					levelButton.setStyle("-fx-background-color: darksalmon");
				}
				else if(_resSet.getObject(9).toString() == "true"){
					Button levelButton = new Button("Advance");
					levelButton.setStyle("-fx-background-color: darksalmon");
					hbox.getChildren().add(levelButton);
				}
				Button typeButton;
				String type = _resSet.getObject(11).toString();
				if(type.length() != 0){
					typeButton = new Button(type);
					typeButton.setStyle("-fx-background-color: darksalmon");
					hbox.getChildren().add(typeButton);
				}
				
				hbox.setSpacing(20);
				hbox.maxWidth(500);
				
				vbox1.setSpacing(10);
				vbox1.getChildren().addAll(tutName,hbox);
				Button block = new Button();
				block.setStyle("-fx-background-color: #00C1E0");
				block.setMinSize(80, 70);
				mainFrame.getChildren().addAll(block,vbox1);
				mainFrame.setSpacing(50);
				mainFrame.setId("hbox:mainFrame");
				_vbox.getChildren().addAll(mainFrame);
				
			}
			
		//MESSAGE DISPLAY IN CASE OF NO TUTORIALS YET UPDATED
			if(noOfTuts == 0){
				Label label = new Label("No results found!");
				label.setStyle("-fx-font: 20px Lato-Light");
				_vbox.getChildren().add(label);
			}
			
		} catch (SQLException e) {e.printStackTrace();}				
		
	}
	
}






















