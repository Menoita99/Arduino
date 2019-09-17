package connection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SerialConnectionDataViewer extends Application implements Initializable{

	private static final int DEFAULT_BAUD_RATE = 9600; 
	private SerialPort port;

	@FXML private HBox connectionsPane;
	@FXML private LineChart<Number, Number> chart;
	@FXML private TextArea console;
	@FXML private Button saveButton;

	private long timeMark;

	private List<String> buffer = Collections.synchronizedList(new ArrayList<>());

	private void initConnection(int connection) {
		port = SerialPort.getCommPorts()[connection];
		port.setComPortParameters(DEFAULT_BAUD_RATE, 8, 1, 0);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		listen();
	}


	public void listen() {
		if(port.openPort()) {
			try(Scanner sc = new Scanner(port.getInputStream())) {
				timeMark = System.currentTimeMillis(); 

				while(sc.hasNext()) {//TODO Redu this to be more universal
					String in = sc.nextLine();
					String consoleText =(in +" : " +((((double)System.currentTimeMillis())/1000)-(((double)timeMark)/1000)));

					buffer.add(consoleText.substring(0,Math.min(11, consoleText.length())));

					Platform.runLater(()->{

						console.appendText(consoleText.substring(0,Math.min(11, consoleText.length()))+"\n");
					});
				}
			}finally {
				if(port.closePort())
					System.out.println("Serial port connection has closed with sucess");
				else {
					System.err.println("Serial port connection failure ");
					return;
				}
			}

		}else {
			System.err.println("Serial port connection failure ");
			return;
		}
	}

	@FXML
	public void save() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("data.txt", true))){//true to append
			for (String data : buffer) 
				writer.append(data+"\n");
			clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void clear() {
		buffer.clear();
		timeMark = System.currentTimeMillis();
		Platform.runLater(()->{
			console.clear();
		});
	}



	@FXML
	public void drawChart() {
		Platform.runLater(()->{
			chart.getXAxis().setAutoRanging(true);
			((NumberAxis) chart.getXAxis()).setForceZeroInRange(false);
			
			chart.getYAxis().setAutoRanging(true);
			((NumberAxis) chart.getYAxis()).setForceZeroInRange(false);
			
			Series<Number, Number> series = new XYChart.Series<Number, Number>();
			
			for (String s : buffer) {
				int value = Integer.parseInt(s.substring(0,4).trim());
				double time = Double.parseDouble(s.substring(6,Math.min(11, s.length())).trim());
				series.getData().add(new XYChart.Data<Number, Number>(time,value));
			}
			
			chart.getData().add(series);
		});
	}

	@FXML
	public void clearCharts() {
		Platform.runLater(()->chart.getData().clear());
	}


	@Override
	public void start(Stage window) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml")); 
		Parent node = loader.load();
		Scene scene = new Scene(node);

		window.setOnCloseRequest(evt -> {
			Platform.exit();
			System.exit(0);
		});

		window.setTitle("Serial SerialConnectionDataViewer");
		window.setScene(scene);
		window.show();

	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int i = 0; i < SerialPort.getCommPorts().length; i++) {
			Button b = new Button(i+" "+SerialPort.getCommPorts()[i]);
			b.setOnAction(actionEvent -> {
				String n = ((Button)actionEvent.getSource()).getText();
				new Thread(()-> initConnection(Integer.parseInt(n.substring(0,1)))).start(); //Initiates the Serial Connection
				connectionsPane.setDisable(true);
			});
			connectionsPane.getChildren().add(b);
		}

	}



	public static void main(String[] args) {
		launch(args);
	}



}
