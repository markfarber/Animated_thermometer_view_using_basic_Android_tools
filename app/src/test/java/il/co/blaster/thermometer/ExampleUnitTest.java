package il.co.blaster.thermometer;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void string_to_double(){
        Log.i(":::ExampleUnitTest", "@string_to_double -> " + Double.parseDouble("50"));

    }

}