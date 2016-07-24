package com.mohamedisoukrane.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Movie Grid Fragment.
 */
public class MovieGridFragment extends Fragment {

    private MovieAdapter mMovieAdapter;
    private UrlResource urlResource;

    public MovieGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        String sort_by = getPreferredSorting();
        new FetchMovies().execute(sort_by);
    }

    private String getPreferredSorting() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_default));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List movies = new ArrayList<Movie>();
        this.urlResource = new UrlResource(
                getString(R.string.movie_db_url),
                getString(R.string.api_key),
                getString(R.string.image_db_url)
        );
        this.mMovieAdapter = new MovieAdapter(getActivity(), movies);
        this.mMovieAdapter.setUrlResource(urlResource);
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("MOVIE", movie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FetchMovies extends AsyncTask<String, Void, List<Movie>>
    {
        final String LOG_TAG = FetchMovies.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {

            // Check that either top_rated or popular is provided.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //
            String moviesJson = null;


            try {
                URL url = new URL(urlResource.getListMoviesUrl(params[0]));

                // Create the request to movie database api
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJson = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesFromJson(moviesJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private List<Movie> getMoviesFromJson(String json) throws JSONException{
            JSONObject data = new JSONObject(json);
            JSONArray moviesData = data.getJSONArray("results");
            List movies = new ArrayList();
            for(int i = 0; i < moviesData.length(); i++) {
                JSONObject movieData = moviesData.getJSONObject(i);
                int id = movieData.getInt("id");
                String title = movieData.getString("original_title");
                String overview = movieData.getString("overview");
                double userRating = movieData.getDouble("vote_average");
                String releaseDate = movieData.getString("release_date");
                String imageUrl = movieData.getString("poster_path");
                movies.add(new Movie(id, title, imageUrl, overview, userRating, releaseDate));
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mMovieAdapter.clear();
                for(Movie movie : result) {
                    mMovieAdapter.add(movie);
                }
            }
            mMovieAdapter.notifyDataSetChanged();
        }
    }
}
