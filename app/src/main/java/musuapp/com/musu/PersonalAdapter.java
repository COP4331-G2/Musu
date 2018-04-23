package musuapp.com.musu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.*;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ContactViewHolder> {

    private List<Post> postList;
    private Context context;
    private RecyclerView recyclerView;
    private Activity fragment;
    private FloatingActionButton cPost;

    public PersonalAdapter(Context context, RecyclerView recyclerView, Activity fragment, List<Post> posts, FloatingActionButton cPost)
    {
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
        contactViewHolder.like.setChecked(post.getIsLiked());
        Picasso.with(context).load(post.getImageURL()).fit().into(contactViewHolder.img);

        ArrayList<TextView> pTags = new ArrayList<TextView>();
        int len = post.getTags().length();
        int j = 0;
        String s = "";
       // while ( )
        try{
            Log.i("TAG", post.getTags().get(0).toString());

            while( len > 0 && j < len && j < 10){
                s = post.getTags().get(j).toString();
                TextView temp = new TextView(context);
                temp.setText(s);
                temp.setBackgroundResource(R.drawable.tag_view);
                pTags.add(temp);
                contactViewHolder.tagArea.addView(temp);
                j++;
            }

            //final ArrayAdapter<TextView> gvAdapter = new ArrayAdapter<TextView>(context, android.R.layout.simple_list_item_1, pTags);
            //contactViewHolder.tagArea.setAdapter(gvAdapter);

        }catch (JSONException e){
            Log.e("TAG", e.toString());
        }



        contactViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactViewHolder.like.isChecked() == true){
                    // like button is now checked

                    // api call to like post for user

                } else {
                    // like button is now unchecked

                    // api call to dislike post for user

                }
            }
        });


        contactViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fragment, DetailPostView.class);
                intent.putExtra("author", post.getUserName());
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

                fragment.findViewById(R.id.overlay_personal).setVisibility(View.VISIBLE);
                ImageView imgOver = fragment.findViewById(R.id.imgOverlaypersonal);
                imgOver.setImageDrawable(contactViewHolder.img.getDrawable());
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

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView postDetail;
        protected TextView author;
        protected ImageView img;
        protected CheckBox like;
        protected CardView card;
        protected GridLayout tagArea;

        public ContactViewHolder(View v) {
            super(v);

            postDetail =  (TextView) v.findViewById(R.id.personal_text);
            author = (TextView) v.findViewById(R.id.user_name);
            img = (ImageView) v.findViewById(R.id.personal_pic);
            like =  (CheckBox) v.findViewById(R.id.like);
            card = (CardView) v.findViewById(R.id.personal_card);
            tagArea = (GridLayout) v.findViewById(R.id.tag_area);

        }



    }
}