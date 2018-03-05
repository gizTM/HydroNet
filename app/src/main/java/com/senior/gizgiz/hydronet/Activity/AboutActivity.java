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
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by Admins on 004 04/03/2018.
 */

public class AboutActivity extends AppCompatActivity {
    private static List<String> licenses = new ArrayList<>();

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
    }

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View contentPage, aboutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // use recyclable main xml w/ ViewStub content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup toolbar drawer
        setup();
        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.activity_about);
        aboutContent = contentStub.inflate();
        findViewById(R.id.fab_layout).setVisibility(View.GONE);

        CustomTextView licenseLink = aboutContent.findViewById(R.id.license);
        licenseLink.setMovementMethod(BetterLinkMovementMethod.getInstance());
        for (String license : licenses)
            licenseLink.append(Html.fromHtml(license+"\n"));
    }

    private void setup() {
        toolbar = findViewById(R.id.toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_license);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.findViewById(R.id.action_quick_user).setVisibility(View.GONE);
        toolbar.findViewById(R.id.action_quick_notification).setVisibility(View.GONE);
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
