import java.util.ArrayList;
import java.util.Collections;//pour shuffle

public class HelbDroneModel {
    
    private boolean gameOver=false;
    private boolean aerialBaseInvincibiltyCheatActive = false;
    private boolean aerialBasehasBeenHit = false;
    private int level = 1;
    private int totalAerialBaseHeartDestroyed = 0;
    private long aerialBaseVulnerabilityEndTime = 0;
    private long startTimeOfTheGame = System.currentTimeMillis();
    
    //mettre en premier dans les listes
    private static final int FIRST_INDEX_LIST = 0; 
    
    private static final int HALF_DIVIDE = 2;
    
    //drone type pour le spawn aleatoire
    private static final int RED_DRONE_TYPE = 0;
    private static final int BLUE_DRONE_TYPE = 1;
    private static final int ORANGE_DRONE_TYPE = 2;
    private static final int PURPLE_DRONE_TYPE = 3;
    
    // list pour les elements/objet du jeu
    private ArrayList<GameElement> gameElementList;
    private ArrayList<Projectile>projectileList;
    private ArrayList<Drone> droneElementList;
    private ArrayList<DestroyableGameElement>destroyableGameElementList;
    private ArrayList<Triggerable>triggerableGameElementList;
    private ArrayList<PhilosopherStone> stoneList;//pour verif si une stone existe deja
    private ArrayList<AutoMissile> autoMissileList;//pareil
    
    //element du jeu
    private Pilot pilot;
    private Teleporter leftTeleporter;
    private Teleporter rightTeleporter;
    private AerialBase aerialBase;
    private Cloud cloud;
    
    public static final int MAX_LEVEL = 4;

    // taille
    public static final int ROWS = 15;
    public static final int COLUMNS = ROWS;
    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH;
    public static final int SQUARE_SIZE = WIDTH / ROWS;

    //bord de la map
    public static final int LEFT_WALL_POSITION = 0;
    public static final int RIGHT_WALL_POSITION = COLUMNS-1;
    public static final int TOP_MAP_POSITION = 0;
    public static final int BOTTOM_MAP_POSITION = ROWS-1;
    
    //hauteur des zones
    public static final int TOP_ZONE_HEIGHT = 2;
    public static final int MIDDLE_ZONE_HEIGHT = 10;
    public static final int BOTTOM_ZONE_HEIGHT = 3;
    
    //debut du territoire de chaque zone
    public static final int TOP_ZONE_START = 0;
    public static final int MIDDLE_ZONE_START = TOP_ZONE_HEIGHT;
    public static final int BOTTOM_ZONE_START = TOP_ZONE_HEIGHT + MIDDLE_ZONE_HEIGHT;
    
    //direction
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int UP = 3;

    //constructeur
    public HelbDroneModel() {
        this.gameElementList= new ArrayList<>();
        this.projectileList= new ArrayList<>();
        this.droneElementList = new ArrayList<> ();
        this.destroyableGameElementList = new ArrayList<>();
        this.triggerableGameElementList = new ArrayList<>();
        this.stoneList = new ArrayList<>();
        this.autoMissileList = new ArrayList<>();

        // instancie le pilote
        this.pilot = new Pilot(COLUMNS / HALF_DIVIDE, BOTTOM_MAP_POSITION);
        this.addDestroyableElementToList(pilot);

        // instancie les tp
        int exitLeft = COLUMNS - 2; // -2 pour sortir derriere le teleporter de droite
        int exitRight = 1; // 1 pour sortir devant le teleporter de gauche
        this.leftTeleporter = new Teleporter(LEFT_WALL_POSITION, COLUMNS-Teleporter.TELEPORTER_HEIGHT, Teleporter.YELLOW_TP_IMAGE,exitLeft);
        this.rightTeleporter = new Teleporter(RIGHT_WALL_POSITION, COLUMNS-Teleporter.TELEPORTER_HEIGHT,Teleporter.BLUE_TP_IMAGE,exitRight);
        this.addTriggerableElementToList(leftTeleporter);
        this.addTriggerableElementToList(rightTeleporter);

        // instancie la base aerienne
        int centerOffset = 1;
        int baseVerticalOffset = 2;
        this.aerialBase = new AerialBase((COLUMNS /HALF_DIVIDE - centerOffset),MIDDLE_ZONE_START + (MIDDLE_ZONE_HEIGHT - baseVerticalOffset) / HALF_DIVIDE);
        this.addDestroyableElementToList(aerialBase);

        //instancie le nuage hors map
        int out_map_pos= -2;
        this.cloud = new Cloud(out_map_pos, out_map_pos);
        this.gameElementList.add(cloud);

        //les drone
        spawnDroneDependOfLevel();
    }
    

