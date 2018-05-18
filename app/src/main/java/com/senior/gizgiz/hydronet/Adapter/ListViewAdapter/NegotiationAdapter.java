package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Entity.Negotiation;
import com.senior.gizgiz.hydronet.HelperClass.CustomEditText;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.ValueStepper;
import com.senior.gizgiz.hydronet.R;

import java.util.Date;
import java.util.List;

public class NegotiationAdapter extends BaseAdapter {
    private Context context;
    private List<Negotiation> negotiations;

    public NegotiationAdapter(Context context,List<Negotiation> negotiations) {
        this.context = context;
        this.negotiations = negotiations;
    }

    @Override
    public int getCount() {
        return negotiations.size();
    }

    @Override
    public Object getItem(int position) {
        return negotiations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NegotiationAdapter.ViewHolder viewHolder;
        if(convertView != null) {
            viewHolder = (NegotiationAdapter.ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_negotiation,null);
            viewHolder = new NegotiationAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return convertView;
    }

    private class ViewHolder {
        private CustomTextView owner,timestamp;
        private CustomEditText condition;
        private LinearLayout negotiationLayout;

        public ViewHolder(View view) {
            this.owner = view.findViewById(R.id.negotiator);
            this.condition = view.findViewById(R.id.condition);
            this.timestamp = view.findViewById(R.id.timestamp);
            this.negotiationLayout = view.findViewById(R.id.negotiation_layout);
        }

        public void bind(int position) {
            final Negotiation card = negotiations.get(position);
            owner.setText(card.getNegotiator());
            condition.setText(card.getCondition());
            timestamp.setText(new TimeAgo().timeAgo(Long.valueOf(card.getTimestamp())));
            condition.setEnabled(false);

            if(card.getNegotiatorId().equalsIgnoreCase(MainActivity.currentUser.getUid())) {
                owner.setText("You");
                owner.setTextColor(ResourceManager.getColor(context,R.color.white));
                condition.setTextColor(ResourceManager.getColor(context,R.color.white));
                timestamp.setTextColor(ResourceManager.getColor(context,R.color.white));
                negotiationLayout.setBackgroundColor(ResourceManager.getColor(context,R.color.light_green));
            } else {
                owner.setTextColor(ResourceManager.getColor(context,R.color.boulder_gray));
                condition.setTextColor(ResourceManager.getColor(context,R.color.boulder_gray));
                timestamp.setTextColor(ResourceManager.getColor(context,R.color.boulder_gray));
                negotiationLayout.setBackgroundColor(ResourceManager.getColor(context,R.color.app_bg));
            }
        }
    }
}
