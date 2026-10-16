import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

@Test
@interface FastTest {}

@CycleB
@interface CycleA {}

@CycleA
@interface CycleB {}

class X {
    @Test
    @Order(<caret>9)
    void first() {}

    @RepeatedTest(2)
    @Order(9)
    void repeated() {}

    @ParameterizedTest
    @Order(9)
    void parameterized() {}

    @TestFactory
    @Order(9)
    Object[] factory() { return new Object[0]; }

    @TestTemplate
    @Order(9)
    void template() {}

    @FastTest
    @Order(9)
    void composed() {}

    @CycleA
    @Order(88)
    void helper() {}
}
