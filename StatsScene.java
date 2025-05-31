package mod08_OYO;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * StatsScene is responsible for creating the user interface layout and 
 * handling user interactions for the Array Statistics application.
 * 
 * @author angel
 */
public class StatsScene {

	private StatsArray array;  // Reference to the StatsArray object for calculations

	// UI components
	private Label timeCreateLabel = new Label("Create Time:");
	private Label timePopulateLabel = new Label("Populate Time:");
	private Label timeComputeLabel = new Label("Compute Time:");
	private Label minLabel = new Label("Min:");
	private Label maxLabel = new Label("Max:");
	private Label meanLabel = new Label("Mean:");

	/**
	 * Constructor that initializes the UI and links it with the StatsArray.
	 * 
	 * @param array The StatsArray object to perform operations on
	 */
	public StatsScene(StatsArray array) {
		this.array = array;
	}

	/**
	 * Builds and returns the layout containing the UI components.
	 * 
	 * @return The layout for the user interface
	 */
	public GridPane getLayout() {
		// Labels and text fields for user input
		Label sizeLabel = new Label("Array Size:");
		TextField sizeInput = new TextField();
		Label threadLabel = new Label("Threads:");
		TextField threadInput = new TextField();

		// Buttons for starting the calculation process and clearing the results
		Button startButton = new Button("Start");
		Button clearButton = new Button("Clear");

		// Set up the layout using a grid pane
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));  // Set padding around the grid
		grid.setVgap(10);  // Set vertical gap between elements
		grid.setHgap(10);  // Set horizontal gap between elements

		// Add all UI elements to the grid layout
		grid.add(sizeLabel, 0, 0);
		grid.add(sizeInput, 1, 0);
		grid.add(threadLabel, 0, 1);
		grid.add(threadInput, 1, 1);
		grid.add(startButton, 1, 2);
		grid.add(timeCreateLabel, 0, 3);
		grid.add(timePopulateLabel, 0, 4);
		grid.add(timeComputeLabel, 0, 5);
		grid.add(minLabel, 0, 6);
		grid.add(maxLabel, 0, 7);
		grid.add(meanLabel, 0, 8);
		grid.add(clearButton, 1, 9);

		// Action handler for the Start button
		startButton.setOnAction(e -> {
			handleStart(sizeInput, threadInput);
		});

		// Action handler for the Clear button
		clearButton.setOnAction(e -> {
			handleClear();
		});

		return grid;  // Return the complete layout
	}

	/**
	 * Handles the logic for the Start button to create, populate,
	 * and compute the statistics of the array based on user input.
	 * 
	 * @param sizeInput   The TextField for the array size input
	 * @param threadInput The TextField for the thread count input
	 */
	private void handleStart(TextField sizeInput, TextField threadInput) {
		try {
			int size = Integer.parseInt(sizeInput.getText());
			int threads = Integer.parseInt(threadInput.getText());

			// Validate input
			if (size <= 0 || threads <= 0) {
				showAlert("Input Error", "Enter positive numbers for both array size and thread count.");
				return;
			}

			// Measure times for create, populate, and compute operations
			long startTime = System.currentTimeMillis();
			array.create(size);  // Create the array
			long createTime = System.currentTimeMillis() - startTime;

			startTime = System.currentTimeMillis();
			array.populate(threads);  // Populate the array using multiple threads
			long populateTime = System.currentTimeMillis() - startTime;

			startTime = System.currentTimeMillis();
			array.compute(threads);  // Compute statistics using multiple threads
			long computeTime = System.currentTimeMillis() - startTime;

			// Update the labels with results
			timeCreateLabel.setText("Create Time: " + createTime + " ms");
			timePopulateLabel.setText("Populate Time: " + populateTime + " ms");
			timeComputeLabel.setText("Compute Time: " + computeTime + " ms");
			minLabel.setText("Min: " + array.getMin());
			maxLabel.setText("Max: " + array.getMax());
			meanLabel.setText("Mean: " + array.getMean());

		} catch (NumberFormatException ex) {
			showAlert("Input Error", "Enter valid numbers for array size and thread count.");
		} catch (OutOfMemoryError ex) {
			showAlert("Memory Error", "Array size is too large.");
		}
	}

	/**
	 * Handles the logic for the Clear button to reset the UI labels.
	 */
	private void handleClear() {
		// Reset labels to their initial state
		timeCreateLabel.setText("Create Time:");
		timePopulateLabel.setText("Populate Time:");
		timeComputeLabel.setText("Compute Time:");
		minLabel.setText("Min:");
		maxLabel.setText("Max:");
		meanLabel.setText("Mean:");
	}

	/**
	 * Displays an error alert with the specified title and message.
	 * 
	 * @param title   The title of the alert window
	 * @param message The message to display in the alert window
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
