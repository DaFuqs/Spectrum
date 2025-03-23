$c = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$c | Foreach-Object {
@"
    {
      "type": "spectrum:pedestal_crafting",
      "recipe_id": "spectrum:pedestal/tier2/colored_spore_blossoms/$_"
    },
"@

}