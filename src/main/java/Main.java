import chat.ChatEmulator;
import mining.Miner;
import structure.Block;
import structure.BlockChain;
import structure.GenerationData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	private static final int MINER_COUNT = 10;
	private static final int BLOCK_COUNT = 5;
	private static final int START_ZERO_COUNT = 0;

	public static void main(String[] args) {
		try {
			BlockChain blockChain = new BlockChain(START_ZERO_COUNT);
			ChatEmulator chat = new ChatEmulator(blockChain);
			Thread chatThread = new Thread(chat);
			chatThread.start();
			for (int i = 0; i < BLOCK_COUNT; i++) {
				createNewBlock(blockChain);
			}
			blockChain.save();
			System.out.println(blockChain);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private static void createNewBlock(BlockChain blockChain) throws ExecutionException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(MINER_COUNT);
		List<Miner> miners = new ArrayList<>();
		GenerationData data = blockChain.getGenerationData();
		for (int i = 0; i < MINER_COUNT; i++) {
			miners.add(new Miner(String.valueOf(i + 1), data.getId(), data.getHash(), data.getZeroCount(), data.getBlockData()));
		}
		Block newBlock = executor.invokeAny(miners);
		executor.shutdownNow();
		blockChain.addBlock(newBlock);
	}
}
