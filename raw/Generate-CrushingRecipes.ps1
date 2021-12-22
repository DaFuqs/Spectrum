$entries = Import-Csv -Path .\crushing_recipes.csv -Delimiter ";"

$entries | ForEach-Object {

    if($_.OUT -ne "-" -and $_.ACTIVE -eq "yes") {
        $inFull = $_.IN
        if (!($inFull -match "spectrum:")) {
            $inFull = "minecraft:" + $inFull
        }

        $outFull = $_.OUT
        if (!($outFull -match "spectrum:")) {
            $outFull = "minecraft:" + $outFull
        }

        if($_.IN -match ":") {
            $inPath = ($_.IN -split ":")[1]
        } else {
            $inPath = $_.IN
        }
        
        if($_.OUT -match ":") {
            $outPath = ($_.OUT -split ":")[1]
        } else {
            $outPath = $_.OUT
        }

        $name = "$outPath`_from_$inPath`_anvil_crushing.json"
        $p = $(Join-Path -Path . -ChildPath $_.FOLDER)
        New-Item -Path $p -Name $name -ItemType File -Force -Value @"
{
  "type": "spectrum:anvil_crushing",
  "ingredient": {
    "item": "$inFull"
  },
  "crushedItemsPerPointOfDamage": $($_.CRUSHED_PER_DAMAGE),
  "experience": $($_.XP),
  "result" : {
    "item": "$outFull",
    "count": $($_.RESULT_AMOUNT)
  },
  "particleEffectIdentifier": "$($_.PARTICLE)",
  "soundEventIdentifier": "$($_.SOUND)"
}
"@
    }
}