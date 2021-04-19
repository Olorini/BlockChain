package chat;

import utils.CryptoUtils;

import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;

public class CryptoUser {

	public static final String KEYS_DIR = "src/main/resources/keys";

	private final String name;
	private final LinkedList<String> messages = new LinkedList<>();
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public CryptoUser(String name) throws Exception {
		this.name = name;
		this.messages.push("Hello!");
		this.messages.push("I'm here!");
		this.messages.push("I'm ready to buy!");
		this.messages.push("I've borrow your money!");
		this.messages.push("Thanks!");
		this.messages.push("Buy!");
		initKeyPair();
	}

	public boolean hasMessage() {
		return !messages.isEmpty();
	}

	public CryptoMessage getMessage(long messageId) throws Exception {
		String message = name + ":" + messages.removeLast();
		byte[] signature = CryptoUtils.sign(messageId + message, privateKey);
		return new CryptoMessage(message, signature, publicKey, messageId);
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
