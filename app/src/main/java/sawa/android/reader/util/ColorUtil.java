package sawa.android.reader.util;

import android.graphics.Color;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by mc100 on 2017/4/11.
 */

public class ColorUtil {

    public static int centerColor(int startColor, int endColor, int position) {
        DecelerateInterpolator interpolator = new DecelerateInterpolator();
        float interpolation = interpolator.getInterpolation(position);

        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int alpha = (int) (alphaStart + ((alphaEnd - alphaStart) * interpolation + 0.5));
        int red = (int) (redStart + ((redEnd - redStart) * interpolation + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * interpolation + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * interpolation + 0.5));
        return Color.argb(alpha, red, greed, blue);
    }
}
