package ir.coleo.handy.constant;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class Constants {

    public static final String INPUT_METHOD = "SOURCE_TO_DETECTION";
    public static final String ANGLE_INTENT_DATA = "ANGLE_TO_SHARE";

    public static final String DETECTION_ANGLE = "ANGLE_TO_DETECTION";

    public static final String INPUT_IMAGE_ONE = "IMAGE_ONE_TO_DETECTION";
    public static final String INPUT_IMAGE_TWO = "IMAGE_TWO_TO_DETECTION";
    public static final String INPUT_VIDEO = "VIDEO_TO_DETECTION";

    public static String createImageFromBitmap(Bitmap bitmap, Context context, boolean first) {
        String fileName;
        if (first) {
            fileName = "firstImage";//no .png or .jpg needed
        }else{
            fileName = "secondImage";//no .png or .jpg needed
        }
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }


}
