package homework;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {

    private final Deque<Customer> customerLinkedList = new LinkedList<>();

    public void add(Customer customer) {
        customerLinkedList.add(customer);
    }

    public Customer take() {
        return customerLinkedList.removeLast();
    }
}
