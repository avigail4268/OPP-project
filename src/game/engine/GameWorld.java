package game.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.items.*;
import game.map.GameMap;
import game.map.Position;


public class GameWorld {
    public GameWorld() {
        gameInitialization();
    }
    public void startGame() {
        while (true){
            //game over when the player achieve more than 500 point or dead.
            PlayerCharacter currentPlayer = players.get(0);
            isVisible(currentPlayer);
            printBoard();
            Position newPosition = chooseMovement(currentPlayer.getPosition(),currentPlayer);
            System.out.println("Current health: " + currentPlayer.getHealth());
            System.out.println("Treasure points: " + currentPlayer.getTreasurePoints());
            if (newPosition != null) {
                Position lastPosition = currentPlayer.getPosition();
                currentPlayer.setPosition(newPosition);
                map.removeFromGrid(lastPosition, currentPlayer);
                map.addToGrid(newPosition, currentPlayer);
                board[lastPosition.getRow()][lastPosition.getCol()] = "_ ";
            }
            if (currentPlayer.isDead()) {
                players.remove(currentPlayer);
                System.out.println("Player: " + currentPlayer.getName() + " is dead, GAME OVER!");
                break;
            }
            if (currentPlayer.getTreasurePoints() >= 500) {
                System.out.println("Player: " + currentPlayer.getName() + " achieve more than 500 points and WON THE GAME!");
                break;
            }
            if (enemies.isEmpty()) {
                System.out.println("Player: " + currentPlayer.getName() + " you WON THE GAME! all the enemies are dead!");
                break;
            }
        }
    }
    private void gameInitialization() {
        createGameMap();
        initPlayers();
        populateGameMap();
    }
    private void printBoard () {
        System.out.println("===== Game Board =====");
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
    }
    private void createGameMap() {
        System.out.println("Enter map size (minimum size is 10x10):");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        while (size < 10) {
            System.out.println("Invalid input! please enter a number greater than 10");
            size = scanner.nextInt();
        }
        board = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board [i][j] = "_ ";
            }
        }
        this.map = new GameMap(size);
    }
    private Position chooseMovement(Position lastPosition,PlayerCharacter currentPlayer) {
        System.out.println("Choose an action:");
        System.out.println("1. Move Up");
        System.out.println("2. Move Down");
        System.out.println("3. Move Left");
        System.out.println("4. Move Right");
        System.out.println("5. Use Potion");
        System.out.println("6. Use Power Potion");
        Scanner scanner = new Scanner(System.in);
        int action = scanner.nextInt();
        while (action < 1 || action > 6) {
            System.out.println("Invalid input, Choose an action:");
            System.out.println("1. Move Up");
            System.out.println("2. Move Down");
            System.out.println("3. Move Left");
            System.out.println("4. Move Right");
            System.out.println("5. Use Potion");
            System.out.println("6. Use Power Potion");
            action = scanner.nextInt();
        }
        Position newPos = lastPosition;
        int row = lastPosition.getRow(),col = lastPosition.getCol();
        if (action == 1) { // Move Up
            newPos = new Position(row - 1, col);
        } else if (action == 2) { // Move Down
            newPos = new Position(row + 1, col);
        } else if (action == 3) { // Move Left
            newPos = new Position(row, col - 1);
        } else if (action == 4) { // Move Right
            newPos = new Position(row, col + 1);
        }
        else if (action == 5) {
           if (currentPlayer.usePotion()){
               System.out.println("Use Health Potion");
               return newPos;
           }
           else {
               System.out.println("Non potion found in the inventory");
               return newPos;
           }
        }
        else {
            if (currentPlayer.usePowerPotion()){
                System.out.println("Use Power Potion");
                return newPos;
            }
            else {
                System.out.println("Non power potion found in the inventory");
                return newPos;
            }
        }
        return isAvailable(newPos,currentPlayer);
    }
    private Position isAvailable(Position newPos, PlayerCharacter currentPlayer) {
        if (!isInMapBounds(newPos)) {
            System.out.println("Out of game bounds! - Invalid move");
            return null;
        }
        else if (map.isEmpty(newPos)) {
            return newPos;
        }
        else {
            boolean move = checkPosition(newPos,currentPlayer);
            if (move) {
                return newPos;
            } else {
                return null;
            }
        }
    }
    private boolean isInMapBounds(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        int size = map.getSize();
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    private boolean checkPosition(Position newPos,PlayerCharacter currentPlayer) {
        List <GameEntity> entity = map.getEntityInPosition(newPos);
        for (GameEntity gameEntity : entity) {
            if (gameEntity instanceof PlayerCharacter) {
                // Only one player at the moment
                return false;
            } else if (gameEntity instanceof Enemy) {
                Enemy enemy = findEnemy(gameEntity);
                CombatSystem combatSystem = new CombatSystem();
                combatSystem.resolveCombat(currentPlayer, enemy);
                if (currentPlayer.isDead()) {
                    if (map.removeFromGrid(newPos, currentPlayer)) {
                        players.remove(currentPlayer);
                    }
                    return false;
                } else if (enemy.isDead()) {
                    Treasure replacement = enemy.defeat();
                    if (map.removeFromGrid(newPos, enemy)){
                        enemies.remove(enemy);
                        items.add(replacement);
                        map.addToGrid(replacement.getPosition(), replacement);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                if (isBlocked(gameEntity.getPosition())) {
                    System.out.println("There is a Wall! you need to choose another spot!");
                    newPos = chooseMovement(currentPlayer.getPosition(), currentPlayer);
                    if (newPos != null) {
                        currentPlayer.setPosition(newPos);
                    }
                    return false;
                } else{
                    if (gameEntity instanceof Interactable item){
                        item.collect(currentPlayer);
                        if (map.removeFromGrid(newPos, gameEntity)){
                            items.remove(item);
                        }
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
    private Enemy findEnemy(GameEntity entity) {
        for (Enemy enemy : enemies) {
            if (enemy.equals(entity)) return enemy;
        }
        return null;
    }
    private void initPlayers() {
        this.players = new ArrayList<>();
        playersTypes();
    }
    private void playersTypes() {
        System.out.println("Select player character: 1.Warrior, 2.Mage, 3.Archer");
        Scanner scanner = new Scanner(System.in);
        int playerType = scanner.nextInt();
        while (1 > playerType || playerType > 3) {
            System.out.println("Wrong input! please choose one of the following options: 1.Warrior, 2.Mage, 3.Archer");
            playerType = scanner.nextInt();
        }
        System.out.println("Enter player name:");
        String playerName = scanner.next();
        createPlayer(playerType,playerName);
    }
    private void createPlayer(int playerType, String playerName) {
        PlayerCharacter player;
        try {
            Position position = this.map.getRandomEmptyPosition();
            if (playerType == 1) {
                player = new Warrior(playerName,position,100);
            } else if (playerType == 2) {
                player = new Mage(playerName,position,100);
            } else {
                player = new Archer(playerName,position,100);
            }
            map.addToGrid(position, player);
            players.add(player);
        }
        catch (RuntimeException e) {
            System.out.println("No empty position found on the board");
            System.out.println("Please try again");
            playersTypes();
        }
    }
    private void populateGameMap() {
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Position pos = new Position(i,j);
                if (!map.isEmpty(pos))
                    continue;
                double random = Math.random();
                if (random <= 0.4) {
                    continue;
                } else if (random <= 0.7) {
                     createEnemy(pos);
                } else if (random < 0.8) {
                    createWall(pos);
                } else if(random < 0.95) {
                    createPotion(pos);
                }else {
                    createPowerPotion(pos);
                }
            }
        }
    }
    private void isVisible (PlayerCharacter currentPlayer) {
        System.out.println(currentPlayer + " !");
        System.out.println("Look around you: ");
        checkArrayList(players,currentPlayer);
        checkArrayList(enemies,currentPlayer);
        checkArrayList(items,currentPlayer);
    }
    private void checkArrayList(List<? extends GameEntity> entities, PlayerCharacter currentPlayer) {
        for (GameEntity entity : entities) {
            Position entityPosition = entity.getPosition();
            if (entityPosition.distanceTo(currentPlayer.getPosition()) <= 2 && !entity.equals(currentPlayer)) {
                entity.setVisible(true);
                int row = entity.getPosition().getRow();
                int col = entity.getPosition().getCol();
                board[row][col] = entity.getDisplaySymbol().substring(0, 2);
            }
            if (entityPosition.distanceTo(currentPlayer.getPosition()) > 2) {
                if (entity.getVisible()) {
                    entity.setVisible(false);
                    int row = entity.getPosition().getRow();
                    int col = entity.getPosition().getCol();
                    board[row][col] = "_ ";
                }
            }
            if (entity.equals(currentPlayer)) {
                entity.setVisible(true);
                int row = entity.getPosition().getRow();
                int col = entity.getPosition().getCol();
                board[row][col] = entity.getDisplaySymbol().substring(0, 2);
            }
        }
    }
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos,false,5,1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }
    private void createPotion(Position pos) {
        Potion potion = new Potion(pos,false,50,10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }
    private void createWall(Position pos) {
        Wall wall = new Wall(pos,true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }
    private boolean isBlocked(Position pos) {
        List<GameEntity> entities = map.getEntityInPosition(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocksMovement()) {
                return true;
            }
        }
        return false;
    }
    public void createEnemy(Position pos){
        double random = Math.random();
        Enemy enemy;
        if (random <= 1.0/3.0){
            enemy = new Dragon(pos,50);
        } else if (random <= 2.0/3.0) {
            enemy = new Orc(pos,50);
        }else {
            enemy = new Goblin(pos,50);
        }
        enemies.add(enemy);
        map.addToGrid(pos,enemy);
    }

    private List <PlayerCharacter> players;
    private List <Enemy> enemies;
    private List <GameItem> items;
    private GameMap map;
    private String[][] board;
}
