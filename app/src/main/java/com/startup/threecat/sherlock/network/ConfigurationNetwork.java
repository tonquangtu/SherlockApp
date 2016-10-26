package com.startup.threecat.sherlock.network;

import com.startup.threecat.sherlock.model.ConstantData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 21-Jul-16.
 */
public class ConfigurationNetwork {

    private static ServiceAPI serviceAPI = null;

    private ConfigurationNetwork() {

    }

    public static ServiceAPI getServiceAPI() {

        if(serviceAPI == null) {

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(ConstantData.BASE_LINK_GOOGLE_SERVICE_PLACE);
            builder.addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            serviceAPI = retrofit.create(ServiceAPI.class);
        }
        return serviceAPI;
    }
}
