import org.junit.jupiter.api.*;

class X {
    static final int SECOND = 2;

    @Test
    @Order(<caret>1)
    void first() {}

    @Test
    @Order(SECOND)
    void second() {}
}
