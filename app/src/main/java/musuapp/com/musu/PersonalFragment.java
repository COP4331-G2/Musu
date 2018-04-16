package musuapp.com.musu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PersonalFragment extends Fragment {
    View inflatedView;
    View overlay;
    ImageView iv;
    FloatingActionButton cPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.personal_fragment, container, false);

        overlay = inflatedView.findViewById(R.id.overlay_personal);
        overlay.setVisibility(View.GONE);
        cPost = getActivity().findViewById(R.id.floatingActionButton2);
        cPost.setVisibility(overlay.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        iv = inflatedView.findViewById(R.id.imgOverlay);

        return this.inflatedView;
    }
}