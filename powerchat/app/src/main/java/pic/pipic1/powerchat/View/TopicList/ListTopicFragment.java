package pic.pipic1.powerchat.View.TopicList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import pic.pipic1.powerchat.Model.MessageText;
import pic.pipic1.powerchat.Model.Sujet;
import pic.pipic1.powerchat.R;
import pic.pipic1.powerchat.View.Adapter.SujetAdapter;

/**
 * Created by emimo on 02/03/2016.
 */
public class ListTopicFragment extends Fragment{


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Firebase ref;


    private List<Sujet> sujetList = new ArrayList();

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("PCfrag","on passe la");
        View view = inflater.inflate(R.layout.fragmentlisttopic,null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SujetAdapter(new Firebase("https://powerchat-iut.firebaseio.com/sujets"));

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
