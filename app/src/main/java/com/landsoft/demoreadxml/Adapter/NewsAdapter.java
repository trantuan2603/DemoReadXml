package com.landsoft.demoreadxml.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.landsoft.demoreadxml.MainActivity;
import com.landsoft.demoreadxml.Model.News;
import com.landsoft.demoreadxml.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by TRANTUAN on 30-Nov-17.
 */

public class NewsAdapter extends BaseAdapter {
    private static final String TAG = "NewsAdapter";
    Context context;
    int layout;
    List<News> newsList;

    public NewsAdapter(MainActivity mainActivity, int list_view, List<News> newsList) {
        this.context = mainActivity;
        this.layout = list_view;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgLogo;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvLink;
        TextView tvUpdateDate;

        public ViewHolder(View view) {
            this.imgLogo = view.findViewById(R.id.img_logo);
            this.tvTitle = view.findViewById(R.id.tv_title);
            this.tvDescription = view.findViewById(R.id.tv_description);
            this.tvLink = view.findViewById(R.id.tv_link);
            this.tvUpdateDate = view.findViewById(R.id.tv_update_date);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        News news = newsList.get(i);
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvDescription.setText(news.getDescription());
        viewHolder.tvLink.setText(news.getLink());
//        Log.d(TAG   , "getView: "+news.getLink());
        viewHolder.tvUpdateDate.setText(news.getPubDate());
        Picasso.with(context)
                .load(news.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .resize(100, 100)
                .into(viewHolder.imgLogo);

//        try {
//            viewHolder.imgLogo.setImageBitmap( new DownloadHinhAnh().execute(news.getImage()).get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    public class DownloadHinhAnh extends AsyncTask<String,Void,Bitmap> {
        Bitmap hinhanhdownload;
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                hinhanhdownload = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return hinhanhdownload;
        }
    }
}
