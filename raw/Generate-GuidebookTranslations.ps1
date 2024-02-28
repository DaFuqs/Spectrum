$RecipeTypes =
    "spectrum:anvil_crushing",
    "spectrum:pedestal_crafting",
    "spectrum:fusion_shrine_crafting",
    "spectrum:enchanter_crafting",
    "spectrum:enchanter_upgrading",
    "spectrum:potion_workshop_brewing",
    "spectrum:potion_workshop_crafting",
    "spectrum:spirit_instiller_crafting",
    "spectrum:liquid_crystal_converting",
    "spectrum:midnight_solution_converting",
    "spectrum:dragonrot_converting",
    "spectrum:mud_converting",
    "spectrum:crystallarieum_growing",
    "spectrum:cinderhearth_smelting",
    "spectrum:titration_barrel_fermenting",
    "modonomicon:crafting_recipe",
    "modonomicon:smoking_recipe",
    "modonomicon:smelting_recipe",
    "modonomicon:blasting_recipe",
    "modonomicon:campfire_cooking_recipe",
    "modonomicon:stonecutting_recipe",
    "modonomicon:smithing_recipe"

$LangPath = "src/main/resources/assets/spectrum/lang/en_us.json"
$GuidebookPath = "src/main/resources/data/spectrum/modonomicon/books/guidebook/entries"

Function Set-Translation {
    Param (
        [Object] $Lang,
        [Object] $Output,
        [Object] $Parent,
        [String] $Prefix,
        [String] $Name
    )

    If ($Parent."$Name") {
        If ($null -eq $Lang."$Prefix.$Name") {
            $Output | Add-Member -NotePropertyName "$Prefix.$Name" -NotePropertyValue $Parent."$Name"
        }
        $Parent."$Name" = "$Prefix.$Name"
    }
    ElseIf ($null -ne $Parent."$Name") {
        $Parent.PSObject.Properties.Remove("$Name")
    }
}

Function Set-CustomTranslation {
    Param (
        [Object] $Lang,
        [Object] $Output,
        [Object] $Parent,
        [String] $Translation,
        [String] $PropertyName
    )

    If ($Parent."$PropertyName") {
        If ($null -eq $Lang."$Translation") {
            $Output | Add-Member -NotePropertyName "$Translation" -NotePropertyValue $Parent."$PropertyName"
        }
        $Parent."$PropertyName" = "$Translation"
    }
    ElseIf ($null -ne $Parent."$PropertyName") {
        $Parent.PSObject.Properties.Remove("$PropertyName")
    }
}

Function Set-RenamedProperty {
    Param (
        [Object] $Parent,
        [String] $OldName,
        [String] $NewName
    )

    If ($null -eq $Parent."$NewName" -and $Parent."$OldName") {
        $Parent | Add-Member -NotePropertyName $NewName -NotePropertyValue $Parent."$OldName"
    }
    If ($null -ne $Parent."$OldName") {
        $Parent.PSObject.Properties.Remove("$OldName")
    }
}

