package pic.pipic1.powerchat.View.TopicList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.util.logging.MemoryHandler;

import pic.pipic1.powerchat.Model.Sujet;
import pic.pipic1.powerchat.R;

/**
 * Created by emimo on 12/03/2016.
 * INNUTILE
 */
public class MainTopicListActivity extends FirebaseLoginBaseActivity{

    private MainTopicListActivity mainTopicListActivity = this;
    public static String TAG = "FirebaseUI.chat";
    private Firebase mRef;
    private Query mChatRef;
    private String mName;

    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private FloatingActionButton fab;
    private ListTopicFragment listTopicFragment;
//    private FirebaseRecyclerAdapter<Chat, ChatHolder> mRecycleViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        mDrawer.setDrawerListener(drawerToggle);

        mRef = new Firebase("https://powerchat-iut.firebaseio.com/sujets");
        mChatRef = mRef.limitToLast(50);



        listTopicFragment = new ListTopicFragment();
        listTopicFragment.setRef(mRef);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.accueil_topic, listTopicFragment, "fragmentListTopic");
        transaction.addToBackStack(null);

        transaction.commit();

        fab = (FloatingActionButton) findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                LayoutInflater inflater = getLayoutInflater();
//                View dialoglayout = inflater.inflate(R.layout.add_layout_activity, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                builder.setPositiveButton();
//                builder.setView(dialoglayout);
//                builder.show();

                Bundle args = new Bundle();
                args.putString("title", "Ajouter un sujet");
                TopicDialog actionbarDialog = new TopicDialog();
                actionbarDialog.setMainTopicListActivity(mainTopicListActivity);
                actionbarDialog.setArguments(args);
                actionbarDialog.show(getFragmentManager(),"action_bar_frag");

            }
        });


    }


    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        Log.i(TAG, "Logged in to " + authData.getProvider().toString());

        switch (authData.getProvider()) {
            case "password":
                mName = (String) authData.getProviderData().get("email");
                break;
            default:
                mName = (String) authData.getProviderData().get("displayName");
                break;
        }

//        if(mName != null){
//            Sujet s = new Sujet(mName,getAuth().getUid(),"test","la description");
//            mRef.push().setValue(s);
//            Log.i("PCajout","on a push un nouveau sujet");
//        }

        invalidateOptionsMenu();
        listTopicFragment.getmAdapter().notifyDataSetChanged();


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        Log.i("PCmenu","on est dans le listener");
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.se_login:
                showFirebaseLoginPrompt();
                break;
            default:
                break;
        }

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("PCmenu","oncreate menu");
//        getMenuInflater().inflate(R.menu.menu_topic_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("PCmenu","on passe avant");
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Log.i("PCmenu","on passe apres");
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
//        setEnabledAuthProvider(AuthProviderType.TWITTER);
        setEnabledAuthProvider(AuthProviderType.GOOGLE);
//        setEnabledAuthProvider(AuthProviderType.PASSWORD);
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);

    }

    @Override
    protected Firebase getFirebaseRef() {
        return mRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {
        Log.e(TAG, "Login provider error: " + firebaseLoginError.toString());
        resetFirebaseLoginPrompt();
    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {
        Log.e(TAG, "Login user error: "+firebaseLoginError.toString());
        resetFirebaseLoginPrompt();
    }

    public String getName() {
        return mName;
    }

    public Firebase getRef() {
        return mRef;
    }


    public ListTopicFragment getListTopicFragment() {
        return listTopicFragment;
    }
}
