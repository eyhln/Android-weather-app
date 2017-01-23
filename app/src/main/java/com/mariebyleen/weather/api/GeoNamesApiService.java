package com.mariebyleen.weather.api;

import com.mariebyleen.weather.location.search_locations.model.SearchLocations;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GeoNamesApiService {

    @GET("searchJSON?cities=cities1000&isNameRequired=true&username=mbyleen")
    Observable<SearchLocations> getSearchLocations(@Query("name_startsWith") String search,
                                                   @Query("maxRows") int maxRows);
}
