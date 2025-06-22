package game.decorator;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.log.LogManager;

public class ExplodingEnemyDecorator extends EnemyDecorator{
    /**
     * Constructs a new EnemyDecorator with the specified Enemy.
     * @param enemy the Enemy to be decorated
     */
    public ExplodingEnemyDecorator(Enemy enemy, PlayerCharacter player) {
        super(enemy);
        this.player = player;
    }

    @Override
    public boolean isDead(){
        if (getDecoratorEnemy().getHealth() <= 0 ){
            if (player.isInRange(getDecoratorEnemy().getPosition(), player.getPosition())){
                player.setHealth((int) (player.getHealth() - getDecoratorEnemy().getMaxHealth() * 0.02));
                LogManager.addLog("[ExplodingEnemyDecorator] Player " + player.getName() + " took explosion damage! -2% of max health.");
            }
        }
        return super.isDead();
    }

    //-- Fields ---
    private final PlayerCharacter player;
}
