package com.mohamedisoukrane.popularmovies;

/**
 * Created by mohamed on 7/17/16.
 */
public class UrlResource {
    private String base;
    private String apiKey;
    private String imageBase;

    public UrlResource(String base, String apiKey, String imageBase) {
        this.base = base;
        this.apiKey = apiKey;
        this.imageBase = imageBase;
    }

    public String getListMoviesUrl(String sortBy) {
        return addKeyQuery(this.base + sortBy);
    }

    public String getImageUrl(String posterPath, String size) {
        return addKeyQuery(imageBase + "w185" + posterPath);
    }

    private String addKeyQuery(String url) {
        return url + "?api_key="+apiKey;
    }
}
