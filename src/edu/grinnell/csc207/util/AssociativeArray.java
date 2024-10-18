package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;
import java.util.Arrays;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Anthony Castleberry
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newarray = new AssociativeArray<K, V>();
    for (int i = 0; i < size; i++) {
      try {
        newarray.set(this.pairs[DEFAULT_CAPACITY + i].key, this.pairs[DEFAULT_CAPACITY + i].val);
      } catch (NullKeyException e) {
        return newarray;
      } // try/catch
    } // for
    return newarray;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String result = "{";
    for (int i = 0; i < size; i++) {
      int index = DEFAULT_CAPACITY + i;
      result = result.concat(this.pairs[index].key.toString());
      result = result.concat(":");
      if (this.pairs[index].val == null) {
        result = result.concat("null");
      } else {
        result = result.concat(this.pairs[index].val.toString());
      } // if
      if (size - i > 1) {
        result = result.concat(", ");
      } // if
    } // for
    result = result.concat("}");
    return result;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *   The key whose value we are seeting.
   * @param value
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } else {
      try {
        int index = this.find(key);
        this.pairs[index] = new KVPair<K, V>(key, value);
      } catch (KeyNotFoundException e) {
        this.pairs = Arrays.copyOf(this.pairs, this.pairs.length + 1);
        this.pairs[this.pairs.length - 1] = new KVPair<K, V>(key, value);
        size++;
      } // try/catch
    } // if
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @return
   *   The corresponding value
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    int index = this.find(key);
    return this.pairs[index].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key, since it cannot appear.
   *
   * @param key
   *   The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    try {
      this.find(key);
      return true;
    } catch (KeyNotFoundException e) {
      return false;
    } // try/catch
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   *   The key to remove.
   */
  public void remove(K key) {
    try {
      int index = this.find(key);
      int arrlength = this.pairs.length - 1;
      this.pairs[index] = new KVPair<K, V>(this.pairs[arrlength].key, this.pairs[arrlength].val);
      this.pairs = Arrays.copyOf(this.pairs, arrlength);
      size--;
    } catch (KeyNotFoundException e) {
      return;
    } // try/catch
  } // remove(K)

  /**
   * Buil an array of Keys in string form
   * @param start - index to start grabbing keys from
   * @return The array of Keys as strings
   */
  public String[] getKeys() {
    String[] keys = new String[this.size];
    for (int i = 0; i < this.size(); i++) {
      keys[i] = this.pairs[i + DEFAULT_CAPACITY].key.toString();
    } // for
    return keys;
  } // getKeys(int)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @return
   *   The index of the key, if found.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = DEFAULT_CAPACITY; i < this.pairs.length; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray