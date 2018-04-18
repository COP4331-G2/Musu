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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;

public class GroupsFragment extends Fragment {

    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    public static final String TAG = GroupsFragment.class.getSimpleName();
    View inflatedView;
    GroupAdapter adapter;
    RecyclerView rv;
    View overlay;
    ImageView iv;
    FloatingActionButton cPost;

    List<Post> results;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        this.inflatedView = inflater.inflate(R.layout.groups_fragment, container, false);
        results = new ArrayList<Post>();
        getPosts();

        rv = inflatedView.findViewById(R.id.list_Post);

        overlay = inflatedView.findViewById(R.id.overlay_group);
        overlay.setVisibility(View.GONE);
        cPost = getActivity().findViewById(R.id.floatingActionButton2);
        cPost.setVisibility(overlay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        iv = inflatedView.findViewById(R.id.imgOverlay_group);

        //if (!results.isEmpty()){
        //adapter = new GroupAdapter(getContext(), rv, getActivity(), results, cPost );
        //rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rv.setAdapter(adapter);}

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


    private Post createPostObject(JSONObject jsonObject)
    {
        try {
            // Set the data from the json object
            String userName = jsonObject.getString("username");
            String bodyText = jsonObject.getString("bodyText");
            String imageURL = jsonObject.getString("imageURL");
            int userID = jsonObject.getInt("userID");
            int postID = jsonObject.getInt("postID");
            JSONArray tags = jsonObject.getJSONArray("tags");
            boolean isLiked = jsonObject.getBoolean("isLiked");

            // Add the data to a Post object
            Post post = new Post(userID, postID, bodyText, imageURL, userName, tags, isLiked);

            // Return the Post object
            return post;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // In case of JSONException, return empty Post object
        Post emptyPost = new Post();
        return emptyPost;
    }

    // Start the task, and in the callback, create the list and adapter
    public void getPosts() {

        // Build a map with the parameters I want to send to server
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("function", "getPostsLatest");
        postParam.put("numberOfPosts", "700");
        postParam.put("userID", "3");

        // JSON Object to send to the server
        JSONObject parameters = new JSONObject(postParam);

        // Building the actual request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, apiURL, parameters,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Logic goes here


                        try{
                            // Declare objects
                            //List<Post> results = new ArrayList<Post>();
                            JSONObject tempObject = new JSONObject();

                            // Get the JSON Array with the Posts
                            JSONArray jsonArray = response.getJSONArray("results");

                            // Parse through the json array
                            for(int i = 0;i < jsonArray.length(); i++)
                            {
                                // Get the JSON Object
                                tempObject = jsonArray.getJSONObject(i);

                                // Create a new Post Object
                                Post post = createPostObject(tempObject);

                                // Add the post to the list
                                results.add(post);
                            }

                            // Create the adapter with the list
                            //adapter = new MyAdapter(rv, getActivity(), results);

                            adapter = new GroupAdapter(getContext(), rv, getActivity(), results, cPost );
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv.setAdapter(adapter);
                        } catch (JSONException e){
                            Log.e(TAG, e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Print VolleyLog to the console
                VolleyLog.e(TAG, "Error: " + error.getMessage());
            }
        }); // !! The request building of "jsonObjReq" ends here !!

        // Add the Request to the queue and execute
        AppController.getInstance().addToRequestQueue(jsonObjReq, "json_obj_req");

    }
}