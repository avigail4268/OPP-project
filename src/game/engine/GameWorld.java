package game.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import game.characters.*;
import game.core.GameEntity;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.items.Wall;
import game.map.GameMap;
import game.map.Position;


public class GameWorld {
    private List <PlayerCharacter> players;
    private List <Enemy> enemies;
    private List <GameItem> items;
    private GameMap map;


    public GameWorld(List<GameItem> items) {
        gameInitialization();

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
        while (true){
            for (int i =0; i<players.size(); i++){
                PlayerCharacter currentPlayer = players.get(i);
                Position newPosition = chooseMovement(currentPlayer.getPosition());
                if (newPosition != null){
                    currentPlayer.setPosition(newPosition);
                } else {
                    // TODO can the player choose not to attack? let him choose a different direction or move to next player?
                }
            }

        }
    }

    private Position chooseMovement(Position lastPosition) {
        System.out.println("Please choose direction: 1.right 2.left 3.forward 4.backwards");
        Scanner scanner = new Scanner(System.in);
        int direction = scanner.nextInt();
        while (direction < 1 || direction > 4) {
            System.out.println(" wrong input! Please choose direction: 1.right 2.left 3.forward 4.backwards");
            direction = scanner.nextInt();
        }
        int row = lastPosition.getRow(),col = lastPosition.getCol();
        if (direction == 1) {
            Position newPos = new Position(row,col+1);
            if (map.isEmpty(newPos)) {
                return newPos;
            }
            else {
                // check who is in the position and let the user choose how to act
                boolean move = checkPosition(newPos);
                if (move) {
                    return newPos;
                } else {
                    return null;
                }
            }
        } else if (direction == 2) {
            Position newPos = new Position(row,col-1);
            if (map.isEmpty(newPos)) {
                return newPos;
            }
            checkPosition(newPos);
        } else if (direction == 3) {
            Position newPos = new Position(row+1,col);
            if (map.isEmpty(newPos)) {
                return newPos;
            }
            map.checkPosition();
        }
        return direction;
    }

    private boolean checkPosition(Position newPos) {
        // currently for simplicity pretending there is one entity at each position
        GameEntity entity = map.getEntityInPosition(newPos).getFirst();
        if (entity instanceof PlayerCharacter) {
            // TODO what happens here
        }
        else if (entity instanceof Enemy) {
            // check enemy type
            Enemy enemy = findEnemy(entity);
            switch (enemy.getDisplaySymbol()) {
                case DisplaySymbols.Goblin:
                    // enemy is of type goblin
                    // player can attack or run
                    // if the player choose to attack - he stay in the current position and start combat
                    // if combat success move the player to the newPos
//                    return true;
                    // if he choose to run - let him pick another position
//                return false;
            }
        } else {
            // TODO check GameItem type
        }
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
            System.out.println("select player character: 1.warrior, 2.mage, 3.archer, 4.no more character ");
            Scanner scanner = new Scanner(System.in);
            int playerType = scanner.nextInt();
            while (1 > playerType || playerType > 4) {
                System.out.println("Wrong input! please choose one of the following options:  1.warrior, 2.mage, 3.archer, 4.no more character");
                playerType = scanner.nextInt();
            }
            if (playerType == 4) {break;}
            System.out.println("Enter player name : ");
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
