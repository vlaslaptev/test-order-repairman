import org.junit.jupiter.api.*;

class X {
    @Order(<caret>9)
    void helper() {}

    @Test
    @Order(9)
    void test() {}
}
