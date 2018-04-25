package musuapp.com.musu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailPostView extends AppCompatActivity {

    @InjectView(R.id.groupname) TextView author;
    @InjectView(R.id.like) CheckBox likeBtn;
    @InjectView(R.id.post_img) ImageView postImg;
    @InjectView(R.id.group_text) TextView postText;
    @InjectView(R.id.detail_tags) GridLayout tagArea;
    ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post_view);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        author   = findViewById(R.id.groupname);
        likeBtn  = findViewById(R.id.like);
        postImg  = findViewById(R.id.post_img);
        postText = findViewById(R.id.group_text);
        tagArea  = findViewById(R.id.detail_tags);

        author.setText(bundle.getString("author"));
        postText.setText(bundle.getString("post_text"));
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("post_image"), 0,bundle.getByteArray("post_image").length );
        //postImg.setImageBitmap(bitmap);

        try {
            Picasso.with(this).load(bundle.getString("post_image")).into(postImg);
        } catch (Exception e) {
            Picasso.with(this).load(R.drawable.image_not_found).into(postImg);
        }

        tags = bundle.getStringArrayList("post_tags");

        for(int i = 0; i < tags.size(); i++){
            TextView temp = new TextView(this);
            temp.setText(tags.get(i));
            temp.setBackgroundResource(R.drawable.tag_view);
            temp.setElevation(5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            temp.setLayoutParams(params);
            //pTags.add(temp);
            tagArea.addView(temp);
        }






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
