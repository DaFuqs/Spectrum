$RecipeTypes =
    'spectrum:anvil_crushing',
    'spectrum:pedestal_crafting',
    'spectrum:fusion_shrine_crafting',
    'spectrum:enchanter_crafting',
    'spectrum:enchanter_upgrading',
    'spectrum:potion_workshop_brewing',
    'spectrum:potion_workshop_crafting',
    'spectrum:spirit_instiller_crafting',
    'spectrum:liquid_crystal_converting',
    'spectrum:midnight_solution_converting',
    'spectrum:dragonrot_converting',
    'spectrum:mud_converting',
    'spectrum:crystallarieum_growing',
    'spectrum:cinderhearth_smelting',
    'spectrum:titration_barrel_fermenting',
    'patchouli:crafting',
    'patchouli:smoking',
    'patchouli:smelting',
    'patchouli:blasting',
    'patchouli:campfire_cooking',
    'patchouli:stonecutting',
    'patchouli:smithing'

$LangCode = 'zh_cn'
$LangPath = "src/main/resources/assets/spectrum/lang/$LangCode.json"
$PatchouliPath = "src/main/resources/assets/spectrum/patchouli_books/guidebook/$LangCode"
$PatchouliBookPath = "src/main/resources/data/spectrum/patchouli_books/guidebook"
$ModonomiconPath = 'src/main/resources/data/spectrum/modonomicon/books/guidebook'
$AdvancementPath = 'src/main/resources/data/spectrum/advancements'
$LangPrefix = 'book.spectrum.guidebook'

Function SetProperty {
    Param (
        [Object] $To,
        [String] $Name,
        $Value
    )

    If ($null -ne $Value -and $Value -ne '') {
        If ($null -eq $To."$Name") {
            $To | Add-Member -NotePropertyName $Name -NotePropertyValue $Value
        }
        Else {
            $To."$Name" = $Value
        }
    }
}

Function CopyProperty {
    Param (
        [Object] $From,
        [Object] $To,
        [String] $FromName,
        [String] $ToName = $FromName
    )

    If ($null -ne $From."$FromName") {
        SetProperty $To $ToName $From."$FromName"
    }
}

Function TranslateProperty {
    Param (
        [Object] $Lang,
        [Object] $To,
        [String] $Prefix,
        [String] $Name,
        [String] $Value
    )

    If ($null -ne $Value) {
        SetProperty $Lang "$Prefix.$Name" $Value
        SetProperty $To $Name "$Prefix.$Name"
    }
}

Function SetTranslatedGuidebookEntry {
    Param (
        [Object] $Lang,
        [String] $CategoryName,
        [String] $EntryName,
        [int] $Index = -1
    )

    Write-Output "Processing $CategoryName/$EntryName..."
    $Prefix = "$LangPrefix.$CategoryName.$EntryName"
    $PathSuffix = "entries/$CategoryName/$EntryName.json"
    $Entry = Get-Content "$PatchouliPath/$PathSuffix" -Raw | ConvertFrom-Json

    SetProperty $Lang "$Prefix.name" $Entry.name

    If ($CategoryName -eq 'general' -and $EntryName -eq 'intro') {
        SetProperty $Lang "$Prefix.description" 'Getting Started'
    }

    For ($I = 0; $I -lt $Entry.pages.length; $I++) {
        $Page = $Entry.pages[$I]
        $PagePrefix = "$Prefix.page$I"

        If ($I -eq 0 -and (-not ($RecipeTypes -contains $Page.type)) -and $null -eq $Page.title) {
            If (Test-Path "$ModonomiconPath/$PathSuffix" -PathType Leaf) {
                $ModonomiconEntry = Get-Content -Raw "$ModonomiconPath/$PathSuffix" | ConvertFrom-Json
                SetProperty $Lang "$PagePrefix.title" $Entry.name
                SetProperty $ModonomiconEntry.pages[$I] 'title' "$PagePrefix.title"
                $ModonomiconEntry | ConvertTo-Json -Depth 100 | Out-File "$ModonomiconPath/$PathSuffix"
            }
        }

        If ($RecipeTypes -contains $Page.type) {
            SetProperty $Lang "$PagePrefix.title" $Page.title
        }
        Else {
            SetProperty $Lang "$PagePrefix.title" $Page.title
        }
        SetProperty $Lang "$PagePrefix.title2" $Page.title2

        If ($Page.type -eq 'patchouli:multiblock') {
            SetProperty $Lang "$PagePrefix.multiblock_name" $Page.name
        }
        Else {
            SetProperty $Lang "$PagePrefix.name" $Page.name
        }

        If ($Page.type -eq 'spectrum:checklist') {
            $J = 0
            $Page.checklist.PSObject.Properties | ForEach-Object {
                SetProperty $Lang "$PagePrefix.checklist.entry$J" $_.Value
                $J++
            }
        }

        SetProperty $Lang "$PagePrefix.button_text" $Page.button_text
        SetProperty $Lang "$PagePrefix.button_text_confirmed" $Page.button_text_confirmed
        SetProperty $Lang "$PagePrefix.link_text" $Page.link_text
        SetProperty $Lang "$PagePrefix.text" $Page.text
    }
}

