package ir.telegif.telegif;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    public static Context context;

    public static LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.gc();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        context=this;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vpPager.setAdapter(adapterViewPager);
        FirstFragment.view=null;
        SecondFragment.view=null;
        ThirdFragment.view=null;
        ForthFragment.view=null;
    }
}
