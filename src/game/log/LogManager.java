
package game.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * A simple logging manager for asynchronous logging to a file.
 * Logs are stored in a blocking queue and written by a single background thread.
 */
public class LogManager {

    /**
     * Starts the logger if not already running.
     * Opens the log file in append mode and launches a background thread to handle writing.
     */
    public static synchronized void startLogger() {
        if (running) return;

        try {
            writer = new BufferedWriter(new FileWriter("logs.txt", true));
        } catch (IOException e) {
            System.err.println(" Failed to open log file: " + e.getMessage());
            return;
        }

        executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Logger-Thread");
            t.setDaemon(true); // Daemon thread ensures it doesn't block JVM shutdown
            return t;
        });

        running = true;
        executor.submit(LogManager::manageWriting);
    }

    /**
     * Stops the logger and shuts down the background writing thread.
     * Flushes any remaining logs and closes the writer.
     */
    public static synchronized void stop() {
        if (!running) return;

        running = false;
        executor.shutdownNow();

        try {
            // מחכים עד שיסיים לעבד את התור (Waits for the queue to finish processing)
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.err.println(" Logger thread did not shut down cleanly.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupted status
        }

        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            System.err.println(" Failed to close log file: " + e.getMessage());
        }
    }

    /**
     * Adds a log message to the queue with a timestamp, if logging is running.
     *
     * @param log The message to log.
     */
    public static void addLog(String log) {
        if (!running) return;
        String timeStamped = "[" + LocalDateTime.now() + "] " + log;
        queue.offer(timeStamped);
    }

    /**
     * Background logging thread that continuously polls the queue and writes logs to the file.
     * Ensures each message is flushed after writing.
     */
    private static void manageWriting() {
        try {
            while (running || !queue.isEmpty()) {
                String log = queue.poll(1, TimeUnit.SECONDS);
                if (log != null && writer != null) {
                    writer.write(log);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (InterruptedException ignored) {
            // Thread was interrupted, exit gracefully
        } catch (IOException e) {
            System.err.println(" Error while writing to log: " + e.getMessage());
        }
    }
    // --- Fields ---
    /** Queue to store log messages before writing. */
    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    /** Single-threaded executor for writing logs. */
    private static ExecutorService executor;

    /** Buffered writer for the log file. */
    private static BufferedWriter writer;

    /** Indicates if the logging system is currently running. */
    private static volatile boolean running = false;
}
