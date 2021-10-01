package ir.coleo.handy.models;

import android.content.Context;

import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutions.hands.Hands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.coleo.handy.R;

import static com.google.mediapipe.solutions.hands.Hands.HAND_CONNECTIONS;

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

    private final int one;
    private final int two;
    private final int three;
    private final int name;

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

    public ArrayList<AngleConnection> getAngleConnections() {
        ArrayList<AngleConnection> connections = new ArrayList<>();
        connections.add(new AngleConnection(getOne(), getTwo()));
        connections.add(new AngleConnection(getTwo(), getThree()));
        return connections;
    }

    public ArrayList<LandmarkProto.NormalizedLandmark> getHandLandmarkList(List<LandmarkProto.NormalizedLandmark> handLandmarkList) {
        ArrayList<LandmarkProto.NormalizedLandmark> result = new ArrayList<>();
        result.add(handLandmarkList.get(getOne()));
        result.add(handLandmarkList.get(getTwo()));
        result.add(handLandmarkList.get(getThree()));

        return result;
    }

    public String calculateAngle(List<LandmarkProto.NormalizedLandmark> landmarks) {
        double diff_one_two_width = landmarks.get(getOne()).getX() - landmarks.get(getTwo()).getX();
        double diff_one_two_height = landmarks.get(getOne()).getY() - landmarks.get(getTwo()).getY();
        double diff_two_three_width = landmarks.get(getThree()).getX() - landmarks.get(getTwo()).getX();
        double diff_two_three_height = landmarks.get(getThree()).getY() - landmarks.get(getTwo()).getY();
        double radians = Math.atan2(diff_two_three_height, diff_two_three_width) - Math.atan2(diff_one_two_height, diff_one_two_width);
        int degree = (int) Math.abs(radians * 180.0 / Math.PI);
        return degree + "°";
    }

}
