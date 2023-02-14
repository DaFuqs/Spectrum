$en18 = Get-Content -Path "C:\Temp\Temp\l_18_en.json" -Encoding UTF8 | ConvertFrom-Json
$endd = Get-Content -Path "C:\Temp\Temp\l_dd_en.json" -Encoding UTF8 | ConvertFrom-Json

$identical = [System.Collections.ArrayList]::new()
$changed = [System.Collections.ArrayList]::new()
$added = [System.Collections.ArrayList]::new()
$removed = [System.Collections.ArrayList]::new()

foreach($element in $endd.PSObject.Members.Name) {
    $edd = $endd.$element
    $e18 = $en18.$element

    if($element -match "Amaranth") {
        "!"
    }
    if($e18) {
        if($e18 -eq $edd) {
            $identical.Add($element) | Out-Null
        } else {
            $changed.Add($element) | Out-Null
        }
    } else {
        $added.Add($element) | Out-Null
    }
}

foreach($element in $en18.PSObject.Members.Name) {
    if(-not $endd.$element) {
        $removed.Add($element) | Out-Null
    }
}


$ench = Get-Content -Path "C:\Temp\Temp\l_ch.json" -Encoding UTF8 | ConvertFrom-Json
$new = "{`r`n"
foreach($element in $endd.PSObject.Members) {
    if($element.TypeNameOfValue -ne "System.String") {
        continue
    }
    $element = $element.Name
    if($element -match "Amaranth") {
        "!"
    }
    
    $edd = $endd.$element
    $e18 = $en18.$element
    $ech = $ench.$element
    if($changed.Contains($element)) {
        $new += "  `"$element`": `"$ech`", # changed from `"$($e18)`" to `"$($edd)`"`r`n"
    } elseif($removed.Contains($element)) {
        $new += "# `"$element`": `"$ech`", # deleted. Was `"$($ech)`"`r`n"
    } elseif($added.Contains($element) -or -not $ech) {
        $new += "  `"$element`": `"$edd`", # new`r`n"
    } else {
        $new += "  `"$element`": `"$ech`",`r`n"
    }
}
$new += "}"
$new | Set-Clipboard

Write-Host "Deleted:"
foreach($d in $removed) {
    $ech = $ench.$d
    Write-Host "$d : $ech"
}

