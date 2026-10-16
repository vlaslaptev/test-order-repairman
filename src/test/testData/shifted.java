import org.junit.jupiter.api.*;

class X {
    @Test
    @<caret>Order(5)
    void first() {}

    @Test
    @Order(6)
    void second() {}
}
