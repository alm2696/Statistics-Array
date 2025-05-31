package mod08_OYO;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The StatsArray class that handles the creation, 
 * population, and computation of statistics for 
 * an array of integers using multi-threading.
 * 
 * @author angel
 */
public class StatsArray {
	private int[] array;  // Array to hold the integer values
	private int min;      // Minimum value found in the array
	private int max;      // Maximum value found in the array
	private double mean;  // Mean value of the array
	private final Lock lock = new ReentrantLock();  // Lock to ensure thread safety during shared resource access

	/**
	 * Creates an array of the specified size.
	 *
	 * @param size              The size of the array to create
	 * @throws OutOfMemoryError If there is insufficient memory to create the array
	 */
	public void create(int size) throws OutOfMemoryError {
		array = new int[size];  // Initialize the array with the specified size
	}

	/**
	 * Populates the array with random integers using multiple threads.
	 * Each thread is responsible for populating a segment of the array.
	 *
	 * @param numThreads The number of threads to use for population
	 */
	public void populate(int numThreads) {
		int length = array.length;
		Thread[] threads = new Thread[numThreads];  // Create an array to hold the threads

		// Create and start threads for populating different segments of the array
		for (int i = 0; i < numThreads; i++) {
			int start = i * (length / numThreads);  // Calculate the start index for this thread
			int end = (i == numThreads - 1) ? length : (i + 1) * (length / numThreads);  // Calculate the end index
			threads[i] = new Thread(new PopulateTask(start, end));  // Create a new thread for this segment
			threads[i].start();  // Start the thread
		}

		// Wait for all threads to finish populating the array
		for (Thread thread : threads) {
			try {
				thread.join();  // Join the threads to ensure they complete before proceeding
			} catch (InterruptedException e) {
				e.printStackTrace();  // Handle interruption
			}
		}
	}

	/**
	 * Computes the minimum, maximum, and mean of the array using multiple 
	 * threads. Each thread computes the statistics for a segment of the array, 
	 * and the results are combined to obtain the final min, max, and mean values.
	 *
	 * @param numThreads The number of threads to use for computation
	 */
	public void compute(int numThreads) {
		int length = array.length;
		min = Integer.MAX_VALUE;  // Initialize min to the highest integer value
		max = Integer.MIN_VALUE;  // Initialize max to the lowest integer value
		mean = 0;  // Initialize mean to zero

		Thread[] threads = new Thread[numThreads];  // Create an array to hold the threads

		// Create and start threads for computing the statistics on different segments of the array
		for (int i = 0; i < numThreads; i++) {
			int start = i * (length / numThreads);  // Calculate the start index for this thread
			int end = (i == numThreads - 1) ? length : (i + 1) * (length / numThreads);  // Calculate the end index
			threads[i] = new Thread(new ComputeTask(start, end));  // Create a new thread for this segment
			threads[i].start();  // Start the thread
		}

		// Wait for all threads to finish computing the statistics
		for (Thread thread : threads) {
			try {
				thread.join();  // Join the threads to ensure they complete before proceeding
			} catch (InterruptedException e) {
				e.printStackTrace();  // Handle interruption
			}
		}

		mean /= array.length;  // Calculate the mean by dividing the accumulated sum by the array length
	}

	// Setters for min, max, and mean
	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) { 
		this.max = max; 
	}

	public void setMean(double mean) {
		this.mean = mean; 
	}

	// Getters for the computed min, max, and mean values
	public int getMin() { 
		return min; 	
	}

	public int getMax() { 
		return max; 
	}

	public double getMean() { 
		return mean; 
	}

	/**
	 * This class handles the population of a segment of the array with random integers.
	 * This class is executed by each thread to populate a portion of the array.
	 */
	private class PopulateTask implements Runnable {
		private int start;  // The start index for this thread's portion of the array
		private int end;    // The end index for this thread's portion of the array
		private Random rand = new Random();  // Random number generator to populate the array

		/**
		 * Constructor to initialize the task with the start and end indices for array population.
		 *
		 * @param start The starting index of the array segment to populate
		 * @param end   The ending index of the array segment to populate
		 */
		public PopulateTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			// Populate the array segment with random integers
			for (int i = start; i < end; i++) {
				array[i] = rand.nextInt();  // Assign a random integer to each position in the array
			}
		}
	}

	/**
	 * This class handles the computation of statistics for a segment of the array. This
	 * class is executed by each thread to compute the statistics for a portion of the array.
	 */
	private class ComputeTask implements Runnable {
		private int start;  // The start index for this thread's portion of the array
		private int end;    // The end index for this thread's portion of the array

		/**
		 * Constructor to initialize the task with the start and end indices for statistic computation.
		 *
		 * @param start The starting index of the array segment to compute statistics for
		 * @param end   The ending index of the array segment to compute statistics for
		 */
		public ComputeTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			int localMin = Integer.MAX_VALUE;  // Local minimum value for this thread's portion of the array
			int localMax = Integer.MIN_VALUE;  // Local maximum value for this thread's portion of the array
			long localSum = 0;  // Local sum for calculating the mean

			// Iterate over the assigned segment of the array
			for (int i = start; i < end; i++) {
				localMin = Math.min(localMin, array[i]);  // Update local minimum
				localMax = Math.max(localMax, array[i]);  // Update local maximum
				localSum += array[i];  // Accumulate sum for mean calculation
			}

			lock.lock();  // Lock the critical section to ensure thread safety when updating shared variables
			try {
				min = Math.min(min, localMin);  // Update global minimum with the local minimum
				max = Math.max(max, localMax);  // Update global maximum with the local maximum
				mean += localSum;  // Accumulate the sum for the global mean
			} finally {
				lock.unlock();  // Ensure the lock is released after the update
			}
		}
	}
}
