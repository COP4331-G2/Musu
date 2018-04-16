package musuapp.com.musu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Kevin Santana on 3/26/2018.
 */

public class Post {
    protected String author;
    protected String postDetail;
    protected String imgUrl;
    protected String[] tags;
    protected String authorPic;

    public Post(String author, String postDetail, String imgUrl, String authorPic, String[] tags){
        this.author = author;
        this.postDetail = postDetail;
        this.imgUrl = imgUrl;
        this.authorPic = authorPic;
        this.tags = tags;
    }

    public Post(String author, String postDetail, String imgUrl){
        this.author = author;
        this.postDetail = postDetail;
        this.imgUrl = imgUrl;
    }

    public Post(){ }
}