    //appeller a chaque frame
    public void updateGame() {
        if (isGameOver()){ return;}
        changeInvincibilityOfAerialBase();
        pilot.move();
        controlDrone();
        manageProjectile();
        checkPilotCollision();
        cloud.move();
        checkIfLevelUp();
    }


    /*
        Crée des drones autour de la base aérienne.
        Chaque drone est généré sur l'un des quatre côtés de la base
        (haut, droite, bas, gauche) selon son type.
        Avant la création, vérifie que la case de spawn n'est pas déjà occupée.
        Le drone est ensuite instancié via la base aérienne et ajouté aux liste necessaire.
    */
    //drone rouge, au dessus de la base (posY-1)
    public void createRedDrone() {
        int posX = aerialBase.getPosX();
        int posY = aerialBase.getPosY() - 1;
        if (!isCaseOccuped(posX, posY)) {
            RedDrone redDrone = aerialBase.createRedDrone(posX, posY, pilot);
            addDroneToListAndTrySpawnStone(redDrone);
        }
    }
    //drone bleu, a droite de la base (posX + Largeur)
    public void createBlueDrone() {
        int posX = aerialBase.getPosX() + aerialBase.getWidth();
        int posY = aerialBase.getPosY();
        if (!isCaseOccuped(posX, posY)) {
            BlueDrone blueDrone = aerialBase.createBlueDrone(posX, posY, cloud);
            addDroneToListAndTrySpawnStone(blueDrone);
        }
    }
    //drone orange, en bas de la base (posY + Hauteur)
    public void createOrangeDrone() {
        int posX = aerialBase.getPosX();
        int posY = aerialBase.getPosY() + aerialBase.getHeight();
        if (!isCaseOccuped(posX, posY)) {
            OrangeDrone orangeDrone = aerialBase.createOrangeDrone(posX, posY);
            addDroneToListAndTrySpawnStone(orangeDrone);
        }
    }
    //drone mauve, a gauche de la base (posX-1)
    public void createPurpleDrone() {
        int posX = aerialBase.getPosX() - 1;
        int posY = aerialBase.getPosY();
        if (!isCaseOccuped(posX, posY)) {
            PurpleDrone purpleDrone = aerialBase.createPurpleDrone(posX, posY);
            addDroneToListAndTrySpawnStone(purpleDrone);
        }
    }
    
    /*
        rajoute les drones au liste qu'il faut + essaie de creer la pierre philo.
        drone rajouter au debut de la liste pour apparaitre derriere les nuages
    */
    private void addDroneToListAndTrySpawnStone(Drone drone) {
        addDestroyableElementToList(drone);
        droneElementList.add(drone);
        tryCreatePhilosopherStone();
    }

    //Verifie que la case n'est pas deja occupé par un drone existant.
    private boolean isCaseOccuped(int posX, int posY) {
        for (Drone drone : droneElementList) {
            if (drone.getPosX() == posX && drone.getPosY() == posY) {
                return true;
            }
        }
        return false;
    }

    
    /*
        Fait apparaître un nombre de drones dépendant du niveau actuel.
        Pour générer les drones de manière aléatoire et sans doublon,
        une liste contenant les quatre types de drones est créée puis mélangée.
        La liste est ensuite parcourue selon le niveau, et chaque type rencontré
        déclenche la création du drone correspondant.
    */
    private void spawnDroneDependOfLevel(){
        ArrayList<Integer> nbOfDroneType = new ArrayList<Integer>();
        nbOfDroneType.add(RED_DRONE_TYPE);
        nbOfDroneType.add(BLUE_DRONE_TYPE);
        nbOfDroneType.add(ORANGE_DRONE_TYPE);
        nbOfDroneType.add(PURPLE_DRONE_TYPE);
        
        Collections.shuffle(nbOfDroneType);
        //System.out.println(nbOfDroneType);
        
        for (int i = 0; i < level; i++) {
            int droneType = nbOfDroneType.get(i);
            
            if (droneType == RED_DRONE_TYPE){
                createRedDrone();
            }
            else if(droneType == BLUE_DRONE_TYPE){
                createBlueDrone();
            }
            else if(droneType == ORANGE_DRONE_TYPE){
                createOrangeDrone();
            }
            else if(droneType == PURPLE_DRONE_TYPE){
                createPurpleDrone();
            }
        }
    }
    
