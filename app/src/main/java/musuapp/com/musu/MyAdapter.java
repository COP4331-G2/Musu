package musuapp.com.musu;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.*;

import java.util.List;


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private List<Post> postList;

    public MyAdapter( RecyclerView recyclerView, FragmentActivity fragment, List<Post> posts)
    {
        RecyclerView.LayoutManager rvlm;
        rvlm = new LinearLayoutManager(fragment);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(rvlm);

        this.postList = posts;

        recyclerView.setAdapter(this);

    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Post post = postList.get(i);
        contactViewHolder.postDetail.setText(post.postDetail);
        contactViewHolder.author.setText(post.author);
        Picasso.with(contactViewHolder.context).load(post.imgUrl).into(contactViewHolder.img);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.contactrow, viewGroup, false);

            return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView postDetail;
        protected TextView author;
        protected ImageView img;
        protected Context context;

        public ContactViewHolder(View v) {
            super(v);
            context = v.getContext();
            postDetail =  (TextView) v.findViewById(R.id.Details);
            author = (TextView) v.findViewById(R.id.title);
            img = (ImageView) v.findViewById(R.id.imageView2);
        }
    }
}
