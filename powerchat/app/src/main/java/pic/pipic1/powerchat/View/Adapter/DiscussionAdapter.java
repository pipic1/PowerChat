package pic.pipic1.powerchat.View.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.List;

import pic.pipic1.powerchat.Model.Message;
import pic.pipic1.powerchat.Model.MessageText;
import pic.pipic1.powerchat.Model.Sujet;
import pic.pipic1.powerchat.R;
import pic.pipic1.powerchat.View.Discussion.DiscussionActivity;

/**
 * Created by ppier on 09/03/2016.
 */
public class DiscussionAdapter extends FirebaseRecyclerAdapter<MessageText,DiscussionAdapter.ViewHolder> {

    public List<MessageText> topic_messages;

    public DiscussionAdapter(Firebase firebase){
        super(MessageText.class,R.layout.activity_discussion_singlemessage, ViewHolder.class, firebase);
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, MessageText messageText, int i) {
        setMessage(messageText,viewHolder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View messageView;

        private TextView message;
        private  TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            messageView = itemView;
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.dateSingleMessage);
        }


    }

    private void setMessage(MessageText message, ViewHolder vh){
        vh.message.setText(message.getMessage());
        vh.date.setText(message.getSendDate().toString());
    }

}

