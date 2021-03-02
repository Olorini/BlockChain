package structure;

public class LastBlockData {

	private final long id;
	private final String hash;
	private final int zeroCount;

	public LastBlockData(long id, String hash, int zeroCount) {
		this.id = id;
		this.hash = hash;
		this.zeroCount = zeroCount;
	}

	public String getHash() {
		return hash;
	}

	public long getId() {
		return id;
	}

	public int getZeroCount() {
		return zeroCount;
	}
}
