
public interface SimpleQueue<T> {
    void add(T value);

    T remove() throws Exception;

    T element() throws Exception;

    int count();

    default boolean empty() {
        return count() == 0;
    }
}