$names = @("aqua_regia", "bagnun", "banyash", "berliner", "bristle_mead", "chauve_sourin_au_vin", "chauve_souris_au_vin", "chrysocolla", "crawfish", "crawfish_cocktail", "cream_pastry", "faded_koi", "fishcake", "foxmeat", "golden_bristle_tea", "hare_roast", "imbrifer_cookbook", "imperial_cookbook", "junket", "koi_fish", "meatloaf", "meatloaf_sandwich", "mellow_shallot_soup", "melochites_cookbook_vol_1", "melochites_cookbook_vol_2", "morchella", "nectered_viognier", "peaches_flambe", "peach_cream", "peach_jam", "rabbit_cream_pie", "sedatives", "slushslide", "surstromming")

Function Generate-ItemFiles {

    Param (
        [Parameter()]
        [String[]]  $ItemNames,

        [Parameter()]
        [String] $Destination = "E:\Downloads\new\"
    )

    Begin {
        ####################################
        #region    FUNCTIONS               #
        ####################################


        ####################################
        #region    CODE                    #
        ####################################


        function Get-ItemObjects([string[]] $Names) {
            $Names | Foreach-Object {
                $o = $_.toUpper()
                Write-Output "public static final Item $o = new Item(Tab.XXX.settings());"
            }
        }

        function Get-RegisterItems([string[]] $Names) {
            $Names | Foreach-Object {
                $o = $_.toUpper()
                Write-Output "register(`"$_`", $o, DyeColor.XXX);"
            }
        }

        function Get-LangEntries([string[]] $Names) {
            $Names | Foreach-Object {
                $words = $_ -split "_"
                $resultingWords = @()
                foreach($word in $words) {
                  $resultingWords += ([string] $word[0]).ToUpper() + $word.Substring(1)
                }
                $translation = $resultingWords -join " "
                Write-Output "`"item.spectrum.$_`": `"$translation`","
            }
        }



        ####################################
        #endregion CODE                    #
        ####################################

        ####################################
        #region    ITEM MODEL              #
        ####################################

        function Get-ItemModel($Name) {
            Write-Output @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "spectrum:item/$Name"
  }
}
"@
        }

        ####################################
        #endregion ITEM MODEL              #
        ####################################


        ####################################
        #endregion FUNCTIONS               #
        ####################################
    }

    Process {
        Get-ItemObjects -Name $ItemNames
        Get-RegisterItems -Name $ItemNames
        Get-LangEntries -Names $ItemNames

        $ItemNames | ForEach-Object {
            # ITEM MODEL
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\item\") -Name "$_`.json" -ItemType File -Force -Value $(Get-ItemModel -Name $_) -ErrorAction SilentlyContinue | Out-Null
        }
        
        Write-Output ""
        Write-Output "Don't Forget:"
        Write-Output "- Item tags"
        Write-Output "- Guidebook Entries"
        Write-Output "- Recipes"
    }

    End {
        
    }
}


Generate-ItemFiles -ItemNames $names