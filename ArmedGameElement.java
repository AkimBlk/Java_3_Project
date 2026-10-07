public abstract class ArmedGameElement extends DestroyableGameElement{

    private long lastShootTime =0;
    private int fireRate=0;

    public ArmedGameElement(int posX, int posY, String[] imagePaths,int life,int fireRate){
        super(posX, posY, imagePaths,life);
        this.fireRate = fireRate;
    }

    /*
        Vérifie si l'arme peut tirer.
        Compare le temps écoulé depuis le dernier tir avec le cooldown de tir
        et réinitialise le temps du dernier tir si le cooldown est terminé.
        
        Retour :
            true si l'arme a rechargé, false sinon
    */
    public boolean hasReloaded(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= fireRate){
            lastShootTime = currentTime; // Mettre à jour le dernier timestamp
            return true;
        }
        return false;
    };
}