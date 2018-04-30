package musuapp.com.musu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import musuapp.com.musu.utils.Utils;

public class DetailPostView extends AppCompatActivity {

    @InjectView(R.id.detail_user) TextView author;
    @InjectView(R.id.detail_like) CheckBox likeBtn;
    @InjectView(R.id.detail_img) NetworkImageView postImg;
    @InjectView(R.id.detail_text) TextView postText;
    ArrayList<String> tags;
    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    public static final String TAG = DetailPostView.class.getSimpleName();
    public static Context context;
    private static String userToken;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int userID;
        int postID;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post_view);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getBaseContext();


        Bundle bundle = getIntent().getExtras();

        mImageLoader = AppController.getInstance().getImageLoader();

        author   = findViewById(R.id.detail_user);
        likeBtn  = findViewById(R.id.detail_like);
        postImg  = findViewById(R.id.detail_img);
        postText = findViewById(R.id.detail_text);


        author.setText(bundle.getString("author"));
        likeBtn.setChecked(bundle.getBoolean("like"));
        postText.setText(bundle.getString("post_text"));
        userID = bundle.getInt("userID");
        postID = bundle.getInt("postID");
        this.userToken = bundle.getString("token", "null");
        final Post post = new Post(userID, postID);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(likeBtn.isChecked() == true){
                    // like button is now checked
                    Utils.LikeOrUnlikeImage(post, DetailPostView.userToken, apiURL, DetailPostView.context, TAG, true);
                    // api call to like post for user

                } else {
                    // like button is now unchecked
                    Utils.LikeOrUnlikeImage(post, DetailPostView.userToken, apiURL, DetailPostView.context, TAG, false);
                    // api call to dislike post for user

                }

            }
        });


        //Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("post_image"), 0,bundle.getByteArray("post_image").length );
        //postImg.setImageBitmap(bitmap);


        postImg.setImageUrl(bundle.getString("post_image"), mImageLoader);



        //try {
        //    Picasso.with(this).load(bundle.getString("post_image")).into(postImg);
        //} catch (Exception e) {
        //    Picasso.with(this).load(R.drawable.image_not_found).into(postImg);
        //}

        tags = bundle.getStringArrayList("post_tags");

        //This function displays the tags. when pass false also pass the len of the array. this will print all the tags .
        Utils.addTags(this, (RelativeLayout)findViewById(R.id.detail_tags), tags, tags.size(), false);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
            // if this doesn't work as desired, another possibility is to call `finish()` here.
            this.onBackPressed();
            //this.finish();
            return true;

        }
        return super.onOptionsItemSelected(item);

    }
}
