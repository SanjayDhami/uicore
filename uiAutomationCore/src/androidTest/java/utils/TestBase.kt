package utils
import androidx.test.rule.GrantPermissionRule
import org.apache.log4j.Logger
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description


open class TestBase : CheckAssertion() {


    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val screenshotTestRule = ScreenshotTestRule()

    @get:Rule
    var watcher: TestRule = object : TestWatcher() {
        override fun starting(description: Description) {
            packageName = description.className
            testName = description.methodName
        }
    }

    @Before
    fun setUp() {
        logSuiteAndTestName()
       //LoginLogout().login(generalUserName, generalUserPassword)
    }

    @After
    fun onFinish() {
        //AssertAll only if test login assertion started
       // if (true) {
            assertAll()
       // }
        logger?.info("*****${testMethodName?.get(testMethodName?.size!! - 1)} Finished*****\n")
    }


    //Get TestSuite Name and Test Method
    private fun logSuiteAndTestName() {
        //Get the name of Test suite and Test method
        className = packageName?.split(".")
        testMethodName = testName?.split(".")
        if (className?.size != null && startTestSuite) {
            logger?.info("!!!!TestSuite- ${className?.get(className?.size!! - 1)} Started!!!!\n")
            startTestSuite = false
        }
        if (testMethodName?.size != null) {
            logger?.info("*****Test- ${testMethodName?.get(testMethodName?.size!! - 1)} Started*****")
        }
        val testClass = Test()
        testClass.testFunction()


    }

    companion object {
        var packageName: String? = null
        var testName: String? = null
        private var className: List<String>? = null
        private var testMethodName: List<String>? = null
        private var startTestSuite: Boolean = true
        var logger: Logger? = LogFactory().getLogger()

        @AfterClass
        @JvmStatic
        fun signOut() {
            if (className?.size != null) {
                logger?.info("!!!!TestSuite- ${className?.get(className?.size!! - 1)} Finished!!!!\n")
            }
        }
    }
}