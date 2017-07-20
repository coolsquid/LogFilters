package coolsquid.logfilters;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public abstract class LogFilter implements Filter {

	protected final String filter;
	private Level maxLevel = Level.ERROR;
	private State state;

	public LogFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void start() {
		this.state = State.STARTED;
	}

	@Override
	public void stop() {
		this.state = State.STOPPED;
	}

	@Override
	public boolean isStarted() {
		return this.state == State.STARTED;
	}

	@Override
	public boolean isStopped() {
		return this.state == State.STOPPED;
	}

	@Override
	public Result getOnMismatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result getOnMatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4, Object p5) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4, Object p5, Object p6) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4, Object p5, Object p6, Object p7) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2,
			Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(LogEvent event) {
		if (event.getMessage() != null && event.getLevel().isLessSpecificThan(this.maxLevel)) {
			return this.remove(event) ? Result.DENY : Result.NEUTRAL;
		}
		return Result.NEUTRAL;
	}

	public Level getMaxLevel() {
		return this.maxLevel;
	}

	public void setMaxLevel(Level maxLevel) {
		this.maxLevel = maxLevel;
	}

	protected abstract boolean remove(LogEvent event);

	public static class PrefixFilter extends LogFilter {

		public PrefixFilter(String filter) {
			super(filter);
		}

		@Override
		public boolean remove(LogEvent event) {
			return event.getMessage().getFormattedMessage().startsWith(this.filter);
		}
	}

	public static class SuffixFilter extends LogFilter {

		public SuffixFilter(String filter) {
			super(filter);
		}

		@Override
		public boolean remove(LogEvent event) {
			return event.getMessage().getFormattedMessage().endsWith(this.filter);
		}
	}

	public static class RegexFilter extends LogFilter {

		public RegexFilter(String filter) {
			super(filter);
		}

		@Override
		public boolean remove(LogEvent event) {
			return event.getMessage().getFormattedMessage().matches(this.filter);
		}
	}

	public static class EqualsFilter extends LogFilter {

		public EqualsFilter(String filter) {
			super(filter);
		}

		@Override
		public boolean remove(LogEvent event) {
			return event.getMessage().getFormattedMessage().equals(this.filter);
		}
	}

	public static class ContainsFilter extends LogFilter {

		public ContainsFilter(String filter) {
			super(filter);
		}

		@Override
		public boolean remove(LogEvent event) {
			return event.getMessage().getFormattedMessage().contains(this.filter);
		}
	}
}