package org.openskope.timeseries.model;

public class IndexRange {
	public final int startIndex;
	public final int endIndex;
	public IndexRange(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
}