package pic.pipic1.powerchat.View.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.List;
import java.util.Objects;

import pic.pipic1.powerchat.Model.Sujet;
import pic.pipic1.powerchat.R;
import pic.pipic1.powerchat.View.Discussion.DiscussionActivity;

/**
 * Created by emimo on 06/03/2016.
 */
public class SujetAdapter extends FirebaseRecyclerAdapter<Sujet,SujetAdapter.ViewHolder>{

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Sujet sujet, int i) {
        setSujet(sujet,viewHolder,i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitre;
        private TextView mDesc;

        private View mView;

        public ViewHolder(View v){
            super(v);
            mView = v;
            mTitre = ((TextView)mView.findViewById(R.id.sujettitre));
            mDesc = ((TextView)mView.findViewById(R.id.sujetdescription));
        }

    }

    private void setSujet(final Sujet s, ViewHolder vh,final int pos){
        vh.mTitre.setText(s.getTitre());
        vh.mDesc.setText(s.getDescription());
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick","on a cliqueez sur : "+s.getTitre());
                Intent intent = new Intent(v.getContext(), DiscussionActivity.class);
                intent.putExtra("Subject",s);
                Log.i("PCintent","la key : "+getRef(pos).getKey());
                intent.putExtra("idSujet",getRef(pos).getKey());
                v.getContext().startActivity(intent);
            }
        });
    }


    public SujetAdapter( Firebase firebase){
        super(Sujet.class,R.layout.describe_topic_layout,ViewHolder.class,firebase);
    }
}
