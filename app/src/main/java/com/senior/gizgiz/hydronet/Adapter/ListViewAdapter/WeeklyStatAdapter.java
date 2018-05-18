package com.senior.gizgiz.hydronet.Adapter.ListViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Entity.SensorData;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class WeeklyStatAdapter extends BaseAdapter {
    private Context context;
    private List<GrowHistory> weeklyStat;

    public WeeklyStatAdapter(Context context, List<GrowHistory> histories) {
        this.context = context;
        this.weeklyStat = histories;
    }

    @Override
    public int getCount() {
        return weeklyStat.size();
    }

    @Override
    public Object getItem(int i) {
        return weeklyStat.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        WeeklyStatAdapter.ViewHolder viewHolder;
        if(view != null) {
            viewHolder = (WeeklyStatAdapter.ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_weekly_stat,null);
            viewHolder = new WeeklyStatAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(i);
        return view;
    }

    private class ViewHolder {
        private CustomTextView date, waterLevel, pHLevel, ecLevel;
        private LineChartView waterChart, pHChart, ecChart;

        public ViewHolder(View view) {
            this.date = view.findViewById(R.id.weekly_date);
            this.waterLevel = view.findViewById(R.id.weekly_water_value);
            this.pHLevel = view.findViewById(R.id.weekly_ph_value);
            this.ecLevel = view.findViewById(R.id.weekly_ec_value);
            this.waterChart = view.findViewById(R.id.weekly_water_graph);
            this.pHChart = view.findViewById(R.id.weekly_ph_graph);
            this.ecChart = view.findViewById(R.id.weekly_ec_graph);
        }

        public void bind(int position) {
            GrowHistory history = weeklyStat.get(position);
            List<SensorData> sensorData = new ArrayList<>();
            if(history.getSensorDataList()!=null) sensorData = history.getSensorDataList();
            List<Date> dates = new ArrayList<>();
            float water = 0, pH = 0, ec = 0;
            for(SensorData unit : sensorData) {
                dates.add(new Date(Long.valueOf(unit.getTimestamp())));
                water+=unit.getWaterLevel();
                pH+=unit.getpHLevel();
                ec+=unit.getECLevel();
            }
            List<PointValue> waterValues = new ArrayList<>(), pHValues = new ArrayList<>(), ecValues = new ArrayList<>();
//            Log.e("sensorData size",sensorData.size()+"");
            for(int i=0; i<sensorData.size(); i++) {
                waterValues.add(new PointValue(i, sensorData.get(i).getWaterLevel()));
                pHValues.add(new PointValue(i, sensorData.get(i).getpHLevel()));
                ecValues.add(new PointValue(i, sensorData.get(i).getECLevel()));
            }
            List<WeekDay> weekDays = getListOfWeeksFromListOfDates(dates);
            date.setText(history.getPlantName().concat(" - planted on ".concat(String.valueOf(ResourceManager.shortDateFormat.format(
                    new Date(Long.valueOf(history.getStartDate())))))));
            waterLevel.setText(String.valueOf(water/sensorData.size()));
            pHLevel.setText(String.valueOf(pH/sensorData.size()));
            ecLevel.setText(String.valueOf(ec/sensorData.size()));
            Line waterLine = new Line(waterValues).setColor(Color.BLUE).setCubic(false).setHasPoints(true).setHasLabelsOnlyForSelected(true);
            Line pHLine = new Line(pHValues).setColor(Color.GREEN).setCubic(false).setHasPoints(true).setHasLabelsOnlyForSelected(true);
            Line ecLine = new Line(ecValues).setColor(Color.RED).setCubic(false).setHasPoints(true).setHasLabelsOnlyForSelected(true);
            List<Line> waterLines = new ArrayList<>();
            List<Line> pHLines = new ArrayList<>();
            List<Line> ecLines = new ArrayList<>();
            waterLines.add(waterLine);
            pHLines.add(pHLine);
            ecLines.add(ecLine);
            LineChartData waterData = new LineChartData();
            LineChartData pHData = new LineChartData();
            LineChartData ecData = new LineChartData();

            List<AxisValue> axisValuesForX = new ArrayList<>();
            List<AxisValue> axisValuesForY = new ArrayList<>();
            AxisValue tempAxisValue;
            String days[] = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
            for (int i=0; i<days.length; i++) {
                tempAxisValue = new AxisValue(i);
                tempAxisValue.setLabel(days[i]);
                axisValuesForX.add(tempAxisValue);
            }

            for (int i=0; i<8; i++) {
                tempAxisValue = new AxisValue(i);
                tempAxisValue.setLabel(i+"");
                axisValuesForY.add(tempAxisValue);
            }

            Axis xAxis = new Axis(axisValuesForX);
            Axis yAxis = new Axis(axisValuesForY);
            waterData.setAxisXBottom(xAxis); pHData.setAxisXBottom(xAxis); ecData.setAxisXBottom(xAxis);
            waterData.setAxisYLeft(yAxis); pHData.setAxisYLeft(yAxis); ecData.setAxisYLeft(yAxis);

            waterData.setLines(waterLines);
            pHData.setLines(pHLines);
            ecData.setLines(ecLines);
            waterChart.setLineChartData(waterData);
            pHChart.setLineChartData(pHData);
            ecChart.setLineChartData(ecData);
//            drawSinAbsChart(waterChart);
        }
    }

    private List<WeekDay> getListOfWeeksFromListOfDates(List<Date> listOfDates) {
        List<WeekDay> listOfWeeks = new ArrayList<>();
        WeekDay weekDay;
        for (Date date : listOfDates) {
            weekDay = new WeekDay(date, ResourceManager.weekFormat.format(date));
            listOfWeeks.add(weekDay);
        }
        return listOfWeeks;
    }

    private class WeekDay {
        Date date;
        String weekIdentifier;
        public WeekDay(Date Date, String WeekIdentifier) {
            this.date = Date;
            this.weekIdentifier = WeekIdentifier;
        }
        public Date getDate() {
            return date;
        }
        public String getWeekIdentifier() {
            return weekIdentifier;
        }

    }
}
