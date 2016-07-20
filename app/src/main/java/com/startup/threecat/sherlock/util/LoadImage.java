package com.startup.threecat.sherlock.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.startup.threecat.sherlock.R;
import java.io.File;

/**
 * Created by Dell on 15-Jul-16.
 */
public class LoadImage {

    public static void loadImagePerson(Context context, ImageView imageView, String url) {

        if(url == null || url.equals("")) {
            Picasso.with(context)
                    .load(R.drawable.default_person)
                    .into(imageView);
        }else {
            Picasso.with(context)
                    .load(new File(url))
                    .placeholder(R.drawable.blue3)
                    .resize(400, 400)
                    .error(R.drawable.default_person)
                    .centerCrop()
                    .into(imageView);
        }
    }

}
