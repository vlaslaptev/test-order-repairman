import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>9)
    void first() {}

    @Test
    @Order(UNKNOWN)
    void unresolved() {}

    @Test
    @Order(9)
    void third() {}
}
