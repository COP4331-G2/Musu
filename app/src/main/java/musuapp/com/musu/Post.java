package musuapp.com.musu;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class Post {



    private String userName, bodyText, imageURL;
    private int postID, userID;
    private JSONArray tags;
    private boolean isLiked;

    // Empty Constructor
    public Post()
    {
        // Empty call
    }

    public void setLiked(String responseText)
    {
        if(responseText.equals("Post liked."))
        {
            this.isLiked = true;
        }
        else if(responseText.equals("Post unliked."))
        {
            this.isLiked = false;
        }
        else
        {
            Log.e("Posts","response not recorded");
        }
    }

    public Post(String userName, String bodyText, String imageURL)
    {
        this.userName = userName;
        this.bodyText = bodyText;
        this.imageURL = imageURL;
    }

    // Constructor without tags or username
    public Post(int postID, int userID, String bodyText, String imageURL)
    {
        this.postID = postID;
        this.userID = userID;
        this.bodyText = bodyText;
        this.imageURL = imageURL;
    }



    // Constructor without tags
    public Post(int postID, int userID, String bodyText, String imageURL, String userName)
    {
        this.postID = postID;
        this.userID = userID;
        this.bodyText = bodyText;
        this.imageURL = imageURL;
        this.userName = userName;
    }

    // Constructor with everything
    public Post(int userID, int postID, String bodyText, String imageURL, String userName, JSONArray tags, boolean isLiked)
    {
        this.userID = userID;
        this.postID = postID;
        this.bodyText = bodyText;
        this.imageURL = imageURL;
        this.userName = userName;
        this.tags = tags;
        this.isLiked = isLiked;
    }

    // Get the Post ID
    public int getPostID()
    {
        return this.postID;
    }

    // Get the User ID
    public int getUserID()
    {
        return this.userID;
    }

    // Get the Post ID
    public String getBodyText()
    {
        return this.bodyText;
    }

    public String getImageURL()
    {
        return this.imageURL;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public boolean getIsLiked() { return this.isLiked; }

    public JSONArray getTags() {return this.tags; }

    public String toString()
    {
        String returnString = "Post ID: " + this.postID + ", User ID: " + this.userID + ", post text: " + this.bodyText + ", imageURL: " + this.imageURL;
        return returnString;
    }
}
