package org.example.myCacheService.cacheRepo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

@Repository
public class CacheRepository<K,V> implements Map<K,V> {


    private class CacheElement {
        K key;
        V value;

        public CacheElement(K k, V v) {
            key = k;
            value = v;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object k){
            if (k!=null && (k.getClass().equals(this.key.getClass()))) {
                return k.equals(key);
            }
            return false;
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
    private static final int INITIAL_CACHE_SIZE = 5;

    
    public CacheRepository(double loadFact){
        if (!isFactorInRange(loadFact)) {
            throw new IllegalArgumentException("cache size must be above zero");
        }
        loadFactor = loadFact;
        readWriteLock = new ReentrantReadWriteLock();
        cache = initArrayWithArrayList(INITIAL_CACHE_SIZE);
        cacheCapacity = INITIAL_CACHE_SIZE;
    }

    public CacheRepository(){
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
        }
        catch (Exception e){
            System.err.println("failed to put element in cache");
            e.printStackTrace();
        } finally {
            try{
                readWriteLock.writeLock().unlock();
            } catch (Exception e){
                System.err.println("failed to unlock acquired locks");
                e.printStackTrace();
            }
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
        V element = null;
        try {
            readWriteLock.readLock().lock();
            var idx = calcIndexByKey(key);
            List<CacheElement> listForIndex = cache[idx];
            if (listForIndex!=null){
                element = listForIndex.stream().filter(e -> e.equals(key)).findFirst().map(CacheElement::getValue).orElse(null);
            }
            return  element;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


    private void resize(){
        resizeCacheElements(cacheCapacity*2);
    }

    private void resizeCacheElements(int newCacheCapacity) {
        var newArrayList = initArrayWithArrayList(newCacheCapacity);
        for (int i=0; i<cacheCapacity; i++){
            ArrayList<CacheElement> listOfIndex = cache[i];
            for (CacheElement element: listOfIndex) {
                var idx = calcIndexByKey(element.key,newCacheCapacity);
                newArrayList[idx].add(element);
            }
        }
        cache = newArrayList;
        cacheCapacity = newCacheCapacity;
    }

    @SuppressWarnings("unchecked")
    private  ArrayList<CacheElement>[] initArrayWithArrayList(int newCacheCapacity) {
        return Stream.generate(ArrayList<CacheElement>::new)
                .limit(newCacheCapacity)
                .toArray(ArrayList[]::new);
    }
}