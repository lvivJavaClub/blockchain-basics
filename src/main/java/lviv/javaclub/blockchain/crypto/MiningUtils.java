package lviv.javaclub.blockchain.crypto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MiningUtils {

    public static int leadingZerosCount(String value) {
        int index = 0;
        while (index < value.length()) {
            if (value.charAt(index) != '0') {
                return index;
            }
            index++;
        }
        return index;
    }

}
