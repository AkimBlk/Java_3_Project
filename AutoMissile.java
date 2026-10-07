public class AutoMissile extends GameElement implements Triggerable {
    public static final String[] AUTO_MISSILE_IMAGE = {
        "img/self_guided_missile.png"
    };

    public AutoMissile(int posX, int posY) {
        super(posX, posY, AUTO_MISSILE_IMAGE);
    }

    /*
        Action realisé lorsque le pilote passe sur le missile.
        Donne un missile au pilote s'il n'en possède pas déjà,
        puis détruit le missile dans tous les cas.

        Paramètres : model – pour accéder au pilote et au méthodes utile pour détruire le missile
    */
    @Override
    public void triggerAction(HelbDroneModel model) {
        if(!model.getPilot().hasAutoMissile()){
            model.getPilot().setHasAutoMissile(true);
        }
        model.destroyAutoMissile(this);
    }
}