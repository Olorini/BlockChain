import structure.Block;
import structure.BlockChain;
import structure.LastBlockData;
import structure.Miner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	private static final int MINER_COUNT = 10;
	private static final int BLOCK_COUNT = 5;

	public static void main(String[] args) {
		try {
			BlockChain blockChain = new BlockChain(0);
			for (int i = 0; i < BLOCK_COUNT; i++) {
				createNewBlock(blockChain);
			}
			blockChain.save();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private static void createNewBlock(BlockChain blockChain) throws ExecutionException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(MINER_COUNT);
		List<Miner> miners = new ArrayList<>();
		LastBlockData data = blockChain.getLastBlockData();
		for (int i = 0; i < MINER_COUNT; i++) {
			miners.add(new Miner(String.valueOf(i + 1), data.getId(), data.getHash(), data.getZeroCount()));
		}
		Block newBlock = executor.invokeAny(miners);
		executor.shutdownNow();
		blockChain.addBlock(newBlock);
		System.out.println(newBlock.toString());
		if (blockChain.getCurrentZeroCount() == data.getZeroCount()) {
			System.out.println("N stays the same");
		} else if (blockChain.getCurrentZeroCount() > data.getZeroCount()) {
			System.out.println("N was increased to " + (blockChain.getCurrentZeroCount() - data.getZeroCount()));
		} else {
			System.out.println("N was decreased by " + (data.getZeroCount() - blockChain.getCurrentZeroCount()));
		}
		System.out.println("\n");
	}
}
