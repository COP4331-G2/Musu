package musuapp.com.musu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class LatestFragment extends Fragment {
    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    public static final String TAG = LatestFragment.class.getSimpleName();
    View inflatedView;
    View overlay;
    ImageView iv;
    FloatingActionButton cPost;
    List<Post> results;
    LatestAdapter adapter;
    SharedPreferences access;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        access = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        results = new ArrayList<Post>();

        this.inflatedView = inflater.inflate(R.layout.latest_fragment, container, false);
        final RecyclerView rv = inflatedView.findViewById(R.id.list_Postlatest);

        overlay = inflatedView.findViewById(R.id.overlay_latest);
        overlay.setVisibility(View.GONE);
        cPost = getActivity().findViewById(R.id.floatingActionButton2);
        cPost.setVisibility(overlay.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        iv = inflatedView.findViewById(R.id.imgOverlaylatest);

        adapter = new LatestAdapter(getContext(), rv, getActivity(), results, cPost, access.getInt("userID", -1));
        rv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv.setAdapter(adapter);

        getPosts();

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView r = inflatedView.findViewById(R.id.list_Postlatest);
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

        String token = access.getString("token", "");
        Integer userID = access.getInt("userID", -1);

        // Build a map with the parameters I want to send to server
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("function", "getPostsLatest");
        postParam.put("numberOfPosts", "50");
        postParam.put("userID", userID.toString());
        postParam.put("token", token);

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


                        } catch (JSONException e){
                            Log.e(TAG, e.toString());
                        }
                        adapter.notifyDataSetChanged();
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