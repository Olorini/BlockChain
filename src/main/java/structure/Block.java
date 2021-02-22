package structure;

import utils.CryptoUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Block implements Serializable {

	private static final long serialVersionUID = 1L;

	private final long id;
	private final long time;
	private final long magicNumber;
	private final String hash;
	private final String previousBlockHash;

	private long creationTime;

	Block (long id, String previousBlockHash, int zeroCount) {
		this.id = id;
		this.previousBlockHash = previousBlockHash;
		this.time = new Date().getTime();
		String prefixString = new String(new char[zeroCount]).replace('\0', '0');
		long magicNumber = 0;
		String hash;
		do {
			hash = CryptoUtils.applySha256(id + time + previousBlockHash + magicNumber);
			magicNumber = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
		} while (!hash.substring(0, zeroCount).equals(prefixString));
		this.hash = hash;
		this.magicNumber = magicNumber;
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

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "Block:\n" +
		"Id: " + id + "\n" +
		"Timestamp: " + time + "\n" +
		"Magic number: " + magicNumber + "\n" +
		"Hash of the previous block:\n" + previousBlockHash + "\n" +
		"Hash of the block:\n" + hash + "\n" +
		"Block was generating for " + creationTime + " seconds";
	}
}
