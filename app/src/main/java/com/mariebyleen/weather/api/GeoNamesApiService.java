package com.mariebyleen.weather.api;

import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GeoNamesApiService {

    @GET("searchJSON?cities=cities1000&isNameRequired=true&orderby=relevance&maxRows=10&username=mbyleen")
    Observable<SearchLocations> getSearchLocations(@Query("name_startsWith") String search);
}
