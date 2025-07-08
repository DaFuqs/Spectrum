@("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow") | ForEach-Object {

    New-Item -Name "from_$_`_pigment.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:color_picker",
  "time": 10,
  "ingredient": {
    "item": "spectrum:$_`_pigment"
  },
  "color": "$_",
  "amount": 100,
  "required_advancement": "spectrum:hidden/collect_pigment/$_"
}
"@

    New-Item -Name "from_$_`_petal.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:color_picker",
  "time": 20,
  "ingredient": {
    "item": "botania:$_`_petal"
  },
  "color": "$_",
  "amount": 25,
  "required_advancement": "spectrum:hidden/collect_pigment/$_",
  "neoforge:conditions": [
    {
      "type": "neoforge:mod_loaded",
      "modid": "botania"
    }
  ]
}
"@

    New-Item -Name "from_$_`_dye.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:color_picker",
  "time": 20,
  "ingredient": {
    "item": "minecraft:$_`_dye"
  },
  "color": "$_",
  "amount": 5,
  "required_advancement": "spectrum:hidden/collect_pigment/$_"
}
"@

}
