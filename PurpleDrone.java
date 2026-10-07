public class PurpleDrone extends Drone {

    private static final int LEFT_WALL = HelbDroneModel.LEFT_WALL_POSITION;
    private static final int RIGHT_WALL = HelbDroneModel.RIGHT_WALL_POSITION;

    private static boolean moveEnable = true;//pour cheat code
    private boolean wasMovingVertically  = false;//est ce que je bougeais verticalement avant d'arriver sur les coord de la target

    public static final String[] PURPLE_DRONE_IMAGE ={ 
        "img/purple_drone.png" 
    };

    public PurpleDrone(int posX, int posY, AerialBase aerialBase) {
        super(posX, posY, PURPLE_DRONE_IMAGE, aerialBase);
        
        // va soit au mur de droite soit au mur de gauche, 50% de chance
        double fiftyPercentChance=0.5;
        int targetPosX;
        if (Math.random() < fiftyPercentChance) {
            targetPosX = LEFT_WALL;
        } else {
            targetPosX = RIGHT_WALL;
        }
        targetCoord = new Coordinate(targetPosX, posY);
    }


    /*
        Déplace le drone vers les coordonnées de la target.
        Si le drone atteint la targetCoord, de nouvelles coordonnées sont choisies via chooseNextTarget.
        Ne fait rien si moveEnable est false (cheat).
    */
    @Override
    public void move() {
        if (moveEnable) {
            if (getPosX() == targetCoord.x && getPosY() == targetCoord.y) {
                chooseNextTarget();
            }
            moveToTarget(targetCoord);
        }
    }

    /*
        Détermine la prochaine coordonnée cible du drone selon son dernier mouvement :
            - Si le drone VIENT de se déplacer verticalement, il se dirige horizontalement vers le mur opposé
            - Sinon il se déplace verticalement vers une hauteur aléatoire dans la même colonne
        Met à jour targetCoord et l'état de wasMovingVertically en conséquence.
    */
    private void chooseNextTarget() {
        if (wasMovingVertically){
            int oppositePosX;
            if (getPosX() == LEFT_WALL) {
                oppositePosX = RIGHT_WALL;
            }else{
                oppositePosX = LEFT_WALL;
            }
            targetCoord = new Coordinate(oppositePosX, getPosY());
            wasMovingVertically = false;
        } else{
            int randomPosY = (int)(Math.random() * HelbDroneModel.MIDDLE_ZONE_HEIGHT) + HelbDroneModel.MIDDLE_ZONE_START;
            targetCoord = new Coordinate(getPosX(), randomPosY);
            wasMovingVertically  = true;
        }
    }

    //pour cheat code
    public static void toggleMovement() {
        moveEnable = !moveEnable;
    }
}