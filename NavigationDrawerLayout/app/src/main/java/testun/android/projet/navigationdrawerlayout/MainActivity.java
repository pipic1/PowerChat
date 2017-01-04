package testun.android.projet.navigationdrawerlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = ( ListView) findViewById(R.id.menu_gauche);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_drawer, new String[] {"opt 1","opt 2","opt 3"}));
    }
}
