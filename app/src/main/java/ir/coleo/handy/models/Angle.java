package ir.coleo.handy.models;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import ir.coleo.handy.R;

public enum Angle implements Serializable {

    /**
     * +y
     * |
     * |
     * -x -- -- +x
     * |
     * |
     * -y
     * <p>
     * -x = -1
     * +x = -2
     * -y = -3
     * +y = -4
     */

    Thumb_IP_Flexion(2, 3, 4, R.string.thumb_ip_flexion),
    Thumb_MCP_Flexion(3, 2, 1, R.string.thumb_mcp_flexion),
    Thumb_CMC_Radial_Abduction(4, 1, 8, R.string.thumb_cmc_radial_abduction),
    Finger_DIP_ROM(8, 7, 6, R.string.finger_dip_rom),
    Finger_PIP_ROM(7, 6, 5, R.string.finger_pip_rom),
    Finger_MCP_ROM(8, 5, 0, R.string.finger_mcp_rom);

    private int one;
    private int two;
    private int three;
    private int name;

    Angle(int one, int two, int three, int name) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.name = name;
    }

    public static ArrayList<Angle> getAngles() {
        ArrayList<Angle> items = new ArrayList<>();
        items.add(Thumb_IP_Flexion);
        items.add(Thumb_MCP_Flexion);
        items.add(Thumb_CMC_Radial_Abduction);
        items.add(Finger_DIP_ROM);
        items.add(Finger_PIP_ROM);
        items.add(Finger_MCP_ROM);
        return items;
    }

    public static ArrayList<String> getStrings(Context context) {
        ArrayList<String> usages = new ArrayList<>();
        usages.add(context.getString(R.string.thumb_ip_flexion));
        usages.add(context.getString(R.string.thumb_mcp_flexion));
        usages.add(context.getString(R.string.thumb_cmc_radial_abduction));
        usages.add(context.getString(R.string.finger_dip_rom));
        usages.add(context.getString(R.string.finger_pip_rom));
        usages.add(context.getString(R.string.finger_mcp_rom));
        return usages;
    }

    public int getOne() {
        return one;
    }

    public int getTwo() {
        return two;
    }

    public int getThree() {
        return three;
    }

    public int getName() {
        return name;
    }
}
