import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>9)
    @Order(9)
    void duplicate() {}

    @Test
    @Order(9)
    void second() {}
}
