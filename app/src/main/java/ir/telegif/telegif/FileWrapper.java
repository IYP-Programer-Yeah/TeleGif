package ir.telegif.telegif;

import android.content.Context;
import android.net.Uri;

import java.io.*;

/**
 * Created by livewin8.1.com on 4/24/2016.
 */
public class FileWrapper {
    private File file;
    private Context context;
    private Data data = new Data();
    private boolean fileFound;
    private boolean error;

    public File getFile() {
        return file;
    }

    public FileWrapper(Context context) {
        this.context = context;
    }

    public Data getData() {
        return this.data;
    }

    public boolean isError() {
        return error;
    }

    public void setPathInCache(String url) {
        String fileName = Uri.parse(url).getLastPathSegment();
        file = new File(context.getCacheDir(), fileName);
        fileFound = file.exists();
    }

    public void readData() {
        error=false;
        FileInputStream fos;
        try {
            fos = new FileInputStream(file);
            try {
                data.data = new byte[(int) file.length()];
                fos.read(data.data);
                fos.close();
            } catch (IOException e) {
                error = true;
            }
        } catch (FileNotFoundException e) {
            error = true;
        }
    }

    public boolean isFileFound() {
        return fileFound;
    }

    public void removeContext(){
        context=null;
    }

}
