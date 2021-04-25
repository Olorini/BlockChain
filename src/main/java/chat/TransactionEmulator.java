package chat;

import org.apache.log4j.Logger;
import structure.BlockChain;

import java.util.Random;

public class TransactionEmulator implements Runnable {

	public static final String[] USER_NAMES = new String[]
			{"Ann", "Joe", "Martha", "Tasha", "Kerry", "Luke", "Bob", "Harry", "Lisa", "Bill"};
	private static final Logger LOGGER = Logger.getLogger(TransactionEmulator.class);

	private final CryptoUser[] users = new CryptoUser[USER_NAMES.length];
	private final BlockChain blockChain;

	public TransactionEmulator(BlockChain blockChain) {
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
				CryptoUser sender = getRandomUser();
				CryptoUser recipient = getRandomUser();
				double transAmount = round(0.01 + 99.99 * new Random().nextDouble(), 2);
				if (transAmount <= sender.getAmount()) {
					sender.setAmount(round(sender.getAmount() - transAmount, 2));
					recipient.setAmount(round(recipient.getAmount() + transAmount, 2));
					long currentMessageId = blockChain.getMessageId();
					CryptoMessage currentMessage = sender.getMessage(currentMessageId, transAmount, recipient);
					blockChain.addData(currentMessage);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	public CryptoUser getRandomUser() {
		return users[new Random().nextInt(10)];
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
