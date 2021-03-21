package lviv.javaclub.blockchain.crypto;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@UtilityClass
public class Keys {

    @SneakyThrows
    public static KeyPair nextKeyPair() {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    @SneakyThrows
    public static byte[] calculateSignature(PrivateKey privateKey, String message) {
        var signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    @SneakyThrows
    public static boolean verifySignature(PublicKey publicKey, String message, byte[] signature) {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes());
        return sig.verify(signature);
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
