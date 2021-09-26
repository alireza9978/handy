package ir.coleo.handy.models;

import android.content.Context;

import java.util.ArrayList;

import ir.coleo.handy.R;

public enum Usages {

    Neutral,
    Domain,
    DomainToNormal,
    Differences;

    public static ArrayList<String> getStrings(Context context){
        ArrayList<String> usages = new ArrayList<>();
        usages.add(context.getString(R.string.neutral));
        usages.add(context.getString(R.string.domain));
        usages.add(context.getString(R.string.domain_to_normal));
        usages.add(context.getString(R.string.differences));
        return usages;
    }


}
