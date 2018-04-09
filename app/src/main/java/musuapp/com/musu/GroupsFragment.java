package musuapp.com.musu;

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

public class GroupsFragment extends Fragment {

    View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.groups_fragment, container, false);

        RecyclerView rv = inflatedView.findViewById(R.id.list_Post);

        new MyAdapter(rv, getActivity(), createList(30));

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
}