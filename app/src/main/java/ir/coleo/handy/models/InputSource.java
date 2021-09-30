package ir.coleo.handy.models;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import ir.coleo.handy.R;

public enum InputSource implements Serializable {

    Image,
    Video,
    Camera;

    public static ArrayList<String> getStrings(Context context) {
        ArrayList<String> usages = new ArrayList<>();
        usages.add(context.getString(R.string.image));
        usages.add(context.getString(R.string.video));
        usages.add(context.getString(R.string.camera));
        return usages;
    }

}
