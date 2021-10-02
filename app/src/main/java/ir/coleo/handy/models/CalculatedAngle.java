package ir.coleo.handy.models;

public class CalculatedAngle {

    private final Angle angle;
    private final int imageAngle;

    public CalculatedAngle(Angle angle, int imageAngle) {
        this.angle = angle;
        this.imageAngle = imageAngle;
    }

    public Angle getAngle() {
        return angle;
    }

    public int getImageAngle() {
        return imageAngle;
    }

}
