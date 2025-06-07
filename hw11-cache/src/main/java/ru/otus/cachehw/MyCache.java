package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(MyCache.class);

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        logAndNotify(key, value, CacheAction.PUT);
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        logAndNotify(key, value, CacheAction.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        logAndNotify(key, value, CacheAction.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void logAndNotify(K key, V value, CacheAction cacheAction) {
        try {
            listeners.forEach(listener -> listener.notify(key, value, cacheAction.name()));
        } catch (Exception e) {
            log.error("Error while notify {}, key {}, value {}", cacheAction.name(), key, value, e);
        }
        log.info("Cache {}, key {}, value {}", cacheAction.name(), key, value);
    }
}
