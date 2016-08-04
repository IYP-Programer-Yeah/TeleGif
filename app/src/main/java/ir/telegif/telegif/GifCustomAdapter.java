package ir.telegif.telegif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by livewin8.1.com on 4/24/2016.
 */
public class GifCustomAdapter extends ArrayAdapter<Gif> {

    private Context context;
    private int resource;
    private ListView listView;
    private Gif[] gifs;

    private int getViewCallCount;

    static private int MaxPreloadCount=10;

    public void startHelp(final int position){
        if (TeleGif.gifsFR&&position==0&&(gifs[position].getLoadedGif()==Gif.LOADING_GIF_LOADED||gifs[position].getLoadedGif()==Gif.WIFI_GIF_LOADED)) {
            ViewTarget t = new ViewTarget(gifs[position].row);

            if (GifMenu.SV!=null){
                GifMenu.SV.hide();
                GifMenu.SV.setClickable(false);
                GifMenu.SV.setVisibility(View.INVISIBLE);
            }
            if (gifs[position].getLoadedGif()==Gif.LOADING_GIF_LOADED) {
                GifMenu.SV = new ShowcaseView.Builder((Activity) context)
                        .setTarget(t)
                        .setContentTitle("گیف ها")
                        .setContentText("منتظر بمانید تا گیف بارگذاری شود.")
                        .build();
            }else{
                GifMenu.SV = new ShowcaseView.Builder((Activity) context)
                        .setTarget(t)
                        .setContentTitle("گیف ها")
                        .setContentText("از اتصال به اینترنت اطمینان حاصل فرمایید.")
                        .build();
            }
            GifMenu.SV.hideButton();
            GifMenu.SV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        if (TeleGif.gifsFR&&position==0&&(gifs[position].getLoadedGif()==Gif.DOWNLOADED_GIF_LOADED||gifs[position].getLoadedGif()==Gif.FILE_GIF_LOADED)){
            ViewTarget t = new ViewTarget(gifs[position].shareButton);
            if (GifMenu.SV != null) {
                GifMenu.SV.hide();
                GifMenu.SV.setClickable(false);
                GifMenu.SV.setVisibility(View.INVISIBLE);
            }


            GifMenu.SV = new ShowcaseView.Builder((Activity) context)
                    .setTarget(t)
                    .setContentTitle("گیف ها")
                    .setContentText("با زدن بر روی دکمه ی شیر گیف را در شبکه های اجتماعی به اشتراک بگذارید.")
                    .build();

            GifMenu.SV.hideButton();
            GifMenu.SV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GifMenu.SV.hide();
                    GifMenu.SV.setClickable(false);
                    GifMenu.SV.setVisibility(View.INVISIBLE);

                    final File settingsFile = new File(context.getCacheDir(), "Settings.bin");
                    SettingsActivity.saveSettings(settingsFile);
                    share(position);
                    TeleGif.gifsFR=false;
                }
            });
        }
    }

    public void share(int position){
        File root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File dir = new File(root.getAbsolutePath() + "/TeleGif");
        dir.mkdirs(); // build directory
        Random generator = new Random();
        int n = 10;
        n = generator.nextInt(n);
        String fileName = "Image-" + n + ".gif";
        File file = new File(dir, fileName);
        if (file.exists()) file.delete();

        if (gifs[position].getLoadedGif() == Gif.FILE_GIF_LOADED || gifs[position].getLoadedGif() == Gif.DOWNLOADED_GIF_LOADED) {

            try {
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(gifs[position].data.data);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/gif");
            intent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
            context.startActivity(Intent.createChooser(intent, SettingsActivity.shareString[SettingsActivity.lang]));

        }
    }

    public void createView(final int position){
        GifImageView GIV;

        if (gifs[position].row==null) {
            View single_row;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            single_row = inflater.inflate(resource, null,
                    true);

            gifs[position].row=single_row;
            GIV = (GifImageView) single_row.findViewById(R.id.GIV1);
            gifs[position].GIV=GIV;
            gifs[position].shareButton = (ImageButton) single_row.findViewById(R.id.shareButton);
            gifs[position].shareButton.setVisibility(View.INVISIBLE);
            gifs[position].shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    share(position);
                }
            });
        } else {
            GIV=gifs[position].GIV;
        }

        if (gifs[position].getLoadedGif()!=Gif.FILE_GIF_LOADED&&gifs[position].getLoadedGif()!=Gif.DOWNLOADED_GIF_LOADED) {
            FileWrapper fileWrapper=new FileWrapper(context);
            fileWrapper.setPathInCache(gifs[position].URL);
            if (fileWrapper.isFileFound()) {//data was in file
                fileWrapper.readData();
                gifs[position].data=fileWrapper.getData();
                try {
                    gifs[position].gifDrawable = new GifDrawable(gifs[position].data.data);
                }catch (IOException e){
                }
                GIV.setImageDrawable(gifs[position].gifDrawable);
                gifs[position].GIV.setLayoutParams(new RelativeLayout.LayoutParams(listView.getWidth(), (int) ((float) gifs[position].gifDrawable.getIntrinsicHeight() * (float) listView.getWidth() / (float) gifs[position].gifDrawable.getIntrinsicWidth())));
                gifs[position].setLoadedGif(Gif.FILE_GIF_LOADED);
                fileWrapper.removeContext();
                if (position >= SettingsActivity.numberCachedGifsInSubCats && SettingsActivity.numberCachedGifsInSubCats != 51)
                    fileWrapper.getFile().delete();
                gifs[position].shareButton.setVisibility(View.VISIBLE);

                startHelp(position);
            }
            else if (Downloader.isNetworkAvailable(context)&&gifs[position].getLoadedGif()!=Gif.LOADING_GIF_LOADED){
                Downloader fileDownloader=new Downloader(context){
                    @Override
                    protected void onPostExecute(Long result){
                        super.onPostExecute(result);
                        if (result==0&&isDone()==true&&context!=null) {
                            gifs[position].data = getData();
                            try {
                                gifs[position].gifDrawable = new GifDrawable(gifs[position].data.data);
                            } catch (IOException e) {

                            }
                            gifs[position].GIV.setImageDrawable(gifs[position].gifDrawable);
                            gifs[position].GIV.setLayoutParams(new RelativeLayout.LayoutParams(listView.getWidth(), (int) ((float) gifs[position].gifDrawable.getIntrinsicHeight() * (float) listView.getWidth() / (float) gifs[position].gifDrawable.getIntrinsicWidth())));
                            gifs[position].setLoadedGif(Gif.DOWNLOADED_GIF_LOADED);
                            if (position >= SettingsActivity.numberCachedGifsInSubCats && SettingsActivity.numberCachedGifsInSubCats != 51)
                                getFile().delete();
                            gifs[position].shareButton.setVisibility(View.VISIBLE);

                            startHelp(position);
                            removeContext();
                        }
                    }
                };
                fileDownloader.setURLSetPathInCache(gifs[position].URL);
                fileDownloader.addToQue();
                try {
                    gifs[position].gifDrawable = new GifDrawable(GifMenu.loadingGIVBytes);
                }catch (IOException e){

                }
                gifs[position].GIV.setImageDrawable(gifs[position].gifDrawable);
                gifs[position].GIV.setLayoutParams(new RelativeLayout.LayoutParams(listView.getWidth(), (int) ((float) gifs[position].gifDrawable.getIntrinsicHeight() * (float) listView.getWidth() / (float) gifs[position].gifDrawable.getIntrinsicWidth())));
                gifs[position].setLoadedGif(Gif.LOADING_GIF_LOADED);
                startHelp(position);
            }
        }

        if (gifs[position].getLoadedGif()==0||gifs[position].getLoadedGif()==Gif.LOADING_GIF_LOADED){
            if (!Downloader.isNetworkAvailable(context)){
                try {
                    gifs[position].gifDrawable = new GifDrawable(GifMenu.wifiGIVBytes);
                }catch (IOException e){

                }
                gifs[position].GIV.setImageDrawable(gifs[position].gifDrawable);
                gifs[position].GIV.setLayoutParams(new RelativeLayout.LayoutParams(listView.getWidth(), (int) ((float) gifs[position].gifDrawable.getIntrinsicHeight() * (float) listView.getWidth() / (float) gifs[position].gifDrawable.getIntrinsicWidth())));
                gifs[position].setLoadedGif(Gif.WIFI_GIF_LOADED);
                startHelp(position);
            }
        }
    }

    public GifCustomAdapter(final Context context,final int resource,final ListView listView, final Gif[] gifs) {
        super(context,resource,gifs);

        getViewCallCount=0;

        this.context = context;
        this.resource = resource;
        this.listView = listView;
        this.gifs = gifs;

        for (int i=0;i<Math.min(gifs.length,MaxPreloadCount);i++){
            createView(i);

        }

    }

    @Override
    public View getView(final int position,final View convertView,final ViewGroup parent) {

        getViewCallCount++;
        if (getViewCallCount>30){
            getViewCallCount=0;
            System.gc();
        }

        createView(position);


        if (gifs[position].getLoadedGif()!=0) {
            gifs[position].GIV.setLayoutParams(new RelativeLayout.LayoutParams(listView.getWidth(), (int) ((float) gifs[position].gifDrawable.getIntrinsicHeight() * (float) listView.getWidth() / (float) gifs[position].gifDrawable.getIntrinsicWidth())));
            if (!gifs[position].gifDrawable.isPlaying())
                gifs[position].gifDrawable.start();
            gifs[position].gifDrawable.reset();
        }

        return gifs[position].row;
    }

    public void removeContext() {
        context=null;
    }
}
