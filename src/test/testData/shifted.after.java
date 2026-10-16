import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(1)
    void first() {}

    @Test
    @Order(2)
    void second() {}
}
