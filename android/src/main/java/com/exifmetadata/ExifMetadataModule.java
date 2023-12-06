package com.exifmetadata;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@ReactModule(name = ExifMetadataModule.NAME)
public class ExifMetadataModule extends ReactContextBaseJavaModule {
  public static final String NAME = "ExifMetadata";
  public Context reactConext;

  public ExifMetadataModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }




  @ReactMethod
  public void saveBase64intoImage(String base64String, Double lat, Double lng, String dateTime, Callback callback) {
    String uuid = UUID.randomUUID().toString();
    String fileName = uuid.replaceAll("-", "");

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      byte[] decodedBytes = Base64.getDecoder().decode(base64String);
      Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

      // Save the rotated bitmap to the file
      File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
      File imageFile = new File(storageDir, fileName + ".png");

      try (FileOutputStream fos = new FileOutputStream(imageFile)) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        String imagePath = imageFile.getAbsolutePath();
        ImageUtils.rotateImage(imagePath,90);
        Log.d("ImageFilePath", "Path: " + imagePath);
        addLocationToExif(imagePath, lat, lng, dateTime);

        callback.invoke(imagePath);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }


  private static String doubleToRationalLatLon(double value) {
    value = Math.abs(value);
    int degrees = (int) value;
    value = (value - degrees) * 60;
    int minutes = (int) value;
    value = (value - minutes) * 60000;
    int seconds = (int) value;

    return degrees + "/1," + minutes + "/1," + seconds + "/1000";
  }

  @ReactMethod
  public void addLocationToExif(String imagePath, double latitude, double longitude,String dateTime) {
    try {
      ExifInterface exifInterface = new ExifInterface(imagePath);

      // Save latitude and longitude
      exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, doubleToRationalLatLon(latitude));
      exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, doubleToRationalLatLon(longitude));

      // Save latitude and longitude reference
      exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude < 0 ? "S" : "N");
      exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude < 0 ? "W" : "E");

      exifInterface.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
      exifInterface.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, dateTime);

      // Save other necessary attributes (optional)
      exifInterface.saveAttributes();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @ReactMethod
  public void getImageExifData(String imagePath,Callback callback){
    try{
      ExifInterface exifInterface = new ExifInterface(imagePath);
      WritableNativeMap map = ImageUtils.getImageMetaData(exifInterface);

      if(callback != null){
        callback.invoke(map);
      }

    }catch (IOException e){
      e.printStackTrace();
    }
  }


}