    /*
        Contrôle tous les drones de la liste droneElementList.
        Boucle sur la liste de drone principal :
        - Si un drone est détruit, il est ajouté à une liste temporaire pour éviter plus tard 'ConcurrentModificationException'.
        - Sinon, il se déplace et tire un projectile si son cooldown est terminé.
        À la fin, tous les drones marqués comme détruits sont supprimés des différentes listes de jeu (droneElementList, gameElementList, destroyableGameElementList) en bouclant sur la liste temporaire !
    */
    private void controlDrone(){
        ArrayList<Drone> destroyedDroneList = new ArrayList<>();
        
        for (Drone drone : droneElementList) {
            if (drone.isDestroy()){
                destroyedDroneList.add(drone);
            }else{
                drone.move();
                
                if (drone.hasReloaded()){
                    int projectileYOffset = 1;
                    Projectile projectile = new Projectile(drone.getPosX(), drone.getPosY() + projectileYOffset, Projectile.DRONE_PROJECTILE_IMAGE, true);
                    projectileList.add(projectile);
                    gameElementList.add(FIRST_INDEX_LIST,projectile);
                }
            }
        }
        for (Drone destroyedDrone : destroyedDroneList) {
            removeDestroyableElementFromList(destroyedDrone);
            droneElementList.remove(destroyedDrone);
        }
    }
    
    
    /*
        Vérifie si la base aérienne est détruite.
        Si ses points de vie sont à zéro et que le niveau maximum n'est pas atteint,
        le niveau augmente, les points de vie de la base sont réinitialisés
        et de nouveaux drones sont générés.
        Si le niveau maximum est atteint, la partie se termine.
    */
    private void checkIfLevelUp() {
        if (aerialBase.isDestroy()) {
            if (level < MAX_LEVEL) {
                level++;
                
                aerialBase.setLife(aerialBase.getAerialBaseMaxLife()); 
                spawnDroneDependOfLevel();
            } else {
                setGameOver(true);
            }
        }
    }
    
    /*
        Gère l'état d'invincibilité de la base aérienne :
        - Tant qu'il y a des drones sur la map, la base reste invincible.
        - Si aucun drone n'est présent :
        • La base devient vulnérable pendant AERIAL_BASE_VULNERABILITY_DURATION si elle n'a pas encore été touchée.
        • Si la base a été touchée ou si le temps de vulnérabilité est écoulé, elle redevient invincible et de nouveaux drones sont générés via spawnDroneDependOfLevel().
        Le cheat d'invincibilité ignore toute cette logique.
    */
    private void changeInvincibilityOfAerialBase(){
        if (!aerialBaseInvincibiltyCheatActive){
            
            if (droneElementList.isEmpty()) {
                if (aerialBase.isInvincible() && !aerialBasehasBeenHit){
                    aerialBase.setInvincible(false);
                    aerialBaseVulnerabilityEndTime = System.currentTimeMillis() + AerialBase.AERIAL_BASE_VULNERABILITY_DURATION;//heure de fin de la vulnerabilty
                }
                else {
                    if (aerialBasehasBeenHit || System.currentTimeMillis() > aerialBaseVulnerabilityEndTime){
                        aerialBase.setInvincible(true);
                        spawnDroneDependOfLevel();
                    }
                }
            }
            //si des drones reapparaisse.
            else{
                if (!aerialBase.isInvincible()){
                    aerialBase.setInvincible(true);
                }
                aerialBaseVulnerabilityEndTime=0;
                aerialBasehasBeenHit = false;
            }
        }
    }
    
    
    /*
        Gère les projectiles : déplacements, collisions et destruction:
        - Déplace chaque projectile
        - Projectiles des drones : vérifient collision avec le pilote
        - Projectiles du pilote : vérifient collision avec tous les éléments destructibles (sauf lui-même)
        - Si collision avec la base : incrémente le score et marque la base comme touchée (si elle n'est pas invincible)
        - Les projectiles qui ont touché ou qui sortent de la carte sont marqués pour destruction
        - Utilise une liste temporaire pour éviter ConcurrentModificationException
        - Vérifie le Game Over si le pilote est détruit
    */
    private void manageProjectile() {
        ArrayList<Projectile> destroyedProjectileList = new ArrayList<>();
        
        for (Projectile projectile : projectileList){
            
            projectile.move();
            
            boolean hasHit = false;
            
            if (projectile.isDroneProjectile()){
                if (hasCollision(projectile, pilot)){
                    projectile.triggerAction(pilot);
                    hasHit = true;//pour detruire le projectile a la fin
                }
            }
            else {
                for (DestroyableGameElement element : destroyableGameElementList) {
                    if (element != pilot){
                        if (hasCollision(projectile, element)){
                            projectile.triggerAction(element);
                            hasHit = true;
                            if (element == aerialBase){
                                if(!aerialBase.isInvincible()){
                                    totalAerialBaseHeartDestroyed++;//pour le score de fin de partie
                                    aerialBasehasBeenHit = true;//pour savoir si la vulnerabilité de la base doit s'arreter
                                }
                            }
                        }
                    }
                }
            }
            
            //rajoute les projectile qui ont touché une cible ou ceux hors map dans la liste a detruire
            if (hasHit || projectile.isOutsideOfMap()) {
                destroyedProjectileList.add(projectile);
            }
        }
        
        //remove les projectile dans les vrai liste en bouclant sur la fausse.
        for (Projectile projectile : destroyedProjectileList){
            projectileList.remove(projectile);
            gameElementList.remove(projectile);
        }
        
        if (pilot.isDestroy()){
            setGameOver(true);
        }
    }


