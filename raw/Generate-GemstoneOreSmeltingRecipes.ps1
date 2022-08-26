$gems = @("sparklestone", "azurite", "paletur", "stratine")

foreach($gem in $gems) {
    New-Item -Name "$gem`_shard_from_$gem`_ore_smelting.json" -ItemType File -Value @"
{
    "type": "minecraft:smelting",
    "ingredient": {
        "item": "spectrum:$gem`_ore"
    },
    "result": "spectrum:$gem`_shard",
    "experience": 0.1,
    "cookingtime": 200
}
"@
    New-Item -Name "$gem`_shard_from_deepslate_$gem`_ore_blasting.json" -ItemType File -Value @"
{
    "type": "minecraft:blasting",
    "ingredient": {
        "item": "spectrum:$gem`_ore"
    },
    "result": "spectrum:$gem`_shard",
    "experience": 0.1,
    "cookingtime": 100
}
"@
}