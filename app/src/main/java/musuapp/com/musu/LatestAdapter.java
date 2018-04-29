package musuapp.com.musu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergioperez on 4/18/18.
 */

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ContactViewHolder>{

    private List<Post> postList;
    private Context context;
    private RecyclerView recyclerView;
    private Activity fragment;
    private FloatingActionButton cPost;
    private static int userID;
    private static String userToken;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public LatestAdapter(Context context, RecyclerView recyclerView, Activity fragment, List<Post> posts, FloatingActionButton cPost, int userID) {
        SharedPreferences token = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        this.postList = posts;
        this.context = context;
        this.fragment = fragment;
        this.cPost = cPost;
        this.recyclerView = recyclerView;
        this.userID = userID;
        this.userToken = token.getString("token", "null");

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


        contactViewHolder.img.setDefaultImageResId(R.drawable.spinner_loader);
        contactViewHolder.img.setImageUrl(post.getImageURL(), mImageLoader);

        //try {
        //    Picasso.with(context).load(post.getImageURL()).into(contactViewHolder.img);
        //} catch (Exception e) {
        //    Picasso.with(context).load(R.drawable.image_not_found).into(contactViewHolder.img);
        //}

        contactViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fragment, DetailPostView.class);
                intent.putExtra("author", post.getUserName());
                intent.putExtra("userID", LatestAdapter.userID);
                intent.putExtra("postID", post.getPostID());
                intent.putExtra("token", userToken);
                intent.putExtra("post_text", post.getBodyText());
                // Bitmap bit = ((BitmapDrawable)contactViewHolder.img.getDrawable()).getBitmap();
                //ByteArrayOutputStream barray = new ByteArrayOutputStream();
                //bit.compress(Bitmap.CompressFormat.PNG, 50, barray);
                //intent.putExtra("post_image", barray.toByteArray());
                intent.putExtra("post_image", post.getImageURL());
                fragment.startActivity(intent);
            }
        });

        contactViewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                fragment.findViewById(R.id.overlay_latest).setVisibility(View.VISIBLE);
                ImageView imgOver = fragment.findViewById(R.id.imgOverlaylatest);
                imgOver.setImageDrawable(contactViewHolder.img.getDrawable());
                recyclerView.setLayoutFrozen(true);
                cPost.setVisibility(View.GONE);

                return true;
            }
        });

        contactViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fragment, DetailPostView.class);
                intent.putExtra("author", post.getUserName());
                intent.putExtra("post_text", post.getBodyText());
                intent.putExtra("userID", LatestAdapter.userID);
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

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.latest_cardview, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected NetworkImageView img;
        protected CardView card;

        public ContactViewHolder(View v) {
            super(v);

            img =  v.findViewById(R.id.latest_pic);
            card = (CardView) v.findViewById(R.id.latest_card);

        }



    }
}
