package ir.telegif.telegif;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class TeleGif extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public static final int version=1;
    public static final int revision=1;
    public static final int major=1;
    public static final int minor=2;

    private boolean playTutorial=false;

    private ImageView Splash;
    private GifDrawable tutorial;

    private Context context=this;

    public static boolean catFR,subCatFR,gifsFR;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_tele_gif);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Splash = (ImageView) findViewById(R.id.imageView);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();


        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int IMGWidthArr[] = {1080, 1280};
        int IMGHeightArr[] = {1920, 800};

        int Img;

        if (width > height) {
            Img = 1;
            Splash.setImageResource(R.drawable.splash1);
        } else {
            Img = 0;
            Splash.setImageResource(R.drawable.splash);
        }

        int IMGWidth = IMGWidthArr[Img], IMGHeight = IMGHeightArr[Img];
        if (((float) height / (float) IMGHeight) > ((float) width / (float) IMGWidth)) {
            Splash.setLayoutParams(new RelativeLayout.LayoutParams((int) ((float) IMGWidth * (float) height / ((float) IMGHeight)),
                            height)
            );
            Splash.setPadding((width - (int) ((float) IMGWidth * (float) height / ((float) IMGHeight))), 0, 0, 0);
        } else {
            Splash.setLayoutParams(new RelativeLayout.LayoutParams(width,
                            (int) ((float) IMGHeight * (float) width / ((float) IMGWidth)))
            );
            Splash.setPadding(0, (height - (int) ((float) IMGHeight * (float) width / ((float) IMGWidth))), 0, 0);
        }

        CategoryMenu.updateStat=new Downloader(this){
            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                if (isDone()) {
                    byte[] data=getData().data;
                    if (data[0] != 0) {
                        String link = new String("");
                        boolean versionIncluded=false;
                        for (int i = 1; i < getSize(); i++) {
                            if (getData().data[i]==0) {
                                versionIncluded = true;
                                break;
                            }
                            link += (char) getData().data[i];
                        }
                        int nVersion=version;
                        int nReversion=revision;
                        int nMajor=major;
                        int nMinor=minor;

                        if (versionIncluded){
                            nVersion=data[data.length-4];
                            nReversion=data[data.length-3];
                            nMajor=data[data.length-2];
                            nMinor=data[data.length-1];
                        }

                        int stat=getData().data[0];


                        if (nVersion!=version||nReversion!=revision||nMajor!=major||nMinor!=minor) {
                            Intent update = new Intent(TeleGif.this, Update.class);
                            update.putExtra("link", link);
                            update.putExtra("stat", stat);
                            startActivity(update);
                        }
                    }
                }
            }

        };

        CategoryMenu.updateStat.setURLSetPathInCache("http://telegif.ir/Update/UpdateStat.bin");

        HGThread  timerThread = new HGThread () {
            @Override
            protected Object doInBackground(Object... params) {
                super.doInBackground();
                try{
                    Thread.sleep(SPLASH_DISPLAY_LENGTH);
                }catch(InterruptedException e){
                }finally{
                    if (playTutorial) {

                        context = null;
                        Intent feature = new Intent(TeleGif.this,MainActivity.class);
                        startActivity(feature);
                        TeleGif.this.finish();
                    }else {

                        context = null;
                        Intent intent = new Intent(TeleGif.this, CategoryMenu.class);
                        startActivity(intent);
                        TeleGif.this.finish();
                    }
                }
                return null;
            }
        };
        timerThread.execute();

        final File settingsFile = new File(this.getCacheDir(), "Settings.bin");

        playTutorial=!settingsFile.exists();
        catFR=playTutorial;
        subCatFR=playTutorial;
        gifsFR=playTutorial;

        SettingsActivity.loadSettings(settingsFile);
        GifMenu.loadGifs(this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}

