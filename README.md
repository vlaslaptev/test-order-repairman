# Test Order Repairman

Renumber JUnit `@Order` values in source order with Alt+Enter.

## Usage

1. Open a Java class containing JUnit Jupiter test methods annotated with `@Order`.
2. Place the caret inside one of those `@Order` annotations.
3. Press **Alt+Enter** and choose **Renumber JUnit @Order values in this class**.

For example, values `3, 1, 2` become `1, 2, 3` in the order the methods appear
in the source file. Numbering always starts at 1, including a single annotated method.

The action changes values on already annotated test methods declared directly in the
selected class. It leaves neighboring and nested classes, class annotations, extension
fields, helper methods, and methods without `@Order` unchanged. Invoke the intention
inside a nested class to renumber that class separately.

Supported methods include `@Test`, `@RepeatedTest`, `@ParameterizedTest`, `@TestFactory`,
`@TestTemplate`, and composed annotations based on them. This plugin supports Java
source files and JUnit Jupiter; it does not implement JUnit 4 or Kotlin intentions.

Annotation names, imports, and the `value =` form are preserved. Integer constants,
constant expressions, and negative values are evaluated before any edits. An expression
is replaced with an integer literal only if its value must change; the shared constant
declaration is never edited. Values that already match their position keep their original
expression. The action is unavailable if the numbering already matches or any participating
test method has an invalid or unresolved order value.

This is a manually invoked intention. It does not watch edits, move methods, add missing
annotations, or configure JUnit's execution order. To execute methods by `@Order`, configure
`@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` on the test class or the equivalent
JUnit default orderer in your project.

## Compatibility

The project targets Java 11 and the IntelliJ IDEA 2021.3.3 SDK. The declared IDE range is
2021.3.3 through 2023.2.x (`213.7172.25` through `232.*`). Support for newer IDEs is not
claimed by this change.

## Support

- [Marketplace](https://plugins.jetbrains.com/plugin/23151-test-order-repairman)
- [Report an issue](https://github.com/vlaslaptev/test-order-repairman/issues)
- [MIT license](LICENSE)
