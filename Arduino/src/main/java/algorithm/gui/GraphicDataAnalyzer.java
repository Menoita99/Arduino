package algorithm.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * @author RuiMenoita
 *
 *This class has made to help to visualise data throw time using linear char.
 *On next versions may have the option to choose the chart type and zoom in/out.
 *
 *Helpful to visualise data (e.g Arduino sensor data) it takes a txt file ("*.txt")
 *that has a certain data inside with the format:
 *
 *number : number
 *
 *e.g
 *
 *512 : 0.25
 *521 : 1.36
 *5235 : 210.3362
 */


public class GraphicDataAnalyzer extends Application{

	@FXML private HBox checkBoxPanel;
	@FXML private HBox chartPanel;

	private Stage window;

	private int chartId = 0;



	/**
	 * This method selects a txt file that was the correct format
	 * otherwise it will display a error dialog informing user that was 
	 * Occurred some kind of error. 
	 * 
	 * Correct format:
	 * 
	 *e.g
	 *
	 *512 : 0.25
	 *521 : 1.36
	 *5235 : 210.3362
	 **/

	@FXML
	public void addChart(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

		File selectedFile = fc.showOpenDialog(null);

		if(selectedFile.getName().matches(".+\\.txt")) {

			try(Scanner s = new Scanner(selectedFile)){

				List<String> strings = new ArrayList<>();
				boolean error = false;	

				while(s.hasNextLine()) {
					String line = s.nextLine();

					if(line.matches("\\d{0,4}( : |:)(\\d|.)+")) {

						String[] splited = line.split(":");

						for (int i = 0; i < splited.length; i++) 
							if(!splited[i].trim().matches(""))
								strings.add(splited[i].trim());

					}else {
						showErrorDialog("Wrong txt fromat");
						error = true;
						break;
					}
				}

				if(!error) drawGraph(strings,selectedFile.getName());

			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}

		}else  showErrorDialog("Invalid file");
	}


	/**
	 * @param strings   data to be add to chart (Must be in the correct format)
	 * @param fileName	chart title
	 * 
	 * This method add a LinearChart<Number,Number> to charPanel
	 * with a title having the chart id plus the filename
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void drawGraph(List<String> strings, String fileName) {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();

		xAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);

		LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		lineChart.setTitle(chartId + " "+fileName);

		XYChart.Series series = new XYChart.Series();

		double value = 0;
		double time;

		for (int i = 0; i<strings.size(); i++) {

			if(i%2==0)	value = Double.parseDouble(strings.get(i).trim());
			else {		
				time = Double.parseDouble(strings.get(i).trim());
				series.getData().add(new XYChart.Data<Number, Number>(time,value));
			}
		}

		lineChart.getData().add(series);

		checkBoxPanel.getChildren().add(new CheckBox(chartId+" "+fileName));
		chartPanel.getChildren().add(new ScrollPane(lineChart));
		chartId++;
	}


	/**
	 * Displays a error dialog with  @param error content
	 */
	private void showErrorDialog(String error) {
		Platform.runLater(() -> {
			Alert dialog = new Alert(AlertType.ERROR);
			dialog.setTitle("Error");
			dialog.setContentText(error);
			dialog.initOwner(window);
			dialog.show();
		});
	}


	/**
	 * Opens a new Window of GDA 
	 */
	@FXML
	public void openNewWindow(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml")); 
			Parent node = loader.load();
			Scene scene = new Scene(node);
			Stage stage = new Stage();

			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Removes all the selected charts on chackBoxPanel 
	 */
	@FXML
	public void removeChart(ActionEvent event) {

	}


	/**
	 * Loads and displays Scene.fxml file
	 */
	@Override
	public void start(Stage stage) throws Exception {
		window = stage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml")); 
		Parent node = loader.load();
		Scene scene = new Scene(node);

		window.setScene(scene);
		window.show();
	}



	public static void main(String[] args) {
		GraphicDataAnalyzer.launch(args);
	}



}
