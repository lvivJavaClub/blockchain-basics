package lviv.javaclub.blockchain.crypto;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Function;

@UtilityClass
public class HashFunctions {

    public static String sha256HexString(Object object) {
        return sha256String(object, HashFunctions::base64String);
    }

    public static String sha256BinaryString(Object object) {
        return sha256String(object, HashFunctions::binaryString);
    }

    @SneakyThrows
    public static String sha256String(Object object, Function<byte[], String> encoder) {
        var messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(Objects.toString(object).getBytes());
        final byte[] digest = messageDigest.digest();
        return encoder.apply(digest);
    }

    public static String base64String(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    public static String binaryString(byte[] source) {
        var binaryValue = new byte[source.length * 8];
        Arrays.fill(binaryValue, (byte) '0');

        for (int index = 0, bit = 0; index < source.length; index++) {
            int singleByte = source[index];
            int testMask = 1 << 7;
            for (int i = 7; i >= 0; i--, testMask >>= 1, bit++) {
                if ((singleByte & testMask) > 0) {
                    binaryValue[bit] = '1';
                }
            }
        }
        return new String(binaryValue);
    }

    public static void main(String[] args) {
        System.out.println(sha256HexString("brown fox"));
        System.out.println(sha256HexString("brown fax"));
        System.out.println(sha256BinaryString("brown fox"));
    }

}
