import org.junit.jupiter.api.*;

@Order(<caret>9)
class X {
    @Test
    @Order(9)
    void test() {}
}
