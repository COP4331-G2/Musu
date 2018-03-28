package musuapp.com.musu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Kevin Santana on 3/26/2018.
 */

public class ContactInfo {
      protected String title;
      protected String details;
      protected ImageView img;

      void setImage(String url)
      {
          DownloadImageTask dit = new DownloadImageTask(img);
          dit.execute(url);
          this.img = dit.returnImage();

      }


      private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
      ImageView bmImage;

      public DownloadImageTask(ImageView bmImage) {
          this.bmImage = bmImage;
      }

      protected Bitmap doInBackground(String... urls) {
          String urldisplay = urls[0];
          Bitmap mIcon11 = null;
          try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
          } catch (Exception e) {
              Log.e("Error", e.getMessage());
              e.printStackTrace();
          }
          return mIcon11;
      }

      protected void onPostExecute(Bitmap result) {
          bmImage.setImageBitmap(result);
      }
      protected ImageView returnImage()
      {
          return this.bmImage;
      }

}
}
