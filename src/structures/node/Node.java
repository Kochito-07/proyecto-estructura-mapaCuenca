package structures.node;
import java.util.Objects;

public class Node<T> {
    private T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() { return data; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(data, node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
