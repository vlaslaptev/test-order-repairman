# Marketplace kit

Test Order Repairman 1.2 has been submitted to Marketplace and is awaiting
approval. The listing copy and gallery identify the behavior as version 1.2.

## Review

Open `preview.html` to review the light and dark logos, gallery, and description.
All public copy is in English. The source assets and raster exports are included.

## Upload assets

| Order | File | What it explains |
| --- | --- | --- |
| 1 | `media/01-renumber-in-source-order.png` | Values 3, 1, 2 become 1, 2, 3 in source order |
| 2 | `media/02-one-class-at-a-time.png` | Other classes, fields, and helpers retain their values |
| 3 | `media/03-alt-enter-workflow.png` | The caret, shortcut, and exact intention name |
| 4 | `media/04-constants-and-imports.png` | Constant expressions and preserved shared declarations |

Every gallery PNG is **1600 × 1000**, with the same aspect ratio and readable code.
Corresponding SVGs are editable sources. The syntax colors follow the Darcula code
palette, but the panels are original informational illustrations, not recreated
IDE windows or screenshots of plugin execution. Each image is labeled accordingly.

The examples come from the before/after regression fixtures listed in `listing.json`.
Presentation changes are limited to shortening excerpts, joining adjacent test
annotations on one line, and using the neutral name `ExampleTest` in the gallery.
No private projects, paths, or user code are included.

`media/brand-preview.png` shows the logos on light and dark backgrounds and at
24, 40, and 80 px. It is a design review asset, not an extra gallery slide.
The transparent 512 px PNG logos are convenience exports; the plugin consumes SVG.

## Logos

- `src/main/resources/META-INF/pluginIcon.svg`: default/light variant.
- `src/main/resources/META-INF/pluginIcon_dark.svg`: dark variant.
- Both have a 40 × 40 view box, at least 2 px of transparent padding, no embedded
  fonts, no raster images, and no external dependencies.
- The selected third design combines a blue ordered list with a green `@` symbol
  above the downward renumbering arrow. Both variants use the IntelliJ action palette.
- `icon-options/selection.json` records the approved design. The other options and
  comparison board are retained as design history, not upload assets.

The logo is taken from the plugin archive when a new plugin version is uploaded;
these source files do not change the existing Marketplace version on their own.

## Listing text and resources

- **Description:** `description.html`, also synchronized into `plugin.xml`.
- **Getting Started:** `getting-started.html`.
- **Change Notes:** `change-notes.html`, also synchronized into `plugin.xml`.
- **Source / Documentation / Bugtracker:** URLs in `listing.json`.
- **Tags:** Testing and Formatting.
- **License:** the existing MIT license.

Use the Marketplace editor's media section for the four PNGs in the order above.
Use the separate Getting Started field for the guide. Keep the repository copy and
`plugin.xml` synchronized so a later update does not restore old text.

The live description, Getting Started guide, and four-image gallery were updated
through Chrome on 2026-10-16. Their version notices identify the behavior as 1.2
while the release awaits moderation. The submission uses the stable channel with
automatic public release after approval. The source copy in `plugin.xml` remains
the basis for future updates; `listing.json` records the live version-notice prefixes
and Marketplace image IDs. The declared range stays 2021.3.3–2023.2.x; these assets do not claim support
for newer IDEs.

## Reproduce the exports

The renderer uses Node.js and `sharp` to rasterize native SVG. It does not run Java,
invoke a build tool, launch tests, or capture a browser or desktop application.

```sh
node tools/marketplace/render.mjs
```

If `sharp` is installed outside the repository, set `REPAIRMAN_SHARP_MODULE` to its
absolute module path before running the renderer. Arial and Menlo are used for the
gallery's text; no font files are distributed. The plugin logos contain only shapes.

The renderer updates the gallery SVGs/PNGs, logo previews, and `preview.html`.
It reads the actual plugin SVGs and regression fixtures. Revisit the illustrations
when fixture behavior or action wording changes.

## Future runtime screenshots

`examples/before/ExampleTest.java` and `examples/after/ExampleTest.java` are sanitized
source examples for opening in an IDE. A runtime screenshot should show the real
intention menu and the resulting code in a compatible IDE with the corrected plugin
installed. No such interaction is claimed by the illustrations in this kit.

## References

- [Marketplace listing guidance](https://plugins.jetbrains.com/docs/marketplace/best-practices-for-listing.html)
- [Plugin logo specification](https://plugins.jetbrains.com/docs/intellij/plugin-icon-file.html)
