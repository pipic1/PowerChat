package pic.pipic1.powerchat.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ppier on 02/03/2016.
 */
public class MessageText extends Message implements Serializable {
    private String message;

    private MessageText(){
        super();
    }

    public MessageText(String message, String uid, String author) {
        super(Calendar.getInstance().getTime(),uid,author);
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}