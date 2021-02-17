package utils

import android.os.Environment.DIRECTORY_PICTURES
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import java.io.File

class IDTScreenCaptureProcessor: BasicScreenCaptureProcessor() {
    init {
        mTag = "IDTScreenCaptureProcessor"
        mFileNameDelimiter = "-"
        mDefaultFilenamePrefix = "Giorgos"
        mDefaultScreenshotPath = getNewFilename()
    }

    private fun getNewFilename(): File? {
        val context = getInstrumentation().getTargetContext().getApplicationContext()
        return context.getExternalFilesDir(DIRECTORY_PICTURES)
    }
}
