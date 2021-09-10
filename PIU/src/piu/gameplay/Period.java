package piu.gameplay;

import java.util.Arrays;

/**
 * A Period holds two integers, a start and an end, representing the start and
 * end times of that period of time (in milliseconds since the start of the song)
 * @author AAA
 */
public class Period {
	// if start + 1 == end then it is not a hold, just a single arrow
	private int _start;
	private int _end;
	// keeps track of whether the arrow has been hit or not
	private boolean[] _hit;
	public Period(int start, int end) {
		_start = start;
		_end = end;
		//(end - start)/Column.MAX_HOLD_ARROW_DENSITY is the index of the nearest available spot in the period
		_hit = new boolean[(end-start)/Column.MAX_HOLD_ARROW_DENSITY + 1];
		Arrays.fill(_hit, false);
	}
	/**
	 * @return the start time for this period
	 */
	public int start() {
		return _start;
	}
	/**
	 * @return the end time for this period
	 */
	public int end() {
		return _end;
	}
	/**
	 * @return the duration of this period
	 */
	public int duration() {
		return _end - _start;
	}
	/**
	 * @return whether or not this is a hold 
	 */
	public boolean hold() {
		return _end != _start + 1;
	}
	/**
	 * @param time the time to check
	 * @return whether this period has been hit
	 */
	public boolean isHit(int time) {
		int index = (time - _start)/Column.MAX_HOLD_ARROW_DENSITY;
		if (index >= _hit.length) {
			return false;
		}
		return _hit[index];
	}
	/**
	 * 
	 */
	@Override
	public String toString() {
		return _start + " " + _end;
	}
	/**
	 * @param time the current time after the start of the song, in ms
	 * @return whether or not the part of this period was correctly hit or not
	 */
	public boolean hit(int time) {
		int index = (time - _start)/Column.MAX_HOLD_ARROW_DENSITY;
		if (index < _hit.length && index >= 0 && (time > _start-Column.DISCREPANCY || time < _end+Column.DISCREPANCY) && _hit[index] == false) {
			_hit[index] = true;
			return true;
		}
		return false;
	}
}
