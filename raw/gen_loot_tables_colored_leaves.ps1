$p = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$p | Foreach-Object {
    New-Item -Name "$_`_leaves.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:alternatives",
          "children": [
            {
              "type": "minecraft:item",
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
              ],
              "name": "spectrum:$_`_leaves"
            },
            {
              "type": "minecraft:item",
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                },
                {
                  "condition": "minecraft:table_bonus",
                  "enchantment": "minecraft:fortune",
                  "chances": [
                    0.005,
                    0.00625,
                    0.0075,
                    0.008
                  ]
                }
              ],
              "name": "spectrum:$_`_sapling"
            }
          ]
        }
      ]
    },
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "conditions": [
            {
              "condition": "minecraft:table_bonus",
              "enchantment": "minecraft:fortune",
              "chances": [
                0.1,
                0.14,
                0.18,
                0.22,
                0.25
              ]
            }
          ],
          "functions": [
            {
              "function": "minecraft:set_count",
              "count": {
                "type": "minecraft:uniform",
                "min": 1.0,
                "max": 2.0
              },
              "add": false
            },
            {
              "function": "minecraft:explosion_decay"
            }
          ],
          "name": "spectrum:$_`_pigment"
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
