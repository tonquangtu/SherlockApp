package com.startup.threecat.sherlock.network;

import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.ListInfoLocation;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Dell on 21-Jul-16.
 */
public interface ServiceAPI {

    @GET(ConstantData.LINK_NEAR_PLACE)
    Call<ListInfoLocation> loadListInfoLocation(@QueryMap Map<String, String> tag);
}
