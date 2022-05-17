$patterns = @{
"logo" = "Color Theory"
"crafting_tablet" = "Crafting Tablet"
"amethyst_cluster" = "Amethyst Cluster"
"amethyst_shard" = "Amethyst Shard"
"four_leaf_clover" = "Four-leafed Clover"
"ink_flask" = "Ink Flask"
"knowledge_gem" = "Knowledge Gem"
"manual" = "Colorful World"
"multitool" = "Multitool"
"neolith" = "Neolith"
"palette" = "Artistry"
"pigment" = "Pigment"
"raw_azurite" = "Azurite"
"shimmer" = "Shimmer"
"vegetal" = "Vegetal"
}

$patterns.GetEnumerator() | ForEach-Object {
$k = $_.Name
$v = $_.Value

@"
  "bannerpp.pattern.spectrum.$k.black": "Black $v",
  "bannerpp.pattern.spectrum.$k.red": "Red $v",
  "bannerpp.pattern.spectrum.$k.green": "Green $v",
  "bannerpp.pattern.spectrum.$k.brown": "Brown $v",
  "bannerpp.pattern.spectrum.$k.blue": "Blue $v",
  "bannerpp.pattern.spectrum.$k.purple": "Purple $v",
  "bannerpp.pattern.spectrum.$k.cyan": "Cyan $v",
  "bannerpp.pattern.spectrum.$k.light_gray": "Light Gray $v",
  "bannerpp.pattern.spectrum.$k.gray": "Gray $v",
  "bannerpp.pattern.spectrum.$k.pink": "Pink $v",
  "bannerpp.pattern.spectrum.$k.lime": "Lime $v",
  "bannerpp.pattern.spectrum.$k.yellow": "Yellow $v",
  "bannerpp.pattern.spectrum.$k.light_blue": "Light Blue $v",
  "bannerpp.pattern.spectrum.$k.magenta": "Magenta $v",
  "bannerpp.pattern.spectrum.$k.orange": "Orange $v",
  "bannerpp.pattern.spectrum.$k.white": "White $v",
"@
}