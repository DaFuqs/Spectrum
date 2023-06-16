class Template {

    [string] $ingredient
    [string] $output
    [int] $amount

    Template([string] $ingredient, [string] $output, [int] $amount) {
        $this.ingredient = $ingredient
        $this.output = $output
        $this.amount = $amount
    }

}

$entries = @(
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_pile", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_tile", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_plating", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_tubing", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_relief", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_stack", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_panneling", 1),
    [Template]::new("spectrum:pyrite", "spectrum:pyrite_vent", 1)
)

$entries | ForEach-Object {
    $inFull = $_.ingredient
    if (!($inFull -match "spectrum:")) {
        $inFull = "minecraft:" + $inFull
    }
    $outFull = $_.output
    if (!($outFull -match "spectrum:")) {
        $outFull = "minecraft:" + $outFull
    }

    if($_.ingredient -match ":") {
        $inPath = ($_.ingredient -split ":")[1]
    } else {
        $inPath = $_.ingredient
    }
    if($_.output -match ":") {
        $outPath = ($_.output -split ":")[1]
    } else {
        $outPath = $_.output
    }

    $name = "$outPath`_from_$inPath`_stonecutting.json"
    New-Item -Path "E:\Downloads\new\recipes\" -Name $name -ItemType File -Force -Value @"
{
  "type": "minecraft:stonecutting",
  "ingredient": {
    "item": "$inFull"
  },
  "result": "$outFull",
  "count": $($_.amount)
}
"@

}