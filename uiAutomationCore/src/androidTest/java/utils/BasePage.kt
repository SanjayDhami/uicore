package utils

import android.os.SystemClock
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import utils.TestBase.Companion.logger


open class BasePage {
    companion object {
        const val Max_TimeOut: Long = 60000
        const val Min_TimeOut: Long = 3000

        inline fun <reified T : BasePage> on(): T {
            return BasePage().on()
        }

    }
    inline fun <reified T : BasePage> on(): T {
        val page = T::class.constructors.first().call()
        page.waitPageForLoad()
        return page
    }

    //Get text from view
    fun getText(matcher: ViewInteraction): String {
        var text = String()
        try {
            matcher.perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(TextView::class.java)
                }

                override fun getDescription(): String {
                    return "Text of the view"
                }

                override fun perform(uiController: UiController, view: View) {
                    val tv = view as TextView
                    text = tv.text.toString()
                }
            })
        } catch (e: Throwable) {
            logger?.info("No Matching View Found")
        }
        return text
    }

    // For Recycler View Only
    fun swipeUpForRecyclerView(dealerName: String, count: Int, matcher: Matcher<View?>) {

        try {
            logger?.info("Swipe up to $dealerName")
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            onView(matcher).perform(
                repeatedlyUntil(
                    this.swipeUp(),
                    hasDescendant(withText(dealerName)),
                    count
                )
            )
        } catch (e: NoMatchingViewException) {
            logger?.info("No Matching View Found")
            Assert.fail(e.message)
        }
    }


    fun swipeUp(): ViewAction? {
        return actionWithAssertions(
            GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.BOTTOM_CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER
            )
        )
    }
    fun swipeDown(): ViewAction? {
        return actionWithAssertions(
            GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.TOP_CENTER,
                GeneralLocation.BOTTOM_CENTER, Press.FINGER
            )
        )
    }

    fun swipeLeft(): ViewAction? {
        return actionWithAssertions(
            GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER
            )
        )
    }

    fun swipeRight(): ViewAction? {
        return actionWithAssertions(
            GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER
            )
        )
    }

    open fun waitPageForLoad() :BasePage{
        return this
    }

    //Press Back
    fun pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack())
    }

    //Explicit time out in milli second
    fun explicitWait(time: Long) {
        SystemClock.sleep(time)
    }


    //Verify if Element is Displayed
    fun ViewInteraction.isDisplayed(): Boolean {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        var found  = false
        try {
            explicitWait(Min_TimeOut)
            check(matches(isCompletelyDisplayed()))
            found = true
        } catch (e: NoMatchingViewException){
        } finally {
            return found
        }

    }

    //waitForElementNotVisible
    fun ViewInteraction.notDisplayed(): Boolean {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        var found  = false
        try {
            explicitWait(Min_TimeOut)
            check(matches(not(isCompletelyDisplayed())))
            found = true
        } catch (e: NoMatchingViewException){
        } finally {
            return found
        }
    }

    fun waitForElementNotExist(matcher: Matcher<View?>){
        val startTime = System.currentTimeMillis();
        val endTime = startTime + Max_TimeOut;

        while (System.currentTimeMillis() < endTime) {
            if (onView(matcher).notDisplayed()) {
                return;
            }
            System.currentTimeMillis()+ Min_TimeOut
        }
    }

    //Return View Matcher for given Index
    private fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            var currentIndex = 0
            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View?): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }

    //click Action
    fun clickEvent(matcher: Matcher<View?>, elementName: String) {
        try {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            onView(matcher).perform(click())
            logger?.info("Clicked- $elementName")
        } catch (e: NoMatchingViewException) {
            logger?.info("$elementName  Not Found!!!!! $e")
            Assert.fail(e.message)
        }
    }

    //Enter text that does not show in Logger
    private fun enterHiddenText(matcher: Matcher<View?>, value: String) {
        try {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            if (onView(matcher).isDisplayed()) {
                onView(matcher).perform(clearText(), typeText(value))
            }
        } catch (e: NoMatchingViewException) {
            logger?.info(" Not Found or View not Matched")
            Assert.fail(e.message)
        }
    }

    //Enter Text
    fun enterText(matcher: Matcher<View?>, value: String, elementName: String) {
        try {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            enterHiddenText(matcher, value)
            logger?.info("Enter: $value in $elementName")
        } catch (e: NoMatchingViewException) {
            logger?.info("$elementName Not Found or View not Matched")
            Assert.fail(e.message)
        }
    }

    //Scroll till element found

    fun scrollToView(
        swipeType: SwipeType,
        pageScroller: Matcher<View?>,
        targetElement: Matcher<View?>
    ) {
        when(swipeType) {
            SwipeType.UP ->
                swipe(swipeUp(), pageScroller, targetElement)
            SwipeType.DOWN ->
                swipe(swipeDown(), pageScroller, targetElement)
            SwipeType.RIGHT ->
                swipe(swipeRight(), pageScroller, targetElement)
            SwipeType.LEFT ->
                swipe(swipeLeft(), pageScroller, targetElement)
        }
    }

    // swipe
    private fun swipe(
        viewAction: ViewAction?,
        matcher: Matcher<View?>,
        targetElement: Matcher<View?>
    ){
        try {
            for (i in 1..10) {
                InstrumentationRegistry.getInstrumentation().waitForIdleSync()
                onView(matcher).perform(viewAction)
                if (onView(targetElement).isDisplayed()) {
                    break
                }
            }
        } catch (e: NoMatchingViewException) {
            logger?.info("View not Matched")
            Assert.fail(e.message)
        }
    }

    //get the text from elements having same resource ID to create list of String
    fun getTextList(matcher: Matcher<View?>): ArrayList<String> {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val objects = ArrayList<String>()
        var next = true
        var i  = 0
        while (next) {
            val obj = getText(onView(withIndex(matcher, i)))
            if (obj.isNotEmpty()){
                objects.add(obj)
                i++
            }
            else {
                next = false
            }
        }
        return objects
    }
}
