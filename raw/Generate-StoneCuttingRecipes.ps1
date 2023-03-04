class Template {

    [string] $ingredient
    [string] $output
    [int] $amount

    Template([string] $ingredient, [string] $output, [int] $amount) {
        $this.ingredient = $ingredient
        $this.output = $output
        $this.amount = $amount
    }

}

$entries = @(
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_bricks", 1),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_tiles", 1),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_polished_shale_clay_stairs", 1),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_polished_shale_clay_slab", 2),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_brick_stairs", 1),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_brick_slab", 2),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_tile_stairs", 1),
    [Template]::new("spectrum:weathered_polished_shale_clay", "spectrum:weathered_shale_clay_tile_slab", 2),
    [Template]::new("spectrum:weathered_shale_clay_bricks", "spectrum:weathered_shale_clay_brick_slab", 2),
    [Template]::new("spectrum:weathered_shale_clay_bricks", "spectrum:weathered_shale_clay_brick_stairs", 1),
    [Template]::new("spectrum:weathered_shale_clay_tiles", "spectrum:weathered_shale_clay_tile_stairs", 1),
    [Template]::new("spectrum:weathered_shale_clay_tiles", "spectrum:weathered_shale_clay_tile_slab", 2)
)

$entries | ForEach-Object {
    $inFull = $_.ingredient
    if (!($inFull -match "spectrum:")) {
        $inFull = "minecraft:" + $inFull
    }
    $outFull = $_.output
    if (!($outFull -match "spectrum:")) {
        $outFull = "minecraft:" + $outFull
    }

    if($_.ingredient -match ":") {
        $inPath = ($_.ingredient -split ":")[1]
    } else {
        $inPath = $_.ingredient
    }
    if($_.output -match ":") {
        $outPath = ($_.output -split ":")[1]
    } else {
        $outPath = $_.output
    }

    $name = "$outPath`_from_$inPath`_stonecutting.json"
    New-Item -Path "E:\Downloads\new\recipes\" -Name $name -ItemType File -Force -Value @"
{
  "type": "minecraft:stonecutting",
  "ingredient": {
    "item": "$inFull"
  },
  "result": "$outFull",
  "count": 2
}
"@

}