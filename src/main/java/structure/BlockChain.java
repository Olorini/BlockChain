package structure;

import utils.ListIteratorWrapper;

import java.util.LinkedList;
import java.util.StringJoiner;

import static utils.StringTools.notEquals;

public class BlockChain {

	private final LinkedList<Block> blocks = new LinkedList<>();

	public BlockChain() { }

	public void generateBlock() {
		if (blocks.isEmpty()) {
			blocks.add(new Block(1L, "0"));
		} else {
			Block previousBlock = blocks.getLast();
			blocks.add(new Block(previousBlock.getId() + 1, previousBlock.getHash()));
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

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\n\n");
		for (Block block : blocks) {
			joiner.add(block.toString());
		}
		return joiner.toString();
	}
}
