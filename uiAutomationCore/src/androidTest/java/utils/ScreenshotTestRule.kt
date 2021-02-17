package utils

import android.graphics.Bitmap.CompressFormat
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException


class ScreenshotTestRule : TestWatcher() {
    override fun failed(e: Throwable?, description: Description) {
        super.failed(e, description)
        takeScreenshot(description)
    }

    private fun takeScreenshot(description: Description) {
        val filename: String = description.getTestClass().getSimpleName()
            .toString() + "-" + description.getMethodName()
        val capture = Screenshot.capture()
        capture.name = filename
        capture.format = CompressFormat.PNG
        val processors: HashSet<ScreenCaptureProcessor> = HashSet()
        processors.add(IDTScreenCaptureProcessor())
        try {
            capture.process(processors)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}