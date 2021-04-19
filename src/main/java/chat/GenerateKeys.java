package chat;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateKeys {

	private final KeyPairGenerator keyGen;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keyLength);
	}

	public void createKeys() {
		KeyPair pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
