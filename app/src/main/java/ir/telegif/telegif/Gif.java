package ir.telegif.telegif;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import pl.droidsonroids.gif.*;

import java.io.IOException;
import java.net.URL;

/**
 * Created by livewin8.1.com on 4/24/2016.
 */
public class Gif {
    public Context context;
    public String URL;
    public GifImageView GIV;
    public View row;
    public Data data=null;
    public GifDrawable gifDrawable;
    public ImageButton shareButton;

    private int loadedGif=0;

    static public int LOADING_GIF_LOADED=1;
    static public int WIFI_GIF_LOADED=2;
    static public int FILE_GIF_LOADED=3;
    static public int DOWNLOADED_GIF_LOADED=4;

    public int getLoadedGif() {
        return loadedGif;
    }

    public void setLoadedGif(int loadedGif) {
        this.loadedGif = loadedGif;
    }

    public Gif(Context context, String URL){
        this.context = context;
        this.URL=URL;
        GIV=null;
        row=null;
        gifDrawable=null;
    }
}
