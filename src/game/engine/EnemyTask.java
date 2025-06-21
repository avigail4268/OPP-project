
package game.engine;

import game.audio.SoundPlayer;
import game.characters.Enemy;
import game.characters.EnemyFactory;
import game.characters.PlayerCharacter;
import game.log.LogManager;
import game.map.Position;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
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
        SoundPlayer soundPlayer = new SoundPlayer();
        if (!gameWorld.getIsGameRunning().get()) return;

        if (enemy.isDead()) {
            Position oldPos = enemy.getPosition();
            ReentrantLock oldLock = GameWorld.getMapLock(oldPos);
            oldLock.unlock();

            try {
                gameWorld.getEnemies().remove(enemy);
                if (gameWorld.getEnemies().size() < 10) {

                    Position newPos = gameWorld.getMap().getRandomEmptyPosition();
                    ReentrantLock newLock = GameWorld.getMapLock(newPos);
                    if (newLock.tryLock(100, TimeUnit.MILLISECONDS)){
                        try {
                            if (newPos == null) throw new NullPointerException("newPos is null");

                            EnemyFactory factory = new EnemyFactory();
                            Enemy newEnemy = factory.createEnemy(newPos);
                            if (newEnemy == null) throw new NullPointerException("newEnemy is null");

                            gameWorld.getEnemies().add(newEnemy);
                            gameWorld.getMap().addToGrid(newPos, newEnemy);

                            EnemyTask newTask = new EnemyTask(newEnemy, gameWorld);
                            gameWorld.getEnemyTasks().add(newTask);
                            gameWorld.getEnemyExecutor().submit(newTask);

                        } catch (NullPointerException e) {
                            System.err.println("Null pointer issue: " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Illegal argument: " + e.getMessage());
                        } catch (RejectedExecutionException e) {
                            System.err.println("Executor rejected the task: " + e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Unexpected error: " + e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                oldLock.unlock();
            }
            return;
        }
        PlayerCharacter player = gameWorld.getPlayer();
        Position enemyPos = enemy.getPosition();
        Position playerPos = player.getPosition();

        if (enemyPos.distanceTo(playerPos) <= 2) {
            moveTowards(playerPos);
            if (enemy.isInRange(enemy.getPosition(),playerPos)){
                enemy.attack(player);
                SoundPlayer.playSound("classic_attack.wav");
            }
        } else if (random.nextDouble() <= 0.2) {
            moveRandomly();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
                        LogManager.addLog("Enemy moved to: " + enemy.getPosition());
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
    private final Enemy enemy;
    private final GameWorld gameWorld;
    private final Random random = new Random();
    private ScheduledFuture<?> scheduledTask;
}

