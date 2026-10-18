# Contributing to Test Order Repairman

Bug reports, focused improvements, documentation corrections, and accessibility
feedback are welcome. Please follow the [Code of Conduct](CODE_OF_CONDUCT.md)
in issues, pull requests, and other project spaces.

## Choose the right channel

- Search [existing issues](https://github.com/vlaslaptev/test-order-repairman/issues)
  before opening a report or proposal.
- Use the bug report form for incorrect behavior, including a minimal Java
  example, caret position, expected result, and actual result.
- Use the feature request form to describe a problem and a concrete use case.
  Discuss changes to scope or IDE compatibility before starting substantial work.
- Use the accessibility report form for barriers to using the plugin or its
  documentation. A text description is sufficient; screenshots are optional.
- Report suspected vulnerabilities privately as described in
  [SECURITY.md](SECURITY.md). Do not include them in public issues.
- For usage questions, first check the [README](README.md), then open an issue
  with the question and relevant environment details.

Use synthetic examples. Remove credentials, private source code, personal data,
and local paths from reports, logs, and screenshots before sharing them.

## Work on a change

1. Fork the repository and create a focused branch from `main`.
2. Read [AGENTS.md](AGENTS.md) and the surrounding code before editing.
3. Keep the change small and explain the user-visible problem it addresses.
4. Update documentation and regression fixtures when behavior changes.
5. Open a pull request against `main`, using the supplied template and linking
   the related issue where applicable.

The project uses Java and the IntelliJ Platform. The configured source and runtime
target is Java 11. The declared IDE range and supported JUnit behavior are listed
in [Compatibility](README.md#compatibility). Keep those declarations consistent
with the source and plugin metadata; do not claim broader support without evidence.

## Code and documentation conventions

- Follow the style of the surrounding Java code: four-space indentation,
  descriptive names, and focused methods.
- Keep the intention scoped to the selected class and preserve unaffected code.
  Explain any intentional change to those guarantees.
- Keep regression examples minimal. Existing examples live in
  `src/test/testData`; `<caret>` marks the invocation position and matching
  `.after.java` files describe the expected result.
- Keep public documentation in English. Explain behavior in text as well as
  images, and provide meaningful alternative text for new images.
- For changes to Marketplace copy or media, follow
  [the Marketplace kit](docs/marketplace/README.md) and keep shared descriptions
  consistent with `src/main/resources/META-INF/plugin.xml`.

## Review and validation

Follow the repository's working agreements for permitted validation. Review Java
changes through static analysis and code inspection, and describe the relevant
paths and edge cases in the pull request. Clearly distinguish source review from
runtime evidence, and state which validation was not performed. Do not claim
that execution succeeded on the basis of static inspection.

For behavior changes, consider class boundaries, nested classes, annotation
resolution, constants and expressions, invalid input, and repeated invocation.
For documentation changes, inspect links, examples, form fields, and consistency
with the current source and declared compatibility.

The maintainer may request revisions before merging. Contributions are made under
the repository's existing [MIT license](LICENSE); preserve applicable notices.
