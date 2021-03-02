package structure;

import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {

	private final long blockId;
	private final String previousBlockHash;
	private final int zeroCount;
	private final String id;


	public Miner(String id, long blockId, String previousBlockHash, int zeroCount) {
		this.id = id;
		this.blockId = blockId;
		this.previousBlockHash = previousBlockHash;
		this.zeroCount = zeroCount;
	}

	@Override
	public Block call() {
		long startTime = System.currentTimeMillis();
		Block block = new Block(blockId, previousBlockHash, zeroCount);
		long endTime = System.currentTimeMillis();
		block.setCreationTime((endTime - startTime) / 1000);
		block.setMinerId(id);
		return block;
	}
}
