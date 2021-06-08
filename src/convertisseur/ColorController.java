package convertisseur;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class ColorController implements Initializable {
	@FXML FlowPane couleurs;
	@FXML Button creer;

	@FXML Circle preview;
	@FXML Slider barre;
	
	@FXML Circle rouge;
	@FXML Circle vert;
	@FXML Circle bleu;
	
	@FXML TextField rouge_entree;
	@FXML TextField vert_entree;
	@FXML TextField bleu_entree;
	@FXML TextField hex_entree;

	@FXML FlowPane noirblanc;
	@FXML Button exporter;
	@FXML Button importer;
	
	@FXML HBox propositions;
	
	Rectangle rectActuel;
	Rectangle rectNoirActuel;
	
	String colorSelected;
	
	
	
	public void creerAction(Event e) {
		barre.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        		double value = barre.getValue() * 2.55;
        		if(colorSelected == "rouge") {
        			rouge_entree.setText("" + (int) Math.ceil(value));
        		} else if(colorSelected == "vert") {
        			vert_entree.setText("" + (int) Math.ceil(value));
        		} else if(colorSelected == "bleu") {
        			bleu_entree.setText("" + (int) Math.ceil(value));
        		}
        		int red = Integer.parseInt(rouge_entree.getText());
        		int green = Integer.parseInt(vert_entree.getText());
        		int blue = Integer.parseInt(bleu_entree.getText());
        		Color newColor = Color.rgb(red, green, blue);
        		preview.setFill(newColor);
				hex_entree.setText("" + preview.getFill());
        		rectActuel.setFill(newColor);
        		int somme = (int) (red * 0.3) + (int) (green * 0.59) + (int) (blue * 0.11);
        		Color grayColor = Color.rgb(somme, somme, somme);
        		rectNoirActuel.setFill(grayColor);
            }
        });
		if(couleurs.getChildren().size() < 21) {
			Rectangle rect = new Rectangle(65,65,Color.WHITE);
				rect.setStroke(Color.BLACK);
				rect.setStrokeWidth(1);
			Rectangle rectnoir = new Rectangle(45,45,Color.WHITE);
				
				rect.setOnMouseClicked(ev -> {
					Rectangle r = (Rectangle) ev.getSource();
					if(ev.getButton() == MouseButton.PRIMARY) {
						rectActuel = r;
						rectNoirActuel = rectnoir;
						Color color = (Color) r.getFill();
						preview.setFill(color);
						rouge_entree.setText("" + (int) (color.getRed()*255));
						vert_entree.setText("" + (int) (color.getGreen()*255));
						bleu_entree.setText("" + (int) (color.getBlue()*255));
						hex_entree.setText("" + r.getFill());
						
						if(colorSelected == "rouge") {
							barre.setValue(color.getRed()*100);
						} else if(colorSelected == "vert") {
							barre.setValue(color.getGreen()*100);
						} else if(colorSelected == "bleu") {
							barre.setValue(color.getBlue()*100);
						}
						
					} else if(ev.getButton() == MouseButton.SECONDARY) {
						noirblanc.getChildren().remove(rectnoir);
						couleurs.getChildren().remove(r);
						if(couleurs.getChildren().size() < 21) creer.setVisible(true);
					}
				});
				
			couleurs.getChildren().add(couleurs.getChildren().size()-1, rect);
			noirblanc.getChildren().add(rectnoir);
			if(couleurs.getChildren().size() == 21) creer.setVisible(false);
		}
	}
	
	public void generer() {
		
		propositions.getChildren().clear();
		
		Color[] colors = new Color[5];
		
		for(int i=0;i<5;i++) {
			int newR,newG,newB;
			boolean different = true;
			do {
				newR = random(0,256);
				newG = random(0,256);
				newB = random(0,256);
        		int newNuance = (int) (newR * 0.3) + (int) (newG * 0.59) + (int) (newB * 0.11);
        		boolean tempDifferent = true;
				for(Node rectangleNode : noirblanc.getChildren()) {
					if(tempDifferent) {
						Rectangle rectangle = (Rectangle) rectangleNode;
						Color rectColor = (Color) rectangle.getFill();
		        		int rectNuance = (int) (rectColor.getRed()*255 * 0.3) + (int) (rectColor.getGreen()*255 * 0.59) + (int) (rectColor.getBlue()*255 * 0.11);
		        		tempDifferent = Math.abs(rectNuance - newNuance) >= 12;
					}
				}
				different = tempDifferent;
			} while(!different);
			Color newColor = Color.rgb(newR, newG, newB);
			colors[i] = newColor;
			Rectangle newRectangle = new Rectangle(65,65);
				newRectangle.setFill(newColor);
				newRectangle.setOnMouseClicked(e -> {
					Color color = (Color) ((Rectangle) e.getSource()).getFill();
					if(rectActuel != null) {
						rectActuel.setFill(color);
						int r = (int) (color.getRed()*255);
						int g = (int) (color.getGreen()*255);
						int b = (int) (color.getBlue()*255);
		        		int newNuance = (int) (r * 0.3) + (int) (g * 0.59) + (int) (b * 0.11);
		        		rectNoirActuel.setFill(Color.rgb(newNuance, newNuance, newNuance));
		        		preview.setFill(color);
		        		rouge_entree.setText("" + r);
		        		vert_entree.setText("" + g);
		        		bleu_entree.setText("" + b);
		        		if(colorSelected == "rouge") barre.setValue(r/2.55);
		        		else if(colorSelected == "vert") barre.setValue(g/2.55);
		        		else if(colorSelected == "bleu") barre.setValue(b/2.55);
		        		hex_entree.setText("" + preview.getFill());
					}
				});
			propositions.getChildren().add(newRectangle);
		}
		
		propositions.getChildren().add(new Separator(Orientation.VERTICAL));

		for(int i=0;i<5;i++) {
			int rectNuance = (int) (colors[i].getRed()*255 * 0.3) + (int) (colors[i].getGreen()*255 * 0.59) + (int) (colors[i].getBlue()*255 * 0.11);
			Color nuanceColor = Color.rgb(rectNuance, rectNuance, rectNuance);
			Rectangle newRectangle = new Rectangle(65,65);
				newRectangle.setFill(nuanceColor);
			propositions.getChildren().add(newRectangle);
		}
	}
	
	private int random(int inf, int sup) {
		return (int) ((Math.random() * (sup - inf)) + inf);
	}
	
	public void exporterAction(Event e) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer les couleurs");
		fileChooser.setInitialFileName("couleurs.txt");
		File saveFile = fileChooser.showSaveDialog(Convertisseur.mainStage);
		if(saveFile != null) {
			saveFile.createNewFile();
			PrintWriter saveFileWriter = new PrintWriter(saveFile);
			for(Node node : couleurs.getChildren()) {
				if(node instanceof Rectangle) {
					Rectangle rect = (Rectangle) node;
					Color c = (Color) rect.getFill();
					saveFileWriter.println("r:" + (int) (c.getRed()*255) + ";g:" + (int) (c.getGreen()*255) + ";b:" + (int) (c.getBlue()*255) + ";hex:" + rect.getFill());
				}
			}
			saveFileWriter.close();
		}
	}
	
	public void rouge_change(){
		if(rectActuel != null) refreshByEntries();
	}
	
	private void refreshByEntries() {
		int r,g,b;
		try {
			r = Integer.parseInt(rouge_entree.getText());
			if(r < 0 || r > 255) {
				r = 0;
				rouge_entree.setText("0");
			}
		} catch(NumberFormatException e) {
			r = 0;
			rouge_entree.setText("0");
		}
		try {
			g = Integer.parseInt(vert_entree.getText());
			if(g < 0 || g > 255) {
				g = 0;
				vert_entree.setText("0");
			}
		} catch(NumberFormatException e) {
			g = 0;
			vert_entree.setText("0");
		}
		try {
			b = Integer.parseInt(bleu_entree.getText());
			if(b < 0 || b > 255) {
				b = 0;
				bleu_entree.setText("0");
			}
		} catch(NumberFormatException e) {
			b = 0;
			bleu_entree.setText("0");
		}
		if(colorSelected == "rouge") barre.setValue(r/2.55);
		else if(colorSelected == "vert") barre.setValue(g/2.55);
			else if(colorSelected == "bleu") barre.setValue(b/2.55);
		Color color = Color.rgb(r,g,b);
		preview.setFill(color);
		rectActuel.setFill(color);
		int somme = (int) (r * 0.3) + (int) (g * 0.59) + (int) (b * 0.11);
		Color grayColor = Color.rgb(somme, somme, somme);
		rectNoirActuel.setFill(grayColor);
		hex_entree.setText("" + preview.getFill());
	}
	
	public void rouge_clicked(Event e){
		rouge.setStrokeWidth(3);
		vert.setStrokeWidth(1);
		bleu.setStrokeWidth(1);
		
		colorSelected = "rouge";
		
		if(rectActuel != null) {
			barre.setValue(((Color) rectActuel.getFill()).getRed() * 100);
		}
		
	}
	
	public void vert_change(){
		if(rectActuel != null) refreshByEntries();
	}
	
	public void vert_clicked(Event e){
		vert.setStrokeWidth(3);
		rouge.setStrokeWidth(1);
		bleu.setStrokeWidth(1);

		colorSelected = "vert";
		
		if(rectActuel != null) {
			barre.setValue(((Color) rectActuel.getFill()).getGreen() * 100);
		}
	}
	
	public void bleu_change(){
		if(rectActuel != null) refreshByEntries();
	}
	public void bleu_clicked(Event e){
		bleu.setStrokeWidth(3);
		vert.setStrokeWidth(1);
		rouge.setStrokeWidth(1);

		colorSelected = "bleu";
		
		if(rectActuel != null) {
			barre.setValue(((Color) rectActuel.getFill()).getBlue() * 100);
		}
		
	}
	

	public void hex_change(){
		String hexStr = hex_entree.getText();
		Color color = Color.rgb(0, 0, 0);
		try {
			color = Color.web(hexStr);
		} catch(IllegalArgumentException e) {
			
		}
		int r = (int) color.getRed() * 255;
		int g = (int) color.getGreen() * 255;
		int b = (int) color.getBlue() * 255;
		rouge_entree.setText("" + r);
		vert_entree.setText("" + g);
		bleu_entree.setText("" + b);
		refreshByEntries();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rouge.setStrokeWidth(3);
		vert.setStrokeWidth(1);
		bleu.setStrokeWidth(1);
		colorSelected = "rouge";
	}
}
