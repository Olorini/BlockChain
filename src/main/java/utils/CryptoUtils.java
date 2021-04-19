package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtils {

	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte elem : hash) {
				String hex = Integer.toHexString(0xff & elem);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static PrivateKey readPrivateKey(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(new File(fileName).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static PublicKey readPublicKey(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(new File(fileName).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public static byte[] sign(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature rsa = Signature.getInstance("SHA1withRSA");
		rsa.initSign(privateKey);
		rsa.update(data.getBytes());
		return rsa.sign();
	}

	public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(publicKey);
		sig.update(data);
		return sig.verify(signature);
	}

}