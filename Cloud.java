public class Cloud extends GameElement implements Movable{

    private static final int CLOUD_OUT_OF_MAP_X = HelbDroneModel.COLUMNS/2;
    private static final int CLOUD_OUT_OF_MAP_Y = -10;
    private static final int SPEED = 1;
    private boolean desactivateSpawnCloud = false;//pour cheat code
    private int cloudWidth;
    private int cloudHeight;

    public static final String[] CLOUD_IMAGE = {
        "img/cloud.png"
    };

    public Cloud(int posX, int posY) {
        super(posX, posY, CLOUD_IMAGE);
        spawnRandomCloud();
    }

    /*
        Déplace le nuage vers le bas.
        Si le nuage sort du bas de la map, le nuage est modifier.
        Tout est ignoré si le spawn des nuages est désactivé (cheat).
    */
    @Override
    public void move() {
        if (!desactivateSpawnCloud) {
            setPosY(getPosY() + SPEED);

            if (getPosY() > HelbDroneModel.ROWS) {
                spawnRandomCloud();
            }
        }
    }

    /*
        Modifie la taille et la position du nuage aléatoirement
        en s'assurant qu'il ne dépasse pas les limites de la carte.
        Le nuage apparaît en haut de la zone ennemie (posY).
    */
    private void spawnRandomCloud() {
        int minCloudSize=1;
        cloudWidth = (int)(Math.random() * HelbDroneModel.COLUMNS) + minCloudSize;
        cloudHeight = (int)(Math.random() * HelbDroneModel.MIDDLE_ZONE_HEIGHT) + minCloudSize;
        
        setWidth(cloudWidth);
        setHeight(cloudHeight);

        int posX = (int)((Math.random()) * (HelbDroneModel.COLUMNS - cloudWidth));
        
        setPosX(posX);
        setPosY(HelbDroneModel.MIDDLE_ZONE_START);
    }

    /*
        Active ou désactive le spawn des nuages.
        Si le spawn est désactivé, le nuage est déplacé hors de la carte
        afin d'éviter de le recréer à chaque fois.
        Si le spawn est réactivé, ce nuage est remodifier aléatoirement.
    */
    public void toggleSpawnCloud() {
        desactivateSpawnCloud = !desactivateSpawnCloud;
        if (desactivateSpawnCloud) {
            setPosX(CLOUD_OUT_OF_MAP_X);
            setPosY(CLOUD_OUT_OF_MAP_Y);
        }else {
            spawnRandomCloud();;
        }
    }
}