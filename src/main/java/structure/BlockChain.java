package structure;

import org.apache.log4j.Logger;
import chat.CryptoMessage;
import utils.ListIteratorWrapper;
import utils.CryptoUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static utils.SerializationUtils.deserialize;
import static utils.SerializationUtils.serialize;
import static utils.StringTools.notEquals;

public class BlockChain {

	private static final Logger LOGGER = Logger.getLogger(BlockChain.class);
	private static final String FILE_NAME = "src/main/resources/serialize/blockchain.data";

	private long messageCount;
	private long maxBlockMessageId;

	private LinkedList<Block> blocks = new LinkedList<>();
	private final List<CryptoMessage> data = new ArrayList<>();
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

	public GenerationData getGenerationData() {
		if (blocks.isEmpty()) {
			return new GenerationData(1L, "0", currentZeroCount, new ArrayList<>());
		} else {
			Block previousBlock = blocks.getLast();
			return new GenerationData(previousBlock.getId() + 1, previousBlock.getHash(), currentZeroCount, getData());
		}
	}

	public boolean validate() {
		ListIteratorWrapper<Block> iterator = new ListIteratorWrapper<>(blocks.listIterator(1));
		while (iterator.hasNext()) {
			Block previousBlock = iterator.previous();
			Block currentBlock = iterator.nextAfterPrevious();
			if (notEquals(previousBlock.getHash(), currentBlock.getPreviousBlockHash())) {
				return false;
			}
			List<CryptoMessage> currentBlockData = currentBlock.getMessages();
			List<CryptoMessage> previousMessages = previousBlock.getMessages();
			long previousMessageId = 0;
			if (!previousMessages.isEmpty()) {
				previousMessageId = previousMessages.get(previousMessages.size() - 1).getId();
			}
			for (CryptoMessage message : currentBlockData) {
				if (message.getId() - previousMessageId != 1) {
					return false;
				} else {
					previousMessageId = message.getId();
				}
				String data = message.getId() + message.getData();
				try {
					boolean isValidSignature = CryptoUtils.verifySignature(
							data.getBytes(),
							message.getSignature(),
							message.getPublicKey()
					);
					if (!isValidSignature) {
						return false;
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return true;
	}

	public void addBlock(Block block) {
		blocks.add(block);
		if (!validate()) {
			blocks.removeLast();
		} else {
			currentZeroCount = block.getNextZeroCount();
		}
	}

	public void save() {
		try {
			serialize(blocks, FILE_NAME);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public synchronized List<CryptoMessage> getData() {
		List<CryptoMessage> result = new ArrayList<>(data);
		data.clear();
		this.maxBlockMessageId = messageCount;
		return result;
	}

	public synchronized long getMessageId() {
		this.messageCount++;
		return messageCount;
	}

	public synchronized void addData(CryptoMessage message) {
		try {
			if (message.getId() > maxBlockMessageId && message.getId() <= messageCount) {
				String signData = message.getId() + message.getData();
				boolean isValidSignature = CryptoUtils.verifySignature(
						signData.getBytes(),
						message.getSignature(),
						message.getPublicKey()
				);
				if (isValidSignature) {
					maxBlockMessageId++;
					data.add(message);
				}
			}
		} catch (Exception e) {
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
