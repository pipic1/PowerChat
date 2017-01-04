package testun.androidi.projet.testfirebasechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private EditText editText;

    private int nbMessage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);
        final Firebase myFirebase = new Firebase("https://sweltering-fire-2814.firebaseio.com/");


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if ((nbMessage % 2) == 0) {
                        myFirebase.child("mess1").setValue(editText.getText().toString());
                    } else {
                        myFirebase.child("mess2").setValue(editText.getText().toString());
                    }
                    Log.i("TestText", "nb message : " + nbMessage);
                    nbMessage++;


                    return false;
                }
                return false;
            }
        });

        myFirebase.child("mess1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myFirebase.child("mess2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
