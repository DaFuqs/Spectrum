$entries = @{
    "coal" = "minecraft:coal"
    "copper" = "minecraft:copper_ingot"
    "diamond" = "minecraft:diamond"
    "emerald" = "minecraft:emerald"
    "glowstone" = "minecraft:glowstone_dust"
    "gold" = "minecraft:gold_ingot"
    "iron" = "minecraft:iron_ingot"
    "lapis" = "minecraft:lapis_lazuli"
    "netherite_scrap" = "minecraft:netherite_scrap"
    "prismarine" = "minecraft:prismarine_crystals"
    "quartz" = "minecraft:quartz"
    "redstone" = "minecraft:redstone"
    "certus_quartz" = "ae2:certus_quartz_dust"
    "fluix" = "ae2:fluix_dust"
    "globette" = "gobber2:gobber2_globette"
    "globette_nether" = "gobber2:gobber2_globette_nether"
    "globette_end" = "gobber2:gobber2_globette_end"
}


foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    $res = $entry.Value

    New-Item -Path ".\loot\small_$name`_bud.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:small_$name`_bud"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:match_tool",
          "predicate": {
            "enchantments": [
              {
                "enchantment": "minecraft:silk_touch",
                "levels": {
                  "min": 1
                }
              }
            ]
          }
        }
      ]
    }
  ]
}
"@

    New-Item -Path ".\loot\large_$name`_bud.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:large_$name`_bud"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:match_tool",
          "predicate": {
            "enchantments": [
              {
                "enchantment": "minecraft:silk_touch",
                "levels": {
                  "min": 1
                }
              }
            ]
          }
        }
      ]
    }
  ]
}
"@

    New-Item -Path ".\loot\$name`_cluster.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:alternatives",
          "children": [
            {
              "type": "minecraft:item",
              "name": "spectrum:$name`_cluster",
              "conditions": [
                {
                  "condition": "minecraft:match_tool",
                  "predicate": {
                    "enchantments": [
                      {
                        "enchantment": "minecraft:silk_touch",
                        "levels": {
                          "min": 1
                        }
                      }
                    ]
                  }
                }
              ]
            },
            {
              "type": "minecraft:alternatives",
              "children": [
                {
                  "type": "minecraft:item",
                  "name": "spectrum:pure_$name",
                  "functions": [
                    {
                      "function": "minecraft:set_count",
                      "count": {
                        "min": 3,
                        "max": 5
                      },
                      "add": false
                    }
                  ],
                  "conditions": [
                    {
                      "condition": "minecraft:match_tool",
                      "predicate": {
                        "tag": "minecraft:cluster_max_harvestables"
                      }
                    }
                  ]
                },
                {
                  "type": "minecraft:item",
                  "name": "spectrum:pure_$name",
                  "functions": [
                    {
                      "function": "minecraft:set_count",
                      "count": {
                        "min": 1,
                        "max": 2
                      },
                      "add": false
                    },
                    {
                      "function": "minecraft:explosion_decay"
                    }
                  ]
                }
              ]
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
  "soundEventIdentifier": "block.amethyst_cluster.break",
  "required_advancement": "spectrum:lategame/collect_pure_resource"
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
  "soundEventIdentifier": "block.amethyst_cluster.break",
  "required_advancement": "spectrum:lategame/collect_pure_resource"
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

    New-Item -Path ".\item_models\pure_$name`.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "spectrum:item/pure_$name"
  }
}
"@

    New-Item -Path ".\blasting_recipes\pure_$name`.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:blasting",
  "ingredient": {
    "item": "spectrum:pure_$name"
  },
  "result": "$res",
  "experience": 0.5,
  "cookingtime": 100
}
"@

    New-Item -Path ".\cinderhearth_recipes\pure_$name`.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:cinderhearth",
  "ingredient": {
    "item": "spectrum:pure_$name"
  },
  "results": [
    {
      "item": "$res",
      "count": 2.0
    }
  ],
  "experience": 0.5,
  "time": 200,
  "required_advancement": "spectrum:lategame/collect_pure_resource"
}
"@

    New-Item -Path ".\alloy_forgery_recipes\pure_$name`.json" -ItemType File -Force -Value @"
{
  "type": "alloy_forgery:forging",
  "inputs": [
    {
      "item": "spectrum:pure_$name",
      "count": 2
    }
  ],
  "output": {
    "id": "$res",
    "count": 4
  },
  "overrides": {
    "4+": {
      "id": "$res",
      "count": 5
    }
  },
  "min_forge_tier": 1,
  "fuel_per_tick": 10,
  "fabric:load_conditions": [
    {
      "condition": "fabric:all_mods_loaded",
      "values": [
        "alloy_forgery"
      ]
    }
  ]
}
"@

}


foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "public static final Item $("pure_" + $name.toUpper()) = new Item(resourcesItemSettings);"
}

foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "register(`"pure_$name`", $("pure_"+ $name.toUpper()), DyeColor.XXX);"
}

foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Key
    "`"item.spectrum.pure_$name`": `"Pure $name`","
}