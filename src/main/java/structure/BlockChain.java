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

	private LinkedList<Block> blocks = new LinkedList<>();

	@SuppressWarnings("unchecked")
	public BlockChain() {
		try {
			if (new File(FILE_NAME).exists()) {
				this.blocks = (LinkedList<Block>) deserialize(FILE_NAME);
			}
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void generateBlock(int zeroCount) {
		long startTime = System.currentTimeMillis();
		Block block;
		if (blocks.isEmpty()) {
			block = new Block(1L, "0", zeroCount);
		} else {
			Block previousBlock = blocks.getLast();
			block = new Block(previousBlock.getId() + 1, previousBlock.getHash(), zeroCount);
		}
		long endTime = System.currentTimeMillis();
		block.setCreationTime((endTime - startTime) / 1000);
		blocks.add(block);
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

	public void save() {
		try {
			serialize(blocks, FILE_NAME);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
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
