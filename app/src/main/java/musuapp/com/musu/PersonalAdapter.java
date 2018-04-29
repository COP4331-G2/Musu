package musuapp.com.musu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import musuapp.com.musu.utils.Utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.squareup.picasso.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import musuapp.com.musu.utils.*;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ContactViewHolder> {

    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    public static final String TAG = PersonalFragment.class.getSimpleName();
    private List<Post> postList;
    public static Context context;
    private RecyclerView recyclerView;
    private static Activity fragment;
    private FloatingActionButton cPost;
    private static String userToken;
    private static int userID;

    public PersonalAdapter(Context context, RecyclerView recyclerView, Activity fragment, List<Post> posts, FloatingActionButton cPost, int userID)
    {
        SharedPreferences token = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        this.userToken = token.getString("token", "null");
        this.postList = posts;
        this.context = context;
        this.fragment = fragment;
        this.cPost = cPost;
        this.recyclerView = recyclerView;
        this.userID = userID;

    }
    public void addPost(Post newPost)
    {
        this.postList.add(newPost);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        final Post post = postList.get(i);
        contactViewHolder.postDetail.setText(post.getBodyText());
        contactViewHolder.author.setText(post.getUserName());
        contactViewHolder.like.setChecked(post.getIsLiked());
        post.setUserID(PersonalAdapter.userID);

        try {
            Picasso.with(context).load(post.getImageURL()).into(contactViewHolder.img);
        } catch (Exception e) {
            Picasso.with(context).load(R.drawable.image_not_found).into(contactViewHolder.img);
        }

        //This function displays the tags. when pass true also pass the len of how many tags you want to show plus the ellipse.
        Utils.addTags(fragment, contactViewHolder.tagArea, (ArrayList<String>)post.getTags(), 5, true);


        contactViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactViewHolder.like.isChecked() == true){
                    // like button is now checked
                    Utils.LikeOrUnlikeImage(post, PersonalAdapter.userToken, apiURL, PersonalAdapter.context, TAG, true);
                    // api call to like post for user

                } else {
                    // like button is now unchecked
                    Utils.LikeOrUnlikeImage(post, PersonalAdapter.userToken, apiURL, PersonalAdapter.context, TAG, false);
                    // api call to dislike post for user

                }
            }
        });


        contactViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fragment, DetailPostView.class);
                intent.putExtra("author", post.getUserName());
                intent.putExtra("like", post.getIsLiked());
                intent.putExtra("post_text", post.getBodyText());
                intent.putExtra("userID", PersonalAdapter.userID);
                intent.putExtra("postID", post.getPostID());
                intent.putExtra("token", userToken);

               // Bitmap bit = ((BitmapDrawable)contactViewHolder.img.getDrawable()).getBitmap();
                //ByteArrayOutputStream barray = new ByteArrayOutputStream();
                //bit.compress(Bitmap.CompressFormat.PNG, 50, barray);
                //intent.putExtra("post_image", barray.toByteArray());
                intent.putExtra("post_image", post.getImageURL());
                intent.putStringArrayListExtra("post_tags", (ArrayList<String>) post.getTags());
                fragment.startActivity(intent);
            }
        });

        contactViewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                fragment.findViewById(R.id.overlay_personal).setVisibility(View.VISIBLE);
                ImageView imgOver = fragment.findViewById(R.id.imgOverlaypersonal);
                //imgOver.setImageDrawable(contactViewHolder.img.getDrawable());

                try {
                    Picasso.with(context).load(post.getImageURL()).into(imgOver);
                } catch (Exception e) {
                    Picasso.with(context).load(R.drawable.image_not_found).into(imgOver);
                }

                recyclerView.setLayoutFrozen(true);
                cPost.setVisibility(View.GONE);

                return false;
            }
        });

    }

    public ArrayAdapter showSomeTags(){

        final List<TextView> personalTags = new ArrayList<>();

        return null;
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(context).
                    inflate(R.layout.personal_cardview, viewGroup, false);

            return new ContactViewHolder(itemView);
    }
    /*public List TagStringArray(Post p){
        List<String> tgs = new ArrayList<String>();
        try{
            for (int k = 0; k < p.getTags().length(); k++){
                tgs.add(p.getTags().get(k).toString());
            }
        }catch (JSONException e){
            Log.e("JSON Array", e.toString());
        }

        return tgs;
    }*/
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView postDetail;
        protected TextView author;
        protected ImageView img;
        protected CheckBox like;
        protected CardView card;
        protected RelativeLayout tagArea;

        public ContactViewHolder(View v) {
            super(v);

            postDetail =  (TextView) v.findViewById(R.id.personal_text);
            author = (TextView) v.findViewById(R.id.user_name);
            img = (ImageView) v.findViewById(R.id.personal_pic);
            like =  (CheckBox) v.findViewById(R.id.like);
            card = (CardView) v.findViewById(R.id.personal_card);
            tagArea = (RelativeLayout) v.findViewById(R.id.tag_area);

        }



    }
}
