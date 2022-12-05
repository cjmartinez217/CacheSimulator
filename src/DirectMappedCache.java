import java.util.HashMap;
import java.util.Map;

public class DirectMappedCache {
    public int tagSize, lineSize, offset;
    public Map<Integer, Integer> cache; // key is line# value is tag

    public DirectMappedCache(int numBytes, int lineSize) {
        offset = (int)(Math.log(lineSize) / Math.log(2));
        this.lineSize = (int)(Math.log((double)numBytes / lineSize) / Math.log(2));
        tagSize = 32 - this.lineSize - offset;
        cache = new HashMap<>();
    }

    public boolean getData(int address) {
        int tag = getBits(address, tagSize, 32 - tagSize);
        int line = getBits(address, lineSize, offset);
        if (cache.containsKey(line) && cache.get(line) == tag) {
            return true;
        } else {
            cache.put(line, tag);
            return false;
        }
    }

    // Function to extract k bits from p position
    // and returns the extracted value as integer
    public int getBits(int address, int numBits, int startingPosition)
    {
        return (((1 << numBits) - 1) & (address >> (startingPosition - 1)));
    }
}
