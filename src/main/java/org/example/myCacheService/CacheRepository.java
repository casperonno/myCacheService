package org.example.myCacheService;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class CacheRepository<K,V> implements Map<K,V> {


    private class CacheElement {
        K key;
        V value;

        public CacheElement(K k, V v) {
            key = k;
            value = v;
        }

        @Override
        public boolean equals(Object k){
            return k.equals(key);
        }
    }

    private ArrayList<CacheElement>[] cache;
    private final ReadWriteLock readWriteLock; //for resizing purpose

    private final double loadFactor;
    private int numOfElements = 0;
    private int cacheCapacity;
    private static final double MAX_LOAD_FACTOR = 1;
    private static final double MIN_LOAD_FACTOR = 0.01;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int INITIAL_CACHE_SIZE = 1; //TODO change it back

    
    CacheRepository(double loadFact){
        if (!isFactorInRange(loadFact)) {
            throw new IllegalArgumentException("cache size must be above zero");
        }
        loadFactor = loadFact;
        readWriteLock = new ReentrantReadWriteLock();
        cache = initArrayWithArrayList(INITIAL_CACHE_SIZE);
        cacheCapacity = INITIAL_CACHE_SIZE;
    }

    CacheRepository(){
        this(DEFAULT_LOAD_FACTOR);
    }

    private static boolean isFactorInRange(double loadFactor) {
        return MIN_LOAD_FACTOR <= loadFactor && loadFactor <= MAX_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        readWriteLock.writeLock().lock();
        if (isAboveLoadThreshold()){
            resize();
        }
        int index = calcIndexByKey(key);

        try{
            if (cache[index]==null) {
                cache[index] = new ArrayList<>();
            }

            cache[index].add(new CacheElement(key,value));
            numOfElements++;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private int calcIndexByKey(K key) {
        return calcIndexByKey(key,cache.length);
    }

    private  int calcIndexByKey(K key, int capacity){
        return Math.abs(hash(key)%capacity);
    }

    private boolean isAboveLoadThreshold() {
        return numOfElements >= loadFactor * cacheCapacity;
    }

    @Override
    public V get(K key) {
        try {
            readWriteLock.readLock().lock();
            int idx = calcIndexByKey(key);
            List<CacheElement> listForIndex = cache[idx];
            if (listForIndex!=null){
                for (CacheElement element: listForIndex) {
                    if (element.equals(key)) {
                        return element.value;
                    }
                }
            }
            return  null;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


    private void resize(){
        resizeCacheElements(cacheCapacity*2);
    }

    private void resizeCacheElements(int newCacheCapacity) {
        ArrayList<CacheElement>[] newArrayList = initArrayWithArrayList(newCacheCapacity);
        int idx;
        for (int i=0; i<cacheCapacity; i++){
            ArrayList<CacheElement> listOfIndex = cache[i];
            for (CacheElement element: listOfIndex) {
                idx = calcIndexByKey(element.key,newCacheCapacity);
                newArrayList[idx].add(element);
            }
        }
        cache = newArrayList;
        cacheCapacity = newCacheCapacity;
    }

    @SuppressWarnings("unchecked")
    private  ArrayList<CacheElement>[] initArrayWithArrayList(int newCacheCapacity) {
        ArrayList<CacheElement>[] newArrayList = (ArrayList<CacheElement>[]) new ArrayList[newCacheCapacity];
        for (int i=0; i<newCacheCapacity; i++){
            newArrayList[i] = new ArrayList<>();
        }
        return newArrayList;
    }
}
