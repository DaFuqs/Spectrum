$p = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$p | Foreach-Object {
    New-Item -Name "$_`_leaves.json" -ItemType File -Force -Value @"
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
              "name": "spectrum:$_`_leaves",
              "conditions": [
                {
                  "condition": "minecraft:alternative",
                  "terms": [
                    {
                      "condition": "minecraft:match_tool",
                      "predicate": {
                        "items": [
                          "minecraft:shears"
                        ]
                      }
                    },
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
            },
            {
              "type": "minecraft:item",
              "name": "spectrum:$_`_sapling",
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                },
                {
                  "condition": "minecraft:table_bonus",
                  "enchantment": "minecraft:fortune",
                  "chances": [
                    0.05,
                    0.0625,
                    0.083333336,
                    0.1
                  ]
                },
                {
                  "condition": "minecraft:table_bonus",
                  "enchantment": "spectrum:resonance",
                  "chances": [
                    0,
                    0.8
                  ]
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:$_`_pigment",
          "conditions": [
            {
              "condition": "minecraft:survives_explosion"
            },
            {
              "condition": "minecraft:table_bonus",
              "enchantment": "minecraft:fortune",
              "chances": [
                0.2,
                0.25,
                0.3,
                0.35,
                0.4
              ]
            }
          ]
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:inverted",
          "term": {
            "condition": "minecraft:alternative",
            "terms": [
              {
                "condition": "minecraft:match_tool",
                "predicate": {
                  "items": [
                    "minecraft:shears"
                  ]
                }
              },
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
        }
      ]
    }
  ]
}
"@

}
