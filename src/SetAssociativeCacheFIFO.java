import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetAssociativeCacheFIFO {

    public int tagSize, setSize, offset, nWay;
    public Map<Integer, SetData> cache; // key is line# value is pair of tracker and list of tags

    public SetAssociativeCacheFIFO(int numBytes, int nWay, int lineSize) {
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
                if (setData.tags.get(i) == tag) {
                    return true;
                }
            }
            if (setData.tags.size() < nWay) {
                setData.tags.add(tag);
            } else {
                setData.tags.set(setData.tracker, tag);
            }
            setData.tracker = (setData.tracker < nWay - 1) ? setData.tracker + 1 : 0;
            return false;
        } else {
            cache.put(set, new SetData());
            cache.get(set).tags.add(tag);
            cache.get(set).tracker++;
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
        public int tracker;
        public List<Integer> tags;

        public SetData() {
            tracker = 0;
            tags = new ArrayList<>();
        }
    }

}
