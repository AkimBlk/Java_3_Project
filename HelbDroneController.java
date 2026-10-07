import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Scanner;

public class HelbDroneController {

    private static final int SPEED_OF_GAME = 200;
    private HelbDroneModel model;
    private HelbDroneView view;

    private Timeline timeline;//declarer ici comme ca apres je peux stop la timeline quand c'est gameover
    
    //Instancie le model+la vue et start la boucle du jeu et le setActions
    public HelbDroneController(Stage primaryStage) {
        this.model = new HelbDroneModel();
        this.view = new HelbDroneView(primaryStage);
        setActions();
        cycleGame();
    }
    
    private void setActions() {
        view.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event){
                //interactions du pilote
                KeyCode code = event.getCode();    
                if (code == KeyCode.RIGHT) {
                    model.setDirection(HelbDroneModel.RIGHT);
                }
                else if (code == KeyCode.LEFT) {
                    model.setDirection(HelbDroneModel.LEFT);
                }
                else if (code == KeyCode.DOWN){
                    model.setDirection(HelbDroneModel.DOWN);
                }
                else if (code == KeyCode.UP){
                    model.shootPilotProjectile();
                }
                else if (code == KeyCode.F){
                    model.shootPilotAutoMissile();
                }

                //cheat code (Bleu)(A/Z/E/R)
                else if(code == KeyCode.A){
                    model.createRedDrone();
                }
                else if(code == KeyCode.Z){
                    model.createBlueDrone();
                }
                else if(code == KeyCode.E){
                    model.createOrangeDrone();
                }
                else if(code == KeyCode.R){
                    model.createPurpleDrone();
                }

                // cheat code (Vert)(W/X/C/V)
                else if(code == KeyCode.W){
                    model.toggleRedDroneMovement();
                } 
                else if(code == KeyCode.X){
                    model.toggleBlueDroneMovement();
                }
                else if(code == KeyCode.C){
                    model.toggleOrangeDroneMovement();
                } 
                else if(code == KeyCode.V){
                    model.togglePurpleDroneMovement();
                }
                
                //cheat code (Orange)(J/K/L/M)
                else if(code == KeyCode.J){
                    model.hitBase();
                } 
                else if(code == KeyCode.K){
                    model.toggleAerialBaseInvincibilty();
                }
                else if(code == KeyCode.L){
                    model.destroyAllDrone();
                }
                else if(code == KeyCode.M){
                    model.killPilot();
                }
                
                //cheat code (Mauve)(U/I/O/P)
                else if(code == KeyCode.U){
                    model.spawnStoneCheat();
                } 
                else if(code == KeyCode.I){
                    model.spawnAutoMissileCheat();
                } 
                else if(code == KeyCode.O){
                    model.togglePilotInvincibility();
                }
                else if(code == KeyCode.P){
                    model.toggleCloudSpawn();
                }
            }
        });
    }
    
    //update le model et la vue en boucle et si game over, demande l'username
    private void cycleGame() {
        
        timeline = new Timeline(new KeyFrame(Duration.millis(SPEED_OF_GAME), e -> {

            if(model.isGameOver()){
                timeline.stop();
                
                Scanner myObj = new Scanner(System.in);
                System.out.println("Game Over, Enter username :");
                String userName = myObj.nextLine();
                
                model.writeScore(userName);
            }else{
                model.updateGame();
                view.refreshGame(model);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
