package musuapp.com.musu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.groups_fragment, container, false);

        RecyclerView rv = inflatedView.findViewById(R.id.list_Post);
        RecyclerView.Adapter rva;
        RecyclerView.LayoutManager rvlm;

        rv.setHasFixedSize(true);

        rvlm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlm);
        rva = new MyAdapter(createList(30));
        rv.setAdapter(rva);

        return this.inflatedView;
    }
       private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.title = "Doge" + i;
            ci.details = "so much wow,  so cool , omg " + i;
            ci.img = inflatedView.findViewById(R.id.imageView2);
            ci.setImage("https://res.cloudinary.com/dgz4xvj8r/image/upload/v1522094342/Users/xhwwcperw7nlsgquq40r.jpg");

            result.add(ci);

        }

        return result;
    }
}