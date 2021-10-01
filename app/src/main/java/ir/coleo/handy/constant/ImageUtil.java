package ir.coleo.handy.constant;

import android.graphics.Bitmap;

public class ImageUtil {

    public static float getRatio(Bitmap bitmap) {
        return (float) bitmap.getWidth() / (float) bitmap.getHeight();
    }

    public static Bitmap scale(Bitmap bitmap, int targetHeight) {
        return Bitmap.createScaledBitmap(bitmap, (int) (getRatio(bitmap) * targetHeight), targetHeight, false);
    }

}