Function Set-TranslatedGuidebookEntry {
    Param (
        [String] $CategoryName,
        [String] $EntryName,
        [Object] $Lang,
        [int] $Index = -1
    )

    Write-Output "Processing $CategoryName/$EntryName..."

    $Output = '{}' | ConvertFrom-Json

    $RawLang = Get-Content -Raw $LangPath
    if ($null -eq $Lang) {
        $Lang = $RawLang | ConvertFrom-Json
    }

    $ContentPath = "$GuidebookPath/$CategoryName/$EntryName.json"
    $Content = Get-Content -Raw $ContentPath | ConvertFrom-Json
    $Prefix = "book.spectrum.guidebook.$CategoryName.$EntryName"

    Set-Translation $Lang $Output $Content $Prefix 'name'
    Set-Translation $Lang $Output $Content $Prefix 'description'

    If ($Index -ge 0) {
        $Content.x = ($index % 6) * 2
        $Content.y = [int][Math]::Floor($index / 6) * 2
    }

    For ($I = 0; $I -lt $Content.pages.length; $I++) {
        $Page = $Content.pages[$I]
        $PagePrefix = "$Prefix.page$I"

        If ($RecipeTypes -contains $Page.type) {
            Set-RenamedProperty $Page 'title' 'title1'
            Set-RenamedProperty $Page 'recipe' 'recipe_id_1'
            Set-RenamedProperty $Page 'recipe2' 'recipe_id_2'
            Set-Translation $Lang $Output $Page $PagePrefix 'title1'
            Set-Translation $Lang $Output $Page $PagePrefix 'title2'
        }
        ElseIf ($Page.type -eq "modonomicon:entity") {
            Set-Translation $Lang $Output $Page $PagePrefix 'name'
        }
        ElseIf ($Page.type -eq "modonomicon:multiblock") {
            Set-RenamedProperty $Page 'name' 'multiblock_name'
            Set-Translation $Lang $Output $Page $PagePrefix 'multiblock_name'
        }
        Else {
            Set-Translation $Lang $Output $Page $PagePrefix 'title'
        }

        If ($Page.type -eq "spectrum:checklist") {
            $J = 0
            $Page.checklist.PSObject.Properties | ForEach-Object {
                Set-CustomTranslation $Lang $Output $Page.checklist "$PagePrefix.checklist.entry$J" $_.Name
                $J++
            }
        }

        Set-Translation $Lang $Output $Page $PagePrefix 'text'
    }

    $JsonContent = $Content | ConvertTo-Json -Depth 100
    $JsonContent | Out-File $ContentPath

    $JsonLang = $Output | ConvertTo-Json -Depth 100
    $JsonLang = $JsonLang.Replace("\\", "\\\n").Replace("\u0027", "'")
    If ($JsonLang.length -ge 3) {
        $StrippedRawLang = $RawLang.Substring(2)
        $StrippedNewLang = $JsonLang.Substring(0, $JsonLang.length - 3)
        $LangWithOutput = $StrippedNewLang + "," + $StrippedRawLang
        $LangWithOutput | Out-File $LangPath
    }
}

Function Set-TranslatedGuidebookCategory {
    Param (
        [String] $CategoryName,
        [Object] $Lang,
        [Boolean] $SetLayout = $false
    )

    Write-Output "`nBeginning $CategoryName..."

    if ($null -eq $Lang) {
        $Lang = Get-Content -Raw $LangPath | ConvertFrom-Json
    }

    $CategoryPath = "$GuidebookPath/$CategoryName"
    $Files = Get-ChildItem $CategoryPath -Filter *.json

    # Reverse order so the lang ends up correctly sorted
    For ($I = $Files.length - 1; $I -ge 0; $I--) {
        $File = $Files[$I]
        $EntryName = $File.Name -replace '(.*)\.json', '$1'
        If ($SetLayout) { $Index = $I }
        Else { $Index = -1 }
        Set-TranslatedGuidebookEntry $CategoryName $EntryName $Lang $Index
    }
}

Function Set-TranslatedGuidebook {
    Param (
        [Boolean] $SetLayout = $false
    )

    Write-Output "Beginning Guidebook..."

    $Lang = Get-Content -Raw $LangPath | ConvertFrom-Json
    $Dirs = Get-ChildItem $GuidebookPath

    # Reverse order so the lang ends up correctly sorted
    For ($I = $Dirs.length - 1; $I -ge 0; $I--) {
        $Dir = $Dirs[$I]
        $CategoryName = $Dir.Name
        Set-TranslatedGuidebookCategory $CategoryName $Lang $SetLayout
    }

    Write-Output "`nProcessing Complete!`n"
}

Set-TranslatedGuidebook
#Set-TranslatedGuidebookCategory 'decoration'
#Set-TranslatedGuidebookEntry 'equipment' 'gemstone_armor'
