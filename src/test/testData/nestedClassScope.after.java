import org.junit.jupiter.api.*;

class Outer {
    @Test
    @Order(9)
    void outerTest() {}

    @Nested
    class Inner {
        @Test
        @Order(1)
        void first() {}

        @Test
        @Order(2)
        void second() {}
    }

    @Nested
    class OtherInner {
        @Test
        @Order(7)
        void other() {}
    }
}