    /*
        Tente de créer une pierre philosophale selon la probabilité d'apparition.
        Une seule pierre peut exister à la fois.
    */
    private void tryCreatePhilosopherStone(){
        if (stoneList.isEmpty()) {
            if (PhilosopherStone.hasChanceOfSpawn()) {
                spawnStone();
            }
        }
    }

    /*
        Fait apparaître une pierre philosophale de type aléatoire (jaune, orange ou rouge) :
        - La pierre apparaît à la même position Y que le pilote.
        - La position X est choisie aléatoirement dans la zone valide (colonnes excluant la largeur combinée des téléporteurs gauche et droite).
        - Si la position générée correspond à celle du pilote, une nouvelle position est tirée.
        - La pierre est ajoutée aux listes de jeu : gameElementList, triggerableGameElementList et stoneList.
    */
    private void spawnStone() {
        int TPWidhtExclusion = leftTeleporter.getWidth()+rightTeleporter.getWidth();
        int validColumnRange = COLUMNS - TPWidhtExclusion;
        int offset=1;//utile pour decaler et faire que ca peut spawn entre posx1 et posx13 et pas entre posx0 et posx12
        int randomPosX = (int)(Math.random() * validColumnRange)+offset; 
        
        while (randomPosX == pilot.getPosX()) {
            randomPosX = (int)(Math.random() * validColumnRange)+offset;
        }
        
        String[] types = { 
            PhilosopherStone.TYPE_YELLOW, 
            PhilosopherStone.TYPE_ORANGE, 
            PhilosopherStone.TYPE_RED 
        };
        
        int randomIndex = (int)(Math.random() * types.length);
        String selectedType = types[randomIndex];
        
        PhilosopherStone stone = new PhilosopherStone(randomPosX, pilot.getPosY(), selectedType);
        addTriggerableElementToList(stone);
        stoneList.add(stone);
    }

    public void destroyPhilosopherStone(PhilosopherStone stone){
        removeTriggerableElementFromList(stone);
        stoneList.remove(stone);
    }


    private void spawnAutoMissile(int x, int y) {
        AutoMissile missile = new AutoMissile(x, y);
        addTriggerableElementToList(missile);
        autoMissileList.add(missile);
    }

    public void destroyAutoMissile(AutoMissile missile){
        removeTriggerableElementFromList(missile);
        autoMissileList.remove(missile);
    }


    /*
        Verifie les collisions du pilot avec les elements triggerable.
    */
    private void checkPilotCollision() {
        ArrayList<Triggerable> triggeredGameElementList = new ArrayList<>();
        
        for (Triggerable element : triggerableGameElementList) {
            if (hasCollision(pilot, (GameElement) element)) {//cast pour verif la collision
                triggeredGameElementList.add(element);
            }
        }
        //triggerAction l'element en bouclant sur la liste temporaire (car on supprime en meme temps)
        for (Triggerable element : triggeredGameElementList) {
            element.triggerAction(this);
        }
    }

    public void shootPilotProjectile() {
        if(pilot.hasReloaded()){
            
            Projectile projectile = new Projectile(pilot.getPosX(), pilot.getPosY(),Projectile.PILOT_PROJECTILE_IMAGE,false);
            projectileList.add(projectile);
            gameElementList.add(FIRST_INDEX_LIST,projectile);
        }
    }
    public void shootPilotAutoMissile(){
        if(pilot.hasAutoMissile()){
            Projectile projectile = new Projectile(pilot.getPosX(), pilot.getPosY(),AutoMissile.AUTO_MISSILE_IMAGE,false);
            projectileList.add(projectile);
            gameElementList.add(FIRST_INDEX_LIST,projectile);
            pilot.setHasAutoMissile(false);
        }
    }

