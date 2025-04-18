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
    private List <PlayerCharacter> players;
    private List <Enemy> enemies;
    private List <GameItem> items;
    private GameMap map;

    public GameWorld(List<GameItem> items) {
        //initialize bord: players, enemy's and items.
        gameInitialization();
        //every player in his turn choose the direction, we need to check if this position available
        //if not available the player have 3 choices:
        //if its item: take it, add it to inventory.
        //if its enemy: attack or not
        //todo  if its another payer i have no idea.
        startGame();
    }
    private void gameInitialization() {
        //create game map
        creatGameMap();
        //create player character
        initPlayers();
        //create game content
        populateGameMap();
    }
    private void startGame() {
        // todo the loop should end when all the players is dead or one win?
        while (true){
            for (int i =0; i<players.size(); i++){
                //currentPlayer is the player how's need to play now.
                PlayerCharacter currentPlayer = players.get(i);
                //chooseMovement return the position than the player choose, if available, if not return null.
                Position newPosition = chooseMovement(currentPlayer.getPosition(),currentPlayer);
                if (newPosition != null) {
                    currentPlayer.setPosition(newPosition);
                }
            }
        }
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
        //check the input is valid.
        while (action < 1 || action > 6) {
            System.out.println("Choose an action:");
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
        if (action == 1) {
            newPos = new Position(row,col+1);
        } else if (action == 2) {
            newPos = new Position(row,col-1);
        } else if (action == 3) {
            newPos = new Position(row+1,col);
        }else if (action == 4) {
            newPos = new Position(row-1,col);
        }
        else if (action == 5) {
           if (currentPlayer.usePotion()){
               System.out.println("Use Potion");
               return newPos;
           }
           else {
               System.out.println("non potion found in the inventory");
               return newPos;
           }
        }
        else {
            if (currentPlayer.usePowerPotion()){
                System.out.println("Use Power Potion");
                return newPos;
            }
            else {
                System.out.println("non power potion found in the inventory");
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
        // currently for simplicity pretending there is one entity at each position
        GameEntity entity = map.getEntityInPosition(newPos).getFirst();
        Scanner scanner = new Scanner(System.in);
        if (entity instanceof PlayerCharacter) {
            // TODO what happens here
            return false;
        }
        else if (entity instanceof Enemy) {
            // check enemy type
            Enemy enemy = findEnemy(entity);
            CombatSystem combatSystem = new CombatSystem();
            combatSystem.resolveCombat(currentPlayer,enemy);
            if (currentPlayer.isDead()){
                players.remove(currentPlayer);
                return false;
            }
            else if (enemy.isDead()){
                //when enemy is dead we need to add treasure in his position instead
                Treasure replacement = enemy.defeat();
                items.add(replacement);
                map.addToGrid(replacement.getPosition(),replacement);
                enemies.remove(enemy);
                return true;
            }
            else {
                return false;
            }
        } else {
            if (isBlocked(entity.getPosition())){
                System.out.println("There is a Wall! you need to pass it!");
                return false;
            }
            else if (entity instanceof Interactable item){
                item.interact(currentPlayer);
                return true;
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
    private void creatGameMap() {
        // ask for user input and create map
        System.out.println("Enter map size (minimum size is 10x10):");
        Scanner scanner = new Scanner(System.in);
        int size;
        size = scanner.nextInt();
        //check map size
        this.map = new GameMap(size);
    }
    private void initPlayers() {
        // user selects the player type from the available types (archer / warrior / magic)
        // after user select character ask for name and give a random position in GameMap
        this.players = new ArrayList<PlayerCharacter>();
        playersTypes();// init players
    }
    private void playersTypes() {
        for (int i = 0; i < 3; i++) { // can be for/while depends on your choice of player number
            System.out.println("Select player character: 1.Warrior, 2.Mage, 3.Archer, 4.No more character ");
            Scanner scanner = new Scanner(System.in);
            int playerType = scanner.nextInt();
            while (1 > playerType || playerType > 4) {
                System.out.println("Wrong input! please choose one of the following options:  1.Warrior, 2.Mage, 3.Archer, 4.No more character");
                playerType = scanner.nextInt();
            }
            if (playerType == 4) {break;}
            System.out.println("Enter player name:");
            String playerName = scanner.next();
            createPlayer(playerType,playerName);
        }
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
            //TODO
        }
    }
    private void populateGameMap() {
        // add content to the map - enemies and items.
        // each one will be generated on a random location in the map
        this.items = new ArrayList<GameItem>();
        this.enemies = new ArrayList<Enemy>();
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Position pos = new Position(i,j);
                //check if player in position
                if (!map.isEmpty(pos))
                    continue;
                double random = Math.random();
                if (random <= 0.4) {
                    continue;
                } else if (random <= 0.7) {
                     createEnemy(pos);
                } else if (random < 0.8) {
                    creatWall(pos);
                } else if(random < 0.95) {
                    creatPotion(pos);
                }else {
                    createPowerPotion(pos);
                }
            }
        }
    }
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos,false,"Power potion",5,1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }
    private void creatPotion(Position pos) {
        Potion potion = new Potion(pos,false,"Potion",50,10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }
    private void creatWall(Position pos) {
        Wall wall = new Wall(pos,true,"Wall");
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
            enemy= new Dragon(pos,50);
        } else if (random <= 2.0/3.0) {
            enemy = new Orc(pos,50);
        }else {
            enemy =new Goblin(pos,50);
        }
        enemies.add(enemy);
        map.addToGrid(pos,enemy);
    }
}
