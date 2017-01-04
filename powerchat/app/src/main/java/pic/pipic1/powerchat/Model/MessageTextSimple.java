package pic.pipic1.powerchat.Model;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by emimo on 17/03/2016.
 */
public class MessageTextSimple {
    private String name;
    private String text;
    private String uid;
    private Date date;
    private String loc;
    private String photo;

    public MessageTextSimple() {
    }

    public MessageTextSimple(String name, String uid, String message) {
        this(name,uid,message,null,null);
    }

    public MessageTextSimple(String name, String uid, String message, String loc){
        this(name,uid,message,loc,null);
    }

    public MessageTextSimple(String name, String uid, String message, String loc, String photo){
        this.name = name;
        this.text = message;
        this.uid = uid;
        this.date = Calendar.getInstance().getTime();
        if(loc != null){
            this.loc = loc;
        }else {
            this.loc = "";
        }
        this.photo = photo;
    }

    public String getLoc() {
        return loc;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }
}