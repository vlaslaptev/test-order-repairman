import org.junit.jupiter.api.Test;
import other.Order;

@Order(99)
class X {
    @Test
    @org.junit.jupiter.api.Order(1)
    void first() {}

    @Test
    @org.junit.jupiter.api.Order(2)
    void second() {}
}
