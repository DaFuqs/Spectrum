$dataFolder = "$PSScriptRoot\..\src\main\resources\data"
$advancementsFolder = $(Join-Path -Path $dataFolder -ChildPath "spectrum\advancements" -Resolve)
$advancements = ((Get-ChildItem -Path $advancementsFolder -File -Recurse).FullName -replace ($advancementsFolder -replace "\\", "\\"), "" -replace ".json", "" -replace "\\", "/").Substring(1).Trim()

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
            if($advancements -notcontains $b) {
                Write-Warning "$_ : $b"
            }
        }
    }
}