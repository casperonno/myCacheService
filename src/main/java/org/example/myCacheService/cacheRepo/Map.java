package org.example.myCacheService.cacheRepo;

import org.springframework.stereotype.Repository;


public interface Map<K,V> {
    void put(K key, V value);

    V get(K key);
    default int hash(K key){
        return key.hashCode();
    }
}