    public void setDirection(int newDirection) {
        pilot.setDirection(newDirection);
    }


    /*
        Vérifie s'il y a une collision entre deux éléments du jeu.
        La collision est détectée si les coordonnées du triggerElement se trouvent à l'intérieur de la zone occupée par le targetElement.

        Paramètres :
        triggerElement – l'élément qui déclenche la collision
        targetElement – l'élément à tester contre

        Retour :
        boolean – true si les deux éléments se chevauchent, false sinon
    */
    public boolean hasCollision(GameElement triggerElement, GameElement targetElement) {
        return triggerElement.getPosX() >= targetElement.getPosX() && triggerElement.getPosX() < targetElement.getPosX() + targetElement.getWidth() &&
        triggerElement.getPosY() >= targetElement.getPosY() && triggerElement.getPosY() < targetElement.getPosY() + targetElement.getHeight();
    }


    /*
        Enregistre et affiche le score du joueur (fin de partie).
        Calcule la durée totale de la partie à partir du temps de début,
        écrit le score dans un fichier et affiche le classement.

        Paramètres :
            userName – nom du joueur
    */
    public void writeScore(String userName) {
        int millisToSecond = 1000;
        long gameDuration = (System.currentTimeMillis() - startTimeOfTheGame)/millisToSecond;
        ScoreWriterAndDisplayer.writeScoreIntoScoreFile(userName, totalAerialBaseHeartDestroyed, gameDuration);
        ScoreWriterAndDisplayer.displayScore(userName, totalAerialBaseHeartDestroyed, gameDuration);
    }


    private void addDestroyableElementToList(DestroyableGameElement element){
        gameElementList.add(FIRST_INDEX_LIST,element);
        destroyableGameElementList.add(element);
    }

    private void removeDestroyableElementFromList(DestroyableGameElement element){
        gameElementList.remove(element);
        destroyableGameElementList.remove(element);
    }

    private void addTriggerableElementToList(Triggerable element) {
        gameElementList.add(FIRST_INDEX_LIST, (GameElement) element);
        triggerableGameElementList.add(element);
    }

    private void removeTriggerableElementFromList(Triggerable element) {
        gameElementList.remove((GameElement) element);
        triggerableGameElementList.remove(element);
    }

    //METHODE CHEATCODE
    public void killPilot() {
        pilot.setLife(DestroyableGameElement.LIFE_TO_DESTROY);
    }

    public void hitBase() {
        int damage = 1;
        aerialBase.setLife(aerialBase.getLife()-damage);
        totalAerialBaseHeartDestroyed++;
    }

    public void spawnStoneCheat() {
        if (stoneList.isEmpty()) {
            spawnStone();
        }
    }

    public void togglePilotInvincibility(){
        boolean newStatus = !pilot.isCheatInvincibility();
        pilot.setCheatInvincibility(newStatus);
    }

    public void toggleAerialBaseInvincibilty(){
        if (aerialBaseInvincibiltyCheatActive) {
            aerialBaseInvincibiltyCheatActive = false;
        }
        else {
            aerialBaseInvincibiltyCheatActive = true;
            if (aerialBase.isInvincible()) {
                aerialBase.setInvincible(false);
            } else {
                aerialBase.setInvincible(true);
            }
        }
    }
    
    public void spawnAutoMissileCheat() {
        if (autoMissileList.isEmpty()) {
            int rightToPilot=pilot.getPosX() + 1;
            spawnAutoMissile(rightToPilot, pilot.getPosY());
        }
    }

    public void destroyAllDrone(){
        for (Drone drone : droneElementList) {
            drone.hit(drone.getLife());
        }
    }

    public void toggleCloudSpawn() {
        cloud.toggleSpawnCloud();
    }

    public void toggleRedDroneMovement() {
        RedDrone.toggleMovement();
    }
    public void toggleBlueDroneMovement() {
        BlueDrone.toggleMovement();
    }
    public void toggleOrangeDroneMovement() {
        OrangeDrone.toggleMovement();
    }
    public void togglePurpleDroneMovement() {
        PurpleDrone.toggleMovement();
    }

    
    // getter et setter
    public boolean isGameOver(){ return gameOver;}
    public void setGameOver(boolean state){ gameOver = state;}
    public int getLevel(){return level;}
    public int getTotalAerialBaseHeartDestroyed(){return totalAerialBaseHeartDestroyed;}
    
    //getter des elements/objet
    public ArrayList<GameElement> getGameElementList(){ return gameElementList;}
    public Pilot getPilot(){ return pilot; }
    public AerialBase getAerialBase(){return aerialBase;}
}