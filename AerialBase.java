public class AerialBase extends DestroyableGameElement{

    private static final int AERIAL_BASE_WIDTH=2;
    private static final int AERIAL_BASE_HEIGHT=2;
    private static final int AERIAL_BASE_MAX_LIFE=5;
    private static final int HIT_IMAGE_DURING_TIME = 500;
    private boolean isInvincible = true;
    private long hitImageEndTime = 0;

    public static final int AERIAL_BASE_VULNERABILITY_DURATION = 5000;//durée de la vulnerabilité quand tout les drones sont détruits.

    public static final String[] AERIAL_BASE_IMAGE = {
        "img/airbase.png",
        "img/airbase_vulnerable.png",
        "img/aerial_base_hit.png"
    };
    public static final String[] AERIAL_BASE_HEART = {
        "img/airbase_heart.png"
    };
    
    public AerialBase(int posX, int posY){
        super(posX, posY, AERIAL_BASE_IMAGE,AERIAL_BASE_MAX_LIFE);
        this.setWidth(AERIAL_BASE_WIDTH);
        this.setHeight(AERIAL_BASE_HEIGHT);
    }
    
    public boolean isInvincible(){
        return isInvincible;
    }
    public void setInvincible(boolean newStatus) {
        isInvincible = newStatus;
    }
    
    /*
        Inflige des degats a la base aérienne si elle n'est pas invincible.
        Active temporairement le cooldown de l'image de hit.
    
        Paramètres : 
            damage – nombre de vie a retirer
     */
    @Override
    public void hit(int damage){
        if (!isInvincible()){
            super.hit(damage);
            hitImageEndTime = System.currentTimeMillis() + HIT_IMAGE_DURING_TIME ;
        }
    }
    
    //passer this(base aerienne) au drone pour verifier les collisions
    /*
        Instancie un drone rouge.
        
        Paramètres :
            posX  – position X de spawn du drone
            posY  – position Y de spawn du drone
            pilot – pilote qui servira de cible
        
        Retour :
            instance du drone rouge créé
    */
    public RedDrone createRedDrone(int posX, int posY, Pilot pilot){
        RedDrone redDrone = new RedDrone(posX,posY, this, pilot);
        return redDrone;
    }
    //cloud – nuage qui servira de cible
    public BlueDrone createBlueDrone(int posX, int posY,Cloud cloud){
        BlueDrone blueDrone = new BlueDrone(posX,posY, this, cloud);
        return blueDrone;
    }
    public OrangeDrone createOrangeDrone(int posX, int posY) {
        OrangeDrone orangeDrone = new OrangeDrone(posX,posY, this);
        return orangeDrone;
    }
    public PurpleDrone createPurpleDrone(int posX, int posY) {
        PurpleDrone purpleDrone = new PurpleDrone(posX,posY, this);
        return purpleDrone;
    }
    

    /*
        Sélectionne le chemin vers l'image appropriée de la base aérienne
        en fonction de son état (invincible, récemment touchée ou normal).
        
        Retour :
            chemin de l'image à afficher. Pour drawGameElement.
    */
    @Override
    public String getPathToImage() {
        if (System.currentTimeMillis() < hitImageEndTime) {
            return AERIAL_BASE_IMAGE[2];
        }
        if (isInvincible()) {
            return AERIAL_BASE_IMAGE[0];
        }
        return AERIAL_BASE_IMAGE[1];
    }

    //valeur utiliser pour remettre la base aerienne a ses pv max quand elle est morte
    public int getAerialBaseMaxLife(){
        return AERIAL_BASE_MAX_LIFE;
    }
}