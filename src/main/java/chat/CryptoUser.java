package chat;

import utils.CryptoUtils;

import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptoUser {

	public static final String KEYS_DIR = "src/main/resources/keys";

	private final String name;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private double amount;

	public CryptoUser(String name) throws Exception {
		this.name = name;
		this.amount = 100.00;
		initKeyPair();
	}

	public CryptoMessage getMessage(long messageId, double transAmount, CryptoUser recipient) throws Exception {
		String message = name + " transfer " + transAmount + " VC to " + recipient.getName() + " REST: " + getAmount();
		byte[] signature = CryptoUtils.sign(messageId + message, privateKey);
		return new CryptoMessage(message, signature, publicKey, messageId);
	}

	public synchronized double getAmount() {
		return amount;
	}

	public synchronized void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	private void initKeyPair() throws Exception {
		File pbKeyFile = new File(getPublicKeyPath());
		File prKeyFile = new File(getPrivateKeyPath());
		if (pbKeyFile.exists() && prKeyFile.exists()) {
			this.publicKey = CryptoUtils.readPublicKey(getPublicKeyPath());
			this.privateKey = CryptoUtils.readPrivateKey(getPrivateKeyPath());
		} else {
			GenerateKeys keyGen = new GenerateKeys(1024);
			keyGen.createKeys();
			this.publicKey = keyGen.getPublicKey();
			this.privateKey = keyGen.getPrivateKey();
			if (pbKeyFile.getParentFile().mkdirs()) {
				Files.write(pbKeyFile.toPath(), publicKey.getEncoded());
				Files.write(prKeyFile.toPath(), privateKey.getEncoded());
			}
		}
	}

	private String getPublicKeyPath() {
		return KEYS_DIR + "/" + name + "/publicKey";
	}

	private String getPrivateKeyPath() {
		return KEYS_DIR + "/" + name + "/privateKey";
	}
}
