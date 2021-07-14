$p = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$p | Foreach-Object {
    New-Item -Name "$_`_leaves.json" -ItemType File -Value
@"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1.0,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:alternatives",
          "children": [
            {
              "type": "minecraft:item",
              "name": "pigment:$_`_leaves"
              "conditions": [
                {
                  "condition": "minecraft:alternative",
                  "terms": [
                    {
                      "condition": "minecraft:match_tool",
                      "predicate": {
                        "item": "minecraft:shears"
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
              "name": "pigment:$_`_sapling"
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
              ]
            }
          ]
        }
      ]
    },
    {
      "rolls": 1.0,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "minecraft:$_`_dye",
          "conditions": [
            {
              "condition": "minecraft:table_bonus",
              "enchantment": "minecraft:fortune",
              "chances": [
                0.06,
                0.07,
                0.08,
                0.09,
                0.1
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
                  "item": "minecraft:shears"
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
