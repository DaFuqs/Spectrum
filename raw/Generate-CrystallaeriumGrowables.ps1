$entries = @{
    "coal" = "minecraft:coal"
    "copper" = "minecraft:copper_ingot"
    "diamond" = "minecraft:diamond"
    "emerald" = "minecraft:emerald"
    "glowstone" = "minecraft:glowstone"
    "gold" = "minecraft:gold_ingot"
    "iron" = "minecraft:iron_ingot"
    "lapis" = "minecraft:lapis_lazuli"
    "netherite" = "minecraft:netherite_ingot"
    "prismarine" = "minecraft:prismarine_crystals"
    "quartz" = "minecraft:quartz"
    "redstone" = "minecraft:redstone"
    "certus_quartz" = "ae2:certus_quartz_crystal"
    "fluix" = "ae2:fluix_crystal"
    "globette" = "gobber2:gobber2_globette"
    "globette_nether" = "gobber2:gobber2_globette_nether"
    "globette_end" = "gobber2:gobber2_globette_end"
}


foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    $res = $entry.Value

    New-Item -Path ".\loot\$name`_cluster.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:native_$name",
          "functions": [
            {
              "function": "minecraft:set_count",
              "count": {
                "min": 5,
                "max": 7
              },
              "add": false
            }
          ]
        }
      ]
    }
  ]
}

"@


    New-Item -Path ".\anvil_crushing\$name`_from_buds.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:anvil_crushing",
  "ingredient": [
    {
      "item": "spectrum:small_$name`_bud"
    },
    {
      "item": "spectrum:medium_$name`_bud"
    },
    {
      "item": "spectrum:large_$name`_bud"
    }
  ],
  "crushedItemsPerPointOfDamage": 1.0,
  "experience": 2.0,
  "result": {
    "item": "$res",
    "count": 2
  },
  "particleEffectIdentifier": "explosion",
  "soundEventIdentifier": "block.amethyst_cluster.break"
}
"@

    New-Item -Path ".\anvil_crushing\$name`_from_cluster.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:anvil_crushing",
  "ingredient": {
    "item": "spectrum:$name`_cluster"
  },
  "crushedItemsPerPointOfDamage": 1.0,
  "experience": 3.0,
  "result": {
    "item": "$res",
    "count": 6
  },
  "particleEffectIdentifier": "explosion",
  "soundEventIdentifier": "block.amethyst_cluster.break"
}
"@

    New-Item -Path ".\item_models\small_$name`_bud.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/small_amethyst_bud",
  "textures": {
    "layer0": "spectrum:block/small_$name`_bud"
  }
}
"@

    New-Item -Path ".\item_models\medium_$name`_bud.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/medium_amethyst_bud",
  "textures": {
    "layer0": "spectrum:block/medium_$name`_bud"
  }
}
"@

    New-Item -Path ".\item_models\large_$name`_bud.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/large_amethyst_bud",
  "textures": {
    "layer0": "spectrum:block/large_$name`_bud"
  }
}
"@


    New-Item -Path ".\item_models\$name`_cluster.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/amethyst_cluster",
  "textures": {
    "layer0": "spectrum:block/$name`_cluster"
  }
}
"@

    New-Item -Path ".\item_models\native_$name`.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "spectrum:item/native_$name"
  }
}
"@




    New-Item -Path ".\blasting_recipes\native_$name`.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:blasting",
  "ingredient": {
    "item": "spectrum:native_$name"
  },
  "result": "$res",
  "experience": 0.5,
  "cookingtime": 100
}
"@

    New-Item -Path ".\fireblaze_recipes\native_$name`.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:fireblaze",
  "ingredient": {
    "item": "spectrum:native_$name"
  },
  "results": [
    {
      "item": "$res",
      "count": 2.0
    }
  ],
  "experience": 0.5,
  "time": 200
}
"@



}


foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "public static final Item $("NATIVE_" + $name.toUpper()) = new Item(resourcesItemSettings);"
}

foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "register(`"native_$name`", $("NATIVE_"+ $name.toUpper()), DyeColor.XXX);"
}

foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "`"item.spectrum.native_$name`": `"Native $name`","
}