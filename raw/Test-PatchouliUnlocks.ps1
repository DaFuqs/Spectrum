$dataFolder = "$PSScriptRoot\..\src\main\resources\data"
$advancementsFolder = $(Join-Path -Path $dataFolder -ChildPath "spectrum\advancements" -Resolve)
$existingAdvancements = ((Get-ChildItem -Path $advancementsFolder -File -Recurse).FullName -replace ($advancementsFolder -replace "\\", "\\"), "" -replace ".json", "" -replace "\\", "/").Substring(1).Trim()
$recipesFolder = $(Join-Path -Path $dataFolder -ChildPath "spectrum\recipes" -Resolve)
$existingRecipes = ((Get-ChildItem -Path $recipesFolder -File -Recurse).FullName -replace ($recipesFolder -replace "\\", "\\"), "" -replace ".json", "" -replace "\\", "/").Substring(1).Trim()

$guidebookFolder = $(Join-Path -Path $dataFolder -ChildPath "spectrum\patchouli_books\guidebook\en_us\entries" -Resolve)
$bookPages = (Get-ChildItem -Path $guidebookFolder -File -Recurse).FullName
$bookPages | ForEach-Object {
    $json = Get-Content $_ | ConvertFrom-Json

    $adv = @()
    $adv += $json.advancement
    $adv += $json.pages.advancement
    $adv += $json.pages.turnin

    foreach($ad in $adv) {
        if($ad) {
            $a, $b = $ad.Trim().Split(":")
            if($existingAdvancements -notcontains $b) {
                Write-Warning "Missing Unlock: $_ : $b"
            }
        }
    }
    
    
    $recipes = @()
    $recipes += $json.pages.recipe
    $recipes += $json.pages.recipe2
    foreach($recipe in $recipes) {
        if($recipe) {
            $a, $b = $recipe.Trim().Split(":")
            if($existingRecipes -notcontains $b -and $b -notmatch "_level_2") {
                Write-Warning "Missing Recipe: $_ : $b"
            }
        }
    }


}