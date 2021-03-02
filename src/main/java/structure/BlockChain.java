package structure;

import org.apache.log4j.Logger;
import utils.ListIteratorWrapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringJoiner;

import static utils.SerializationUtils.deserialize;
import static utils.SerializationUtils.serialize;
import static utils.StringTools.notEquals;

public class BlockChain {

	private static final Logger LOGGER = Logger.getLogger(BlockChain.class);
	private static final String FILE_NAME = "src/main/resources/serialize/blockchain.data";
	private static final long MAX_CREATION_TIME = 1000L;

	private LinkedList<Block> blocks = new LinkedList<>();
	private int currentZeroCount;

	@SuppressWarnings("unchecked")
	public BlockChain(int startZeroCount) {
		try {
			this.currentZeroCount = startZeroCount;
			if (new File(FILE_NAME).exists()) {
				this.blocks = (LinkedList<Block>) deserialize(FILE_NAME);
			}
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public LastBlockData getLastBlockData() {
		if (blocks.isEmpty()) {
			return new LastBlockData(1L, "0", currentZeroCount);
		} else {
			Block previousBlock = blocks.getLast();
			return new LastBlockData(previousBlock.getId() + 1, previousBlock.getHash(), currentZeroCount);
		}
	}

	public boolean validate() {
		ListIteratorWrapper<Block> iterator = new ListIteratorWrapper<>(blocks.listIterator(1));
		while (iterator.hasNext()) {
			String previousHash = iterator.previous().getHash();
			String currentHash = iterator.nextAfterPrevious().getPreviousBlockHash();
			if (notEquals(previousHash, currentHash)) {
				return false;
			}
		}
		return true;
	}

	public void addBlock(Block block) {
		blocks.add(block);
		if (!validate()) {
			blocks.removeLast();
		}
		if (block.getCreationTime() < MAX_CREATION_TIME) {
			currentZeroCount++;
		} else if (currentZeroCount != 0){
			currentZeroCount--;
		}
	}

	public void save() {
		try {
			serialize(blocks, FILE_NAME);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public int getCurrentZeroCount() {
		return currentZeroCount;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\n\n");
		for (Block block : blocks) {
			joiner.add(block.toString());
		}
		return joiner.toString();
	}
}
