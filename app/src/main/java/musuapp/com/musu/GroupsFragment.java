package musuapp.com.musu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GroupsFragment extends Fragment {

    View inflatedView;
    MyAdapter adapter;

    View overlay;
    ImageView iv;
    FloatingActionButton cPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.groups_fragment, container, false);

        final RecyclerView rv = inflatedView.findViewById(R.id.list_Post);

        overlay = inflatedView.findViewById(R.id.overlay);
        overlay.setVisibility(View.GONE);
        cPost = getActivity().findViewById(R.id.floatingActionButton2);
        cPost.setVisibility(overlay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        iv = inflatedView.findViewById(R.id.imgOverlay_group);


        adapter =  new MyAdapter(getContext(), rv, getActivity(), createList(30), cPost);

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView r = inflatedView.findViewById(R.id.list_Post);
                r.setLayoutFrozen(false);
                cPost.setVisibility(View.VISIBLE);
                overlay.setVisibility(View.GONE);
            }
        });

        return this.inflatedView;
    }

       private List<Post> createList(int size) {

        List<Post> result = new ArrayList<Post>();

        for (int i=1; i <= size; i++) {
            Post post = new Post();
            post.author = "Doge" + i;
            post.postDetail = "so much wow,  so cool , omg " + i;
            post.imgUrl  = getString(R.string.test_image);
            result.add(post);

        }

        return result;
    }

    /**
     * This function will populate the list as fast as it can, in the background
     *
    private void populateGroups()
    {
        SharedPreferences access = getSharedPreferences("Login", MODE_PRIVATE);

        int userID = access.get(userID);

        String url = API_LINK;

        JSONObject payload = new JSONObject();

        try
        {
            payload.add("function","getPosts");
            payload.add("numberOfPosts",30);
            payload.add("userID", userID);
        } catch (JSONException e){
            Log.e("Groups Fragment: ", e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                url, payload,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        // In here we add the items to the list
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });
    }
     */
}