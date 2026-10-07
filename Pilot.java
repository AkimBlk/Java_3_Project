public class Pilot extends ArmedGameElement implements Movable{

    private static final int PILOT_MAX_LIFE = 3;
    private static final int FIRE_RATE = 500;
    private static final int HIT_IMAGE_DURING_TIME =500;
    private static final int SPEED=1;
    private int currentDirection = HelbDroneModel.DOWN;
    private long invincibiltyEndTime = 0;
    private long hitImageEndTime = 0;
    private boolean hasAutoMissile = false;
    private boolean cheatInvincibility = false;
    
    public static final String[] PILOT_IMAGE = {
        "img/pilot_sentry.png",
        "img/pilot_sentry_invincible.png",
        "img/pilot_sentry_hit.png"
    };
    public static final String[] PILOT_HEART = {
        "img/pilot_sentry_heart.png"
    };

    public Pilot(int posX, int posY) {
        super(posX, posY, PILOT_IMAGE, PILOT_MAX_LIFE, FIRE_RATE);
    }
    

    public boolean isInvincible() {
        return cheatInvincibility || System.currentTimeMillis() < invincibiltyEndTime;
    }
    //verifier si le cheat n'est pas activer pour ne pas avoir de cooldown activer et pouvoir direct toggle l'invincibilité si besoin et pas attendre la fin du cooldown
    public void setInvincible(int effectDuration) {
        if(!isCheatInvincibility()){
            invincibiltyEndTime = System.currentTimeMillis() + effectDuration;
        }
    }

    public boolean hasAutoMissile() {
        return hasAutoMissile;
    }
    public void setHasAutoMissile(boolean newStatus) {
        hasAutoMissile = newStatus;
    }

    
    public boolean isCheatInvincibility() {
        return cheatInvincibility;
    }
    public void setCheatInvincibility(boolean newStatus) {
        cheatInvincibility = newStatus;
    }

    /*
        Inflige des degats au pilot si il n'est pas invincible.
        Active temporairement le cooldown de l'image de hit.
    
        Paramètres : 
            damage – nombre de vie a retirer
     */
    @Override
    public void hit(int damage) {
        if (!isInvincible()) {
            super.hit(damage);
            hitImageEndTime = System.currentTimeMillis() + HIT_IMAGE_DURING_TIME ;
        }
    }

    /*
        Déplace le pilote horizontalement selon la direction actuelle :
            - Vers la droite si currentDirection == RIGHT
            - Vers la gauche si currentDirection == LEFT
            - Ne bouge pas si currentDirection == DOWN
        Vérifie que le pilote ne sort pas des limites de la map avant de déplacer.
    */
    @Override
    public void move() {
        if (currentDirection == HelbDroneModel.RIGHT) {
            if (getPosX() + SPEED <= HelbDroneModel.RIGHT_WALL_POSITION) {
                setPosX(getPosX() + SPEED);
            }
        } 
        else if (currentDirection == HelbDroneModel.LEFT) {
            if (getPosX() - SPEED >= HelbDroneModel.LEFT_WALL_POSITION) {
                setPosX(getPosX() - SPEED);
            }
        }
        else if(currentDirection == HelbDroneModel.DOWN){
            setPosX(getPosX());
        }
    }

    public void setDirection(int direction) {
        currentDirection = direction;
    }
    
    /*
        Sélectionne le chemin vers l'image appropriée du pilot
        en fonction de son état (invincible, récemment touchée ou normal).
        
        Retour :
            chemin de l'image à afficher. Pour drawGameElement.
    */
    @Override
    public String getPathToImage() {
        if (isInvincible()) {
            return PILOT_IMAGE[1];
        }
        if (System.currentTimeMillis() < hitImageEndTime) {
            return PILOT_IMAGE[2];
        }
        return PILOT_IMAGE[0];
    }
}