package utils
import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.*
import org.hamcrest.Matchers.*
import java.io.IOException

/**
 * Wait for a condition to happen
 *
 * @param condition The condition to wait for
 * @param timeout Time to wait in milliseconds
 */
class WaitAction(private val condition: Matcher<View>, private val timeout: Long) : ViewAction {

    override fun getDescription(): String {
        return "wait"
    }

    override fun getConstraints(): Matcher<View> {
        return anything() as Matcher<View>
    }

    override fun perform(uiController: UiController, view: View?) {
        uiController.loopMainThreadUntilIdle();
        val startTime = System.currentTimeMillis();
        val endTime = startTime + timeout;

        while (System.currentTimeMillis() < endTime) {
            if (condition.matches(view)) {
                return;
            }

            uiController.loopMainThreadForAtLeast(1000)
        }

        // Timeout.
        println(PerformException.Builder().build().stackTrace)
    }
}

/**
 * Wait for a condition to happen
 *
 * @param condition The condition to wait for
 * @param timeout Time to wait in milliseconds
 */
fun waitFor(condition: Matcher<View>, timeout: Long): ViewAction {
    return WaitAction(condition, timeout)
}

@Throws(IOException::class)
fun writeToFile() {
    /*...*/
    throw IOException()
}
