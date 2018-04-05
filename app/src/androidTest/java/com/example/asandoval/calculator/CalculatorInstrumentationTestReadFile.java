/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.asandoval.calculator;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnitRunner;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.asandoval.calculator.TestUtils.loadJSONFromAsset;

/**
 * Test parses Json file calculator-numbers.json to get inputs to add and the expected result.
 * Using Parameterize testing we can repeat same test many times for all the inputs from the json file.
 *
 * This is a very simple example to give an idea of Parameterized testing.
 * At work we used Parameterized testing to perform a test that needs to repeat many times depending on input.
 *
 * JUnit4 Ui Tests for {@link MainActivity} using the {@link AndroidJUnitRunner}.
 * This class uses the JUnit4 syntax for tests.
 *
 * TODO: Update README and notes
 *
 */
@RunWith(Parameterized.class)
@LargeTest
public class CalculatorInstrumentationTestReadFile {

    private static final String jsonFile = "calculator-numbers.json";
    private static String sNumberA;
    private static String sNumberB;
    private static String sResult;

    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * With Parameters we can execute the same test over and over again using different values
     * <p>
     * The static method returns a Collection where each entry in the
     * collection will be the input data for one iteration of the test.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        Collection<Object[]> params = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(jsonFile));
            JSONArray m_jArry = obj.getJSONArray("numbers");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                sNumberA = jo_inside.getString("number_a");
                sNumberB = jo_inside.getString("number_b");
                sResult = jo_inside.getString("result");

                params.add(new Object[]{
                        jo_inside.get("number_a"),
                        jo_inside.get("number_b"),
                        jo_inside.get("result")
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public CalculatorInstrumentationTestReadFile(String number_a, String number_b, String result) {
        sNumberA = number_a;
        sNumberB = number_b;
        sResult = result;
    }

    @Test
    public void Read_file_typeOperandsAndPerformAddOperation() {
        performOperation(R.id.operation_add_btn, sNumberA, sNumberB, sResult);
    }

    private void performOperation(int btnOperationResId, String operandOne,
                                  String operandTwo, String expectedResult) {
        // Type the two operands in the EditText fields
        onView(withId(R.id.operand_one_edit_text)).perform(typeText(operandOne),
                closeSoftKeyboard());
        onView(withId(R.id.operand_two_edit_text)).perform(typeText(operandTwo),
                closeSoftKeyboard());

        // Click on a given operation button
        onView(withId(btnOperationResId)).perform(click());

        // Check the expected test is displayed in the Ui
        onView(withId(R.id.operation_result_text_view)).check(matches(withText(expectedResult)));
    }

}
