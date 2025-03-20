package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> innerMap;

    public CustomerService() {
        Comparator<Customer> scoresComparator = Comparator.comparingLong(Customer::getScores);
        innerMap = new TreeMap<>(scoresComparator);
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = innerMap.firstEntry();
        return getEntryCopy(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = innerMap.higherEntry(customer);
        return getEntryCopy(entry);
    }

    public void add(Customer customer, String data) {
        Customer copy = new Customer(customer.getId(), customer.getName(), customer.getScores());
        innerMap.put(copy, data);
    }

    private static Map.Entry<Customer, String> getEntryCopy(Map.Entry<Customer, String> entry) {
        if (entry != null) {
            Customer copy = new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
            return Map.entry(copy, entry.getValue());
        }
        return null;
    }
}
