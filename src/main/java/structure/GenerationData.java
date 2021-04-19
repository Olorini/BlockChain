package structure;

import chat.CryptoMessage;

import java.util.List;

public class GenerationData {

	private final long id;
	private final String hash;
	private final int zeroCount;
	private final List<CryptoMessage> blockData;

	public GenerationData(long id, String hash, int zeroCount, List<CryptoMessage> blockData) {
		this.id = id;
		this.hash = hash;
		this.zeroCount = zeroCount;
		this.blockData = blockData;
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

	public List<CryptoMessage> getBlockData() {
		return blockData;
	}
}
