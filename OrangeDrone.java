public class OrangeDrone extends Drone {
    private static boolean moveEnable = true;//pour cheat code

    public static final String[] ORANGE_DRONE_IMAGE = {
        "img/orange_drone.png"
    };

    public OrangeDrone(int posX, int posY, AerialBase aerialBase) {
        super(posX, posY, ORANGE_DRONE_IMAGE, aerialBase);
        randomTargetCoord();
    }

    /*
        Déplace le drone vers les coordonnées de la target.
        Si le drone atteint la targetCoord, de nouvelles coordonnées aléatoires sont choisies.
        Ne fait rien si moveEnable est false (cheat).
    */
    @Override
    public void move(){
        if (moveEnable){
            if (getPosX() == targetCoord.x && getPosY() == targetCoord.y) {
                randomTargetCoord();
            }
            moveToTarget(targetCoord);
        }
    }

    /*
        Set une nouvelles coordonnée aléatoire cible dans la zone ennemie.
        Régénère tant que la coordonnée tombe sur la base aérienne.
     */
    private void randomTargetCoord(){
        Coordinate randomTargetCoord = getRandomCoord();

        while (isCoordinateInAerialbase(randomTargetCoord)){
            randomTargetCoord = getRandomCoord();
        }
        targetCoord = randomTargetCoord;
    }

    private Coordinate getRandomCoord(){
        int randomPosX = (int)(Math.random() * HelbDroneModel.COLUMNS);
        int randomPosY = HelbDroneModel.MIDDLE_ZONE_START + (int)(Math.random() * HelbDroneModel.MIDDLE_ZONE_HEIGHT);
        return new Coordinate(randomPosX, randomPosY);
    }

    //pour cheat code
     public static void toggleMovement() {
        moveEnable = !moveEnable;
    }
}