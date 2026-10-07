public abstract class DestroyableGameElement extends GameElement{

    private int life;
    public static final int LIFE_TO_DESTROY = 0;

    public DestroyableGameElement(int posX, int posY, String[] imagePaths, int life){
        super(posX, posY, imagePaths);
        this.life = life;
    }
    
    @Override
    public String toString(){
        return super.toString() + ", pv="+getLife();
    }

    public void hit(int damage){
        if (life > LIFE_TO_DESTROY){
            life -= damage;
        }
    }
    
    public boolean isDestroy(){
        return life==LIFE_TO_DESTROY;
    }
    
    public int getLife(){
        return life;
    }
    public void setLife(int newLife){
        life = newLife;
    }
}