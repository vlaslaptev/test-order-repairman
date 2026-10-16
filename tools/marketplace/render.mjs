import fs from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { createRequire } from "node:module";

const require = createRequire(import.meta.url);
const sharp = require(process.env.REPAIRMAN_SHARP_MODULE || "sharp");
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "../..");
const output = path.join(root, "docs/marketplace/media");
const icons = path.join(root, "src/main/resources/META-INF");
await fs.mkdir(output, { recursive: true });

const C = {
  background: "#F5F7FB", ink: "#17233B", muted: "#58657B", border: "#DCE3EF",
  blue: "#3574F0", blueSoft: "#E8F0FF", green: "#25814B", greenSoft: "#E8F5ED",
  code: "#2B2B2B", codeHeader: "#242424", codeText: "#BABABA",
  codeBlue: "#A9B7C6", keyword: "#CC7832", annotation: "#BBB529",
  number: "#6897BB", comment: "#808080", before: "#F4AD91", after: "#91D5A0",
};
const escape = value => String(value).replaceAll("&", "&amp;").replaceAll("<", "&lt;")
  .replaceAll(">", "&gt;").replaceAll('"', "&quot;");
const rect = (x, y, w, h, fill, radius = 0, stroke = "") =>
  '<rect x="' + x + '" y="' + y + '" width="' + w + '" height="' + h +
  '" rx="' + radius + '" fill="' + fill + '"' +
  (stroke ? ' stroke="' + stroke + '"' : "") + "/>";
const text = (x, y, value, size = 24, fill = C.ink, weight = 400, extra = "") =>
  '<text x="' + x + '" y="' + y + '" font-family="Arial, sans-serif" font-size="' +
  size + '" font-weight="' + weight + '" fill="' + fill + '" ' + extra + ">" + escape(value) + "</text>";
const mono = (x, y, value, size = 24, fill = C.codeText, extra = "") =>
  '<text x="' + x + '" y="' + y + '" font-family="Menlo, monospace" font-size="' +
  size + '" fill="' + fill + '" xml:space="preserve" ' + extra + ">" + escape(value) + "</text>";
const line = (x1, y1, x2, y2, color, width = 1) =>
  '<path d="M' + x1 + " " + y1 + "H" + x2 + '" fill="none" stroke="' + color +
  '" stroke-width="' + width + '"/>';
