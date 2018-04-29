package musuapp.com.musu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ContactViewHolder> {

    private List<Post> postList;
    private Context context;
    private RecyclerView recyclerView;
    private Activity fragment;
    private FloatingActionButton cPost;
    private static int userID;
    private static String userToken;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public GroupAdapter(Context context, RecyclerView recyclerView, Activity fragment, List<Post> posts, FloatingActionButton cPost, int userID)
    {
        SharedPreferences token = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        this.userToken = token.getString("token", "null");
        this.userID  = userID;
        this.postList = posts;
        this.context = context;
        this.fragment = fragment;
        this.cPost = cPost;
        this.recyclerView = recyclerView;

        /*RecyclerView.LayoutManager rvlm;
        rvlm = new LinearLayoutManager(fragment);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(rvlm);

        recyclerView.setAdapter(this);*/

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
        //contactViewHolder.like.setChecked(false);

        contactViewHolder.img.setImageUrl(post.getImageURL(), mImageLoader);


        contactViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fragment, DetailPostView.class);
                intent.putExtra("author", post.getUserName());
                intent.putExtra("userID", GroupAdapter.userID);
                intent.putExtra("postID", post.getPostID());
                intent.putExtra("token", userToken);
                intent.putExtra("post_text", post.getBodyText());
                intent.putExtra("post_image", post.getImageURL());
                intent.putStringArrayListExtra("post_tags", (ArrayList<String>) post.getTags());
                intent.putExtra("like", post.getIsLiked());
                fragment.startActivity(intent);
            }
        });

        contactViewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                fragment.findViewById(R.id.overlay_group).setVisibility(View.VISIBLE);
                ImageView imgOver = fragment.findViewById(R.id.imgOverlay_group);
                imgOver.setImageDrawable(contactViewHolder.img.getDrawable());
                recyclerView.setLayoutFrozen(true);
                cPost.setVisibility(View.GONE);

                return false;
            }
        });

    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.contactrow, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView postDetail;
        protected TextView author;
        protected NetworkImageView img;
        protected CardView card;

        public ContactViewHolder(View v) {
            super(v);

            postDetail =  (TextView) v.findViewById(R.id.group_text);
            author = (TextView) v.findViewById(R.id.groupname);
            img = (NetworkImageView) v.findViewById(R.id.group_pic);
            card = (CardView) v.findViewById(R.id.group_card);

        }



    }
}
