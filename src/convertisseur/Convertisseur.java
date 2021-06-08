package convertisseur;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Convertisseur extends Application {
	static Stage mainStage;
	
	public void start(Stage stage) throws IOException {
		mainStage = stage;
		FXMLLoader loader = new FXMLLoader();
		URL fxmlFileUrl = getClass().getResource("convertisseur.fxml");
		if (fxmlFileUrl == null) {
			System.out.println("Impossible de charger le fichier fxml");
			System.exit(-1);
		}
		loader.setLocation(fxmlFileUrl);
		Parent root = loader.load();

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Convertisseur de couleurs par Maxime Bimont et Théo Ernould");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
