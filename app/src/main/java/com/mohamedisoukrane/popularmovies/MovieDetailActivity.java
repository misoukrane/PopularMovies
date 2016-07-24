package com.mohamedisoukrane.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private UrlResource urlResource;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            this.urlResource = new UrlResource(
                    getString(R.string.movie_db_url),
                    getString(R.string.api_key),
                    getString(R.string.image_db_url)
            );
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("MOVIE")) {
                Movie movie = intent.getExtras().getParcelable("MOVIE");
                TextView title = (TextView) rootView.findViewById(R.id.movie_title);
                TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
                TextView userRating = (TextView) rootView.findViewById(R.id.user_rating);
                TextView overview = (TextView) rootView.findViewById(R.id.overview);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_image);

                // Populate data.
                title.setText(movie.title);
                releaseDate.setText(movie.releaseDate);
                userRating.setText(String.valueOf(movie.userRating));
                overview.setText(movie.overview);
                String imageUrl = this.urlResource.getImageUrl(movie.posterPath, "w185");
                Picasso
                        .with(getContext())
                        .load(imageUrl)
                        .into(imageView);
            }
            return rootView;
        }
    }
}
