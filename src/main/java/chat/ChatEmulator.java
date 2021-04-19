package chat;

import org.apache.log4j.Logger;
import structure.BlockChain;

import java.util.Random;

public class ChatEmulator implements Runnable {

	private static final String[] USER_NAMES = new String[]
			{"Ann", "Joe", "Martha", "Tasha", "Kerry", "Luke", "Bob", "Harry", "Lisa", "Bill"};
	private static final Logger LOGGER = Logger.getLogger(ChatEmulator.class);

	private final CryptoUser[] users = new CryptoUser[USER_NAMES.length];
	private final BlockChain blockChain;

	public ChatEmulator(BlockChain blockChain) {
		this.blockChain = blockChain;
		try {
			for (int i = 0; i < USER_NAMES.length; i++) {
				this.users[i] = new CryptoUser(USER_NAMES[i]);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 20; i++) {
				Thread.sleep(500);
				int randomIndex = new Random().nextInt(10);
				CryptoUser currentUser = users[randomIndex];
				if (currentUser.hasMessage()) {
					long currentMessageId = blockChain.getMessageId();
					CryptoMessage currentMessage = currentUser.getMessage(currentMessageId);
					blockChain.addData(currentMessage);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
