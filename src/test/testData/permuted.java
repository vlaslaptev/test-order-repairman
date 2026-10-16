import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>3)
    void first() {}

    @Test
    @Order(1)
    void second() {}

    @Test
    @Order(2)
    void third() {}
}
