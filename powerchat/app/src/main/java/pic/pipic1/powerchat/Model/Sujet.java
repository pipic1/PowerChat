package pic.pipic1.powerchat.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emimo on 02/03/2016.
 */
public class Sujet implements Serializable{
    private String titre;
    private String description;
    private String uid;
    private String author;

    public Sujet(){

    }

    public Sujet(String author, String uid,String titre, String description) {
        Log.i("PCSujet","l'auteur est "+author);
        this.uid = uid;
        this.author = author;
        this.titre = titre;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitre() {
        return titre;
    }

    public String getUid() {
        return uid;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
