public class Projectile extends GameElement implements Movable{

    private static final int SPEED_PROJECTILE=1;
    private int damage;
    private boolean isDroneProjectile;
    
    public static final String[] PILOT_PROJECTILE_IMAGE = {
        "img/pilot_projectile.png",
    };
    public static final String[] DRONE_PROJECTILE_IMAGE = {
        "img/ennemi_projectile.png",
    };

    public Projectile(int posX, int posY,String[] imagePaths, boolean isDroneProjectile){
        super(posX, posY, imagePaths);
        this.isDroneProjectile=isDroneProjectile;
        this.damage = 1;
    }

    public boolean isDroneProjectile(){
        return isDroneProjectile;
    }
    
    //verifie si le projectile (pilote || drone) sort de la map
    public boolean isOutsideOfMap(){
        return (getPosY() < HelbDroneModel.TOP_ZONE_HEIGHT|| getPosY() >= HelbDroneModel.ROWS);
    }

    //deplace le projectile vers le haut ou vers le bas en fonction de si c'est un projectile drone ou pilot
    @Override
    public void move() {
        if (isDroneProjectile){
            setPosY(getPosY() + SPEED_PROJECTILE);
        } else{
            setPosY(getPosY() - SPEED_PROJECTILE);
        }
    }

    /*
        Action qui ce déclenche quand le projectile touche un élément destructible.
        Inflige des dégats à la cible touchée.

        Paramètres :
            target – élément destructible qui subit les dégats.
     */
    public void triggerAction(DestroyableGameElement target) {
        target.hit(damage);
    }
}