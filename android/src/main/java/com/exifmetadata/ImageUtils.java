package com.exifmetadata;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.facebook.react.bridge.WritableNativeMap;

import java.io.IOException;

public class ImageUtils {

  public static void rotateImage(String imagePath,int rotation) {
    try {
      // Load the original image using BitmapFactory
      Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);

      // Create a rotation matrix
      Matrix matrix = new Matrix();
      matrix.postRotate(rotation);

      // Rotate the original bitmap
      Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

      // Save the rotated bitmap back to the file
      saveBitmapToFile(rotatedBitmap, imagePath);

    } catch (Exception e) {
      Log.e("ImageUtils", "Error rotating image: " + e.getMessage());
    }
  }

  private static void saveBitmapToFile(Bitmap bitmap, String filePath) {
    try {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new java.io.FileOutputStream(filePath));
    } catch (java.io.IOException e) {
      Log.e("ImageUtils", "Error saving rotated image: " + e.getMessage());
    }
  }



  public static double convertToDecimal(String rationalLatLon){
    String[] parts = rationalLatLon.split(",");

    // Extract the degrees, minutes, and seconds parts
    int degrees = Integer.parseInt(parts[0].split("/")[0]);
    int minutes = Integer.parseInt(parts[1].split("/")[0]);
    int seconds = Integer.parseInt(parts[2].split("/")[0]);

    // Calculate the decimal value
    double decimalValue = degrees + (minutes / 60.0) + (seconds / 3600000.0);

    // Check if the value is South or West and adjust the sign accordingly
    if (rationalLatLon.contains("S") || rationalLatLon.contains("W")) {
      decimalValue = -decimalValue;
    }

    return decimalValue;
  }

  public static WritableNativeMap getImageMetaData(ExifInterface exifInterface){
    String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
    String contrast = exifInterface.getAttribute(ExifInterface.TAG_CONTRAST);
    String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
    String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
    String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
    String dateTimeOriginal = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
    String gpsAltitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
    String gpsAltitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);

    WritableNativeMap map = new WritableNativeMap();

    map.putString("orientation",orientation);
    map.putString("contrast",contrast);
    map.putString("latitude",String.valueOf(ImageUtils.convertToDecimal(latitude)));
    map.putString("longitude",String.valueOf(ImageUtils.convertToDecimal(longitude)));
    map.putString("dateTime",dateTime);
    map.putString("dateTimeOriginal",dateTimeOriginal);
    map.putString("gpsAltitude",gpsAltitude);
    map.putString("gpsAltitudeRef",gpsAltitudeRef);

    return map;
  }
}


