$c = $c = @("coal","iron","gold","diamond","emerald","redstone","lapis","copper","quartz","netherite","glowstone","prismarine","certus_quartz","fluix","globette","globette_nether","globette_end")


$c | Foreach-Object {
New-Item -Name "native_$_`_storage_block`.json" -ItemType File -Value @"
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
    "item": "spectrum:native_$_`_block",
    "count": 1
  }
}
"@

New-Item -Name "native_$_`_from_$_`_storage_block`.json" -ItemType File -Value @"
{
  "type": "minecraft:crafting_shapeless",
  "group": "native_ore_storage",
  "ingredients": [
    {
      "item": "spectrum:native_$_`_block"
    }
  ],
  "result": {
    "item": "spectrum:native_$_",
    "count": 9
  }
}
"@

}