const lightIcon = await fs.readFile(path.join(icons, "pluginIcon.svg"), "utf8");
const darkIcon = await fs.readFile(path.join(icons, "pluginIcon_dark.svg"), "utf8");
function icon(x, y, size, dark = false) {
  const source = (dark ? darkIcon : lightIcon).replace(/<svg[^>]*>/, "").replace("</svg>", "");
  return '<g transform="translate(' + x + " " + y + ") scale(" + size / 40 + ')">' + source + "</g>";
}
function svg(title, body, height = 1000) {
  return '<svg xmlns="http://www.w3.org/2000/svg" width="1600" height="' + height +
    '" viewBox="0 0 1600 ' + height + '"><title>' + escape(title) +
    "</title><desc>Original informational illustration. Code examples are based on repository fixtures; this is not an IDE screenshot.</desc>" +
    body + "</svg>\n";
}
function header(index, title, subtitle) {
  return rect(0, 0, 1600, 1000, C.background) + icon(84, 52, 44) +
    text(144, 83, "Test Order Repairman", 24, C.ink, 700) +
    text(1512, 82, "JAVA / JUNIT JUPITER", 15, C.muted, 700, 'text-anchor="end" letter-spacing="1.7"') +
    text(88, 192, title, 60, C.ink, 700, 'letter-spacing="-1.8"') +
    text(88, 246, subtitle, 25, C.muted) +
    text(88, 977, "Illustrated code examples · Test Order Repairman", 15, C.muted) +
    text(1512, 977, String(index).padStart(2, "0") + " / 04", 15, C.muted, 700, 'text-anchor="end"');
}
function footer(primary, secondary = "", keyboard = false) {
  let result = rect(88, 880, 1424, 64, "#FFFFFF", 12, C.border);
  if (keyboard) {
    result += rect(108, 893, 154, 38, C.blueSoft, 8) +
      text(185, 919, "Alt + Enter", 20, C.blue, 700, 'text-anchor="middle"');
    result += text(284, 920, primary, 23, C.ink, 700);
  } else {
    result += text(112, 920, primary, 22, C.ink, 600);
  }
  if (secondary) result += text(1488, 919, secondary, 19, C.muted, 400, 'text-anchor="end"');
  return result;
}
function syntax(source, changed) {
  const token = /\/\/.*|\/\*.*?\*\/|@[\w.$]+|\b(?:class|void|static|final|int|Object|new|return)\b|\b\d+\b|[A-Za-z_]\w*(?=\()/g;
  let result = "", end = 0;
  for (const match of source.matchAll(token)) {
    result += escape(source.slice(end, match.index));
    const word = match[0];
    let color = C.codeText;
    if (word.startsWith("//") || word.startsWith("/*")) color = C.comment;
    else if (word.startsWith("@")) color = C.annotation;
    else if (/^\d/.test(word)) color = changed || C.number;
    else if (/^(class|void|static|final|int|Object|new|return)$/.test(word)) color = C.keyword;
    else color = C.codeBlue;
    result += '<tspan fill="' + color + '">' + escape(word) + "</tspan>";
    end = match.index + word.length;
  }
  return result + escape(source.slice(end));
}
function codeCard(x, y, w, h, label, filename, source, options = {}) {
  const font = options.font || 27;
  const leading = options.leading || 40;
  const changed = options.changed || [];
  const status = options.status || "after";
  const accent = status === "before" ? C.before : C.after;
  let result = rect(x, y, w, h, C.code, 16) +
    rect(x, y, w, 62, C.codeHeader, 16) +
    rect(x, y + 38, w, 24, C.codeHeader) +
    text(x + 28, y + 40, label, 16, accent, 700, 'letter-spacing="1.2"') +
    text(x + w - 28, y + 40, filename, 16, "#A6A6A6", 400, 'text-anchor="end"') +
    line(x, y + 62, x + w, y + 62, "#424242");
  const lines = source.split("\n");
  for (const [index, row] of lines.entries()) {
    const baseline = y + 106 + index * leading;
    if (baseline > y + h - 18) throw new Error("Code exceeds card: " + filename);
    if (changed.includes(index)) result += rect(x + 14, baseline - font - 4, w - 28, leading - 1,
      status === "before" ? "#3D302B" : "#2D3B31", 5);
    result += '<text x="' + (x + 30) + '" y="' + baseline + '" font-family="Menlo, monospace" font-size="' +
      font + '" fill="' + C.codeText + '" xml:space="preserve">' +
      syntax(row, changed.includes(index) ? accent : "") + "</text>";
  }
  return result;
}
function arrow(x = 800, y = 576) {
  return '<circle cx="' + x + '" cy="' + y + '" r="27" fill="' + C.blue + '"/>' +
    '<path d="M' + (x - 10) + " " + y + "h19m-7-7 7 7-7 7" +
    '" fill="none" stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>';
}
const fixture = async name => (await fs.readFile(path.join(root, "src/test/testData", name), "utf8")).replace("<caret>", "");
function compact(source) {
  return source.replace(/^import[^\n]*\n/gm, "").trim()
    .replace("class X {", "class ExampleTest {")
    .replace(/    @Test\n    @Order/g, "    @Test @Order");
}
const before = compact(await fixture("permuted.java"));
const after = compact(await fixture("permuted.after.java"));
const scopeBefore = await fixture("classScope.java");
const scopeAfter = await fixture("classScope.after.java");
for (const unchanged of ["@Order(50)", "@Order(80)", "@Order(70)", "@Order(8)", "@Order(11)", "@Order(5)"]) {
  if (!scopeBefore.includes(unchanged) || !scopeAfter.includes(unchanged)) {
    throw new Error("Class-scope fixture changed; update the illustration.");
  }
}
if (!scopeBefore.includes("@Order(1)\n    void second()") || !scopeAfter.includes("@Order(2)\n    void second()")) {
  throw new Error("Class-scope numbering changed; update the illustration.");
}
const beforeRows = before.split("\n").map((row, i) => row.includes("@Order") ? i : -1).filter(i => i >= 0);
const gallery = [];

let body = header(1, "Put test order back in order.", "Fix duplicate numbers, gaps, and values out of sequence — in source order.");
body += codeCard(88, 302, 676, 548, "BEFORE", "ExampleTest.java · excerpt", before, { changed: beforeRows, status: "before" });
body += codeCard(836, 302, 676, 548, "AFTER", "ExampleTest.java · excerpt", after, { changed: beforeRows });
body += arrow() + footer("Renumber JUnit @Order values in this class", "", true);
gallery.push(["01-renumber-in-source-order", "Renumber in source order", svg("Put test order back in order", body)]);

body = header(2, "One class. A focused change.", "Renumber the selected class while other scopes keep their own values.");
const selected = "@Order(50)\nclass Outer {\n    @Test @Order(1)\n    void first() {}\n\n    @Test @Order(2)\n    void second() {}\n    // other members unchanged\n}";
body += codeCard(88, 302, 800, 548, "SELECTED CLASS / AFTER", "Outer.java · excerpt", selected,
  { font: 27, leading: 43, changed: [5] });
const scopes = [
  ["Nested classes", "Inner: @Order(8), @Order(11)", "01"],
  ["Neighboring classes", "Neighbor: @Order(5)", "02"],
  ["Extension fields", "extension: @Order(80)", "03"],
  ["Helper methods", "helper(): @Order(70)", "04"],
];
for (const [index, item] of scopes.entries()) {
  const y = 302 + index * 139;
  body += rect(944, y, 568, 121, "#FFFFFF", 14, C.border) +
    text(972, y + 39, item[0], 24, C.ink, 700) +
    mono(972, y + 81, item[1], 20, C.muted) +
    '<circle cx="1472" cy="' + (y + 35) + '" r="7" fill="' + C.green + '"/>';
}
body += footer("Invoke inside a nested class to renumber that class separately.");
gallery.push(["02-one-class-at-a-time", "One class at a time", svg("One class. A focused change.", body)]);

body = header(3, "One intention. Three steps.", "Use Alt+Enter when you want to renumber the annotated test methods.");
const steps = [
  ["01", "Place the caret", "Inside a test method’s", "@Order annotation."],
  ["02", "Press Alt+Enter", "Open the intention actions", "at the caret."],
  ["03", "Choose the action", "Apply the new values", "in source order."],
];
for (const [index, step] of steps.entries()) {
  const x = 88 + index * 490;
  body += rect(x, 302, 444, 548, "#FFFFFF", 16, C.border) +
    '<circle cx="' + (x + 52) + '" cy="360" r="25" fill="' + C.blueSoft + '"/>' +
    text(x + 52, 367, step[0], 18, C.blue, 700, 'text-anchor="middle"') +
    text(x + 28, 438, step[1], 29, C.ink, 700) +
    text(x + 28, 484, step[2], 23, C.muted) +
    text(x + 28, 519, step[3], 23, C.muted);
  if (index === 0) {
    body += rect(x + 28, 591, 388, 118, C.code, 12) +
      mono(x + 55, 664, "@Order(3)", 37, C.annotation) +
      '<path d="M' + (x + 233) + ' 626v46" stroke="' + C.blue + '" stroke-width="3"/>';
    body += text(x + 28, 785, "Any position inside @Order", 20, C.muted);
  } else if (index === 1) {
    body += rect(x + 28, 601, 122, 96, C.blueSoft, 12, "#CBDCFF") +
      text(x + 89, 663, "Alt", 35, C.blue, 700, 'text-anchor="middle"') +
      text(x + 180, 663, "+", 29, C.muted, 400, 'text-anchor="middle"') +
      rect(x + 210, 601, 206, 96, C.blueSoft, 12, "#CBDCFF") +
      text(x + 313, 663, "Enter", 35, C.blue, 700, 'text-anchor="middle"');
    body += text(x + 28, 785, "Use your IDE’s intention shortcut", 20, C.muted);
  } else {
    body += rect(x + 28, 574, 388, 152, C.blueSoft, 12) +
      text(x + 49, 616, "Renumber JUnit @Order", 22, C.blue, 700) +
      text(x + 49, 650, "values in this class", 22, C.blue, 700) +
      text(x + 49, 698, "1  →  2  →  3", 27, C.green, 700);
    body += text(x + 28, 785, "Start at 1, follow source order", 20, C.muted);
  }
}
body += footer("JUnit execution order: @TestMethodOrder(MethodOrderer.OrderAnnotation.class)");
gallery.push(["03-alt-enter-workflow", "The Alt+Enter workflow", svg("One intention. Three steps.", body)]);

function expressionsExcerpt(source) {
  const end = source.indexOf("    @Test\n    @Order(2 + 2)");
  if (end < 0) throw new Error("Expression fixture changed; update the illustration.");
  return compact(source.slice(0, end).trimEnd() + "\n}");
}
const expressionsBefore = expressionsExcerpt(await fixture("expressions.java"));
const expressionsAfter = expressionsExcerpt(await fixture("expressions.after.java"));
const expressionRows = expressionsBefore.split("\n").map((row, i) =>
  row.includes("@Order(-5)") || row.includes("START + 4") ? i : -1).filter(i => i >= 0);
body = header(4, "Constants understood. Names kept.", "Resolve expressions first. Preserve imports, annotation names, and shared constants.");
body += codeCard(88, 302, 676, 548, "BEFORE", "ExampleTest.java · excerpt", expressionsBefore,
  { font: 22.5, leading: 32, changed: expressionRows, status: "before" });
body += codeCard(836, 302, 676, 548, "AFTER", "ExampleTest.java · excerpt", expressionsAfter,
  { font: 22.5, leading: 32, changed: expressionRows });
body += arrow() + footer("SECOND stays SECOND. START stays 7.", "Only changed values become literals.");
gallery.push(["04-constants-and-imports", "Constants, expressions, and imports", svg("Constants understood. Names kept.", body)]);

for (const [name, title, source] of gallery) {
  await fs.writeFile(path.join(output, name + ".svg"), source);
  await sharp(Buffer.from(source)).png().toFile(path.join(output, name + ".png"));
  const metadata = await sharp(path.join(output, name + ".png")).metadata();
  if (metadata.width !== 1600 || metadata.height !== 1000) throw new Error("Wrong dimensions: " + name);
  console.log("Rendered " + name + ".png (1600 × 1000)");
}
let board = rect(0, 0, 1600, 600, C.background) +
  text(64, 80, "Test Order Repairman", 42, C.ink, 700) +
  text(64, 124, "A clear order. A lighter touch.", 24, C.muted) +
  rect(64, 172, 708, 360, "#FFFFFF", 18, C.border) +
  rect(828, 172, 708, 360, "#2B2B2B", 18);
board += icon(112, 235, 190) + icon(876, 235, 190, true) +
  text(342, 224, "LIGHT", 17, C.muted, 700, 'letter-spacing="2"') +
  text(1106, 224, "DARK", 17, "#A9B7C6", 700, 'letter-spacing="2"');
for (const [index, size] of [24, 40, 80].entries()) {
  const x = 342 + index * 125;
  board += icon(x, 296, size) + text(x, 430, size + " px", 17, C.muted);
  board += icon(x + 764, 296, size, true) + text(x + 764, 430, size + " px", 17, "#A9B7C6");
}
board += text(342, 485, "40 × 40 SVG · transparent padding", 18, C.muted) +
  text(1106, 485, "Dedicated dark-theme colors", 18, "#A9B7C6");
const brandSvg = svg("Test Order Repairman logo variants", board, 600);
await fs.writeFile(path.join(output, "brand-preview.svg"), brandSvg);
await sharp(Buffer.from(brandSvg)).png().toFile(path.join(output, "brand-preview.png"));
for (const [name, source] of [["logo-light", lightIcon], ["logo-dark", darkIcon]]) {
  await sharp(Buffer.from(source), { density: 921.6 }).resize(512, 512).png()
    .toFile(path.join(output, name + "-512.png"));
}
const galleryHtml = gallery.map(([name, title]) =>
  '<section><h2>' + escape(title) + '</h2><img src="media/' + name + '.png" alt="' + escape(title) + '"></section>').join("\n");
const description = await fs.readFile(path.join(root, "docs/marketplace/description.html"), "utf8");
const preview = '<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">' +
  '<title>Test Order Repairman · Marketplace review</title><style>' +
  'body{margin:0;background:#f5f7fb;color:#17233b;font:17px/1.65 Arial,sans-serif}' +
  'main{max-width:1200px;margin:64px auto;padding:0 24px}h1{font-size:42px;letter-spacing:-1.2px}' +
  'h2{font-size:25px;margin-top:44px}img{display:block;width:100%;height:auto;border-radius:12px}' +
  '.copy{background:white;border:1px solid #dce3ef;border-radius:16px;padding:32px;margin-top:48px}' +
  'a{color:#3574f0}code{font-family:Menlo,monospace;font-size:.9em}li{margin:.5em 0}' +
  '.note{color:#58657b;max-width:900px}</style><main><h1>Test Order Repairman</h1>' +
  '<p class="note">Prepared Marketplace assets. The gallery contains original informational illustrations based on source fixtures, not captures of a running plugin. No new release has been published.</p>' +
  '<img src="media/brand-preview.png" alt="Light and dark logo variants">' +
  galleryHtml + '<section class="copy"><h2>Marketplace description</h2>' + description + "</section></main></html>\n";
await fs.writeFile(path.join(root, "docs/marketplace/preview.html"), preview);
