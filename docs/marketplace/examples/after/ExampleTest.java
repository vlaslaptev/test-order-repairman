import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExampleTest {
    @Test @Order(1)
    void first() {}

    @Test @Order(2)
    void second() {}

    @Test @Order(3)
    void third() {}
}
