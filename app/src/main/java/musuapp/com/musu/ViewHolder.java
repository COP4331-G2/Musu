package musuapp.com.musu;

/**
 * Created on 4/16/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.widget.*;
import android.view.LayoutInflater;

//public class ViewHolder extends RecyclerView.ViewHolder{

    //These are the general elements in the RecyclerView

   /* public TextView username, personal_post_text;
    public ImageView personal_post_pic;

    //This is the Header on the Recycler (viewType = 0)
    public TextView author, latest_post_text;
    public ImageView latest_post_pic;

    //This constructor would switch what to findViewBy according to the type of viewType
    public ViewHolder(View v, int viewType) {
        super(v);
        if (viewType == 0) {
            username = (TextView) v.findViewById(R.id.name);
            personal_post_text = (TextView) v.findViewById(R.id.description);
            personal_post_pic = (ImageView) v.findViewById(R.id.);
        } else if (viewType == 1) {
            author = (TextView) v.findViewById(R.id.place);
            latest_post_text = (TextView) v.findViewById(R.id.place);
            latest_post_pic = (ImageView) v.findViewById(R.id.pics);
        }

}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType)
    {
        View v;
        ViewHolder vh;
        // create a new view
        switch (viewType) {
            case 0: //This would be the header view in my Recycler
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_welcome, parent, false);
                vh = new ViewHolder(v,viewType);
                return  vh;
            default: //This would be the normal list with the pictures of the places in the world
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_picture, parent, false);
                vh = new ViewHolder(v, viewType);

        }
    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //position == 0 means its the info header view on the Recycler
        if (position == 0) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"name clicked", Toast.LENGTH_SHORT).show();
                }
            });
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"description clicked", Toast.LENGTH_SHORT).show();
                }
            });
            //this means it is beyond the headerview now as it is no longer 0. For testing purposes, I'm alternating between two pics for now
        } else if (position > 0) {
            holder.place.setText(mDataset[position]);
            if (position % 2 == 0) {
                holder.pics.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pic1));
            }
            if (position % 2 == 1) {
                holder.pics.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pic2));
            }

        }*/

    //}