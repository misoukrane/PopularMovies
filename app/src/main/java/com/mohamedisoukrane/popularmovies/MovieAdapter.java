package com.mohamedisoukrane.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mohamed on 7/11/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private UrlResource urlResource;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }
    public void setUrlResource(UrlResource urlResource) {
        this.urlResource = urlResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        final  ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.list_item_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String imageUrl = this.urlResource.getImageUrl(movie.posterPath, "w185");
        Picasso
                .with(getContext())
                .load(imageUrl)
                .into(holder.imageView);
        return view;
    }
    static class ViewHolder {
        ImageView imageView;
    }
}
