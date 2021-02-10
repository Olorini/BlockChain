package structure;

import utils.CryptoUtils;

import java.util.Date;

public class Block {

	private final long id;
	private final long time;
	private final String hash;
	private final String previousBlockHash;

	Block (long id, String previousBlockHash) {
		this.id = id;
		this.previousBlockHash = previousBlockHash;
		this.time = new Date().getTime();
		this.hash = CryptoUtils.applySha256(id + time + previousBlockHash);
	}

	String getHash() {
		return hash;
	}

	long getId() {
		return id;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	@Override
	public String toString() {
		return "Block:\n" +
		"Id: " + id + "\n" +
		"Timestamp:" + time + "\n" +
		"Hash of the previous block:\n" + previousBlockHash + "\n" +
		"Hash of the block:\n" + hash;
	}
}
