$TextInfo = (Get-Culture).TextInfo


Get-ChildItem -Path "I:\FabricModding\Spectrum\src\main\resources\data\spectrum\recipes\enchanter\vanilla_books" | ForEach-Object {
    $j = Get-Content $_.FullName | ConvertFrom-Json
    $adv = $j.result.nbt.Substring(28, $j.result.nbt.Length - 41)
    $adv2 = $adv -replace "minecraft:", ""
    $adv3 = $adv2 -replace "_", " "
    $name = $TextInfo.ToTitleCase($adv3)
    $req = $j.required_advancement

@"
        {
            "type": "spectrum:enchanter_crafting",
            "title": "$name",
            "advancement": "$req",
            "recipe": "spectrum:enchanter/vanilla_books/book_$adv2",
            "text": ""
        },
"@

}