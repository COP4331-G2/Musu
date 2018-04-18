package musuapp.com.musu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailPostView extends AppCompatActivity {

    @InjectView(R.id.groupname) TextView author;
    @InjectView(R.id.like) CheckBox likeBtn;
    @InjectView(R.id.post_img) ImageView postImg;
    @InjectView(R.id.group_text) TextView postText;

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

        author.setText(bundle.getString("author"));
        postText.setText(bundle.getString("post_text"));
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("post_image"), 0,bundle.getByteArray("post_image").length );
        //postImg.setImageBitmap(bitmap);
        Picasso.with(this).load(bundle.getString("post_image")).into(postImg);




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
