package it.lucaneg.logutils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.lucaneg.logutils.iterations.IterationLogger;
import it.lucaneg.logutils.timings.TimeFormat;
import it.lucaneg.logutils.timings.TimerLogger;

public class EnrichedLogger {
	protected final Logger logger;

	public EnrichedLogger(Class<?> loggingClass) {
		logger = LogManager.getLogger(loggingClass);
	}

	public TimerLogger mkTimerLogger(String message) {
		return mkTimerLogger(message, TimeFormat.UP_TO_SECONDS);
	}

	public TimerLogger mkTimerLogger(String message, TimeFormat formatter) {
		return new TimerLogger(logger, message, formatter);
	}

	public IterationLogger mkIterationLogger(String message, String objects) {
		return new IterationLogger(logger, message, objects);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.CharSequence)
	 */
	public void debug(CharSequence message) {
		logger.debug(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void debug(CharSequence message, Throwable t) {
		logger.debug(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.Object)
	 */
	public void debug(Object message) {
		logger.debug(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void debug(Object message, Throwable t) {
		logger.debug(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.String)
	 */
	public void debug(String message) {
		logger.debug(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#debug(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void debug(String message, Throwable t) {
		logger.debug(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.CharSequence)
	 */
	public void error(CharSequence message) {
		logger.error(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void error(CharSequence message, Throwable t) {
		logger.error(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.Object)
	 */
	public void error(Object message) {
		logger.error(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void error(Object message, Throwable t) {
		logger.error(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.String)
	 */
	public void error(String message) {
		logger.error(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#error(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void error(String message, Throwable t) {
		logger.error(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.CharSequence)
	 */
	public void fatal(CharSequence message) {
		logger.fatal(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void fatal(CharSequence message, Throwable t) {
		logger.fatal(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.Object)
	 */
	public void fatal(Object message) {
		logger.fatal(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void fatal(Object message, Throwable t) {
		logger.fatal(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.String)
	 */
	public void fatal(String message) {
		logger.fatal(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#fatal(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void fatal(String message, Throwable t) {
		logger.fatal(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.CharSequence)
	 */
	public void info(CharSequence message) {
		logger.info(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void info(CharSequence message, Throwable t) {
		logger.info(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.Object)
	 */
	public void info(Object message) {
		logger.info(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void info(Object message, Throwable t) {
		logger.info(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.String)
	 */
	public void info(String message) {
		logger.info(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#info(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void info(String message, Throwable t) {
		logger.info(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isEnabled(org.apache.logging.log4j.Level)
	 */
	public boolean isEnabled(Level level) {
		return logger.isEnabled(level);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isFatalEnabled()
	 */
	public boolean isFatalEnabled() {
		return logger.isFatalEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.CharSequence)
	 */
	public void log(Level level, CharSequence message) {
		logger.log(level, message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.CharSequence, java.lang.Throwable)
	 */
	public void log(Level level, CharSequence message, Throwable t) {
		logger.log(level, message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.Object)
	 */
	public void log(Level level, Object message) {
		logger.log(level, message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.Object, java.lang.Throwable)
	 */
	public void log(Level level, Object message, Throwable t) {
		logger.log(level, message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.String)
	 */
	public void log(Level level, String message) {
		logger.log(level, message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#log(org.apache.logging.log4j.Level,
	 *      java.lang.String, java.lang.Throwable)
	 */
	public void log(Level level, String message, Throwable t) {
		logger.log(level, message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.CharSequence)
	 */
	public void trace(CharSequence message) {
		logger.trace(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void trace(CharSequence message, Throwable t) {
		logger.trace(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.Object)
	 */
	public void trace(Object message) {
		logger.trace(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void trace(Object message, Throwable t) {
		logger.trace(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.String)
	 */
	public void trace(String message) {
		logger.trace(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#trace(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void trace(String message, Throwable t) {
		logger.trace(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.CharSequence)
	 */
	public void warn(CharSequence message) {
		logger.warn(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.CharSequence,
	 *      java.lang.Throwable)
	 */
	public void warn(CharSequence message, Throwable t) {
		logger.warn(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.Object)
	 */
	public void warn(Object message) {
		logger.warn(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void warn(Object message, Throwable t) {
		logger.warn(message, t);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.String)
	 */
	public void warn(String message) {
		logger.warn(message);
	}

	/**
	 * Forwards the call to the underlying logger
	 * 
	 * @see org.apache.logging.log4j.Logger#warn(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void warn(String message, Throwable t) {
		logger.warn(message, t);
	}
}
