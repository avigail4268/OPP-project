package game.characters;
import game.engine.GameWorld;
import game.items.Treasure;
import game.map.Position;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Enemy represents a character that can be defeated by a player.
 * It extends the AbstractCharacter class and adds loot functionality.
 * When defeated, it may drop a treasure for the player to collect.
 */
public abstract class Enemy extends AbstractCharacter implements Runnable {

    /**
     * Constructs a new Enemy with the given position and health.
     * A random loot value between 100 and 300 is assigned to the enemy.
     *
     * @param position the position of the enemy
     * @param health the health of the enemy
     */
    public Enemy(Position position, int health) {
        super(position, health);
        Random r = new Random();
        this.loot = r.nextInt(100, 300); // Random loot between 100 and 300
    }

    /**
     * Defeats the enemy and returns a Treasure object if the enemy is dead.
     * If the enemy is alive, no treasure is dropped.
     *
     * @return a Treasure object representing the loot, or null if the enemy is not dead
     */
    public Treasure defeat() {
        if (this.isDead()) {
            return new Treasure(this.getPosition(), false, getLoot());
        }
        return null;
    }

    /**
     * Gets the amount of loot that the enemy holds.
     * This is a random value between 100 and 300.
     * @return the amount of loot
     */
    public int getLoot() {
        //TODO delete this statement
        System.out.println("hello");
        return loot;
    }


    @Override
    public void run() {
        Random random = new Random();
        while (isRunning.get()) {
            try {
                List<PlayerCharacter> players = GameWorld.getPlayers();
                int delay = 500 + random.nextInt(1001);
                Thread.sleep(delay);

                enemyMovement(players);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void enemyMovement(List<PlayerCharacter> players) {
        if (players.isEmpty()) return;

        for (PlayerCharacter player : players) {
            Position playerPos = player.getPosition();

            if (this.getPosition().distanceTo(playerPos) <= 2) {
                ReentrantLock playerLock = player.getCombatLock();

                try {
                    if (playerLock.tryLock(500, TimeUnit.MILLISECONDS)) {
                        try {
                            if (isInRange(this.getPosition(), playerPos)) {
                                this.attack(player);
                            } else {
                                moveTowards(playerPos);
                            }
                        } finally {
                            playerLock.unlock();
                        }
                    } else {
                        System.out.println("Player " + player.getName() + " is currently in combat. Skipping.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                return;
            }
        }

        if (Math.random() <= 0.2) {
            Position newPos = getRandomPosition();
            this.setPosition(newPos);
        }
    }


    private Position getRandomPosition() {
        double rand = Math.random();
        int row = this.getPosition().getRow();
        int col = this.getPosition().getCol();
        if (rand <= 0.25) {
            return new Position(row + 1, col);
        } else if (rand <= 0.5) {
            return new Position(row - 1, col);
        } else if (rand <= 0.75) {
            return new Position(row, col + 1);
        } else {
            return new Position(row, col - 1);
        }

    }

    private void moveTowards (Position position) {
        int rowDiff = position.getRow() - this.getPosition().getRow();
        int colDiff = position.getCol() - this.getPosition().getCol();

        if (Math.abs(rowDiff) > Math.abs(colDiff)) {
            if (rowDiff > 0) {
                this.setPosition(new Position(this.getPosition().getRow() + 1, this.getPosition().getCol()));
            } else {
                this.setPosition(new Position(this.getPosition().getRow() - 1, this.getPosition().getCol()));
            }
        } else {
            if (colDiff > 0) {
                this.setPosition(new Position(this.getPosition().getRow(), this.getPosition().getCol() + 1));
            } else {
                this.setPosition(new Position(this.getPosition().getRow(), this.getPosition().getCol() - 1));
            }
        }
    }

    public void stop() {
        isRunning.set(false);
    }


    /**
     * Returns the maximum health points of the enemy.
     * This method can be overridden in subclasses to provide different health values.
     * @return the maximum health (default is 50)
     */
    @Override
    public int getMaxHealth() {
        return 50; // Example max health, can be overridden in subclasses
    }

    /**
     * Returns a string representation of the enemy, including its current position.
     * @return a string describing the enemy's position
     */
    @Override
    public String toString() {
        return "Enemy in position " + getPosition();
    }

    /**
     * Compares this enemy with another object for equality.
     * Two enemies are considered equal if they occupy the same position on the map.
     * @param obj the object to compare to
     * @return true if the other object is an Enemy and has the same position; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Enemy other) {
            return getPosition().equals(other.getPosition());
        }
        return false;
    }


    // --- Fields ---
    /**
     * The amount of loot that the enemy holds.
     * This is a random value between 100 and 300.
     */
    private final int loot;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
}
