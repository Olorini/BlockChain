package mining;

import chat.CryptoMessage;
import structure.Block;

import java.util.List;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {

	private final long blockId;
	private final String previousBlockHash;
	private final int zeroCount;
	private final String id;
	private final List<CryptoMessage> blockData;

	public Miner(String id, long blockId, String previousBlockHash, int zeroCount, List<CryptoMessage> blockData) {
		this.id = id;
		this.blockId = blockId;
		this.previousBlockHash = previousBlockHash;
		this.zeroCount = zeroCount;
		this.blockData = blockData;
	}

	@Override
	public Block call() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		long startTime = System.currentTimeMillis();
		Block block = new Block(blockId, previousBlockHash, zeroCount, blockData);
		long endTime = System.currentTimeMillis();
		block.setCreationTime((endTime - startTime) / 1000);
		block.setMinerId(id);
		return block;
	}
}
