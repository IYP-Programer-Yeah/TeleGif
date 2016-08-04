package ir.telegif.telegif;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public final static int fa=1;
    public final static int eng=0;

    public static int numberCachedGifsInSubCats=20;
    public static int lang=fa;

    public static int activity_cat_menu_res[]={R.layout.activity_category_menu,R.layout.activity_category_menu_fa};
    public static int about_text_res_ID[]={R.string.large_text,R.string.large_text_fa};

    public static String langs[]={"English","فارسی"};

    public static String seekBarString[][]={{"Cache every downloaded GIF.","Cache "," GIF"," GIFs"," of each sub-category."},{"همه ی گیف های دانلود شده را ذخیره کن.",""," "," ","گیف از هر زیر شاخه را ذخیره کن."}};
    public static String title[]={"Settings","تنظیمات"};
    public static String totalBytesTVString[][]={{"Total size of "," bytes cached data"},{""," بایت داده ذخیره شده است."}};
    public static String cacheSettingsString[]={"Cache Settings", "تنظیمات حافظه"};
    public static String langSettingsString[]={"Language Settings", "تنظیمات زبان"};
    public static String NoCGString[]={"Number of Cached GIFs", "تعداد گیف های ذخیره شده"};
    public static String clearCacheButtonString[]={"Clear Cache","حافظه را پاک کن"};
    public static String shareString[]={"Share this gif via","برنامه ای برای ارسال گیف انتخاب کنید"};

    public static String CatMenuTitle[]={"TeleGif","تله گیف"};

    public static String PermissionNotGranted[]={"The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission","برنامه به حافظه دسترسی ندارد. به همین دلیل ممکن است به درستی عمل نکند. لطفا به برنامه اجازه ی دسترسی به حافظه بدهید."};

    public static int loadingGifsRes[]={R.raw.loading,R.raw.loading_fa};
    public static int wifiGifsRes[]={R.raw.wifi,R.raw.wifi_fa};

    private Button clearCache;
    private SeekBar seekBar;
    private TextView countTV;
    private TextView totalBytesTV;
    private Toolbar toolbar;
    private TextView cacheSettings;
    private TextView langSettings;
    private TextView NoCG;

    //Total bytes of data in cache
    long totalByteSize=0;

    //Save settings
    public static void saveSettings(File file){
        try {
            byte[] data=new byte[2];
            data[0]=(byte)numberCachedGifsInSubCats;
            data[1]=(byte)lang;
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
        }

    }
    //load settings
    public static void loadSettings(File file) {
        if (file.exists()){
            FileInputStream fos;
            try {
                fos = new FileInputStream(file);
                try {
                    byte[] data = new byte[(int) file.length()];
                    fos.read(data);
                    numberCachedGifsInSubCats=data[0];
                    if (file.length()==2)
                        lang=data[1];
                    fos.close();
                } catch (IOException e) {
                }
            } catch (FileNotFoundException e) {
            }
        }
    }
    //Change lang and update UI
    private void updateLang(int lang){
        this.lang=lang;
        //Update number of gifs UI
        if (numberCachedGifsInSubCats == 51)
            countTV.setText(seekBarString[lang][0]);
        else
            countTV.setText(seekBarString[lang][1] + numberCachedGifsInSubCats + (numberCachedGifsInSubCats == 1 ? seekBarString[lang][2] : seekBarString[lang][3]) + seekBarString[lang][4]);
        //Update toolbar title
        toolbar.setTitle(title[lang]);
        setSupportActionBar(toolbar);
        //update bytes UI
        totalBytesTV.setText(totalBytesTVString[lang][0]+ totalByteSize + totalBytesTVString[lang][1]);
        //set the settings menu UI
        cacheSettings.setText(cacheSettingsString[lang]);
        langSettings.setText(langSettingsString[lang]);
        NoCG.setText(NoCGString[lang]);
        clearCache.setText(clearCacheButtonString[lang]);
        //reload gifs for the current lang
        GifMenu.loadGifs(this);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;
        //load settings
        final File file = new File(context.getCacheDir(), "Settings.bin");

        if (file.exists()){
            FileInputStream fos;
            try {
                fos = new FileInputStream(file);
                try {
                    byte[] data = new byte[(int) file.length()];
                    fos.read(data);
                    numberCachedGifsInSubCats=data[0];
                    if (file.length()==2)
                        lang=data[1];
                    fos.close();
                } catch (IOException e) {
                }
            } catch (FileNotFoundException e) {
            }
        }

        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title[lang]);
        setSupportActionBar(toolbar);
        //get the ref to UI
        clearCache = (Button) findViewById(R.id.button3);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        countTV = (TextView) findViewById(R.id.textView5);
        totalBytesTV = (TextView) findViewById(R.id.textView6);
        cacheSettings = (TextView) findViewById(R.id.textView2);
        langSettings = (TextView) findViewById(R.id.textView7);
        NoCG = (TextView) findViewById(R.id.textView4);
        //init texts
        cacheSettings.setText(cacheSettingsString[lang]);
        langSettings.setText(langSettingsString[lang]);
        NoCG.setText(NoCGString[lang]);
        //init seek bar value
        seekBar.setProgress((numberCachedGifsInSubCats - 1) * 2);
        //init seek bar text
        if (numberCachedGifsInSubCats == 51)
            countTV.setText(seekBarString[lang][0]);
        else
            countTV.setText(seekBarString[lang][1] + numberCachedGifsInSubCats + (numberCachedGifsInSubCats == 1 ? seekBarString[lang][2] : seekBarString[lang][3]) + seekBarString[lang][4]);

        //set up language spinner
        Spinner spinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdaptor=new ArrayAdapter<String>(this,R.layout.spinner_row,langs);

        spinner.setAdapter(spinnerAdaptor);
        spinner.setSelection(lang);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateLang(position);
                saveSettings(file);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set up seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberCachedGifsInSubCats = (progress / 2 + 1);
                if (numberCachedGifsInSubCats == 51)
                    countTV.setText(seekBarString[lang][0]);
                else
                    countTV.setText(seekBarString[lang][1] + numberCachedGifsInSubCats + (numberCachedGifsInSubCats == 1 ? seekBarString[lang][2] : seekBarString[lang][3]) + seekBarString[lang][4]);
                saveSettings(file);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final ArrayList<Integer>[] GifCounts = new ArrayList[12];


        final int[][] EmojiIDs = {
                {R.drawable.agree, R.drawable.amused, R.drawable.angry, R.drawable.applause, R.drawable.bfd, R.drawable.bored, R.drawable.confused, R.drawable.crying, R.drawable.disappointed, R.drawable.disgusted, R.drawable.embarrassed, R.drawable.excited, R.drawable.eyeroll, R.drawable.facepalm, R.drawable.fistbump, R.drawable.fml, R.drawable.frown, R.drawable.goodjob, R.drawable.godbye, R.drawable.happy, R.drawable.highfive, R.drawable.laughing, R.drawable.lol, R.drawable.no, R.drawable.notbad, R.drawable.ok, R.drawable.rage, R.drawable.sad, R.drawable.scared, R.drawable.shutup, R.drawable.sleepy, R.drawable.smile, R.drawable.sorry, R.drawable.success, R.drawable.thankyou, R.drawable.what, R.drawable.yawn, R.drawable.yes},
                {R.drawable.blackandwhite, R.drawable.cold, R.drawable.funny, R.drawable.scary, R.drawable.slowmotion, R.drawable.hd},
                {R.drawable.breakingup, R.drawable.cooking, R.drawable.crying, R.drawable.eating, R.drawable.dreaming, R.drawable.faint, R.drawable.falling, R.drawable.fighting, R.drawable.running, R.drawable.singing, R.drawable.pout, R.drawable.smoking, R.drawable.slapping, R.drawable.sneezing, R.drawable.waiting, R.drawable.tossingdrink},
                {R.drawable.minions, R.drawable.spongebob, R.drawable.aladdin, R.drawable._101dalmatians, R.drawable.batman, R.drawable.garfield, R.drawable.hoteltransylvania, R.drawable.pinkpanther, R.drawable.pinocchio, R.drawable.thelionking, R.drawable.walle, R.drawable.toystory, R.drawable.beautyandthebeast},
                {R.drawable.cat, R.drawable.dog, R.drawable.goat, R.drawable.hedgehog, R.drawable.monkey, R.drawable.rabbit, R.drawable.sloth, R.drawable.panda, R.drawable.others},
                {R.drawable.blackandwhite, R.drawable._3d, R.drawable.architecture, R.drawable.art, R.drawable.loop, R.drawable.photography, R.drawable.timelapse, R.drawable.typography},
                {R.drawable.obama, R.drawable.clinton, R.drawable.trump},
                {R.drawable.soccer, R.drawable.baseball, R.drawable.basketball, R.drawable.boxing, R.drawable.parkour, R.drawable.volleyball, R.drawable.wrestling, R.drawable.mma},
                {R.drawable.bmw, R.drawable.ferrari, R.drawable.audi, R.drawable.lamborghini, R.drawable.subway},
                {R.drawable.troll, R.drawable.surprisedpatrick, R.drawable.sturgeonface, R.drawable.comeatmebro, R.drawable.likeaboss, R.drawable.fail},
                {R.drawable.barcelona, R.drawable.realmadrid, R.drawable.tele_gif_football, R.drawable.mnu, R.drawable.chelsea, R.drawable.liverpool, R.drawable.mnc, R.drawable.juventus, R.drawable.bayern},
                {R.drawable.gameofthrones}};


        for (int i = 0; i < 12; i++) {
            GifCounts[i]=new ArrayList<Integer>();
            for (int j = 0; j < EmojiIDs[i].length; j++) {
                FileWrapper GCCachedFile = new FileWrapper(this);
                GCCachedFile.setPathInCache("www.telegif.ir/" + (i + 1) + "_" + (j + 1) + ".bin");
                if (GCCachedFile.isFileFound()) {
                    GCCachedFile.readData();
                    if (!GCCachedFile.isError()) {
                        int GCNum = 0;
                        for (int k = 0; k < 4; k++) {
                            GCNum *= 256;
                            GCNum += GCCachedFile.getData().data[3 - k];
                        }
                        GifCounts[i].add(GCNum);
                    }
                }else
                    GifCounts[i].add(0);
                GCCachedFile.removeContext();
            }
        }
        //size of cached gifs
        long gifSize = 0;
        //calc the cached gif size
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < EmojiIDs[i].length; j++)
                for (int k = 0; k < GifCounts[i].get(j); k++) {
                    FileWrapper GCCachedFile = new FileWrapper(this);
                    GCCachedFile.setPathInCache("www.telegif.ir/" + (i + 1) + "_" + (j + 1) + "_" + (k + 1) + ".gif");
                    if (GCCachedFile.isFileFound())
                        gifSize += GCCachedFile.getFile().length();
                }
        //update total byte size
        totalByteSize = gifSize;

        //initialize total bytes text
        totalBytesTV.setText(totalBytesTVString[lang][0]+ totalByteSize + totalBytesTVString[lang][1]);
        //initialize clear cache button text
        clearCache.setText(clearCacheButtonString[lang]);

        //clear cache
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 12; i++)
                    for (int j = 0; j < EmojiIDs[i].length; j++)
                        for (int k = 0; k < GifCounts[i].get(j); k++) {
                            FileWrapper GCCachedFile = new FileWrapper(context);
                            GCCachedFile.setPathInCache("www.telegif.ir/" + (i + 1) + "_" + (j + 1) + "_" + (k + 1) + ".gif");
                            if (GCCachedFile.isFileFound())
                                GCCachedFile.getFile().delete();
                        }
                totalByteSize=0;
                totalBytesTV.setText(totalBytesTVString[lang][0]+ totalByteSize + totalBytesTVString[lang][1]);
            }

        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //reload category menu to update lang
        Intent intent = new Intent(this,CategoryMenu.class);
        startActivity(intent);
        finish();
    }
}
