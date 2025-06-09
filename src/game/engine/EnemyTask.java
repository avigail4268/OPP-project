
package game.engine;

import game.characters.Enemy;
import game.characters.EnemyFactory;
import game.characters.PlayerCharacter;
import game.log.LogManager;
import game.map.Position;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Runnable task that defines the behavior of an enemy character in the game world.
 * The enemy either moves randomly or towards the player, depending on proximity and chance.
 */
public class EnemyTask implements Runnable {

    /**
     * Constructs an EnemyTask with the specified enemy and game world context.
     * @param enemy      The enemy to control.
     * @param gameWorld  The game world in which the enemy exists.
     */
    public EnemyTask(Enemy enemy, GameWorld gameWorld) {
        this.enemy = enemy;
        this.gameWorld = gameWorld;
    }

    /**
     * Main logic executed when the task runs.
     * If the enemy is alive, it will attempt to move toward the player if within range,
     * or move randomly with a 20% chance otherwise.
     */
    public void run() {
        if (!gameWorld.getIsGameRunning().get()) return;

        if (enemy.isDead()) {
            Position pos = enemy.getPosition();
            ReentrantLock lock = GameWorld.getMapLock(pos);
            lock.unlock();
            try {
                gameWorld.getEnemies().remove(enemy);
                if (gameWorld.getEnemies().size() < 10) {
                    Position newPos = gameWorld.getMap().getRandomEmptyPosition();
                    ReentrantLock newLock = GameWorld.getMapLock(newPos);
                    try {
                        newLock.lock();
                        EnemyFactory enemyFactory = new EnemyFactory();
                        Enemy newEnemy = enemyFactory.createEnemy(newPos);
                        gameWorld.getEnemies().add(newEnemy);
                        EnemyTask task = new EnemyTask(newEnemy, gameWorld);
                        gameWorld.getEnemyTasks().add(task);
                        gameWorld.getEnemyExecutor().submit(task);
                    }

                }

            } finally {
                lock.unlock();
            }
            return;
        }

        LogManager.addLog("Enemy moved to: " + enemy.getPosition());
        PlayerCharacter player = gameWorld.getPlayer();
        Position enemyPos = enemy.getPosition();
        Position playerPos = player.getPosition();

        // If player is close, move toward them. Else, move randomly with 20% chance.
        if (enemyPos.distanceTo(playerPos) <= 2) {
            moveTowards(playerPos);
        } else if (random.nextDouble() <= 0.2) {
            moveRandomly();
        }

        try {
            Thread.sleep(300); // 300 milliseconds pause between enemy actions
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted status
        }
    }

    /**
     * Moves the enemy one tile toward the target position .
     * @param targetPos The position to move towards.
     */
    private void moveTowards(Position targetPos) {
        Position currentPos = enemy.getPosition();
        int rowDiff = targetPos.getRow() - currentPos.getRow();
        int colDiff = targetPos.getCol() - currentPos.getCol();

        Position newPos;

        if (Math.abs(rowDiff) > 0) {
            int dx = Integer.compare(rowDiff, 0);
            newPos = new Position(currentPos.getRow() + dx, currentPos.getCol());
        } else if (Math.abs(colDiff) > 0) {
            int dy = Integer.compare(colDiff, 0);
            newPos = new Position(currentPos.getRow(), currentPos.getCol() + dy);
        } else {
            return; // Already at target position.
        }

        attemptMove(newPos);
    }

    /**
     * Moves the enemy one tile in a random direction .
     */
    private void moveRandomly() {
        Position currentPos = enemy.getPosition();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] dir = directions[random.nextInt(directions.length)];
        Position newPos = new Position(currentPos.getRow() + dir[0], currentPos.getCol() + dir[1]);

        attemptMove(newPos);
    }

    /**
     * Attempts to move the enemy to the specified position.
     * If the destination is within bounds and unoccupied, the enemy moves there.
     * Thread-safe with locking to prevent conflicts with other entities.
     * @param newPos The destination position for the enemy.
     */
    private void attemptMove(Position newPos) {
        if (!gameWorld.getMap().isWithinBounds(newPos)) {
            LogManager.addLog("Enemy tried to move out of bounds: " + newPos);
            return;
        }

        ReentrantLock lock = GameWorld.getMapLock(newPos);
        try {
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                try {
                    if (gameWorld.getMap().isEmpty(newPos)) {
                        Position oldPos = enemy.getPosition();
                        gameWorld.getMap().removeFromGrid(oldPos, enemy);
                        enemy.setPosition(newPos);
                        gameWorld.getMap().addToGrid(newPos, enemy);

                        if (gameWorld.getController() != null) {
                            SwingUtilities.invokeLater(gameWorld::notifyObservers);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    /**
     * Stops the enemy's scheduled behavior by canceling the scheduled task.
     */
    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
    }

    /**
     * Sets the scheduled task associated with this enemy's behavior.
     * This allows the task to be canceled later.
     * @param task The scheduled future task controlling this enemy.
     */
    public void setScheduledTask(ScheduledFuture<?> task) {
        this.scheduledTask = task;
    }
    // --- Fields ---
    /** The enemy character associated with this task. */
    private final Enemy enemy;

    /** The game world in which the enemy exists. */
    private final GameWorld gameWorld;

    /** Random number generator for random movements. */
    private final Random random = new Random();

    /** Reference to the scheduled task, used for cancellation. */
    private ScheduledFuture<?> scheduledTask;
}

