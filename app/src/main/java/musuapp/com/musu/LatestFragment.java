package musuapp.com.musu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class LatestFragment extends Fragment {
    View inflatedView;
    View overlay;
    ImageView iv;
    FloatingActionButton cPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflatedView = inflater.inflate(R.layout.latest_fragment, container, false);
        final RecyclerView rv = inflatedView.findViewById(R.id.list_Postlatest);

        overlay = inflatedView.findViewById(R.id.overlay_latest);
        overlay.setVisibility(View.GONE);
        cPost = getActivity().findViewById(R.id.floatingActionButton2);
        cPost.setVisibility(overlay.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        iv = inflatedView.findViewById(R.id.imgOverlaylatest);

        LatestAdapter adapter = new LatestAdapter(getContext(), rv, getActivity(), createList(30), cPost);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv.setAdapter(adapter);

        return this.inflatedView;
    }

    private List<Post> createList(int size) {

        List<Post> result = new ArrayList<Post>();

        for (int i=1; i <= size; i++) {
            Post post = new Post();
           /* post.author = "Doge" + i;
            post.postDetail = "so much wow,  so cool , omg " + i;
            post.imgUrl  = getString(R.string.test_image);*/
            result.add(post);

        }

        return result;
    }
}