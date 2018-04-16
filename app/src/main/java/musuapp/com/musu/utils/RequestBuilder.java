package musuapp.com.musu.utils;


// Imports
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import musuapp.com.musu.AppController;
import musuapp.com.musu.Post;


public class RequestBuilder {

    public static final String TAG = RequestBuilder.class.getSimpleName();

    final String apiURL = "http://www.musuapp.com/API/API.php";
    boolean loginBoolean;
    JSONObject returnedObject = new JSONObject();

    // Login Request
    public boolean login(String username, String password)
    {
        // Build a map with the parameters I want to send to server
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("function", "loginAttempt");
        postParam.put("username", username);
        postParam.put("password", password);

        // JSON Object to send to the server
        JSONObject parameters = new JSONObject(postParam);

        // Building the actual request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, apiURL, parameters,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the JSON response in the console
                        Log.e(TAG, response.toString());

                        // Set the class object to be the response
                        returnedObject = response;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Print VolleyLog to the console
                VolleyLog.e(TAG, "Error: " + error.getMessage());
            }
        });

        try {
            if (returnedObject.get("success") == "true")
            {
                return true;
            } else {
                return false;
            } } catch (JSONException e) {
            // If we get JSON Exception, assume login fails
            return false;
        }
    }

    public ArrayList<Post> getLatestPosts(int numberOfPosts) throws ExecutionException, InterruptedException {

        String tag_json_arry = "json_obj_req";

        // Build map with JSON for server
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("function", "getPostsLatest");

        // JSON Object to send to the server
        JSONObject parameters = new JSONObject(postParam);
        try {
            parameters.put("numberOfPosts", numberOfPosts);
        } catch (JSONException e){
            Log.e("JSON Exception: ", e.toString());
        }

        Log.e("JSON Payload: ", parameters.toString());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        // Build the Request and receive response
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, apiURL, parameters, future, future);

        future.setRequest(jsonObjReq);

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_arry);

        try {
            return parseJSONPosts(future.get(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            Log.e("Interrupted Excetiion", e.toString());
        } catch (ExecutionException e) {
            Log.e("Execution Exception", e.toString());
        } catch (TimeoutException e) {
            Log.e("Timeout Excetiion: ", e.toString());
        }

        ArrayList<Post> posts = new ArrayList<Post>();
        return posts;
    }

    public ArrayList<Post> parseJSONPosts(JSONObject json)
    {
        // Instantiate ArrayList
        ArrayList<Post> posts = new ArrayList<Post>();

        // try getting the results array from the JSON Object
        try {
            // get the jsonArray
            JSONArray jsonArray = json.getJSONArray("results");

            // For each Post in the Array, make a Post Object and add it to the ArrayList
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject singlePost = jsonArray.getJSONObject(i);
                Post newPost = new Post(singlePost.getInt("postID"), singlePost.getInt("userID"), singlePost.getString("bodyText"), singlePost.getString("imageURL"));
                posts.add(newPost);
            }
        } catch (JSONException e)
        {
            Log.e("JSON Exception", e.toString());
        }

        return posts;
    }



}
