package game.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import game.characters.*;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.items.Wall;
import game.map.GameMap;
import game.map.Position;


public class GameWorld {
    private List <PlayerCharacter> players;
    private List <Enemy> enemies;
    private List<GameItem> items;
    private GameMap map;


    public GameWorld() {
        //create game map
        creatGameMap();
        //create player character
        initPlayers();
        //create game content
        populateGameMap();
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
                player = new Warrior(playerName,position);
            } else if (playerType == 2) {
                player = new Mage(playerName,position);
            } else {
                player = new Archer(playerName,position);
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
            enemy= new Dragon(pos);
        } else if (random <= 2.0/3.0) {
            enemy = new Orc(pos);
        }else {
            enemy =new Goblin(pos);
        }
        enemies.add(enemy);
        map.addToGrid(pos,enemy);
    }
}
