package ir.telegif.telegif;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.concurrent.Exchanger;

/**
 * Created by HoseinGhahremanzadeh on 4/21/2016.
 */
public class Downloader extends AsyncTask<URL, Integer, Long> {
    private File file;
    private Context context;
    private URL url;

    private int size;
    private int error;
    private boolean done;
    private Data data = new Data();
    private boolean onQue = false;
    private boolean onSaving=false;


    static private Thread executer = null;
    static private boolean usingArrayList = false;
    static private ArrayList<Downloader> que = new ArrayList<Downloader>();
    static private boolean terminateAll = false;

    static private  int runningThreads=0;

    public static int getRunningThreads() {
        return runningThreads;
    }

    public static void startRunningDownloads() {
        terminateAll=false;
        usingArrayList=false;
        runningThreads=1;

        if (executer != null) {
            executer.interrupt();
            executer = null;
        }
        executer = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (terminateAll) {
                        return;
                    }
                    Downloader queTop = null;
                    if (!usingArrayList) {
                        usingArrayList = true;
                        if (que.size() != 0) {
                            queTop = que.get(0);
                            que.remove(0);
                        }
                        usingArrayList = false;
                    }
                    if (queTop != null) {
                        while (!queTop.isDone() && queTop.getError() != 7) {
                            if (terminateAll) {
                                if (!queTop.onSaving&& queTop.getStatus()==Status.RUNNING)
                                    queTop.cancel(true);
                                queTop.context=null;
                                return;
                            }
                            if (isNetworkAvailable(queTop.context)) {
                                if (queTop.getStatus()== Status.PENDING)
                                    queTop.execute();
                            }
                            if (!(!queTop.isDone() && queTop.getError() != 7))
                                break;
                            try {
                                sleep(1000);
                            } catch (Exception e) {
                            }
                        }
                        queTop.removeContext();
                        queTop.onQue = false;
                    } else {
                        try {
                            sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
        executer.start();
    }
    public static void stopRunningDownloads() {
        if (executer!=null) {
            executer.interrupt();
            executer = null;
        }
        for (int i=0;i<que.size();i++)
            que.get(i).context=null;
        que = new ArrayList<Downloader>();
    }

    public static void setTerminateAll(boolean terminateAll) {
        Downloader.terminateAll = terminateAll;
    }

    public void addToQue() {
        while (usingArrayList) {
        }
        usingArrayList = true;
        que.add(this);
        onQue = true;
        usingArrayList = false;
    }

    public int getSize() {
        return size;
    }

    public boolean isDone() {
        return done;
    }

    public int getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public Context getContext() {
        return context;
    }

    public boolean isOnQue() {
        return onQue;
    }

    public Downloader(Context context) {
        this.context = context;
        error = 0;
        size = 0;
        done = false;
    }

    public boolean setPath(String name, String path) {
        file = new File(path, name);
        return true;
    }

    public boolean setURL(String url) {
        try {
            this.url = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public boolean setURLSetPathInCache(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        String fileName = Uri.parse(url).getLastPathSegment();
        file = new File(context.getCacheDir(), fileName);
        return true;
    }

    public File getFile() {
        return file;
    }

    private boolean writeToFile() {
        onSaving=true;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.data);
            fos.close();
            onSaving=false;
            return true;
        } catch (Exception e) {
            error = 7;
        }
        onSaving=false;
        return false;
    }

    protected Long doInBackground(URL... urls) {
        runningThreads++;
        error = 0;
        size = 0;
        done = false;
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            error = 1;
            return (long) error;
        }
        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            error = 2;
            return (long) error;
        }
        urlConnection.setDoOutput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            error = 3;
            return (long) error;
        }
        InputStream inputStream;
        try {
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            error = 5;
            return (long) error;
        }
        byte[] buffer = new byte[1024*1024*5];
        int bufferLength;
        do {
            if (terminateAll)
                return 8L;
            try {
                bufferLength = inputStream.read(buffer);
                if (bufferLength > 0) {
                    byte NewData[] = new byte[size + bufferLength];
                    for (int i = 0; i < size; i++)
                        NewData[i] = data.data[i];
                    for (int i = 0; i < bufferLength; i++)
                        NewData[size + i] = buffer[i];
                    data.data = NewData;
                    size += bufferLength;
                }
            } catch (IOException e) {
                error = 6;
                return (long) error;
            }
        }
        while (bufferLength > 0);
        writeToFile();
        if (error == 7)
            return (long) error;
        done = true;
        return 0L;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        runningThreads--;
    }

    static public boolean isNetworkAvailable(Context context) {
        if (context!=null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                URL google = null;
                try {
                    google = new URL("http://www.telegif.ir/ConnectionTest/Connected.bin");
                } catch (Exception e) {
                    return false;
                }
                HttpURLConnection urlConnection;
                try {
                    urlConnection = (HttpURLConnection) google.openConnection();
                } catch (IOException e) {
                    return false;
                }
                try {
                    urlConnection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    return false;
                }
            /*urlConnection.setDoOutput(true);
            try {
                urlConnection.connect();
            } catch (IOException e) {
                return false;
            }*/
                return true;
            }
        }
        return false;
    }
    public void removeContext() {
        context=null;
    }
}
