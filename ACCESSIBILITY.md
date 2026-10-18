# Accessibility

Test Order Repairman aims to make its intention action and documentation usable
with keyboard navigation, assistive technology, and different visual preferences.
Accessibility feedback and improvements are welcome.

## Supported environment and interaction

The plugin runs inside IntelliJ IDEA and uses its standard intention action UI.
The declared environment is IntelliJ IDEA 2021.3.3–2023.2.x with Java support;
see [Compatibility](README.md#compatibility) for the full declaration.

To invoke the action with a keyboard, place the caret inside a test method's
`@Order` annotation, open the IDE intention menu (Alt+Enter in the default
keymap), and select **Renumber JUnit @Order values in this class**. Custom
keymaps and platform settings may use a different shortcut.

The plugin does not add a custom dialog. The intention has a descriptive text
label, and plugin icons have light and dark variants. The README explains the
workflow in text and includes Java examples and alternative text for its images.

## Known limitations

- Screen reader compatibility, high contrast modes, and the full range of
  keyboard and assistive technology combinations have not been independently
  verified for this plugin. No formal accessibility conformance is claimed.
- IDE menu navigation, text scaling, and screen reader behavior depend on the
  host IDE, operating system, and user configuration.
- Marketplace gallery illustrations contain code as images. The README provides
  a text example and describes the behavior; the images are supplementary.
- Only Java/JUnit Jupiter intentions are implemented. Kotlin and JUnit 4 are
  outside the current scope, and newer IDE versions are not declared compatible.

## Report a barrier

Open an [accessibility report](https://github.com/vlaslaptev/test-order-repairman/issues/new?template=accessibility_report.yml)
or email [vlaslaptev@gmail.com](mailto:vlaslaptev@gmail.com) if a public report is
not suitable. Describe what you were trying to do, the barrier, the plugin and IDE
versions, operating system, and any relevant assistive technology or settings.
A text description is enough; recordings and screenshots are optional.

Remove private source code and personal information before sharing evidence.
Reports are reviewed as maintainer availability permits; there is no guaranteed
response or resolution timeline.
