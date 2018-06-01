package com.propelld.app.bakingapp.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.propelld.app.bakingapp.models.Step;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThumbnailTask extends AsyncTask<String, Void, Bitmap>
{
    private ImageView imageView;
    private Context context;
    private final int kind;
    private ProgressDialog progressDialog;
    private TaskListener listener;
    private Step step;

    public ThumbnailTask(ImageView imageView,
                         Context context,
                         int kind,
                         TaskListener listener,
                         Step step)
    {
        super();
        this.imageView = imageView;
        this.context = context;
        this.kind = kind;
        this.listener = listener;
        this.step = step;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected Bitmap doInBackground(String... urls)
    {
        Bitmap bitmap = null;
        try
        {
            String fileName = Uri.parse(urls[0]).getLastPathSegment();
            File directory = context.getFilesDir();
            File file = new File(directory, fileName);

            if (file.exists())
            {
                bitmap = ThumbnailUtils
                        .createVideoThumbnail(file.getPath(), kind);
            }
            else
            {
                URL url = new URL(urls[0]);
                HttpURLConnection con = null;

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                int status = con.getResponseCode();

                InputStream is = con.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1)
                {
                    fos.write(buffer, 0, len1);
                }

                bitmap = ThumbnailUtils
                        .createVideoThumbnail(file.getPath(), kind);

                fos.close();
                is.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap resBitmap )
    {
        if (resBitmap != null)
        {
            imageView.setImageBitmap(resBitmap);
            step.setThumbnail(resBitmap);
        }

        listener.onTaskFinished();
    }
}