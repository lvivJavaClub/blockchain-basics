package lviv.javaclub.blockchain.crypto.model;

import lombok.Builder;
import lviv.javaclub.blockchain.crypto.HashFunctions;
import lviv.javaclub.blockchain.crypto.Keys;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static lviv.javaclub.blockchain.crypto.Keys.keyToString;

@Builder
public class Transaction {

    private PublicKey sender;
    private PublicKey reciver;
    private long amount;
    private long sequenceNumber;
    private Instant instant;
    private byte[] signature;


    private String id;

    public String getMessageBody() {
        return getSignData() + HashFunctions.base64String(signature);
    }

    public String getSignData() {
        return String.join(
                "",
                keyToString(sender),
                Long.toHexString(sequenceNumber),
                Long.toHexString(instant.toEpochMilli()),

                Long.toHexString(amount),
                keyToString(reciver)
        );
    }

    public void signData(PrivateKey privateKey) {
        signature = Keys.calculateSignature(privateKey, getSignData());
    }

    public static void main(String[] args) {
        var sender = Keys.nextKeyPair();
        var receiver = Keys.nextKeyPair();

        var tx = Transaction.builder()
                .sender(sender.getPublic())
                .reciver(receiver.getPublic())
                .sequenceNumber(100)
                .instant(Instant.now())
                .amount(10_000)
                .build();

        tx.signData(sender.getPrivate());


        final SimpleBlock block = SimpleBlock.builder()
                .timestamp(Instant.now())
                .prevBlockHash(UUID.randomUUID().toString())
                .difficultyLevel(20)
                .transactions(List.of(tx))
                .build();

        block.mineBlock();

        System.out.println(block.mineBlock());
        System.out.printf("nonce=%d%n", block.getNonce());
        System.out.printf("hashBase64=%s%n", block.getBlockHash());
        System.out.printf("hashBinary=%s%n", HashFunctions.sha256BinaryString(block.getMessageBody()));

    }

}
