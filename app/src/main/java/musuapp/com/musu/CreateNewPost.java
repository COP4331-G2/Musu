package musuapp.com.musu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import musuapp.com.musu.utils.Utils;

public class CreateNewPost extends AppCompatActivity {

    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    public static final String TAG = CreateNewPost.class.getSimpleName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1001;
    String currentPhotoPath;
    String cloudinaryLink = "";
    private EditText bodyText;
    private EditText tags;
    private Bitmap currentBitmap;
    SharedPreferences access;
    private ProgressDialog progressDialog;
    private ProgressBar suggest_loader;
    private View createNewPostView;
    //int id;
    private ArrayList<String> tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        File testFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String testPath = testFile != null ? testFile.getPath() : null;
        Log.d("File path: ", testPath);
        bodyText = findViewById(R.id.editText2);
        tags = findViewById(R.id.postTags);
        access = getSharedPreferences("Login", MODE_PRIVATE);
        progressDialog = new ProgressDialog(CreateNewPost.this,
                R.style.AppTheme_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        suggest_loader = findViewById(R.id.suggest_loader);
        tagList = new ArrayList<String>();

        tags.addTextChangedListener(commaPressed);
    }

    TextWatcher commaPressed = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String tp = s.toString();
            if(s.length() > 0) {
                if (tp.charAt(tp.length() - 1) == ',') {

                    Log.w("TEXT CHANGED", tp.substring(0, tp.lastIndexOf(',')));
                    tagList.add(tp.substring(0, tp.lastIndexOf(',')));
                    createTags();
                    s.clear();


                }
            }
        }
    };

    public void createTags(){

        int counter = 0;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        final RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.create_tags);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.i("WIDTH OF AREA:", String.valueOf(screenWidth));

        //int id = 0;
        wrapper.removeAllViews();

        int currCounter = 0;
        int currWidth = 0;
        boolean isNewLine = false;
        boolean firstLine = false;



        for(int id = 0; id < tagList.size(); id++){

            TextView tag = new TextView(this);
            String text = tagList.get(id);
            tag.setText(text.trim());



            tag.setTextSize(16);
            tag.setTextColor(getResources().getColor(R.color.black));
            tag.setId(4000+id);
            tag.setElevation(3);
            tag.setBackgroundResource(R.drawable.tag_create);
            tag.measure(0,0);
            final int index = id;
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagList.remove(index);
                    createTags();
                }
            });

            RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp1.setMargins(10, 10,10,20);

            //tag.setMaxLines(1);

            int width =  tag.getMeasuredWidth();//text.length()*15;

            Log.i("WIDTH OF TAG", String.valueOf(width));

                if ((currWidth + width + 100) <= screenWidth) {
                    currWidth += width;
                    isNewLine = false;
                    currCounter++;
                } else {
                    currWidth = width;
                    firstLine = false;
                    isNewLine = true;
                    currCounter = 1;
                }

                if (id == 0) {
                    rlp1.addRule(RelativeLayout.ALIGN_START);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);
                } else if (isNewLine) {
                    rlp1.addRule(RelativeLayout.ALIGN_LEFT);
                    rlp1.addRule(RelativeLayout.BELOW, 4000 - 1 + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);
                } else if (firstLine) {
                    rlp1.addRule(RelativeLayout.RIGHT_OF, 4000 - 1 + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);

                } else {

                    rlp1.addRule(RelativeLayout.RIGHT_OF, 4000 - 1 + id);
                    rlp1.addRule(RelativeLayout.BELOW, 4000 - currCounter + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);

                }
            }

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

    public void checkCameraPermissions(View view) {
        // Check to see if CAMERA permission has been granted first
        if (ContextCompat.checkSelfPermission(CreateNewPost.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Save the view into a global variable
            createNewPostView = view;

            // If the permission has NOT already been granted, request the permissions
            ActivityCompat.requestPermissions(CreateNewPost.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            // If the permission has already been granted

            takePicture(view);
        }
    }

    public void checkReadExternalStoragePermissions(View view) {
        // Check to see if READ_EXTERNAL_STORAGE permission has been granted first
        if (ContextCompat.checkSelfPermission(CreateNewPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Save the view into a global variable
            createNewPostView = view;

            // If the permission has NOT already been granted, request the permissions
            ActivityCompat.requestPermissions(CreateNewPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // If the permission has already been granted

            chosePicture(view);
        }
    }

    public void chosePicture(View view)
    {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(pickIntent, PICK_IMAGE);
    }

    public void uploadImage(String imagePath)
    {
        // Upload the image to the Cloudinary server
        MediaManager.get().upload(imagePath).unsigned("musu_preset").constrain(TimeWindow.immediate()).callback(new UploadCallback() {

            @Override
            public void onStart(String requestId) {

            }
            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
            }
            @Override
            public void onSuccess(String requestId, Map resultData) {
                cloudinaryLink = resultData.get("url").toString();
                // Unfreeze screen when upload is done
                progressDialog.dismiss();

                //Intent intent = getIntent();
                //int userID = intent.getIntExtra("userID", 0);
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

        // Close Activity and return to MainActivity

        // NOTE: The image and post upload will continue while this is happening
        // Check for upload in Android Notifications? Maybe.

    }

    // This will trigger when the
    // suggest Tags button is pushed
    public void suggestTags(View view)
    {
        if(cloudinaryLink == "")
        {
            Toast.makeText(this, "Take or Select a pic first", Toast.LENGTH_SHORT).show();
        }
        else {

            // Show the loader
            suggest_loader.setVisibility(View.VISIBLE);

            // Get the body text from bodyText
            String bodyText = this.bodyText.getText().toString();

            // Volley call that will send imageURL to API
            String token = access.getString("token", "");
            Integer userID = access.getInt("userID", -1);

            // Build a map with the parameters I want to send to server
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("function", "suggestTags");
            postParam.put("imageURL", cloudinaryLink);
            postParam.put("bodyText", bodyText);

            // JSON Object to send to the server
            JSONObject parameters = new JSONObject(postParam);

            // Building the actual request
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, apiURL, parameters,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Logic goes here


                            try {
                                // Declare objects
                                String tempTag;
                                String tagList = "";

                                // Get the JSON Array with the Posts
                                JSONArray jsonArray = response.getJSONArray("results");

                                // Parse through the json array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    // Get the String
                                    tempTag = jsonArray.getString(i);

                                    // Create a total String
                                    tagList = tagList + tempTag + ", ";
                                }

                                // Add the text to the editText
                                tags.setText(tagList);

                                suggest_loader.setVisibility(View.INVISIBLE);


                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Print VolleyLog to the console
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                }
            }); // !! The request building of "jsonObjReq" ends here !!

            // Add the Request to the queue and execute
            AppController.getInstance().addToRequestQueue(jsonObjReq, "json_obj_req");
        }
    }


    public void createPost(View view)
    {
        // Make sure there is an image to upload
        if(cloudinaryLink == "")
        {
            Toast.makeText(this, "Either take or select an image first", Toast.LENGTH_SHORT).show();
        }
        else{
            // Start volley for making a new post
            // Show the loader
            suggest_loader.setVisibility(View.VISIBLE);

            // Get the body text from bodyText
            String bodyText = this.bodyText.getText().toString();

            // Volley call that will send imageURL to API
            String token = access.getString("token", "");
            Integer userID = access.getInt("userID", -1);

            // JSON Object to send to the server
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("function", "createPost");
                parameters.put("imageURL", cloudinaryLink);
                parameters.put("bodyText", bodyText);
                parameters.put("userID", userID);
                parameters.put("token", token);
                parameters.put("tags", new JSONArray(this.tags.getText().toString().split(",")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("JSON Payload", parameters.toString());

            // Building the actual request
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, apiURL, parameters,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Logic goes here


                            try {
                                if(response.getBoolean("success")) {
                                    Log.e("Some JSON Stuff", "TRUE");
                                    finish();
                                } else{
                                    Log.e("Some JSON Stuff", "FALSE");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }

                            suggest_loader.setVisibility(View.INVISIBLE);


                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Print VolleyLog to the console
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                }
            }); // !! The request building of "jsonObjReq" ends here !!

            // Add the Request to the queue and execute
            AppController.getInstance().addToRequestQueue(jsonObjReq, "json_obj_req");
        }
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

            progressDialog.show();

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
                this.currentBitmap = Bitmap.createScaledBitmap(imageBitmap, 512, newHeight, true);

                // Set the image view to the freshly created Bitmap
                imageButton.setImageBitmap(currentBitmap);

                // Upload Image
                uploadImage(currentPhotoPath);
            }
        }

        // If we requested an image from the gallery and we got one back
        // also if the data and getData are not null for safety
        else if(requestCode == PICK_IMAGE && data != null && data.getData() != null)
        {

            progressDialog.show();
            // Create Uri object with the image path
            // The image path from the file choser is in the data.getData()
            Uri imageUri = data.getData();

            try {
                // Create a Bitmap from the image Uri
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                //Scale down image
                int newHeight = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
                this.currentBitmap = Bitmap.createScaledBitmap(imageBitmap, 512, newHeight, true);

                // Set the imageView to the newly created Bitmap
                imageButton.setImageBitmap(currentBitmap);

                // Upload this image
                uploadImage(getRealPathFromUri(getBaseContext(), imageUri));
            } catch (IOException e) {
                // Error Handling
                e.printStackTrace();
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    /**
     * react to the user tapping the back/up icon in the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    takePicture(createNewPostView);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    chosePicture(createNewPostView);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
