class X {

    @Test
    @org.junit.jupiter.api.Order(<caret>1)
    void nullValues() {
        // test code
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void emptyValues() {
        // test code
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void validValues() {
        // test code
    }
}