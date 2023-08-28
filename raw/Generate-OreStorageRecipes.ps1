$c = $c = @("coal","iron","gold","diamond","emerald","redstone","lapis","copper","quartz","netherite","glowstone","prismarine","certus_quartz","fluix","globette","globette_nether","globette_end")


$c | Foreach-Object {
New-Item -Name "pure_$_`_block`.json" -ItemType File -Value @"
{
  "type": "minecraft:crafting_shaped",
  "group": "native_ore_storage",
  "pattern": [
    "GGG",
    "GGG",
    "GGG"
  ],
  "key": {
    "G": {
      "item": "spectrum:native_$_"
    }
  },
  "result": {
    "item": "spectrum:pure_$_`_block",
    "count": 1
  }
}
"@

New-Item -Name "pure_$_`_from_pure_$_`_block`.json" -ItemType File -Value @"
{
  "type": "minecraft:crafting_shapeless",
  "group": "native_ore_storage",
  "ingredients": [
    {
      "item": "spectrum:pure_$_`_block"
    }
  ],
  "result": {
    "item": "spectrum:pure_$_",
    "count": 9
  }
}
"@

}