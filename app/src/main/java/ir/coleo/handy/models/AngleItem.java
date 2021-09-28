package ir.coleo.handy.models;

import java.util.ArrayList;
import java.util.List;

public class AngleItem {

    private final Angle angle;
    private boolean selected;

    public AngleItem(Angle angle) {
        this.angle = angle;
        selected = false;
    }

    public static ArrayList<AngleItem> getAll() {
        ArrayList<AngleItem> items = new ArrayList<>();
        for (Angle angle: Angle.getAngles()){
            items.add(new AngleItem(angle));
        }
        return items;
    }

    public void select() {
        this.selected = !selected;
    }

    public Angle getAngle() {
        return angle;
    }

    public boolean isSelected() {
        return selected;
    }
}
