package chat;

import java.io.Serializable;
import java.security.PublicKey;

public class CryptoMessage implements Serializable {

	private final String data;
	private final byte[] signature;
	private final PublicKey publicKey;
	private final long id;

	public CryptoMessage(String data, byte[] signature, PublicKey publicKey, long id) {
		this.data = data;
		this.signature = signature;
		this.publicKey = publicKey;
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public long getId() {
		return id;
	}
}
