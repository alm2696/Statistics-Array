package mod08_OYO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The App provides a user interface for creating, populating, and computing statistics of an integer array.
 * Users can specify the array size and the number of threads. The application displays elapsed time for 
 * array creation, population, and computation of statistics such as minimum, maximum, and mean values.
 * 
 * @author angel
 */
public class App extends Application {

	private StatsArray array = new StatsArray();  // Instance of StatsArray to manage array operations

	/**
	 * Main method that launches the application.
	 * 
	 * @param args Command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		launch(args);  // Launch the JavaFX application
	}

	/**
	 * Starts the application and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for the application
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Array Statistics");  // Set the window title

		// Create an instance of the StatsScene to handle the UI
		StatsScene view = new StatsScene(array);

		// Set up the scene with the UI layout from StatsScene
		Scene scene = new Scene(view.getLayout(), 400, 350);
		primaryStage.setScene(scene);
		primaryStage.show();  // Display the window
	}
}
