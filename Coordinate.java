public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDistanceWith(Coordinate coord) {
        return Math.sqrt((coord.y - this.y) * (coord.y - this.y) + (coord.x - this.x) * (coord.x - this.x));
    }
}