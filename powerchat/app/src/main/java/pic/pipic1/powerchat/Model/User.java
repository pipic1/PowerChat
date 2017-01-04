package pic.pipic1.powerchat.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by emimo on 02/03/2016.
 */
public class User implements Serializable{
    private String pseudo;

    public User(){}

    public User(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }
}
