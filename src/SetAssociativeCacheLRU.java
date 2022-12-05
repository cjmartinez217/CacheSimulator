import java.util.*;

public class SetAssociativeCacheLRU {
    public int tagSize, setSize, offset, nWay;
    public Map<Integer, SetData> cache; // key is line# value is linkedlist of tags

    public SetAssociativeCacheLRU(int numBytes, int nWay, int lineSize) {
        this.nWay = nWay;
        offset = (int)(Math.log(lineSize) / Math.log(2));
        this.setSize = (int)(Math.log((double)numBytes / lineSize / nWay) / Math.log(2));
        tagSize = 32 - this.setSize - offset;
        cache = new HashMap<>();
    }

    public boolean getData(int address) {
        int tag = getBits(address, tagSize, 32 - tagSize);
        int set = getBits(address, setSize, offset);
        if (cache.containsKey(set)) {
            SetData setData = cache.get(set);
            for (int i = 0; i < setData.tags.size(); i++) {
                if (setData.tags.contains(tag)) {
                    setData.tags.removeFirstOccurrence(tag);
                    setData.tags.offerFirst(tag);
                    return true;
                }
            }
            if (setData.tags.size() < nWay) {
                setData.tags.offerFirst(tag);
            } else {
                // remove tail add to head
                setData.tags.removeLast();
                setData.tags.offerFirst(tag);
            }
            return false;
        } else {
            cache.put(set, new SetData());
            cache.get(set).tags.offerFirst(tag);
            return false;
        }

    }

    // Function to extract k bits from p position
    // and returns the extracted value as integer
    public int getBits(int address, int numBits, int startingPosition)
    {
        return (((1 << numBits) - 1) & (address >> (startingPosition - 1)));
    }

    public class SetData {
        LinkedList<Integer> tags;

        //linked list to store order

        public SetData() {
            tags = new LinkedList<>();
        }
    }
}
