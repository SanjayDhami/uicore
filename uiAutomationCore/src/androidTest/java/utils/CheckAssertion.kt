package utils

import org.junit.Assert
import utils.TestBase.Companion.logger

open class CheckAssertion {

    private var errorList: MutableList<String>? = ArrayList()
    private var enabledAssertion: Boolean = false // Make sure assertion is done

    //Verify Expected and Actual Objects are Equals
    fun checkVerificationEqual(assertType: AssertionType, expectedValue: String, actualValue: String, message: String) {
        enabledAssertion =true
        when( assertType) {
            AssertionType.HARD -> {
                if (expectedValue == actualValue) {
                    logger?.info(String.format("%s - %s", message, "Hard Assertion Passed!"))
                } else {
                    val errorMessage = "(Hard Assert: FAIL) $message"
                    errorList?.add(errorMessage)
                    logger?.info(
                        String.format(
                            "%s - %s",
                            message,
                            "Hard Assertion Failed! Expecting: $expectedValue Actual: $actualValue"
                        )
                    )
                }
                Assert.assertEquals(expectedValue, actualValue)
            }

            AssertionType.SOFT->
                softAssertEqual(message,expectedValue,actualValue)
        }


    }

    //Verify Element Present
    fun checkVerificationTrue(assertType: AssertionType, compareInput: Boolean, message: String) {
        enabledAssertion =true
        when( assertType) {
            AssertionType.HARD -> {
                if (compareInput) {
                    logger?.info(String.format("%s - %s", message, "Hard Assertion Passed!"))
                } else {
                    val errorMessage = "(Hard Assert: FAIL) $message"
                    errorList?.add(errorMessage)
                    logger?.info(
                        String.format(
                            "%s - %s",
                            message,
                            "Hard Assertion Failed! Expecting:true Actual:$compareInput"
                        )
                    )
                }
                Assert.assertTrue(compareInput)
            }

            AssertionType.SOFT->
                softAssertTrue(message,compareInput)
        }
    }

    //Assert Condition
    private fun softAssertTrue(message: String, condition: Boolean) {
        enabledAssertion =true
        if (condition) {
            logger?.info(String.format("%s - %s", message, "Soft Assertion Passed!"))
        } else {
            val errorMessage = "(Soft Assert: FAIL) $message"
            logger?.info(String.format("%s - %s", message, "Soft Assertion Failed! Expecting:true Actual:$condition"))
            errorList?.add(errorMessage)
        }
    }

    //Assert Equal

    private fun softAssertEqual(message: String, expectedResult: String, actualResult: String){
        enabledAssertion =true
        if(expectedResult == actualResult){
            logger?.info(String.format("%s - %s", message, "Soft Assertion Passed!"))
        } else {
            val errorMessage = "(Soft Assert: FAIL) $message"
            logger?.info(String.format("%s - %s", message, "Soft Assertion Failed! Expecting: $expectedResult Actual:$actualResult"))
            errorList?.add(errorMessage)
        }
    }

    //called at the end of Test method
    fun assertAll() {
        if (enabledAssertion) {  // Execute only if assertion is done
            if (errorList?.isEmpty()!!) {
                // Test pass if there are no error message in errorMessageSet
                logger?.info(String.format("%s", "All Assertion Passed"))
                Assert.assertTrue(true)
            } else {
                val message = StringBuilder()
                for (msg in errorList!!) {
                    message.append(
                        """
                        $msg
                        
                        """.trimIndent()
                    )
                }
                logger?.info(String.format("%s", "Failing Assert are: \n$message"))
                Assert.fail("Failing Assert are: \n$message")
            }
        }
    }
}