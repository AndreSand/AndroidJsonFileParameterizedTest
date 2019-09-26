package com.example.asandoval.calculator;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import static androidx.test.InstrumentationRegistry.getContext;

public class TestUtils {
    /**
     * Helper class to parse JSON file
     */
    public static String loadJSONFromAsset(String jsonFile) {
        String json = null;
        try {
            AssetManager assetManager = getContext().getAssets();
            InputStream is = assetManager.open(jsonFile);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
