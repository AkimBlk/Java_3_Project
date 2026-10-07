public class Teleporter extends GameElement implements Triggerable{
    
    private int exitTeleporterPosX;

    public static final String[] YELLOW_TP_IMAGE = {
        "img/yellow_teleporter.png"
    };
    public static final String[] BLUE_TP_IMAGE = {
        "img/blue_teleporter.png"
    };
    public static final int TELEPORTER_HEIGHT=3;

    public Teleporter(int posX, int posY, String[] imagePaths,int exitTeleporterPosX) {
        super(posX, posY, imagePaths);
        this.exitTeleporterPosX=exitTeleporterPosX;
        this.setHeight(TELEPORTER_HEIGHT);
    }
    
    /*
        Action realisé lorsque le pilote passe sur un tp.
        Téléporte le pilote vers la position de sortie définie.
        Le pilote ne peut pas ce teleporter si il est invincible sauf si l'invincibilité vient de cheat.

        Paramètres :
            model – référence au modèle du jeu pour accéder au pilote
    */
    @Override
    public void triggerAction(HelbDroneModel model) {
        if(!model.getPilot().isInvincible() || model.getPilot().isCheatInvincibility()){
            model.getPilot().setPosX(exitTeleporterPosX);
        }
    }
}