import org.junit.jupiter.api.*;

class X {
    static final int SECOND = 2;
    static final int START = 7;

    @Test
    @Order(1)
    void first() {}

    @Test
    @Order(SECOND)
    void second() {}

    @Test
    @Order(value = /* keep */ 3)
    void third() {}

    @Test
    @Order(2 + 2)
    void fourth() {}

    @Test
    @Order(5)
    void fifth() {}

    @Test
    @Order(6)
    void sixth() {}
}
