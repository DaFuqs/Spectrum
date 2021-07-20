$gems = @("amethyst", "citrine", "topaz", "onyx", "moonstone")
$materials = @("basalt", "calcite")

foreach($gem in $gems) {
    foreach($material in $materials) {

        New-Item -Name "$gem`_chiseled_$material`_from_cluster.json" -ItemType File -Value @"
{
  "type": "pigment_altar",
  "time": 80,
  "tier": 1,
  "magenta": 0,
  "yellow": 0,
  "cyan": 0,
  "black": 0,
  "white": 0,
  "experience": 1.0,
  "pattern": [
    "WWW",
    "WXW",
    "WWW"
  ],
  "key": {
    "W": {
	  "item": "pigment:polished_$material"
    },
    "X": {
	  "item": "minecraft:$gem`_cluster"
    }
  },
  "result": {
    "item": "pigment:$gem`_chiseled_$material",
    "count": 4
  }
}
"@

        New-Item -Name "$gem`_chiseled_$material`_from_shards.json" -ItemType File -Value @"
{
  "type": "pigment_altar",
  "time": 80,
  "tier": 1,
  "magenta": 0,
  "yellow": 0,
  "cyan": 0,
  "black": 0,
  "white": 0,
  "experience": 1.0,
  "pattern": [
    "WXW",
    "XXX",
    "WXW"
  ],
  "key": {
    "W": {
	  "item": "pigment:polished_$material"
    },
    "X": {
	  "item": "minecraft:$gem`_shard"
    }
  },
  "result": {
    "item": "pigment:$gem`_chiseled_$material",
    "count": 1
  }
}
"@
    }
}