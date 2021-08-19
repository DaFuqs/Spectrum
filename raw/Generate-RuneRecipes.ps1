$gems = @("amethyst", "citrine", "topaz", "onyx", "moonstone")
$materials = @("basalt", "calcite")

foreach($gem in $gems) {
    foreach($material in $materials) {

        New-Item -Name "$gem`_chiseled_$material`_from_cluster.json" -ItemType File -Value @"
{
  "type": "spectrum_pedestal",
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
	  "item": "spectrum:polished_$material"
    },
    "X": {
	  "item": "minecraft:$gem`_cluster"
    }
  },
  "result": {
    "item": "spectrum:$gem`_chiseled_$material",
    "count": 4
  }
}
"@

        New-Item -Name "$gem`_chiseled_$material`_from_shards.json" -ItemType File -Value @"
{
  "type": "spectrum_pedestal",
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
	  "item": "spectrum:polished_$material"
    },
    "X": {
	  "item": "minecraft:$gem`_shard"
    }
  },
  "result": {
    "item": "spectrum:$gem`_chiseled_$material",
    "count": 1
  }
}
"@
    }
}