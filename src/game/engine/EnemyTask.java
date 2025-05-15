package game.engine;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.map.Position;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class EnemyTask implements Runnable {
    private final Enemy enemy;
    private final GameWorld gameWorld;
    private final Random random = new Random();
    private boolean isRunning = true;
    private ScheduledFuture<?> scheduledTask;

    public EnemyTask(Enemy enemy, GameWorld gameWorld) {
        this.enemy = enemy;
        this.gameWorld = gameWorld;
    }

    @Override
    public void run() {
        if (!isRunning || enemy.isDead()) return;
        System.out.println("Enemy running: " + enemy.getPosition());

        PlayerCharacter player = gameWorld.getPlayer();
        Position enemyPos = enemy.getPosition();
        Position playerPos = player.getPosition();

        if (enemyPos.distanceTo(playerPos) <= 3) {
            moveTowards(playerPos);
        } else if (random.nextDouble() <= 0.2) {
            moveRandomly();
        }
    }
    private void moveTowards(Position targetPos) {
        Position currentPos = enemy.getPosition();
        int rowDiff = targetPos.getRow() - currentPos.getRow();
        int colDiff = targetPos.getCol() - currentPos.getCol();

        Position newPos;

        if (Math.abs(rowDiff) > 0) {
            int dx = Integer.compare(rowDiff, 0);
            newPos = new Position(currentPos.getRow() + dx, currentPos.getCol());
        }
        else if (Math.abs(colDiff) > 0) {
            int dy = Integer.compare(colDiff, 0);
            newPos = new Position(currentPos.getRow(), currentPos.getCol() + dy);
        } else {
            return;
        }

        attemptMove(newPos);
    }

    private void moveRandomly() {
        Position currentPos = enemy.getPosition();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] dir = directions[random.nextInt(directions.length)];
        Position newPos = new Position(currentPos.getRow() + dir[0], currentPos.getCol() + dir[1]);

        attemptMove(newPos);
    }

    private void attemptMove(Position newPos) {
        if (!gameWorld.getMap().isWithinBounds(newPos)) return;

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
                            SwingUtilities.invokeLater(() -> gameWorld.notifyObservers());
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        isRunning = false;
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
    }

    public void setScheduledTask(ScheduledFuture<?> task) {
        this.scheduledTask = task;
    }
}
