package th.co.itg.ktb_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by ToCHe on 27/8/2018 AD.
 */
public  class FileUtils {

    public static String encodeImage(String path) {
        Bitmap bm =  BitmapFactory.decodeFile(path);
        if (bm != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,80,baos);
            return byteArrayToBase64(baos.toByteArray());
        }else {
            return "";
        }
    }

    public static String byteArrayToBase64(byte[] byteArrayImage){
        //"data:image/jpeg;base64,"
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

}
