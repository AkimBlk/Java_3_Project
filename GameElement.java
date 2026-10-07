public abstract class GameElement {

    private final String[] IMAGE_PATHS ;
    private int posX;
    private int posY;
    private int elementWidth=1;
    private int elementHeight=1;
    
    public GameElement(int posX, int posY, String[] imagePaths){
        this.posX = posX ; 
        this.posY = posY ;
        this.IMAGE_PATHS = imagePaths;
    }

    @Override
    public String toString(){
        return getClass().getName() +"X=" + getPosX() + ", Y=" + getPosY();
    }

    public String getPathToImage() {return IMAGE_PATHS[0];}
    public String getPathToImage(int index) {return IMAGE_PATHS[index];}
    public int getPathToImageLen() {return IMAGE_PATHS.length;}

    public int getPosX() {return posX;}
    public void setPosX(int newPosX) {posX = newPosX;}
    public int getPosY() {return posY;}
    public void setPosY(int newPosY) {posY = newPosY;}

    public int getWidth() {return elementWidth;}
    public void setWidth(int newWidth){elementWidth = newWidth;}
    public int getHeight(){return elementHeight;}
    public void setHeight(int newHeight) {elementHeight = newHeight;}
}