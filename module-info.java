/**
 * This module requires the JavaFX graphics and controls libraries for building the
 * GUI. It exports the package to allow access to its classes by the JavaFX runtime.
 * 
 * @author angel
 */
module edu.commonwealthu.alm2696.CMSC230 {
	requires transitive javafx.graphics;   // Allows usage of JavaFX graphics library
	requires javafx.controls;              // Allows usage of JavaFX controls

	exports mod08_OYO to javafx.graphics;  // Exports the package to JavaFX graphics
}
