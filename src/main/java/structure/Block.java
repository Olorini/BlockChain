package structure;

import chat.CryptoMessage;
import utils.CryptoUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Block implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long MAX_CREATION_TIME = 0L;

	private final long id;
	private final long time;
	private final long magicNumber;
	private final String hash;
	private final String previousBlockHash;
	private final List<CryptoMessage> messages;
	private final int currentZeroCount;

	private int nextZeroCount;
	private String text;
	private long creationTime;
	private int minerId;

	public Block (long id, String previousBlockHash, int zeroCount, List<CryptoMessage> blockData) {
		this.id = id;
		this.previousBlockHash = previousBlockHash;
		this.time = new Date().getTime();
		this.messages = blockData;
		this.currentZeroCount = zeroCount;
		String prefixString = new String(new char[zeroCount]).replace('\0', '0');
		long magicNumber = 0;
		String hash;
		do {
			StringBuilder builder = new StringBuilder();
			StringBuilder cryptoData = builder.append(id).append(time).append(previousBlockHash).append(magicNumber);
			if (!messages.isEmpty()) {
				this.text = messages.stream()
						.map(e -> e.getId() + ": " + e.getData())
						.collect(Collectors.joining("\n"));
				cryptoData.append(text);
			}
			hash = CryptoUtils.applySha256(cryptoData.toString());
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
//		if (creationTime < MAX_CREATION_TIME) {
//			nextZeroCount = currentZeroCount + 1;
//		} else if (currentZeroCount > 0){
//			nextZeroCount = currentZeroCount - 1;
//		} else {
			nextZeroCount = 0;
//		}
	}

	public void setMinerId(int minerId) {
		this.minerId = minerId;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public List<CryptoMessage> getMessages() {
		return messages;
	}

	public int getNextZeroCount() {
		return nextZeroCount;
	}

	public int getMinerId() {
		return minerId;
	}

	@Override
	public String toString() {
		String zeroText;
		if (currentZeroCount == nextZeroCount) {
			zeroText = "N stays the same";
		} else if (currentZeroCount > nextZeroCount) {
			zeroText = "N was decreased to " + nextZeroCount;
		} else {
			zeroText = "N was increased by " + nextZeroCount;
		}
		return "Block:\n" +
		"Created by miner # " + minerId + "\n" +
		"Id: " + id + "\n" +
		"Timestamp: " + time + "\n" +
		"Magic number: " + magicNumber + "\n" +
		"Hash of the previous block:\n" + previousBlockHash + "\n" +
		"Hash of the block:\n" + hash + "\n" +
		"Block data: " + ((text != null) ? "\n" + text : "no messages") + "\n" +
		"Block was generating for " + creationTime + " seconds" + "\n" + zeroText;
	}
}
