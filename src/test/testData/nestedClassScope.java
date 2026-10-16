import org.junit.jupiter.api.*;

class Outer {
    @Test
    @Order(9)
    void outerTest() {}

    @Nested
    class Inner {
        @Test
        @Order(<caret>4)
        void first() {}

        @Test
        @Order(8)
        void second() {}
    }

    @Nested
    class OtherInner {
        @Test
        @Order(7)
        void other() {}
    }
}
