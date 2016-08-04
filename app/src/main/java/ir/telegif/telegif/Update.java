package ir.telegif.telegif;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String link = getIntent().getExtras().getString("link");
        final int stat = getIntent().getExtras().getInt("stat");

        Button B1=(Button)findViewById(R.id.button);
        Button B2=(Button)findViewById(R.id.button2);


        if (B2 != null) {
            B2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (B1 != null) {
            B1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (stat == 2) {
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.farsitel.bazaar");

                        if (intent != null) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("bazaar://details?id=" + link));
                            intent.setPackage("com.farsitel.bazaar");
                            startActivity(intent);
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/" + link + "/?l=fa"));
                            startActivity(Intent.createChooser(intent, "Open link with:"));
                        }
                    }
                    if (stat == 3) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(Intent.createChooser(intent, "Open link with:"));
                    }
                    finish();
                }
            });
        }

    }

}
