package br.com.marcosprado.timesbackend.config.cache;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.dto.ProductSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryCache {
     private Map<Integer, List<Product>> cache = new HashMap<>();

    public void put(Integer customerId, List<Product> recommendations) {
        cache.put(customerId, recommendations);
    }

    public List<Product> get(Integer customerId) {
        return cache.get(customerId);
    }

    public boolean containsKey(Integer customerId) {
        return cache.containsKey(customerId);
    }

    public void clear() {
        cache.clear();
    }

    public void remove(Integer customerId) {
        cache.remove(customerId);
    }
}
