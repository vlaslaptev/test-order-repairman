import org.junit.jupiter.api.*;

class X {
    static final int SECOND = 2;
    static final int START = 7;

    @Test
    @Order(<caret>-5)
    void first() {}

    @Test
    @Order(SECOND)
    void second() {}

    @Test
    @Order(value = /* keep */ START + 4)
    void third() {}

    @Test
    @Order(2 + 2)
    void fourth() {}

    @Test
    @Order((byte) 9)
    void fifth() {}

    @Test
    @Order('z')
    void sixth() {}
}
