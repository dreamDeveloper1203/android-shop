package store.technologycenter.android.services;

import android.content.Context;
import store.technologycenter.android.R;

public class Utility {


    public static String actionBarTitle(Context context, String value){
        return value.length() > 22 ? value.substring(0, 18)+ context.getString(R.string.ellipsis): value;
    }

    public static double editTextDoubleValue(CharSequence charSequence){
        String value = charSequence.toString();
        if(value.isEmpty() || value.equals(".")) return 0;
        try {
            return Double.parseDouble(value);
        }catch (Exception exception){
            return 0;
        }
    }
}
