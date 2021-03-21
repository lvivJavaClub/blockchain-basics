package lviv.javaclub.blockchain.crypto.model;

import lombok.Builder;
import lombok.Getter;
import lviv.javaclub.blockchain.crypto.HashFunctions;
import lviv.javaclub.blockchain.crypto.MiningUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class SimpleBlock {

    private Instant timestamp;
    private List<Transaction> transactions;
    private String prevBlockHash;
    private String blockHash;
    private int nonce;

    @Builder.Default
    private int difficultyLevel = 20;


    public String getMessageBody() {
        return String.join(
                "",
                Long.toHexString(timestamp.toEpochMilli()),
                transactions.stream()
                        .map(Transaction::getMessageBody)
                        .collect(Collectors.joining()),
                Integer.toHexString(nonce)
        );
    }

    public boolean mineBlock() {
        while (nonce < Integer.MAX_VALUE &&
                MiningUtils.leadingZerosCount(HashFunctions.sha256BinaryString(getMessageBody())) < difficultyLevel) {
            nonce++;
        }

        if (nonce < Integer.MAX_VALUE) {
            blockHash = HashFunctions.sha256HexString(getMessageBody());
        }

        return nonce < Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        var block = SimpleBlock.builder()
                .timestamp(Instant.now())
                .transactions(List.of(
                        Transaction.builder()
                                .id(UUID.randomUUID().toString())
                                .build()
                ))
                .prevBlockHash(UUID.randomUUID().toString())
                .build();

        System.out.println(block.mineBlock());
        System.out.printf("nonce=%d%n", block.nonce);
        System.out.printf("hashBase64=%s%n", block.blockHash);
        System.out.printf("hashBinary=%s%n", HashFunctions.sha256BinaryString(block.getMessageBody()));

    }


}
