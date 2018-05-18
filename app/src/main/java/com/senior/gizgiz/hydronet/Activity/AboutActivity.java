package com.senior.gizgiz.hydronet.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by Admins on 004 04/03/2018.
 */

public class AboutActivity extends AppCompatActivity {
    private boolean expandCredit = true, expandSource = true;
    private static List<String> licenses = new ArrayList<>();
    private static List<String> sources = new ArrayList<>();

    static {
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/ocha\" title=\"OCHA\">OCHA</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"http://www.freepik.com\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/smashicons\" title=\"Smashicons\">Smashicons</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/twitter\" title=\"Twitter\">Twitter</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/google\" title=\"Google\">Google</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/vectors-market\" title=\"Vectors Market\">Vectors Market</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/eleonor-wang\" title=\"Eleonor Wang\">Eleonor Wang</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/simpleicon\" title=\"SimpleIcon\">SimpleIcon</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/dave-gandy\" title=\"Dave Gandy\">Dave Gandy</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/roundicons\" title=\"Roundicons\">Roundicons</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/gregor-cresnar\" title=\"Gregor Cresnar\">Gregor Cresnar</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/cole-bemis\" title=\"Cole Bemis\">Cole Bemis</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/lyolya\" title=\"Lyolya\">Lyolya</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/those-icons\" title=\"Those Icons\">Those Icons</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/yannick\" title=\"Yannick\">Yannick</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/chanut\" title=\"Chanut\">Chanut</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/lucy-g\" title=\"Lucy G\">Lucy G</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");
        licenses.add("<div>Icons made by <a href=\"https://www.flaticon.com/authors/popcorns-arts\" title=\"Icon Pond\">Icon Pond</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>");

        sources.add("<div>PH/ TDS / PPM / EC LEVELS FOR HYDROPONIC VEGETABLES from <a href=\"https://growguru.co.za/ph-tds-ppm-ec-levels-for-hydroponic-vegetables/\">here</a></div>");
        sources.add("<div>Plant PH/EC/PPM from <a href=\"https://hydroponic.co.za/hydroponics-quickstart/plant-phecppm/\">here</a></div>");
        sources.add("<div>What Is The Best pH And EC Levels For Hydro Marijuana Growers? from <a href=\"https://www.themaven.net/theweedblog/growing/what-is-the-best-ph-and-ec-levels-for-hydro-marijuana-growers-PFG2tYveHUaZIMsZ_8td5w?full=1\">here</a></div>");
        sources.add("<div>What Can You Grow Hydroponically? from <a href=\"http://modularhydro.com/ArticleLibrary/WhatCanYouGrowHydroponically.html\">here</a></div>");
    }

    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setup();

        final CustomTextView licenseLink = findViewById(R.id.license);
        final CustomTextView sourceLink =  findViewById(R.id.info_credit);
        findViewById(R.id.credit_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!expandCredit) NavigationManager.expand(licenseLink);
                else  NavigationManager.collapse(licenseLink);
                expandCredit = !expandCredit;
            }
        });
        findViewById(R.id.source_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!expandSource) NavigationManager.expand(sourceLink);
                else NavigationManager.collapse(sourceLink);
                expandSource = !expandSource;
            }
        });
        licenseLink.setMovementMethod(BetterLinkMovementMethod.getInstance());
        sourceLink.setMovementMethod(BetterLinkMovementMethod.getInstance());
        for (String license : licenses)
            licenseLink.append(Html.fromHtml(license));
        for (String source : sources)
            sourceLink.append(Html.fromHtml(source));
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_license);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
        drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
