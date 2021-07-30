$gems = @("amethyst", "citrine", "topaz", "onyx", "moonstone")

$gems | ForEach-Object {

    if($_ -eq "amethyst") {
        $ident = "minecraft"
    } else {
        $ident = "spectrum"
    }

    foreach($ore in @("", "deepslate_")) {

    New-Item -Name "$ore$_`_ore.json" -ItemType File -Value @"
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
		      "name": "spectrum:$ore$_`_ore",
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
              "type": "minecraft:item",
			  "name": "$ident`:$_`_shard",
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
                  "function": "minecraft:apply_bonus",
                  "enchantment": "minecraft:fortune",
                  "formula": "minecraft:ore_drops"
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
"@
    }

}