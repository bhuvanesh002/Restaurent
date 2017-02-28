package javadev.example.com.restaurant.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by earul on 4/28/15.
 */
public class FontUtil {

    private static final Map<String, Typeface> FontCache = new HashMap<String, Typeface>();

    public static Typeface getTypeface(Context context, String typefaceName) {
        Typeface typeface = FontCache.get(typefaceName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + typefaceName);
            FontCache.put(typefaceName, typeface);
        }
        return typeface;
    }

    public static void setTypeface(TextView view, String typefaceName) {
        view.setTypeface(getTypeface(view.getContext(), typefaceName));
    }
}

