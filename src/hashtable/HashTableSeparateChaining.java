package hashtable;

import java.util.*;

public class HashTableSeparateChaining<K, V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private final double mMaxLoadFactor;

    private LinkedList<Entry<K, V>>[] mTable;
    private int mCapacity = 0;
    private int mThreshold = 0;
    private int mSize = 0;

    public HashTableSeparateChaining() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity, double maxLoadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity");
        }
        if (maxLoadFactor <= 0 || Double.isNaN(maxLoadFactor) || Double.isInfinite(maxLoadFactor)) {
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        }
        mMaxLoadFactor = maxLoadFactor;
        mCapacity = Math.max(DEFAULT_CAPACITY, capacity);
        mThreshold = (int) (mCapacity * maxLoadFactor);
        mTable = new LinkedList[mCapacity];
    }

    // Return the size of the hash-table
    public int size() {
        return mSize;
    }

    // Returns if the hash-table contains no elements
    public boolean isEmpty() {
        return mSize == 0;
    }

    // Clear everything in the hash-table
    public void clear() {
        Arrays.fill(mTable, null);
        mSize = 0;
    }

    // Check is element contained in the hash-table
    public boolean contains(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());
        return getBucketEntry(bucketIndex, key) != null;
    }

    // Gets a key's values from the map and returns the value
    public V get(K key) {
        if (key == null) {
            return null;
        }
        int bucketIndex = normalizeIndex(key.hashCode());
        Entry<K, V> entry = getBucketEntry(bucketIndex, key);
        if (entry != null) {
            return entry.value;
        }
        return null;
    }

    // Place a value in the hash-table
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }
        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(newEntry.hash);
        return insertBucketEntry(bucketIndex, newEntry);
    }

    // Removes a key from the map and returns the value
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int bucketIndex = normalizeIndex(key.hashCode());
        return removeBucketEntry(bucketIndex, key);
    }

    // Converts a hash value to an index in the domain [0, capacity)
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % mCapacity;
    }

    // Returns a particular entry in a given bucket if it exists, returns null otherwise
    private Entry<K, V> getBucketEntry(int bucketIndex, K key) {
        if (key == null) {
            return null;
        }
        LinkedList<Entry<K, V>> bucket = mTable[bucketIndex];
        if (bucket == null) {
            return null;
        }
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

    // Inserts an entry in a given bucket only if the entry not exist, update the entry value otherwise
    private V insertBucketEntry(int bucketIndex, Entry<K, V> entry) {
        LinkedList<Entry<K, V>> bucket = mTable[bucketIndex];
        if (bucket == null) {
            mTable[bucketIndex] = bucket = new LinkedList<>();
        }

        Entry<K, V> existentEntry = getBucketEntry(bucketIndex, entry.key);
        if (existentEntry == null) {
            bucket.add(entry);
            if (++mSize > mThreshold) {
                resizeTable();
            }
            return null;
        } else {
            V oldVal = existentEntry.value;
            existentEntry.value = entry.value;
            return oldVal;
        }
    }

    // Removes an entry from a given bucket if it exists
    private V removeBucketEntry(int bucketIndex, K key) {
        Entry<K, V> entry = getBucketEntry(bucketIndex, key);
        if (entry != null) {
            LinkedList<Entry<K, V>> links = mTable[bucketIndex];
            links.remove(entry);
            --mSize;
            return entry.value;
        }
        return null;
    }

    // Resizes the internal table holding buckets of entries
    private void resizeTable() {
        mCapacity *= 2;
        mThreshold = (int) (mCapacity * mMaxLoadFactor);

        LinkedList<Entry<K, V>>[] newTable = new LinkedList[mCapacity];

        for (int i = 0; i < mTable.length; i++) {
            if (mTable[i] != null) {
                for (Entry<K, V> entry : mTable[i]) {
                    int bucketIndex = normalizeIndex(entry.hash);
                    LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
                    if (bucket == null) {
                        newTable[bucketIndex] = bucket = new LinkedList<>();
                    }
                    bucket.add(entry);
                }

                // Avoid memory leak. Help the GC
                mTable[i].clear();
                mTable[i] = null;
            }
        }

        mTable = newTable;
    }

    // Returns the list of keys
    public List<K> keys() {
        List<K> keys = new ArrayList<>(size());
        for (LinkedList<Entry<K, V>> bucket : mTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }

    // Returns the list of values
    public List<V> values() {
        List<V> values = new ArrayList<>(size());
        for (LinkedList<Entry<K, V>> bucket : mTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    values.add(entry.value);
                }
            }
        }
        return values;
    }

    @Override
    public Iterator<K> iterator() {
        final int elementCount = size();
        return new Iterator<K>() {

            int bucketIndex = 0;
            Iterator<Entry<K, V>> bucketIter = (mTable[0] == null) ? null : mTable[0].iterator();

            @Override
            public boolean hasNext() {

                // An item was added or removed while iterating
                if (elementCount != mSize) throw new ConcurrentModificationException();

                // No iterator or the current iterator is empty
                if (bucketIter == null || !bucketIter.hasNext()) {

                    // Search next buckets until a valid iterator is found
                    while (++bucketIndex < mCapacity) {
                        if (mTable[bucketIndex] != null) {

                            // Make sure this iterator actually has elements -_-
                            Iterator<Entry<K, V>> nextIter = mTable[bucketIndex].iterator();
                            if (nextIter.hasNext()) {
                                bucketIter = nextIter;
                                break;
                            }
                        }
                    }
                }
                return bucketIndex < mCapacity;
            }

            @Override
            public K next() {
                return bucketIter.next().key;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < mCapacity; i++) {
            if (mTable[i] == null) {
                continue;
            }
            for (Entry<K, V> entry : mTable[i]) {
                sb.append(entry + ", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
