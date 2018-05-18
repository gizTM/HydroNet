package com.senior.gizgiz.hydronet.Adapter.GridViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.senior.gizgiz.hydronet.ClassForList.CalendarDate;
import com.senior.gizgiz.hydronet.Fragment.MonthlyStatFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<CalendarDate> calendarDates;
    private int selectedPosition = 999999;

    public CalendarAdapter(Context context, List<CalendarDate> calendarDates) {
        this.context = context;
        this.calendarDates = calendarDates;
    }

    @Override
    public int getCount() { return calendarDates.size(); }

    @Override
    public Object getItem(int i) { return calendarDates.get(i); }

    @Override
    public long getItemId(int i) { return 0; }

    public void setSelectedDate(boolean isDisplay,Date date,String event) {
        for(CalendarDate d : calendarDates) {
            Calendar calD = Calendar.getInstance();
            calD.setTime(d.getDate());
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);
            if(calD.get(Calendar.DAY_OF_MONTH)==calDate.get(Calendar.DAY_OF_MONTH) &&
                    calD.get(Calendar.MONTH)==calDate.get(Calendar.MONTH) && calD.get(Calendar.YEAR)==calDate.get(Calendar.YEAR)) {
                d.setDisplayDot(isDisplay);
                d.addEvents(event);
                notifyDataSetChanged();
//                Log.e("event","added");
            }
        }
    }
    public void resetAdapter() {
        calendarDates.clear();
        selectedPosition = 9999;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        CalendarAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (CalendarAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_calendar, null);
            viewHolder = new CalendarAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return view;
    }
    private class ViewHolder {
        private ImageView img;
        private CustomTextView day;
        private LinearLayout layout;

        public ViewHolder(View view) {
            this.img = view.findViewById(R.id.event);
            this.day = view.findViewById(R.id.cal_date);
            this.layout = view.findViewById(R.id.cal_date_layout);
        }

        public void bind(final int position) {
            final CalendarDate date = calendarDates.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date.getDate());
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            if(calendar.get(Calendar.DAY_OF_MONTH)==now.get(Calendar.DAY_OF_MONTH)
                    && calendar.get(Calendar.MONTH)==now.get(Calendar.MONTH)
                    && calendar.get(Calendar.YEAR)==now.get(Calendar.YEAR)) {
                day.setStyle("Bold");
                day.setTextColor(ResourceManager.getColor(context,R.color.alto_gray));
                layout.setBackground(ResourceManager.getDrawable(context,"bg_view_dropdown_item"));
            } else {
                day.setStyle("Regular");
                day.setTextColor(ResourceManager.getColor(context,R.color.gray));
                if(date.getSensorData()!=null) layout.setBackground(ResourceManager.getDrawable(context,"bg_frame_green_transparent"));
                else layout.setBackground(ResourceManager.getDrawable(context,"bg_sharp_frame"));
            }
            if(calendar.get(Calendar.YEAR)==2 && calendar.get(Calendar.MONTH)==11 && calendar.get(Calendar.DATE)==31)
                day.setText("");
            else day.setText(String.valueOf(calendar.get(Calendar.DATE)));
            if(selectedPosition == position) layout.setBackground(ResourceManager.getDrawable(context,"bg_frame_selected_date"));
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MonthlyStatFragment.setupEvent(context,date.getDate());
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });
            if(date.isDisplayDot()) img.setVisibility(View.VISIBLE);
            else img.setVisibility(View.INVISIBLE);
        }
    }
}
