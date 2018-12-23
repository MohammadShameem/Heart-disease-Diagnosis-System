package io.left.core.util.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.left.core.assignment.data.helper.SharedPreferenceHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by shameem on 29/03/2018.
 */

public class SharedPreferenceTest {

    Context context;

    @Before
    public void before() {
        context = InstrumentationRegistry.getTargetContext();
        //sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Test
    public void put_and_get_preference() throws Exception {
        String string1 = "test";

        SharedPreferenceHelper.onInstance(context).setFirstName(string1);
        String string2 =SharedPreferenceHelper.onInstance(context).getFirstName();


  /*      sharedPreferences.edit().putString(KEY_PREF, string1).apply();
        String string2 = sharedPreferences.getString(KEY_PREF, "");*/

        // Verify that the received data is correct.
        assertEquals(string1, string2);
    }




}
