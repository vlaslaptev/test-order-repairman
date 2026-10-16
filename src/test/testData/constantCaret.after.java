import org.junit.jupiter.api.*;

class X {
    static final int STEP = 1;

    @Test
    @Order(STEP)
    void first() {}

    @Test
    @Order(2)
    void second() {}
}
