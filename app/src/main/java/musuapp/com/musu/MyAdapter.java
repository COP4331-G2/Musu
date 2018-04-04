package musuapp.com.musu;

import android.content.Context;
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

    private List<ContactInfo> contactList;

    public MyAdapter(List<ContactInfo> contactList)
    {
        this.contactList  = contactList;
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.details.setText(ci.details);
        contactViewHolder.title.setText(ci.title);
        Picasso.with(contactViewHolder.context).load(ci.imgUrl).into(contactViewHolder.img);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.contactrow, viewGroup, false);

            return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView details;
        protected TextView title;
        protected ImageView img;
        protected Context context;

        public ContactViewHolder(View v) {
            super(v);
            context = v.getContext();
            details =  (TextView) v.findViewById(R.id.Details);
            title = (TextView) v.findViewById(R.id.title);
            img = (ImageView) v.findViewById(R.id.imageView2);
        }
    }
}
