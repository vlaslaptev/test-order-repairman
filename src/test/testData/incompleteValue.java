import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>9)
    void first() {}

    @Test
    @Order()
    void incomplete() {}

    @Test
    @Order(9)
    void third() {}
}
