import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class HelbDroneView {

    private static final String[] LEVEL_IMAGE = {
        "img/level_1.png",
        "img/level_2.png",
        "img/level_3.png",
        "img/level_4.png"
    };

    private Scene scene;
    private GraphicsContext gc;

    private Map<String, Image> imageMap = new HashMap<>();//map qui va contenir les path des images + l'image 
    
    public HelbDroneView(Stage primaryStage) {
        primaryStage.setTitle("HELBDrone");
        Group root = new Group();
        Canvas canvas = new Canvas(HelbDroneModel.WIDTH, HelbDroneModel.HEIGHT);
        root.getChildren().add(canvas);
        scene = new Scene(root);
        gc = canvas.getGraphicsContext2D();
        primaryStage.setScene(scene);
        primaryStage.show();
        loadImage();
    }

    /*
        Charge l'ensemble des images du jeu.
        Ajoute toutes les images nécessaires dans la imageMap
        afin qu'elles puissent être réutilisées plus tard sans rechargement.
    */
    private void loadImage() {
        addToMap(Pilot.PILOT_IMAGE);
        addToMap(Pilot.PILOT_HEART);
        addToMap(Teleporter.BLUE_TP_IMAGE);
        addToMap(Teleporter.YELLOW_TP_IMAGE);
        addToMap(AerialBase.AERIAL_BASE_IMAGE);
        addToMap(AerialBase.AERIAL_BASE_HEART);
        addToMap(Projectile.PILOT_PROJECTILE_IMAGE);
        addToMap(Projectile.DRONE_PROJECTILE_IMAGE);
        addToMap(RedDrone.RED_DRONE_IMAGE);
        addToMap(BlueDrone.BLUE_DRONE_IMAGE);
        addToMap(OrangeDrone.ORANGE_DRONE_IMAGE);
        addToMap(PurpleDrone.PURPLE_DRONE_IMAGE);
        addToMap(PhilosopherStone.STONE_IMAGE);
        addToMap(Cloud.CLOUD_IMAGE);
        addToMap(AutoMissile.AUTO_MISSILE_IMAGE);
        addToMap(LEVEL_IMAGE);
    }


    /*
        Ajoute un ensemble d'images à la imageMap.
        Charge chaque image à partir de son chemin afin de pouvoir la réutiliser plus tard.

        Paramètres :
            paths – chemins des images à charger
    */
    private void addToMap(String[] paths) {
        for(String path : paths){
         imageMap.put(path, new Image(path));
        }
    }
    
    //appeller a chaque frame
    public void refreshGame(HelbDroneModel model) {
        drawBackground(gc);
        drawGameElements(model);
        drawTopZone(model);
        drawGameOver(model);
    }
    
    //dessine le fond + les zones
    private void drawBackground(GraphicsContext gc) {
        int width=HelbDroneModel.WIDTH;
        int height=HelbDroneModel.HEIGHT;

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, width, height);

        //bord du haut + bas
        gc.strokeRect(0, 0, width, HelbDroneModel.MIDDLE_ZONE_START * HelbDroneModel.SQUARE_SIZE);
        gc.strokeRect(0, HelbDroneModel.MIDDLE_ZONE_START * HelbDroneModel.SQUARE_SIZE, width, HelbDroneModel.MIDDLE_ZONE_HEIGHT * HelbDroneModel.SQUARE_SIZE);
    }
    
    private void drawGameElements(HelbDroneModel model) {
        for (GameElement element : model.getGameElementList()) {
            
            String path = element.getPathToImage();//recup le path de l'image actuelle de l'element
            Image img = imageMap.get(path);//recup l'image qu'il faut dans la map via le path recuperer avant

            gc.drawImage(img, 
                element.getPosX() * HelbDroneModel.SQUARE_SIZE, 
                element.getPosY() * HelbDroneModel.SQUARE_SIZE, 
                element.getWidth() * HelbDroneModel.SQUARE_SIZE, 
                element.getHeight() * HelbDroneModel.SQUARE_SIZE);
        }
    }


    /*
        Dessine les éléments de la zone d'information située en haut de l'écran.
        Affiche les points de vie du pilote et de la base aérienne sous forme de coeurs,
        ainsi que l'image correspondant au niveau actuel.
        Récupère les données (PV et niveau) depuis le modèle
        et dessine les éléments aux positions appropriées.

        Paramètres :
            model – modèle du jeu contenant le pilote, la base aérienne et le niveau actuel
    */
    private void drawTopZone(HelbDroneModel model) {

        int iconSize = HelbDroneModel.SQUARE_SIZE;

        int topMargin = 10;
        int posY = HelbDroneModel.MIDDLE_ZONE_START * topMargin;

        //ou commencerons les coeurs (par rapport au bord gauche/droit)
        int xPilotHeart = 10;//bord gauche
        int xBaseHeart = HelbDroneModel.WIDTH - iconSize-xPilotHeart;//bord droit
        
        //pv des elements
        int pilotLife = model.getPilot().getLife();
        int baseLife = model.getAerialBase().getLife();
        
        // recup les images des coeurs
        Image pilotHeart = imageMap.get(Pilot.PILOT_HEART[0]);
        Image baseHeart = imageMap.get(AerialBase.AERIAL_BASE_HEART[0]);
        
        int spacing = 50;

        //coeur du pilote
        for (int i = 0; i < pilotLife; i++) {
            gc.drawImage(pilotHeart,
                xPilotHeart, posY, iconSize, iconSize);
            xPilotHeart += spacing; // ecart entre les coeurs, decale a droite
        }   
        
        //coeur de la base aerienne
        for (int i = 0; i < baseLife; i++) {
            gc.drawImage(baseHeart,
                xBaseHeart, posY, iconSize, iconSize);
            xBaseHeart -= spacing; // decale a gauche a chaque coeur
        }

        //level
        int level = model.getLevel();
        int levelArrayOffset = 1;
        String path = LEVEL_IMAGE[level - levelArrayOffset];
        Image levelImg = imageMap.get(path);
        
        int halfDivider = 2;
        int posXCenter = HelbDroneModel.WIDTH / halfDivider;

        int addSize=20;
        gc.drawImage(levelImg, 
            posXCenter, posY, iconSize+addSize, iconSize+addSize);
    }

    private void drawGameOver(HelbDroneModel model){
        if(model.isGameOver()){

            int fontSize = 40;
            int horizontalOffsetDivisor = 5;
            int verticalOffsetDivisor = 4;

            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", fontSize));
            gc.fillText("Game Over : \n" + model.getTotalAerialBaseHeartDestroyed() + " heart destroyed",HelbDroneModel.HEIGHT/horizontalOffsetDivisor, HelbDroneModel.HEIGHT / verticalOffsetDivisor);
        }
    }

    /*
        Retourne la scène associée à la vue.
        Permet au contrôleur d'accéder à la scène afin de définir
        les actions clavier et autres événements.
        
        Retour :
            scène du jeu
    */
    public Scene getScene() {
        return scene;
    }

    /*pour info, parametre draw image : 
    gc.drawImage(image,
        posX, posY, Largeur, Hauteur)
    */
}
