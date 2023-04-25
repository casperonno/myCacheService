package org.example.myCacheService.cacheRepo;

public interface Map<K,V> {
    void put(K key, V value);

    V get(K key);
    default int hash(K key){
        return key.hashCode();
    }
}
