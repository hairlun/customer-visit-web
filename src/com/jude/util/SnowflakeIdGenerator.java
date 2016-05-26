package com.jude.util;

public class SnowflakeIdGenerator {
	private final long workerId;
	private static final long twepoch = 1361753741828L;
	private long sequence = 0L;
	private static final long workerIdBits = 4L;
	public static final long maxWorkerId = 15L;
	private static final long sequenceBits = 10L;
	private static final long workerIdShift = 10L;
	private static final long timestampLeftShift = 14L;
	public static final long sequenceMask = 1023L;
	private long lastTimestamp = -1L;

	public long nextId() {
		long timestamp = timeGen();

		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1L & 0x3FF);
			if (this.sequence == 0L)
				timestamp = tilNextMillis(this.lastTimestamp);
		} else {
			this.sequence = 0L;
		}
		if (timestamp < this.lastTimestamp)
			;
		long nextId;
		try {
			throw new Exception(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds",
					new Object[] { Long.valueOf(this.lastTimestamp - timestamp) }));
		} catch (Exception e) {
			e.printStackTrace();

			this.lastTimestamp = timestamp;
			nextId = timestamp - 1361753741828L << 14 | this.workerId << 10 | this.sequence;
		}

		return nextId;
	}

	public SnowflakeIdGenerator(long workerId) {
		if ((workerId > 15L) || (workerId < 0L)) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					new Object[] { Long.valueOf(15L) }));
		}

		this.workerId = workerId;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();

		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}

		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}