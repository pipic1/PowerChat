package pic.pipic1.powerchat.View.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;

import java.text.SimpleDateFormat;

import pic.pipic1.powerchat.Model.MessageTextSimple;
import pic.pipic1.powerchat.R;

/**
 * Created by emimo on 17/03/2016.
 */
public class TextSimpleAdapter extends FirebaseRecyclerAdapter<MessageTextSimple, TextSimpleAdapter.MessageTextSimpleHolder> {

    private FirebaseLoginBaseActivity mContext;

    public TextSimpleAdapter(Query ref, FirebaseLoginBaseActivity context) {
        super(MessageTextSimple.class, R.layout.activity_discussion_singlemessage, MessageTextSimpleHolder.class, ref);
        mContext = context;

    }

    @Override
    public void populateViewHolder(MessageTextSimpleHolder chatView, MessageTextSimple chat, int position) {
//        chatView.setName(DateFormat.getDateFormat(mContext).format(chat.getDate()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM hh:mm");
        SimpleDateFormat formatter_hour_min = new SimpleDateFormat("HH:mm");
        chatView.setDate(formatter.format(chat.getDate()));
        chatView.setText(chat.getText());
        chatView.setPosition(chat.getLoc());
        chatView.setAuthor(chat.getName() + " à " + formatter_hour_min.format(chat.getDate()));
        chatView.setImageView(chat.getPhoto());
        boolean test = false;
        try {
            test = mContext.getAuth().getUid().equals(chat.getUid());
        }catch (Exception e){
            test = chat.getUid().equals(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        }
        chatView.setProp(test,mContext);
    }

    public static class MessageTextSimpleHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private TextView mAuthor;
        private TextView mPosition;
        private TextView mText;
        private View mSeparator;
        private ImageView mImageView;
        private LinearLayout mLayout;

        public MessageTextSimpleHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.author_textview);
            mDate = (TextView) itemView.findViewById(R.id.dateSingleMessage);
            mPosition = (TextView) itemView.findViewById(R.id.position_textview);
            mLayout = (LinearLayout) itemView.findViewById(R.id.singleMessageLayout);
            mText = (TextView) itemView.findViewById(R.id.message);
            mSeparator = itemView.findViewById(R.id.separator);
            mImageView = (ImageView) itemView.findViewById(R.id.photoview);
        }

        public void setProp(boolean test, Context mContext){
            if(test){
                mLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMyBubble));
            }else {
                 mLayout.setGravity(Gravity.LEFT);
                mLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bubbleColorBack));
            }
        }


        public void setDate(String name) {
            mDate.setText(name);
        }


        public void setAuthor(String author) {
            mAuthor.setVisibility(View.VISIBLE);
            mSeparator.setVisibility(View.VISIBLE);
            mAuthor.setText(author);
        }

        public void setPosition(String position) {
            mPosition.setVisibility(View.VISIBLE);
            mPosition.setText(position);
        }

        public void setText(String text) {
            mText.setText(text);
        }

        public void setImageView(String image) {
            if (!image.equals("")) {
                try {
                    mImageView.setVisibility(View.VISIBLE);
                    byte[] decodedBytes = Base64.decode(image, 0);
                    mImageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
                }catch (Exception e){
                    mImageView.setVisibility(View.GONE);
                }
            } else {
                mImageView.setVisibility(View.GONE);
            }
        }

    }
}
