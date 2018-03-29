package musuapp.com.musu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CreateNewPost extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REGUEST = 1;
    String currentPhotoPath;
    String cloudinaryLink;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        File testFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String testPath = testFile != null ? testFile.getPath() : null;
        Log.d("File path: ", testPath);
    }

    // Create a unique file name space for the image
    private File createPhotoPath() throws IOException
    {
        // Create image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // save the file path for later user
        // Important as we need to upload it later
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void takePicture(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check to see if there is an application to handle this intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create image file
            File imageFile = null;
            try
            {
                imageFile = createPhotoPath();
            } catch (IOException e)
            {
                // We caught an IOException
                Log.e("Error: ", e.toString());
            }

            // Continue now that image has a file path
            // but still check that everything is ok
            if(imageFile != null)
            {
                Uri imageUri = FileProvider.getUriForFile(this, "musuapp.com.musu.fileprovider", imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void chosePicture(View view)
    {
        Intent intent = new Intent();
        intent.setType("iamge/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REGUEST);
    }

    public void uploadImage(View view)
    {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        // Upload the image to the Cloudinary server
        MediaManager.get().upload(currentPhotoPath).unsigned("musu_preset").constrain(TimeWindow.immediate()).callback(new UploadCallback() {
            ProgressBar progressBar = findViewById(R.id.progressBar);

            @Override
            public void onStart(String requestId) {
            }
            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                // post progress to app UI (e.g. progress bar, notification)
                double progress = (double) bytes/totalBytes;
                int intProgress = (int) progress;
                progressBar.setProgress(intProgress);
            }
            @Override
            public void onSuccess(String requestId, Map resultData) {
                cloudinaryLink = resultData.get("url").toString();
                progressBar.setProgress(100);

                Intent intent = getIntent();
                int userID = intent.getIntExtra("userID", 0);
                // Add API call to store image and information
            }
            @Override
            public void onError(String requestId, ErrorInfo error) {
                // your code here
            }
            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                // your code here
            }}).dispatch();
    }

    // Return function catching image from camera app
    // and file returned from media chooser
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        // Get the views by ID and put them in objects
        TextView textView = findViewById(R.id.textView2);
        ImageButton imageButton = findViewById(R.id.imageButton);

        // If we requested an image and it was taken
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // Create a file out of the imagepath
            // This is the imagepath we passed to the camera app
            File imgFile = new File(currentPhotoPath);

            // If the file exists
            if(imgFile.exists()) {

                // Hide the ImageButton "Tap to take Picture"
                textView.setVisibility(View.GONE);

                //Create a Bitmap from the image filepath
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                int newHeight = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 512, newHeight, true);

                // Set the image view to the freshly created Bitmap
                imageButton.setImageBitmap(scaledBitmap);
            }
        }
        // TODO: This needs to be fixed because it always evaluates to FALSE according to Android Studio
        // If we requested an image from the gallery and we got one back
        // also if the data and getData are not null for safety
        else if(requestCode == PICK_IMAGE_REGUEST && data != null && data.getData() != null)
        {
            // Create Uri object with the image path
            // The image path from the file choser is in the data.getData()
            Uri imageUri = data.getData();

            try {
                // Create a Bitmap from the image Uri
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Set the imageView to the newly created Bitmap
                imageButton.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                // Error Handling
                e.printStackTrace();
            }
        }
    }
}
