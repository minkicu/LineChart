/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testlinechartgraphs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 *
 * @author krissada.r
 */
public class Testlinechartgraphs extends Application {
    
final static ObservableList<XYChart.Series<Number, Number>> lineChartData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        Random randomNumbers = new Random();
        ArrayList<Integer> arrayList = new ArrayList<>();

        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        lineChart.setLegendSide(Side.RIGHT);

        // Random Gen 
        /*
        int randomCount = 2;//randomNumbers.nextInt(14)+1;
        //System.out.println("randomCount = " + randomCount);
        for (int i = 0; i < randomCount; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName("series_" + i);
            for (int k = 0; k < 20; k++) {
                int x = randomNumbers.nextInt(50);

                series.getData().add(new XYChart.Data(k, x));
            }
            //seriesList.add(series);
            lineChartData.add(series);
        }
        */
        
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Outward");
        
        getdata(series1,"statdata");
        
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Inhouse");
        
        getdata(series2,"statdata_IH");
        
        lineChartData.add(series1);
        lineChartData.add(series2);
        
       
        lineChart.setData(lineChartData);
        

        final StackPane chartContainer = new StackPane();
        chartContainer.getChildren()
                .add(lineChart);
        zoom zoom = new zoom(lineChart, chartContainer);

        

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(chartContainer);
        //borderPane.setCenter(lineChart);

        borderPane.setBottom(getLegend());
////        
        //Scene scene = new Scene(lineChart, 800, 600);
        Scene scene = new Scene(borderPane, 800, 600);
        //lineChart.getData().addAll(series, series1);

        stage.setScene(scene);
        scene.getStylesheets().addAll("file:///C://fancychart.css");
        //scene.getStylesheets().addAll(getClass().getResource("fancychart.css").toExternalForm());

        stage.show();
    }

    public static Node getLegend() {
        HBox hBox = new HBox();

        for (final XYChart.Series<Number, Number> series : lineChartData) {
            CheckBox checkBox = new CheckBox(series.getName());


            checkBox.setSelected(true);
            checkBox.setOnAction(event -> {
                if (lineChartData.contains(series)) {

                    lineChartData.remove(series);
                } else {
                    lineChartData.add(series);
                }
            });

            hBox.getChildren().add(checkBox);
        }

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-padding: 0 10 20 10");

        return hBox;
    }
    
    public static void getdata(XYChart.Series series,String Table){
        Connection connect = null;
        String connString=null;
                
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        connString = "jdbc:sqlserver://172.30.132.75;DatabaseName=tfb_inreturn;user=su;Password=ncr";
			connect = DriverManager.getConnection(connString);
                        //connect =  DriverManager.getConnection("jdbc:sqlserver://172.30.132.75:1433;DatabaseName=TFB_Image;user=su;Password=ncr");
			if(connect != null){
				//System.out.println("Database Connected.");
                                Statement statement = connect.createStatement();
                                
                                
                                String queryString = "select ProcessingDate,SUM(Chq1_total) Total from "+Table+" where Processingdate > '2015-01-01' group by ProcessingDate order by 1";
                          
                                ResultSet rs = statement.executeQuery(queryString);
                                int i=0;
                                int total=0;
                                while (rs.next()) {
                                    i+=1;
                                    total = Integer.parseInt(rs.getString(rs.findColumn("Total")));
                                    
                                    //System.out.println(i+" ");

                                   series.getData().add(new XYChart.Data(i,total));
                                    //byte[] decodedBytes = decoder.decodeBuffer(imgaeEncode);
                                    
                                    //System.out.println("-- decode --\n"+ new String(decodedBWF, "utf-8") );
                                    
                                    //return new String(decoded, "utf-8");
                                   
                                    
                                }
			} else {
				System.out.println("Database Connect Failed.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
