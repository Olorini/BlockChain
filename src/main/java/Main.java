import chat.CryptoUser;
import chat.TransactionEmulator;
import mining.Miner;
import org.apache.log4j.Logger;
import structure.Block;
import structure.BlockChain;
import structure.GenerationData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);
	private static final int MINER_COUNT = 10;
	private static final int BLOCK_COUNT = 15;
	private static final int START_ZERO_COUNT = 0;

	public static void main(String[] args) {
			BlockChain blockChain = new BlockChain(START_ZERO_COUNT);
			TransactionEmulator transactionEmulator = new TransactionEmulator(blockChain);
			Thread chatThread = new Thread(transactionEmulator);
			chatThread.start();
			for (int i = 0; i < BLOCK_COUNT; i++) {
				createNewBlock(blockChain, transactionEmulator);
			}
			blockChain.save();
			System.out.println(blockChain);
	}

	private static void createNewBlock(BlockChain blockChain, TransactionEmulator transactionEmulator) {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(MINER_COUNT);
			List<Miner> miners = new ArrayList<>();
			GenerationData data = blockChain.getGenerationData();
			for (int i = 0; i < MINER_COUNT; i++) {
				CryptoUser wallet = transactionEmulator.getRandomUser();
				Miner miner = new Miner(i + 1,
						data.getId(),
						data.getHash(),
						data.getZeroCount(),
						data.getBlockData(),
						wallet
				);
				miners.add(miner);
			}
			Block newBlock = executor.invokeAny(miners);
			executor.shutdownNow();
			blockChain.addBlock(newBlock);
			Miner winner = miners.get(newBlock.getMinerId() - 1);
			winner.transferMoneyToWallet();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