Function SetTranslatedGuidebookCategory {
    Param (
        [Object] $Lang,
        [String] $CategoryName,
        [Boolean] $SetLayout = $false
    )

    Write-Output "`nProcessing $CategoryName..."
    $Prefix = "$LangPrefix.$CategoryName"

    $Category = Get-Content -Raw "$PatchouliPath/categories/$CategoryName.json" | ConvertFrom-Json
    SetProperty $Lang "$Prefix.name" $Category.name

    $Entries = Get-ChildItem "$PatchouliPath/entries/$CategoryName"
    For ($I = 0; $I -lt $Entries.length; $I++) {
        $EntryName = $Entries[$I].Name -replace '(.*)\.json', '$1'
        If ($SetLayout) { $Index = $I }
        Else { $Index = -1 }
        SetTranslatedGuidebookEntry $Lang $CategoryName $EntryName $Index
    }
}

Function SetTranslatedGuidebook {
    Param (
        [Boolean] $SetLayout = $false
    )

    Write-Output "`Processing book..."
    $RawLang = Get-Content -Raw $LangPath
    $ModonomiconLang = '{}' | ConvertFrom-Json

    $Categories = Get-ChildItem "$PatchouliPath/categories"
    For ($I = 0; $I -lt $Categories.length; $I++) {
        $CategoryName = $Categories[$I].Name -replace '(.*)\.json', '$1'
        SetTranslatedGuidebookCategory $ModonomiconLang $CategoryName $SetLayout
    }

    # Dummy, to get a comma at the end for the regex
    SetProperty $ModonomiconLang '_' '_'

    $RawModonomiconLang = ($ModonomiconLang | ConvertTo-Json -Depth 100) `
        -replace('\u0027', "'") `
        `
        -replace('\$\(c_black\)', '$(#000000)') `
        -replace('\$\(c_blue\)', '$(#2c2e8e)') `
        -replace('\$\(c_brown\)', '$(#613c20)') `
        -replace('\$\(c_cyan\)', '$(#157687)') `
        -replace('\$\(c_gray\)', '$(#36393d)') `
        -replace('\$\(c_green\)', '$(#495b24)') `
        -replace('\$\(c_light_blue\)', '$(#258ac8)') `
        -replace('\$\(c_light_gray\)', '$(#7d7d73)') `
        -replace('\$\(c_lime\)', '$(#5faa19)') `
        -replace('\$\(c_magenta\)', '$(#aa32a0)') `
        -replace('\$\(c_orange\)', '$(#e16201)') `
        -replace('\$\(c_pink\)', '$(#d6658f)') `
        -replace('\$\(c_purple\)', '$(#641f9c)') `
        -replace('\$\(c_red\)', '$(#8f2121)') `
        -replace('\$\(c_white\)', '$(#d0d6d7)') `
        -replace('\$\(c_yellow\)', '$(#f0af15)') `
        -replace('\$\(br3\)', '$(br2)$(br)') `
        -replace('\$\(br4\)', '$(br2)$(br2)') `
        -replace('\$\(fuck\)', '$(#ff3d71)') `
        `
        -replace('\$\(obf\)', '$(k)') `
        -replace('\$\(bold\)', '$(l)') `
        -replace('\$\(strike\)', '$(m)') `
        -replace('\$\(italic\)', '$(o)') `
        -replace('\$\(italics\)', '$(o)') `
        -replace('\$\(list', '$(li') `
        -replace('\$\(reset\)', '$()') `
        -replace('\$\(clear\)', '$()') `
        -replace('\$\(2br\)', '$(br2)') `
        -replace('\$\(p\)', '$(br2)') `
        -replace('\/\$', '$()') `
        -replace('<br>', '$(br)') `
        -replace('\$\(nocolor\)', '$(#000000)') `
        -replace('\$\(item\)', '$(#bb00bb)') `
        -replace('\$\(thing\)', '$(#449900)') `
        `
        -replace('": "\$\(li\d*\)', '": "- ') `
        -replace('\$\(li\d*\)', '$(br)- ') `
        -replace('\$\(#(\w)(\w)(\w)\)', '$(#$1$1$2$2$3$3)') `
        -replace('\$\(0\)', '$(#000000)') `
        -replace('\$\(1\)', '$(#0000aa)') `
        -replace('\$\(2\)', '$(#00aa00)') `
        -replace('\$\(3\)', '$(#00aaaa)') `
        -replace('\$\(4\)', '$(#aa0000)') `
        -replace('\$\(5\)', '$(#aa00aa)') `
        -replace('\$\(6\)', '$(#ffaa00)') `
        -replace('\$\(7\)', '$(#aaaaaa)') `
        -replace('\$\(8\)', '$(#555555)') `
        -replace('\$\(9\)', '$(#5555ff)') `
        -replace('\$\([aA]\)', '$(#55ff55)') `
        -replace('\$\([bB]\)', '$(#55ffff)') `
        -replace('\$\([cC]\)', '$(#ff5555)') `
        -replace('\$\([dD]\)', '$(#ff55ff)') `
        -replace('\$\([eE]\)', '$(#ffff55)') `
        -replace('\$\([fF]\)', '$(#ffffff)') `
        `
        -replace('\$\(#(\w\w\w\w\w\w)\)', '[#]($1)') `
        -replace('\$\(l:(\w+\/\w+)#(\w+)\)(.*?)(?:(?=\$\(\))|\$\(\/l\)|\$\(\/t\))', '[$3](entry://$1@$2)') `
        -replace('\$\(l:(\w+\/\w+)\)(.*?)(?:(?=\$\(\))|\$\(\/l\)|\$\(\/t\))', '[$2](entry://$1)') `
        -replace('\$\(l:(\w+)\)(.*?)(?:(?=\$\(\))|\$\(\/l\)|\$\(\/t\))', '[$2](category://$1)') `
        -replace('\$\(l:https:\/\/(.*?)\)(.*?)(?:(?=\$\(\))|\$\(\/l\)|\$\(\/t\))', '[$2](https://$1)') `
        -replace('\$\(l\)(.*?[^\\])(?=\$\(\)|",)', '**$1**') `
        -replace('\$\(m\)(.*?[^\\])(?=\$\(\)|",)', '~~$1~~') `
        -replace('\$\(n\)(.*?[^\\])(?=\$\(\)|",)', '++$1++') `
        -replace('\$\(o\)(.*?[^\\])(?=\$\(\)|",)', '*$1*') `
        -replace('\$\(br\)', '\\\n') `
        -replace('\$\(br2\)', '\\\n\\\n') `
        -replace('\$\(\)', '[#]()')

    If ($RawModonomiconLang.length -gt 2) {
        $StrippedLang = $RawLang.Substring(2)
        $StrippedModonomiconLang = $RawModonomiconLang.Substring(0, $RawModonomiconLang.length - 15)
        $OutputLang = $StrippedModonomiconLang + $StrippedLang
        $OutputLang | Out-File $LangPath
    }

    Write-Output "`nProcessing Complete!`n"
}

SetTranslatedGuidebook
