package musuapp.com.musu;

import android.view.View;
/**
 * Created by sergioperez on 4/11/18.
 */

public interface RVItemClickListener {
    //public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
