package utils

import org.apache.log4j.*


class LogFactory {
    fun getLogger(): Logger? {
        val logName: String = "Android"
        var result: Logger? = null
        result = Logger.getLogger(logName)
        initializeLogger(result)

        return result
    }

    private fun initializeLogger(logger: Logger) {
        //  val logName = logger.name

        //  Create root appender
        val pattern = "%d [%p|%c|%C{1}]- %m%n"
        val consoleAppender = ConsoleAppender()
        consoleAppender.threshold = Level.DEBUG
        consoleAppender.target = "println"
        consoleAppender.layout = PatternLayout(pattern)
        consoleAppender.activateOptions()
        logger.addAppender(consoleAppender)

        // Create file appender
        val fa = FileAppender()
        fa.name = "Android"

        fa.file = "/sdcard/Android/data/com.coxautoinc.recon.debug/files/log.file"
        fa.layout = PatternLayout(pattern)
        fa.threshold = Level.DEBUG
        fa.append = false
        fa.activateOptions()
        logger.addAppender(fa)
        logger.additivity = false

    }
}