import org.junit.jupiter.api.*;

class X {
    @Order(<caret>9)
    Object extension;

    @Test
    @Order(9)
    void test() {}
}
