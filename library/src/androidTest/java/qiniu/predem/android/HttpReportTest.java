package qiniu.predem.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import qiniu.predem.android.http.HttpMonitorManager;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HttpReportTest extends AndroidTestCase {
    private static final String TAG = "ExampleInstrumentedTest";
    private static final String appKey = "9a9c127726b746e5b5fa7fc816a17407";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("qiniu.predem.library", appContext.getPackageName());
    }

    @Test
    public void testManagerInit() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        HttpMonitorManager.getInstance().register(appContext);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HttpMonitorManager.getInstance().unregister();
    }

//    @After
//    public void destroy(){
//        System.out.println("------destroy");
//        HttpMonitorManager.getInstance().unregister();
//    }
}
