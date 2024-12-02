import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create axes
        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Processes");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        // Create chart
        StackedBarChart<Number, String> chart = new StackedBarChart<>(xAxis, yAxis);
        chart.setTitle("CPU Scheduling Graph");

        // Sample data
        String[] processes = {"Process1", "Process2", "Process3", "Process4", "Process5"};
        Color[] colors = {Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PURPLE};

        // Add data to chart
        for (int i = 0; i < processes.length; i++) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series.setName(processes[i]);

            // Add sample blocks for the process
            for (int j = 0; j < 5; j++) {
                int startTime = j * 20;
                int duration = 15;
                series.getData().add(new XYChart.Data<>(startTime, processes[i], duration));
            }
            chart.getData().add(series);
        }

        // Styling
        chart.setStyle("-fx-bar-fill: derive(-fx-color, 50%);");

        // Display statistics at the bottom
        Text statistics = new Text("Statistics:\nAWT: 3125\nATA: 12331\nSchedule Name: ABC Schedule");
        statistics.setFill(Color.RED);
        statistics.setStyle("-fx-font-size: 14px;");

        // Layout
        BorderPane layout = new BorderPane();
        layout.setCenter(chart);
        layout.setBottom(statistics);

        // Create scene
        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setTitle("CPU Scheduling Graph");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
