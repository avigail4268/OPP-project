package game.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class LogManager {
    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static ExecutorService executor;
    private static BufferedWriter writer;
    private static volatile boolean running = false;


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
            t.setDaemon(true);
            return t;
        });

        running = true;
        executor.submit(LogManager::manageWriting);
    }

    // עצירת המערכת
    public static synchronized void stop() {
        if (!running) return;

        running = false;
        executor.shutdownNow();

        try {
            // מחכים עד שיסיים לעבד את התור
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.err.println(" Logger thread did not shut down cleanly.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            System.err.println(" Failed to close log file: " + e.getMessage());
        }
    }

    // הוספת לוג לתור
    public static void addLog(String log) {
        if (!running) return;
        String timeStamped = "[" + LocalDateTime.now() + "] " + log;
        queue.offer(timeStamped);
    }

    // Thread הרישום עצמו
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
        } catch (IOException e) {
            System.err.println(" Error while writing to log: " + e.getMessage());
        }
    }
}
