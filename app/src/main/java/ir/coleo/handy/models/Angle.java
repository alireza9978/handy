package ir.coleo.handy.models;

import android.content.Context;

import java.util.ArrayList;

import ir.coleo.handy.R;

public enum Angle {

    /**
     *       +y
     *       |
     *       |
     *  -x -- -- +x
     *       |
     *       |
     *      -y
     *
     * -x = -1
     * +x = -2
     * -y = -3
     * +y = -4
     */

    Thumb_IP_Flexion(2,3,4),
    Thumb_MCP_Flexion(3,2,1),
    Thumb_CMC_Radial_Abduction(4,1,8),
    Finger_DIP_ROM(8,7,6),
    Finger_PIP_ROM(7,6,5),
    Finger_MCP_ROM(8,5,0);

    private int one;
    private int two;
    private int three;

    Angle(int one, int two, int three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public static ArrayList<String> getStrings(Context context){
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
}