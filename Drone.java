import java.util.ArrayList;
public abstract class Drone extends ArmedGameElement implements Movable{

    private static final int DRONE_MAX_LIFE=1;
    private static final int FIRE_RATE=1000;

    public AerialBase aerialBase;
    public Coordinate targetCoord;

    //passer la base aerienne ici pour tout les drones pour "isCoordinateInAerialbase"
    public Drone (int posX, int posY, String[] imagePaths,AerialBase aerialBase){
        super(posX, posY, imagePaths, DRONE_MAX_LIFE, FIRE_RATE);
        this.aerialBase=aerialBase;
    }
    
    @Override
    public abstract void move();
    
    /*
        Déplace le drone vers les coordonées de la cible.

        Paramètres :
            targetCoord – coordonnée cible vers laquelle le drone doit se déplacer
    */
    public void moveToTarget(Coordinate targetCoord) {
        Coordinate droneCoord = new Coordinate(getPosX(), getPosY());
        Coordinate nextPosition = getNextCoordinateForTarget(droneCoord, targetCoord);
        setPosX(nextPosition.x);
        setPosY(nextPosition.y);
    }
    
    //1:pour chaque case de la liste restante, verifie la quel est la plus proche de la cible et la return -> (posX , posY)
    public Coordinate getNextCoordinateForTarget(Coordinate currentCoordinate, Coordinate targetCoordinate){
        
        double minDistance = HelbDroneModel.ROWS+HelbDroneModel.COLUMNS;
        int minIndex = -1;
        ArrayList<Coordinate> accessibleAdjacentCoordinatesList = getAccessibleAdjacentCoordinates(currentCoordinate);
        
        for (int index = 0 ; index < accessibleAdjacentCoordinatesList.size(); index++) {
            Coordinate coord = accessibleAdjacentCoordinatesList.get(index);
            double distance = coord.getDistanceWith(targetCoordinate);
            if(distance <= minDistance){
                minDistance = distance;
                minIndex = index;
            } 
        }
        return accessibleAdjacentCoordinatesList.get(minIndex);
    }
    
    //5:
    public boolean isCoordinateInAerialbase(Coordinate coord){
        return coord.x >= aerialBase.getPosX() && coord.x < aerialBase.getPosX() + aerialBase.getWidth() && 
        coord.y >= aerialBase.getPosY() && coord.y < aerialBase.getPosY() + aerialBase.getHeight();
    }
    
    /*4:
        Vérifie si une coordonnée est valide pour le déplacement du drone.
        La coordonnée n'est pas valide si :
            - elle dépasse le début de la zone pilote
            - si elle se situe dans la base aérienne.
        
        Paramètres :
            coord – coordonnées à valider
        
        Retour :
            true si la coordonnée est valide, false sinon
    */
    private boolean isCoordinateIsValid(Coordinate coord){
        
        //si ca depasse la zone pilote (bottom zone)
        if(coord.y >= HelbDroneModel.BOTTOM_ZONE_START){
            return false;
        }
        
        if (isCoordinateInAerialbase(coord)){
            return false;
        }
        return true;
    }
    
    //3:recupere les 8 cases autour de soi et return une list
    private ArrayList<Coordinate> getAdjacentCoordinates(Coordinate coord){
        
        ArrayList<Coordinate> resultList = new ArrayList<Coordinate>();
        
        for(int i = coord.x-1 ;  i <= coord.x+1 ; i++){
            for(int j = coord.y-1 ;  j <= coord.y+1 ; j++){
                if(!(i == coord.x && j == coord.y)){
                    resultList.add(new Coordinate(i, j));
                }
            }     
        }
        return resultList;
    }
    
    //2:recupere la liste de "getAdjacentCoordinates", test pour chaque coord de la liste si elle est valide via "isCoordinateIsValid".return la liste Valide
    private ArrayList<Coordinate> getAccessibleAdjacentCoordinates(Coordinate coord){
        
        ArrayList<Coordinate> resultList = new ArrayList<Coordinate>();
        
        ArrayList<Coordinate> adjacentCoordinatesList = getAdjacentCoordinates(coord);
        for (Coordinate adjacent : adjacentCoordinatesList) {
            if(isCoordinateIsValid(adjacent)){
                resultList.add(adjacent);
            }
        }
        return resultList;
    }
    
}
