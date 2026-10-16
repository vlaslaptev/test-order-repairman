import org.junit.jupiter.api.*;

class X {
    static final int STEP = 1;

    @Test
    @Order(<caret>STEP)
    void first() {}

    @Test
    @Order(STEP)
    void second() {}
}
