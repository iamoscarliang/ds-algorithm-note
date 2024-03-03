package ds.hashtable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public abstract class HashTableOpenAddressing<K, V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 7;
    private static final double DEFAULT_LOAD_FACTOR = 0.65;

    protected double mLoadFactor;
    protected int mCapacity;
    protected int mThreshold;
    protected int mModificationCount;

    // 'usedBuckets' counts the total number of used buckets inside the
    // hash-table (includes cells marked as deleted). While 'keyCount'
    // tracks the number of unique keys currently inside the hash-table.
    protected int mUsedBuckets;
    protected int mKeyCount;

    // These arrays store the key-value pairs.
    protected K[] mKeys;
    protected V[] mValues;

    // Special marker token used to indicate the deletion of a key-value pair
    protected final K TOMBSTONE = (K) (new Object());

    protected HashTableOpenAddressing() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    protected HashTableOpenAddressing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    protected HashTableOpenAddressing(int capacity, double loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }

        if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor)) {
            throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);
        }

        mLoadFactor = loadFactor;
        mCapacity = Math.max(DEFAULT_CAPACITY, capacity);
        adjustCapacity();
        mThreshold = (int) (this.mCapacity * loadFactor);

        mKeys = (K[]) new Object[mCapacity];
        mValues = (V[]) new Object[mCapacity];
    }

    // These three methods are used to dictate how the probing is to actually
    // occur for whatever open addressing scheme you are implementing.
    protected abstract void setupProbing(K key);

    protected abstract int probe(int x);

    // Adjusts the capacity of the hash table after it's been made larger.
    // This is important to be able to override because the size of the ds.hashtable
    // controls the functionality of the probing function.
    protected abstract void adjustCapacity();

    // Return the size of the hash-table
    public int size() {
        return mKeyCount;
    }

    // Returns if the hash-table contains no elements
    public boolean isEmpty() {
        return mKeyCount == 0;
    }

    // Clear everything in the hash-table
    public void clear() {
        for (int i = 0; i < mCapacity; i++) {
            mKeys[i] = null;
            mValues[i] = null;
        }
        mKeyCount = mUsedBuckets = 0;
        mModificationCount++;
    }

    // Check is element contained in the hash-table
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }

        setupProbing(key);
        int offset = normalizeIndex(key.hashCode());

        // Starting at the original hash linearly probe until we find a spot where
        // our key is, or we hit a null element in which case our element does not exist.
        for (int i = offset, j = -1, x = 1; ; i = normalizeIndex(offset + probe(x++))) {

            // Ignore deleted cells, but record where the first index
            // of a deleted cell is found to perform lazy relocation later.
            if (mKeys[i] == TOMBSTONE) {

                if (j == -1) j = i;

                // We hit a non-null key, perhaps it's the one we're looking for.
            } else if (mKeys[i] != null) {

                // The key we want is in the hash-table!
                if (mKeys[i].equals(key)) {

                    // If j != -1 this means we previously encountered a deleted cell.
                    // We can perform an optimization by swapping the entries in cells
                    // i and j so that the next time we search for this key it will be
                    // found faster. This is called lazy deletion/relocation.
                    if (j != -1) {
                        // Swap the key-value pairs of positions i and j.
                        mKeys[j] = mKeys[i];
                        mValues[j] = mValues[i];
                        mKeys[i] = TOMBSTONE;
                        mValues[i] = null;
                    }
                    return true;
                }

                // Key was not found in the hash-table :/
            } else return false;
        }
    }

    // Get the value associated with the input key.
    // NOTE: returns null if the value is null AND also returns
    // null if the key does not exists.
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Null key");

        setupProbing(key);
        final int offset = normalizeIndex(key.hashCode());

        // Starting at the original hash linearly probe until we find a spot where
        // our key is or we hit a null element in which case our element does not exist.
        for (int i = offset, j = -1, x = 1; ; i = normalizeIndex(offset + probe(x++))) {

            // Ignore deleted cells, but record where the first index
            // of a deleted cell is found to perform lazy relocation later.
            if (mKeys[i] == TOMBSTONE) {

                if (j == -1) j = i;

                // We hit a non-null key, perhaps it's the one we're looking for.
            } else if (mKeys[i] != null) {

                // The key we want is in the hash-table!
                if (mKeys[i].equals(key)) {

                    // If j != -1 this means we previously encountered a deleted cell.
                    // We can perform an optimization by swapping the entries in cells
                    // i and j so that the next time we search for this key it will be
                    // found faster. This is called lazy deletion/relocation.
                    if (j != -1) {
                        // Swap key-values pairs at indexes i and j.
                        mKeys[j] = mKeys[i];
                        mValues[j] = mValues[i];
                        mKeys[i] = TOMBSTONE;
                        mValues[i] = null;
                        return mValues[j];
                    } else {
                        return mValues[i];
                    }
                }

                // Element was not found in the hash-table :/
            } else return null;
        }
    }

    // Place a key-value pair into the hash-table. If the value already
    // exists inside the hash-table then the value is updated
    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }
        if (mUsedBuckets >= mThreshold) {
            resizeTable();
        }

        setupProbing(key);
        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 1; ; i = normalizeIndex(offset + probe(x++))) {

            // The current slot was previously deleted
            if (mKeys[i] == TOMBSTONE) {
                if (j == -1) j = i;

                // The current cell already contains a key
            } else if (mKeys[i] != null) {
                // The key we're trying to insert already exists in the hash-table,
                // so update its value with the most recent value
                if (mKeys[i].equals(key)) {

                    V oldValue = mValues[i];
                    if (j == -1) {
                        mValues[i] = val;
                    } else {
                        mKeys[i] = TOMBSTONE;
                        mValues[i] = null;
                        mKeys[j] = key;
                        mValues[j] = val;
                    }
                    mModificationCount++;
                    return oldValue;
                }

                // Current cell is null so an insertion/update can occur
            } else {
                // No previously encountered deleted buckets
                if (j == -1) {
                    mUsedBuckets++;
                    mKeyCount++;
                    mKeys[i] = key;
                    mValues[i] = val;

                    // Previously seen deleted bucket. Instead of inserting
                    // the new element at i where the null element is insert
                    // it where the deleted token was found.
                } else {
                    mKeyCount++;
                    mKeys[j] = key;
                    mValues[j] = val;
                }

                mModificationCount++;
                return null;
            }
        }
    }

    // Removes a key from the map and returns the value.
    // NOTE: returns null if the value is null AND also returns
    // null if the key does not exist
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("Null key");

        setupProbing(key);
        final int offset = normalizeIndex(key.hashCode());

        // Starting at the hash linearly probe until we find a spot where
        // our key is or we hit a null element in which case our element does not exist
        for (int i = offset, x = 1; ; i = normalizeIndex(offset + probe(x++))) {

            // Ignore deleted cells
            if (mKeys[i] == TOMBSTONE) continue;

            // Key was not found in hash-table.
            if (mKeys[i] == null) return null;

            // The key we want to remove is in the hash-table!
            if (mKeys[i].equals(key)) {
                mKeyCount--;
                mModificationCount++;
                V oldValue = mValues[i];
                mKeys[i] = TOMBSTONE;
                mValues[i] = null;
                return oldValue;
            }
        }
    }

    // Converts a hash value to an index. Essentially, this strips the
    // negative sign and places the hash value in the domain [0, capacity)
    protected int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % mCapacity;
    }

    // Finds the greatest common denominator of a and b.
    protected int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // Increases the capacity of the hash table.
    protected void increaseCapacity() {
        mCapacity = (2 * mCapacity) + 1;
    }

    // Double the size of the hash-table
    protected void resizeTable() {
        increaseCapacity();
        adjustCapacity();

        mThreshold = (int) (mCapacity * mLoadFactor);

        K[] oldKeyTable = (K[]) new Object[mCapacity];
        V[] oldValueTable = (V[]) new Object[mCapacity];

        // Perform key table pointer swap
        K[] keyTableTmp = mKeys;
        mKeys = oldKeyTable;
        oldKeyTable = keyTableTmp;

        // Perform value table pointer swap
        V[] valueTableTmp = mValues;
        mValues = oldValueTable;
        oldValueTable = valueTableTmp;

        // Reset the key count and buckets used since we are about to
        // re-insert all the keys into the hash-table.
        mKeyCount = mUsedBuckets = 0;

        for (int i = 0; i < oldKeyTable.length; i++) {
            if (oldKeyTable[i] != null && oldKeyTable[i] != TOMBSTONE)
                put(oldKeyTable[i], oldValueTable[i]);
            oldValueTable[i] = null;
            oldKeyTable[i] = null;
        }
    }

    // Returns a list of keys found in the hash table
    public List<K> keys() {
        List<K> hashtableKeys = new ArrayList<>(size());
        for (int i = 0; i < mCapacity; i++) {
            if (mKeys[i] != null && mKeys[i] != TOMBSTONE) {
                hashtableKeys.add(mKeys[i]);
            }
        }
        return hashtableKeys;
    }

    // Returns a list of non-unique values found in the hash table
    public List<V> values() {
        List<V> hashtableValues = new ArrayList<>(size());
        for (int i = 0; i < mCapacity; i++) {
            if (mKeys[i] != null && mKeys[i] != TOMBSTONE) {
                hashtableValues.add(mValues[i]);
            }
        }
        return hashtableValues;
    }

    @Override
    public Iterator<K> iterator() {
        // Before the iteration begins record the number of modifications
        // done to the hash-table. This value should not change as we iterate
        // otherwise a concurrent modification has occurred :0
        final int MODIFICATION_COUNT = mModificationCount;

        return new Iterator<K>() {
            int index, keysLeft = mKeyCount;

            @Override
            public boolean hasNext() {
                // The contents of the table have been altered
                if (MODIFICATION_COUNT != mModificationCount) throw new ConcurrentModificationException();
                return keysLeft != 0;
            }

            // Find the next element and return it
            @Override
            public K next() {
                while (mKeys[index] == null || mKeys[index] == TOMBSTONE) index++;
                keysLeft--;
                return mKeys[index++];
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
        for (int i = 0; i < mCapacity; i++)
            if (mKeys[i] != null && mKeys[i] != TOMBSTONE) sb.append(mKeys[i] + " => " + mValues[i] + ", ");
        sb.append("}");

        return sb.toString();
    }

}
