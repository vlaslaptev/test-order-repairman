import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>9)
    void first() {}

    @Test
    @Order("invalid")
    void invalid() {}

    @Test
    @Order(9)
    void third() {}
}
