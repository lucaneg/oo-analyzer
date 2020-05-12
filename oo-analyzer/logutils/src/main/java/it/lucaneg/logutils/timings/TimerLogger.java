package it.lucaneg.logutils.timings;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class TimerLogger {

	private final String message;
	private final TimeFormat formatter;
	private final Logger logger;
	
	public TimerLogger(Logger logger, String message, TimeFormat formatter) {
		this.logger = logger;
		this.message = message;
		this.formatter = formatter;
	}
	
	public void execAction(LoggableAction action) {
		execAction(Level.INFO, action);
	}
	
	public void execAction(Level logLevel, LoggableAction action) {
		execAux(logLevel, action);
	}
	
	public <T> T execSupplier(LoggableSupplier<T> action) {
		return execSupplier(Level.INFO, action);
	}
	
	public <T> T execSupplier(Level logLevel, LoggableSupplier<T> action) {
		Wrapper<T> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run());
		return w.ret;
	}
	
	public <R, T0> R execFunction(LoggableFunction<R, T0> action, T0 arg0) {
		return execFunction(Level.INFO, action, arg0);
	}
	
	public <R, T0> R execFunction(Level logLevel, LoggableFunction<R, T0> action, T0 arg0) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(arg0));
		return w.ret;
	}
	
	public <R, T0, T1> R execFunction(LoggableBiFunction<R, T0, T1> action, T0 arg0, T1 arg1) {
		return execFunction(Level.INFO, action, arg0, arg1);
	}
	
	public <R, T0, T1> R execFunction(Level logLevel, LoggableBiFunction<R, T0, T1> action, T0 arg0, T1 arg1) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(arg0, arg1));
		return w.ret;
	}
	
	public <R, T0, T1, T2> R execFunction(LoggableTriFunction<R, T0, T1, T2> action, T0 arg0, T1 arg1, T2 arg2) {
		return execFunction(Level.INFO, action, arg0, arg1, arg2);
	}
	
	public <R, T0, T1, T2> R execFunction(Level logLevel, LoggableTriFunction<R, T0, T1, T2> action, T0 arg0, T1 arg1, T2 arg2) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(arg0, arg1, arg2));
		return w.ret;
	}
	
	public <R, T0, T1, T2, T3> R execFunction(LoggableQuadFunction<R, T0, T1, T2, T3> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3) {
		return execFunction(Level.INFO, action, arg0, arg1, arg2, arg3);
	}
	
	public <R, T0, T1, T2, T3> R execFunction(Level logLevel, LoggableQuadFunction<R, T0, T1, T2, T3> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(arg0, arg1, arg2, arg3));
		return w.ret;
	}
	
	public <R, T0, T1, T2, T3, T4> R execFunction(LoggablePentaFunction<R, T0, T1, T2, T3, T4> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		return execFunction(Level.INFO, action, arg0, arg1, arg2, arg3, arg4);
	}
	
	public <R, T0, T1, T2, T3, T4> R execFunction(Level logLevel, LoggablePentaFunction<R, T0, T1, T2, T3, T4> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(arg0, arg1, arg2, arg3, arg4));
		return w.ret;
	}
	
	public <R> R execFunction(LoggableMultiFunction<R> action, Object... args) {
		return execFunction(Level.INFO, action, args);
	}
	
	public <R> R execFunction(Level logLevel, LoggableMultiFunction<R> action, Object... args) {
		Wrapper<R> w = new Wrapper<>();
		execAux(logLevel, () -> w.ret = action.run(args));
		return w.ret;
	}
	
	public <T0> void execConsumer(LoggableConsumer<T0> action, T0 arg0) {
		execConsumer(Level.INFO, action, arg0);
	}
	
	public <T0> void execConsumer(Level logLevel, LoggableConsumer<T0> action, T0 arg0) {
		execAux(logLevel, () -> action.run(arg0));
	}
	
	public <T0, T1> void execConsumer(LoggableBiConsumer<T0, T1> action, T0 arg0, T1 arg1) {
		execConsumer(Level.INFO, action, arg0, arg1);
	}
	
	public <T0, T1> void execConsumer(Level logLevel, LoggableBiConsumer<T0, T1> action, T0 arg0, T1 arg1) {
		execAux(logLevel, () -> action.run(arg0, arg1));
	}
	
	public <T0, T1, T2> void execConsumer(LoggableTriConsumer<T0, T1, T2> action, T0 arg0, T1 arg1, T2 arg2) {
		execConsumer(Level.INFO, action, arg0, arg1, arg2);
	}
	
	public <T0, T1, T2> void execConsumer(Level logLevel, LoggableTriConsumer<T0, T1, T2> action, T0 arg0, T1 arg1, T2 arg2) {
		execAux(logLevel, () -> action.run(arg0, arg1, arg2));
	}
	
	public <T0, T1, T2, T3> void execConsumer(LoggableQuadConsumer<T0, T1, T2, T3> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3) {
		execConsumer(Level.INFO, action, arg0, arg1, arg2, arg3);
	}
	
	public <T0, T1, T2, T3> void execConsumer(Level logLevel, LoggableQuadConsumer<T0, T1, T2, T3> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3) {
		execAux(logLevel, () -> action.run(arg0, arg1, arg2, arg3));
	}
	
	public <T0, T1, T2, T3, T4> void execConsumer(LoggablePentaConsumer<T0, T1, T2, T3, T4> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		execConsumer(Level.INFO, action, arg0, arg1, arg2, arg3, arg4);
	}
	
	public <T0, T1, T2, T3, T4> void execConsumer(Level logLevel, LoggablePentaConsumer<T0, T1, T2, T3, T4> action, T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		execAux(logLevel, () -> action.run(arg0, arg1, arg2, arg3, arg4));
	}
	
	public void execConsumer(LoggableMultiConsumer action, Object... args) {
		execConsumer(Level.INFO, action, args);
	}
	
	public void execConsumer(Level logLevel, LoggableMultiConsumer action, Object... args) {
		execAux(logLevel, () -> action.run(args));
	}
	
	private void execAux(Level logLevel, LoggableAction action) {
		long startTime = System.nanoTime();
		logger.log(logLevel, message + " [start]");
		action.run();
		logger.log(logLevel, message + " [stop] [completed in " + formatter.format(System.nanoTime() - startTime) + "]");
	}
	
	private class Wrapper<T> {
		private T ret;
	}
	
	public interface LoggableAction {
		void run();
	}
	
	public interface LoggableSupplier<R> {
		R run();
	}
	
	public interface LoggableFunction<R, T0> {
		R run(T0 arg0);
	}
	
	public interface LoggableBiFunction<R, T0, T1> {
		R run(T0 arg0, T1 arg1);
	}
	
	public interface LoggableTriFunction<R, T0, T1, T2> {
		R run(T0 arg0, T1 arg1, T2 arg2);
	}
	
	public interface LoggableQuadFunction<R, T0, T1, T2, T3> {
		R run(T0 arg0, T1 arg1, T2 arg2, T3 arg3);
	}
	
	public interface LoggablePentaFunction<R, T0, T1, T2, T3, T4> {
		R run(T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4);
	}
	
	public interface LoggableMultiFunction<R> {
		R run(Object... args);
	}
	
	public interface LoggableConsumer<T0> {
		void run(T0 arg0);
	}
	
	public interface LoggableBiConsumer<T0, T1> {
		void run(T0 arg0, T1 arg1);
	}
	
	public interface LoggableTriConsumer<T0, T1, T2> {
		void run(T0 arg0, T1 arg1, T2 arg2);
	}
	
	public interface LoggableQuadConsumer<T0, T1, T2, T3> {
		void run(T0 arg0, T1 arg1, T2 arg2, T3 arg3);
	}
	
	public interface LoggablePentaConsumer<T0, T1, T2, T3, T4> {
		void run(T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4);
	}
	
	public interface LoggableMultiConsumer {
		void run(Object... args);
	}
}