package ir.telegif.telegif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView aboutTV = (TextView) findViewById(R.id.aboutTV1);
        aboutTV.setText(SettingsActivity.about_text_res_ID[SettingsActivity.lang]);
        aboutTV.setClickable(true);
        aboutTV.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
