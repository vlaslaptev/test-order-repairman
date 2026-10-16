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
    @Order(1)
    void first() {}

    @RepeatedTest(2)
    @Order(2)
    void repeated() {}

    @ParameterizedTest
    @Order(3)
    void parameterized() {}

    @TestFactory
    @Order(4)
    Object[] factory() { return new Object[0]; }

    @TestTemplate
    @Order(5)
    void template() {}

    @FastTest
    @Order(6)
    void composed() {}

    @CycleA
    @Order(88)
    void helper() {}
}
