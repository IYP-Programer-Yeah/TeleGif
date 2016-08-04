package ir.telegif.telegif;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;

import pl.droidsonroids.gif.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GifMenu extends AppCompatActivity {

    static public ShowcaseView SV;

    public Thread cleanerThread;
    public Thread contextRemoverThread;

    public Context context;

    public static byte[] wifiGIVBytes = {};
    public static byte[] loadingGIVBytes = {};

    private boolean stopAllThreads=false;

    private GifCustomAdapter NGCA=null;
    private GifCustomAdapter GCA=null;

    boolean internetConnectionAvailable=false;

    private ListView listView;


    private static final int REQUEST_WRITE_STORAGE = 112;


    public static void loadGifs(Context context) {
        //read the internet connection gif's bytes
        InputStream inputStreamWifi = context.getResources().openRawResource(SettingsActivity.wifiGifsRes[SettingsActivity.lang]);
        try {
            int wifiGIVSize = 0;
            wifiGIVSize = inputStreamWifi.available();
            wifiGIVBytes = new byte[wifiGIVSize];
            inputStreamWifi.read(wifiGIVBytes);
        } catch (Exception e) {
        }

        //read the loading gif's bytes
        InputStream inputStreamLoading = context.getResources().openRawResource(SettingsActivity.loadingGifsRes[SettingsActivity.lang]);

        try {
            int loadingGIVSize = 0;
            loadingGIVSize = inputStreamLoading.available();
            loadingGIVBytes = new byte[loadingGIVSize];
            inputStreamLoading.read(loadingGIVBytes);
        } catch (Exception e) {
        }
    }


    private void clearEverything(){
        //stop the thread to remove the invisible gif threads
        stopAllThreads=true;
        if (cleanerThread!=null) {
            cleanerThread.interrupt();
            while (cleanerThread.isAlive()) ;
        }
        cleanerThread=null;
        //stop downloader
        Downloader.setTerminateAll(true);
        Downloader.stopRunningDownloads();
        //clean the list view
        if (listView!=null) {
            listView.setVisibility(View.INVISIBLE);
            listView.invalidate();
            listView = null;
        }
        //remove contex
        context=null;
        if (GCA!=null) {
            GCA.removeContext();
        }
        if (NGCA!=null) {
            NGCA.removeContext();
        }
        NGCA=null;
        GCA=null;
        //run remover thread
        contextRemoverThread.run();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        //set title
        final String subCatName = getIntent().getExtras().getString("SubCatName");
        toolbar.setTitle(subCatName);
        setSupportActionBar(toolbar);

        //start que
        Downloader.startRunningDownloads();


        //get the data from the last activity
        final int CatID = getIntent().getExtras().getInt("CatID");
        final int SubCatID = getIntent().getExtras().getInt("SubCatID");
        //get the list view
        listView = (ListView) findViewById(R.id.GifLV1);

        //gifs of this sub cat
        final SubCatGifs subCatGifs = new SubCatGifs();

        //path address of the data in the server
        String GCAddress = ((("http://telegif.ir/GC/" + CatID) + "_") + SubCatID) + ".bin";

        //check cache to see if the file is already cached so if internet connection is not available we still can get the last data
        FileWrapper GCCachedFile = new FileWrapper(this);
        GCCachedFile.setPathInCache(GCAddress);
        if (GCCachedFile.isFileFound()) {
            GCCachedFile.readData();
            if (!GCCachedFile.isError()) {
                int GCNum = 0;
                for (int i = 0; i < 4; i++) {
                    GCNum *= 256;
                    GCNum += GCCachedFile.getData().data[3 - i];
                }

                subCatGifs.gifCount = GCNum;
                subCatGifs.gifs = new Gif[subCatGifs.gifCount];
            }
        }
        GCCachedFile.removeContext();

        //setting gif data
        for (int i = 0; i < subCatGifs.gifs.length; i++) {
            subCatGifs.gifs[i] = new Gif(this, ((((("http://telegif.ir/Gifs/" + CatID) + "_") + SubCatID) + "_") + (subCatGifs.gifs.length - i)) + ".gif");
        }

        //set the adapter for the list view
        GCA = new GifCustomAdapter(this, R.layout.gif_row, listView, subCatGifs.gifs);
        listView.setAdapter(GCA);

        //check if there is internet connection, if there is download GC and refresh if needed
        if (Downloader.isNetworkAvailable(this)) {
            Downloader GC = new Downloader(this) {
                @Override
                protected void onPostExecute(Long result) {
                    super.onPostExecute(result);
                    if (result == 0 && isDone()) {
                        int GCNum = 0;
                        for (int i = 0; i < 4; i++) {
                            GCNum *= 256;
                            GCNum += this.getData().data[3 - i];
                        }
                        if (subCatGifs.gifCount != GCNum) {
                            subCatGifs.gifCount = GCNum;
                            subCatGifs.gifs = new Gif[subCatGifs.gifCount];
                            //setting gif data
                            for (int i = 0; i < subCatGifs.gifs.length; i++) {
                                subCatGifs.gifs[i] = new Gif(this.getContext(), ((((("http://telegif.ir/Gifs/" + CatID) + "_") + SubCatID) + "_") + (subCatGifs.gifs.length - i)) + ".gif");
                            }
                            NGCA = new GifCustomAdapter(this.getContext(), R.layout.gif_row, listView, subCatGifs.gifs);
                            listView.setVisibility(View.INVISIBLE);
                            listView.invalidate();
                            listView.setAdapter(NGCA);
                            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                            listView.setVisibility(View.VISIBLE);
                            GCA.removeContext();
                        }
                    }
                    removeContext();
                }
            };
            GC.setURLSetPathInCache(GCAddress);
            GC.execute();
        }

        //thread to remove the invisible gif threads
        cleanerThread = new Thread() {
            public void run() {
                while (true) {
                    boolean connectionConditionUpdated=!(internetConnectionAvailable&&Downloader.isNetworkAvailable(context));
                    internetConnectionAvailable=Downloader.isNetworkAvailable(context);
                    try {
                        sleep(100);
                    } catch (Exception e) {
                    }
                    if (stopAllThreads)
                        return;
                    cleanerThread=Thread.currentThread();
                    for (int i = 0; i < subCatGifs.gifs.length; i++) {

                        if (listView.getFirstVisiblePosition() > i || listView.getLastVisiblePosition() < i) {
                            if (subCatGifs.gifs[i].gifDrawable != null)
                                if (subCatGifs.gifs[i].gifDrawable.isPlaying())
                                    subCatGifs.gifs[i].gifDrawable.stop();
                        }else if (subCatGifs.gifs[i].getLoadedGif()!=0&&connectionConditionUpdated&&!stopAllThreads){
                            final int position=i;
                            if (NGCA!=null){
                                Runnable notify = new Runnable(){
                                    @Override
                                    public void run() {
                                        NGCA.createView(position);
                                    }
                                };
                                Handler mainHandler = new Handler(context.getMainLooper());
                                mainHandler.post(notify);

                            } else if (GCA!=null){
                                Runnable notify = new Runnable(){
                                    @Override
                                    public void run() {
                                        GCA.createView(position);
                                    }
                                };
                                Handler mainHandler = new Handler(context.getMainLooper());
                                mainHandler.post(notify);
                            }
                        }
                    }

                }
            }
        };

        cleanerThread.start();

        contextRemoverThread = new  Thread() {
            public void run() {
                for (int i=0;i<subCatGifs.gifs.length;i++){
                    subCatGifs.gifs[i].context=null;
                    subCatGifs.gifs[i].data=null;
                    subCatGifs.gifs[i].GIV=null;
                    if (subCatGifs.gifs[i].gifDrawable!=null) {
                        subCatGifs.gifs[i].gifDrawable.stop();
                    }
                    subCatGifs.gifs[i].gifDrawable=null;
                    subCatGifs.gifs[i].URL=null;
                    subCatGifs.gifs[i].row=null;
                }
                System.gc();
            }
        };

        listView.setClickable(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("test", "value");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearEverything();
        GifMenu.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearEverything();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                } else
                {
                    Toast.makeText(this, SettingsActivity.PermissionNotGranted[SettingsActivity.lang], Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
