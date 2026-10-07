public class RedDrone extends Drone {
    
    private static boolean moveEnable = true;//pour cheat code
    private Pilot pilot;
    
    public static final String[] RED_DRONE_IMAGE = {
        "img/red_drone.png"
    };
    
    public RedDrone(int posX, int posY, AerialBase aerialBase, Pilot pilot) {
        super(posX, posY, RED_DRONE_IMAGE, aerialBase);
        this.pilot = pilot;
    }
     
    /*
        Déplace le drone vers la position actuelle du pilote.
        Ne fait rien si moveEnable est false (cheat).
     */
    @Override
    public void move() {
        if (moveEnable) {
            targetCoord = new Coordinate(pilot.getPosX(), pilot.getPosY());
            moveToTarget(targetCoord);
        }
    }
    
    //pour cheat code
    public static void toggleMovement() {
        moveEnable = !moveEnable;
    }
}