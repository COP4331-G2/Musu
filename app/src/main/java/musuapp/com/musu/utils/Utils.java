package musuapp.com.musu.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import musuapp.com.musu.AppController;
import musuapp.com.musu.DetailPostView;
import musuapp.com.musu.PersonalAdapter;
import musuapp.com.musu.Post;
import musuapp.com.musu.R;

/**
 * Created by sergioperez on 4/29/18.
 */

public class Utils {

    public static void LikeOrUnlikeImage(final Post post, String userToken, String apiURL, final Context context, final String TAG, boolean like)
    {
        String postID = Integer.toString(post.getPostID());
        String userID = Integer.toString(post.getUserID());

        final String[] returnResponse = {""};
        // Build a map with the parameters I want to send to server
        Map<String, String> postParam = new HashMap<String, String>();

        if(like) postParam.put("function", "likePost");
        else postParam.put("function", "unlikePost");

        postParam.put("userID", userID);
        postParam.put("postID", postID);
        postParam.put("token", userToken);

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

                            // Get the JSON Array with the Posts
                            boolean responseSuccess = (boolean)response.get("success");
                            final String responseText = (String)response.get("message");


                            if(responseSuccess)
                            {
                                Toast toast = Toast.makeText(context,responseText, Toast.LENGTH_LONG);
                                post.setLiked(responseText);
                                toast.show();
                            }

                            // Create the adapter with the list
                            //adapter = new MyAdapter(rv, getActivity(), results);


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

    public static void addTags(Activity activity, final RelativeLayout wrapper, ArrayList<String> tags, int len, boolean isThisAprevious){

        int counter = 0;
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        screenWidth -= isThisAprevious?200:0;
        //final RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.detail_tags);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.i("WIDTH OF AREA:", String.valueOf(screenWidth));

        //int id = 0;
        wrapper.removeAllViews();

        int currCounter = 0;
        int currWidth = 0;
        boolean isNewLine = false;
        boolean firstLine = false;



        for(int id = 0; id < len; id++){

            TextView tag = new TextView(activity);

            if(id  == 4 && tags.size() > 4 && isThisAprevious){

                tag.setText(" ... ");

            }

            else{
                if(id < tags.size()) {
                    String text = tags.get(id);
                    tag.setText(text.trim());

                }
            }
            tag.setTextSize(16);
            tag.setTextColor(activity.getResources().getColor(R.color.black));
            tag.setId(4000+id);
            tag.setElevation(5);
            tag.setBackgroundResource(R.drawable.tag_view);
            tag.measure(0,0);

            RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp1.setMargins(10, 10,10,20);

            //tag.setMaxLines(1);

            int width =  tag.getMeasuredWidth();//text.length()*15;

            Log.i("WIDTH OF TAG", String.valueOf(width));
            if(tag.getText().length() > 0) {
                if ((currWidth + width + 100) <= screenWidth) {
                    currWidth += width;
                    isNewLine = false;
                    currCounter++;
                } else {
                    currWidth = width;
                    firstLine = false;
                    isNewLine = true;
                    currCounter = 1;
                }

                if (id == 0) {
                    rlp1.addRule(RelativeLayout.ALIGN_START);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);
                } else if (isNewLine) {
                    rlp1.addRule(RelativeLayout.ALIGN_LEFT);
                    rlp1.addRule(RelativeLayout.BELOW, 4000 - 1 + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);
                } else if (firstLine) {
                    rlp1.addRule(RelativeLayout.RIGHT_OF, 4000 - 1 + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);

                } else {

                    rlp1.addRule(RelativeLayout.RIGHT_OF, 4000 - 1 + id);
                    rlp1.addRule(RelativeLayout.BELOW, 4000 - currCounter + id);
                    tag.setLayoutParams(rlp1);
                    wrapper.addView(tag);

                }
            }
        }
    }
}
