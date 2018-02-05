package com.senior.gizgiz.hydronet.Activity;

/**
 * Created by Admins on 003 3/2/2018.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.apptik.widget.multiselectspinner.ExpandableMultiSelectSpinner;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;


public class ExampleMultiSpinner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_multi_spinner);

        ArrayList<String> options = new ArrayList<>();
        options.add("1");
        options.add("2");
        options.add("3");
        options.add("A");
        options.add("B");
        options.add("C");


        MultiSelectSpinner multiSelectSpinner1 = findViewById(R.id.multiselectSpinner1);
        multiSelectSpinner1.setItems(options)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(false)
                .selectItem(0, true)
                .selectItem(1, true)
                .selectItem(2, true)
        ;

        MultiSelectSpinner multiSelectSpinner2 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner2);
        MultiSelectSpinner multiSelectSpinner3 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner3);
        MultiSelectSpinner multiSelectSpinner4 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner4);
        MultiSelectSpinner multiSelectSpinner5 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner5);
        ArrayAdapter<String> adapter2 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_multiple_choice, options);
        ArrayAdapter<String> adapter3 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_checked, options);
        ArrayAdapter<String> adapter4 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, options);
        ArrayAdapter<String> adapter5 = new ArrayAdapter <String>(this, R.layout.example_custom_item, options);


        multiSelectSpinner2
                .setListAdapter(adapter2)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                //deselects item
                .selectItem(2,false)
                .setMinSelectedItems(1);

        multiSelectSpinner3
                .setListAdapter(adapter3)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(R.string.title)
                .setMinSelectedItems(1);

        multiSelectSpinner4
                .setListAdapter(adapter4)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(getResources().getString(R.string.title))
                .setMinSelectedItems(1);

        multiSelectSpinner5
                .setListAdapter(adapter5)
                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setSpinnerItemLayout(R.layout.example_custom_spinner_item)
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle("Custom Types Selector")
                .setMinSelectedItems(1);



        ExpandableMultiSelectSpinner multiSelectSpinner6 = (ExpandableMultiSelectSpinner) findViewById(R.id.multiselectSpinner6);
        LinkedHashMap<String, List<String>> items =  new LinkedHashMap<>();
        ArrayList<String> items1 = new ArrayList<>();
        items1.add("A");items1.add("B");items1.add("C");items1.add("D");items1.add("E");
        ArrayList<String> items2 = new ArrayList<>();
        items2.add("1");items2.add("2");items2.add("3");items2.add("4");items2.add("5");

        items.put("Abc", items1);
        items.put("123", items2);
        multiSelectSpinner6.setItems(items)
//                .setListAdapter(adapter5, "All Types", "none selected",
//                        new MultiSelectSpinner.MultiSpinnerListener() {
//                    @Override
//                    public void onItemsSelected(boolean[] checkedItems) {
//                    }
//                })
                //
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")

                // .setSelectAll(true)
                .setTitle("Select Types from Groups")


        ;

        MultiSelectSpinner multiSelectSpinner7 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner7);
        ArrayAdapter<String> adapter7 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_checked, options);
        MultiSelectSpinner multiSelectSpinner8 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner8);
        ArrayAdapter<String> adapter8 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_checked, options);
        MultiSelectSpinner multiSelectSpinner9 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner9);
        ArrayAdapter<String> adapter9 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_checked, options);
        MultiSelectSpinner multiSelectSpinner10 = (MultiSelectSpinner) findViewById(R.id
                .multiselectSpinner10);
        ArrayAdapter<String> adapter10 = new ArrayAdapter <String>(this, android.R.layout
                .simple_list_item_checked, options);

        multiSelectSpinner7
                .setListAdapter(adapter7)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(R.string.title)
                .setMinSelectedItems(1);

        multiSelectSpinner8
                .setListAdapter(adapter8)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(R.string.title)
                .setMinSelectedItems(1);

        multiSelectSpinner9
                .setListAdapter(adapter9)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(R.string.title)
                .setMinSelectedItems(1);

        multiSelectSpinner10
                .setListAdapter(adapter10)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("none selected")
                .setSelectAll(true)
                .setTitle(R.string.title)
                .setMinSelectedItems(1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
