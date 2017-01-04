package pic.pipic1.powerchat.View.Discussion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.util.List;

import pic.pipic1.powerchat.Location.Constants;
import pic.pipic1.powerchat.Location.FetchAddressIntentService;
import pic.pipic1.powerchat.Model.Message;
import pic.pipic1.powerchat.Model.MessageTextSimple;
import pic.pipic1.powerchat.Model.Sujet;
import pic.pipic1.powerchat.R;
import pic.pipic1.powerchat.View.Adapter.TextSimpleAdapter;

public class DiscussionActivity extends FirebaseLoginBaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_CAMERA = 2;
    private DiscussionActivity discussionActivity = this;

    public static String TAG = "powerchat-iut.messages";
    private Toolbar toolbar;
    private Sujet sujet;
    private String idSujet;
    private List<Message> listMessage;
    private Firebase mRef;
    private String mName;
    private RecyclerView.Adapter mDiscussionAdapter;
    private RecyclerView recyclerView;
    private Query mChatRef;

    private ImageButton mphotoButton;
    private ImageButton sendButton;
    private EditText mMessageASend;
    private CheckBox mposition_check;

    // pour la location
    private AddressResultReceiver mResultReceiver;
    protected Location mLastLocation;
    protected boolean mAddressRequested;
    protected GoogleApiClient mGoogleApiClient;
    protected String mAddressOutput;


    // pour la photo
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discussion);
        sujet = (Sujet) getIntent().getSerializableExtra("Subject");
        idSujet = (String) getIntent().getSerializableExtra("idSujet");
        mRef = new Firebase("https://powerchat-iut.firebaseio.com/messages/" + idSujet);
        mChatRef = mRef.limitToLast(50);
        Log.i("PCidSujet", idSujet);
        sendButton = (ImageButton) findViewById(R.id.imageButton);
        mMessageASend = (EditText) findViewById(R.id.editText);
        mposition_check = (CheckBox) findViewById(R.id.position_check);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sujet.getTitre()+ " créer par " + sujet.getAuthor());


        recyclerView = (RecyclerView) findViewById(R.id.message_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDiscussionAdapter = new TextSimpleAdapter(mChatRef, this);

        recyclerView.setAdapter(mDiscussionAdapter);

        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // location
        mAddressRequested = false;
        mResultReceiver = new AddressResultReceiver(new Handler());
        buildGoogleApiClient();
        fetchAddressButtonHandler();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                String uid = "";
                try {
                    name = mName;
                    uid = getAuth().getUid();
                } catch (Exception e) {
                    name = "anonyme";
                    uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                }


                String photoString = "";
                try{
                    ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
                    photo.recycle();
                    byte[] byteArray = bYtE.toByteArray();
                    photoString = Base64.encodeToString(byteArray, Base64.DEFAULT) ;
                }catch (Exception e){
                    photoString = "";
                }
                MessageTextSimple chat = new MessageTextSimple(name, uid, mMessageASend.getText().toString(), mAddressOutput,photoString);

                mRef.push().setValue(chat, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e(TAG, firebaseError.toString());
                        }
                    }
                });

                mMessageASend.setText("");
                mphotoButton.setBackground(ContextCompat.getDrawable(discussionActivity,R.color.white));
            }

        });

        //photo


        mphotoButton = (ImageButton) findViewById(R.id.photobutton);
        mphotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                dispatchTakePictureIntent();
        }else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            mphotoButton.setBackground(ContextCompat.getDrawable(this,R.color.colorAccent));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

        if (mName != null) {
            Log.i("PCajout", "ici : " + mName);
        }

        invalidateOptionsMenu();
        mDiscussionAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        Log.e(TAG, "Login user error: " + firebaseLoginError.toString());
        resetFirebaseLoginPrompt();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
            // It is possible that the user presses the button to get the address before the
            // GoogleApiClient object successfully connects. In such a case, mAddressRequested
            // is set to true, but no attempt is made to fetch the address (see
            // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
            // user has requested an address, since we now have a connection to GoogleApiClient.
            if (mAddressRequested) {
                startIntentService();
            }
        }
    }

    public void fetchAddressButtonHandler() {
        // We only start the service to fetch the address if GoogleApiClient is connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, we process the user's request by setting
        // mAddressRequested to true. Later, when GoogleApiClient connects, we launch the service to
        // fetch the address. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
    }

    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.

        Log.i("PCLocation","on lancer les Services");
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(mposition_check.isChecked()){
                mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            }
            Log.i("PCLocation","mAdressesOutput : "+mAddressOutput);
        }
    }


}
