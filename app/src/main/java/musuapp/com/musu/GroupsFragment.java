package musuapp.com.musu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GroupsFragment extends Fragment {

    View inflatedView;
    RecyclerView rv;
    RecyclerView.Adapter rva;
    RecyclerView.LayoutManager rvlm;
    SharedPreferences access;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.groups_fragment, container, false);

        RecyclerView rv = inflatedView.findViewById(R.id.list_Post);

        adapter =  new MyAdapter(rv, getActivity(), createList(30));

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