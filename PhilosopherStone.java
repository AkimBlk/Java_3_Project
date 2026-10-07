import java.util.HashMap;
import java.util.Map;

public class PhilosopherStone extends GameElement implements Triggerable {
    public static final String[] STONE_IMAGE = {
        "img/yellow_philosophers_stone.png",
        "img/orange_philosophers_stone.png",
        "img/red_philosophers_stone.png"
    };
    
    public static final String TYPE_YELLOW = "YELLOW";
    public static final String TYPE_ORANGE = "ORANGE";
    public static final String TYPE_RED    = "RED";

    private static final Map<String, String> PATH_TO_IMAGE_MAP = new HashMap<String, String>(){{
        put(TYPE_YELLOW, STONE_IMAGE[0]);
        put(TYPE_ORANGE, STONE_IMAGE[1]);
        put(TYPE_RED, STONE_IMAGE[2]);
    }};

    private static final Map<String, Integer> EFFECT_DURATION_MAP = new HashMap<String, Integer>(){{
        put(TYPE_YELLOW, 5000);
        put(TYPE_ORANGE, 10000);
        put(TYPE_RED, 15000);
    }};
    
    private int effectDuration;

    public PhilosopherStone(int posX, int posY,String stoneType) {
        super(posX, posY, new String[] {PATH_TO_IMAGE_MAP.get(stoneType)});
        this.effectDuration = EFFECT_DURATION_MAP.get(stoneType);
    }

    public static boolean hasChanceOfSpawn(){
        double chanceToSpawn = 0.25;
        return Math.random() < chanceToSpawn;
    }

    /*
        Action realisé lorsque le pilote passe sur la pierre.
        Rends le pilote invincible pendant un certain temps,
        puis détruit la pierre.
        
        Paramètres : model – pour accéder au pilote et au méthodes utile pour détruire la pierre
     */
    @Override
    public void triggerAction(HelbDroneModel model) {
        model.getPilot().setInvincible(effectDuration);
        model.destroyPhilosopherStone(this);
    }
}