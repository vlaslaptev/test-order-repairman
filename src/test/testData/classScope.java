import org.junit.jupiter.api.*;

@Order(50)
class Outer {
    @Order(80)
    Object extension;

    @Order(70)
    void helper() {}

    @Test
    @Order(<caret>1)
    void first() {}

    @Test
    @Order(1)
    void second() {}

    @Test
    void unordered() {}

    @Nested
    @Order(60)
    class Inner {
        @Test
        @Order(8)
        void nestedFirst() {}

        @Test
        @Order(11)
        void nestedSecond() {}
    }
}

class Neighbor {
    @Test
    @Order(5)
    void neighbor() {}
}
