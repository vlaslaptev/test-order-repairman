import org.junit.jupiter.api.*;

class X {
    @Test
    @Order(<caret>42)
    void onlyTest() {}
}
