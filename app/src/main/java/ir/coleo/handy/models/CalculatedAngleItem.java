package ir.coleo.handy.models;

public class CalculatedAngleItem {

    private final Angle angle;
    private final int firstImageAngle;
    private final int secondImageAngle;

    public CalculatedAngleItem(Angle angle, int firstImageAngle, int secondImageAngle) {
        this.angle = angle;
        this.firstImageAngle = firstImageAngle;
        this.secondImageAngle = secondImageAngle;
    }

    public Angle getAngle() {
        return angle;
    }

    public int getFirstImageAngle() {
        return firstImageAngle;
    }

    public String getFirstImageAngleString() {
        return firstImageAngle + "°";
    }

    public int getSecondImageAngle() {
        return secondImageAngle;
    }

    public String getSecondImageAngleString() {
        return secondImageAngle + "°";
    }

    public String anglesDifferent() {
        return anglesDifferentValue() + "°";
    }

    public int anglesDifferentValue() {
        return secondImageAngle - firstImageAngle;
    }
}
