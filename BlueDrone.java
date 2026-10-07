public class BlueDrone extends Drone{
    private static boolean moveEnable = true;//pour cheat code
    private Cloud cloud;

    public static final String[] BLUE_DRONE_IMAGE = {
        "img/blue_drone.png"
    };

    public BlueDrone(int posX, int posY,AerialBase aerialBase, Cloud cloud) {
        super(posX, posY, BLUE_DRONE_IMAGE,aerialBase);
        this.cloud = cloud;
    }

    /*
        Déplace le drone vers le centre du nuage.
        Si le centre du nuage n'est pas atteignable (zone pilote OU zone info),
        le drone se déplace vers le centre supérieur de la carte.
        Ne fait rien si moveEnable est false (cheat).
    */
    @Override
    public void move() {
        if (moveEnable){
            int halfDivider = 2;
            int cloudYOffset = 1;
            int cloudMarginY = 2;

            int centerOfCloudX = cloud.getPosX() + cloud.getWidth() / halfDivider;
            int centerOfCloudY = cloud.getPosY() + cloud.getHeight() / halfDivider + cloudYOffset;

            //si le (centre du nuage -2(pour un peu de marge)) est pas atteignable (zone pilote/zone info), retour en haut milieu
            if (centerOfCloudY - cloudMarginY >= HelbDroneModel.BOTTOM_ZONE_START || centerOfCloudY < HelbDroneModel.MIDDLE_ZONE_START){
                targetCoord = new Coordinate(HelbDroneModel.COLUMNS / halfDivider, HelbDroneModel.MIDDLE_ZONE_START);
            //sinon follow le nuage
            } else {
                targetCoord = new Coordinate(centerOfCloudX, centerOfCloudY);
            }
            
            moveToTarget(targetCoord);
        }
    }

    //pour cheat code
     public static void toggleMovement() {
        moveEnable = !moveEnable;
    }
}