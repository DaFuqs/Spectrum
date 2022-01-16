$leaves = @("black_leaves", "blue_leaves", "brown_leaves", "cyan_leaves", "gray_leaves", "green_leaves", "light_blue_leaves", "light_gray_leaves", "lime_leaves", "magenta_leaves", "orange_leaves", "pink_leaves", "purple_leaves", "red_leaves", "white_leaves", "yellow_leaves")
$saplings = @("black_sapling", "blue_sapling", "brown_sapling", "cyan_sapling", "gray_sapling", "green_sapling", "light_blue_sapling", "light_gray_sapling", "lime_sapling", "magenta_sapling", "orange_sapling", "pink_sapling", "purple_sapling", "red_sapling", "white_sapling", "yellow_sapling")
$logs = @("black_log", "blue_log", "brown_log", "cyan_log", "gray_log", "green_log", "light_blue_log", "light_gray_log", "lime_log", "magenta_log", "orange_log", "pink_log", "purple_log", "red_log", "white_log", "yellow_log")
$flat = @("black_glowblock", "blue_glowblock", "brown_glowblock", "cyan_glowblock", "gray_glowblock", "green_glowblock", "light_blue_glowblock", "light_gray_glowblock", "lime_glowblock", "magenta_glowblock", "orange_glowblock", "pink_glowblock", "purple_glowblock", "red_glowblock", "white_glowblock", "yellow_glowblock")
$lamp = @("black_lamp", "blue_lamp", "brown_lamp", "cyan_lamp", "gray_lamp", "green_lamp", "light_blue_lamp", "light_gray_lamp", "lime_lamp", "magenta_lamp", "orange_lamp", "pink_lamp", "purple_lamp", "red_lamp", "white_lamp", "yellow_lamp")
$ores = @("sparklestone_block", "koenigsblau_ore", "koenigsblau_block")
$upgrades = @("upgrade_speed", "upgrade_speed2", "upgrade_speed3", "upgrade_efficiency", "upgrade_efficiency2", "upgrade_yield", "upgrade_yield2", "upgrade_experience", "upgrade_experience2")

$new = @("shooting_star_glistering", "shooting_star_fiery", "shooting_star_colorful", "shooting_star_pristine", "shooting_star_gemstone")

enum BlockType {
    Default
    Lamp
    Log
    Upgrade
}

Function Generate-BlockFiles {

    Param (
        [Parameter()]
        [String[]]  $BlockNames,

        [Parameter()]
        [BlockType] $BlockType,

              
        [Parameter()]
        [int] $AnimationFrameTime = 0, # 0 = not animated; in ticks per frame

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


        function Get-BlockObjects([string[]] $Names) {
            $Names | Foreach-Object {
                $o = $_.toUpper()
                Write-Output "public static final Block $o = new Block(FabricBlockSettings.of(Material.STONE).hardness(4.0f));"
            }
        }

        function Get-RegisterBlockAndItems([string[]] $Names) {
            $Names | Foreach-Object {
                $o = $_.toUpper()
                "registerBlockWithItem(`"$_`", $o, fabricItemSettings);"
            }
        }

        function Get-LangEntries([string[]] $Names) {
            $Names | Foreach-Object {
                "`"block.spectrum.$_`": `"$_`","
            }
        }



        ####################################
        #endregion CODE                    #
        ####################################


        ####################################
        #region    BLOCK STATE             #
        ####################################


        # The blockstate file determines which model a block should use depending on its blockstate. Our block doesn't have any potential states, so we cover everything with “”.
        # LOG
        function Get-BlockStateLog($Name) {
            Write-Output @"
{
	"variants": {
		"axis=x": {
			"model": "minecraft:block/$Name`_horizontal",
			"x": 90,
			"y": 90
		},
		"axis=y": {
			"model": "spectrum:block/$Name"
		},
		"axis=z": {
			"model": "spectrum:block/$Name`_horizontal",
			"x": 90
		}
	}
}
"@
        }

        function Get-BlockStateDefault($Name) {
            Write-Output @"
{
	"variants": {
		"": {
			"model": "spectrum:block/$Name"
		}
	}
}
"@
}


        function Get-BlockStateLamp($Name) {
            Write-Output @"
{
	"variants": {
		"lit=false": {
			"model": "spectrum:block/$Name"
		},
		"lit=true": {
			"model": "spectrum:block/$Name`_on"
		}
	}
}
"@
        }

        function Get-BlockStateUpgrade($Name) {
            $NameWithoutNumber = $Name -replace "[0-9]", ""
            Write-Output @"
{
	"variants": {
		"": {
			"model": "spectrum:block/$NameWithoutNumber"
		}
	}
}
"@
        }


        ####################################
        #endregion BLOCK STATE             #
        ####################################


        ####################################
        #region    BLOCK MODEL             #
        ####################################


        # The block model file defines the shape and texture of your block. Our model will have block/cube_allas a parent, which applies the texture all to all sides of the block.
        function Get-BlockModel($Name) {
            Write-Output @"
{
    "parent": "minecraft:block/cube_all",
    "textures": {
    "all": "spectrum:block/$Name"
    }
}
"@
        }

        function Get-BlockModelLog($Name) {
            Write-Output @"
{
    "parent": "minecraft:block/cube_column",
    "textures": {
    "end": "spectrum:block/$_Name`_top",
    "side": "spectrum:block/$Name"
    }
}
"@
        }

        function Get-BlockModelLogHorizontal($Name) {
            Write-Output @"
{
    "parent": "minecraft:block/cube_column_horizontal",
    "textures": {
    "end": "spectrum:block/$Name`_top",
    "side": "spectrum:block/$Name"
    }
}
"@
        }

        function Get-BlockModelLampOn($Name) {
            Write-Output @"
{
    "parent": "minecraft:block/cube_all",
    "textures": {
    "all": "spectrum:block/$Name`_on"
    }
}
"@
        }


        function Get-BlockModelLampOff($Name) {
            Write-Output @"
{
    "parent": "minecraft:block/cube_all",
    "textures": {
    "all": "spectrum:block/$Name"
    }
}
"@
        }


        function Get-BlockModelUpgrade($Name) {
            $NameWithoutNumber = $Name -replace "[0-9]", ""
            Write-Output @"
{
    "parent": "minecraft:block/template_upgrade",
    "textures": {
    "inner": "spectrum:block/$NameWithoutNumber`_inner",
    "outer": "spectrum:block/$NameWithoutNumber`_outer",
    "base": "spectrum:block/$NameWithoutNumber`_base",
    "particle": "spectrum:block/$NameWithoutNumber`_inner"
    }
}
"@
        }



        ####################################
        #endregion BLOCK MODEL              #
        ####################################


        ####################################
        #region    ITEM MODEL              #
        ####################################

        function Get-ItemModel($Name) {
            Write-Output @"
{
    "parent": "spectrum:block/$Name"
}
"@
        }

        ####################################
        #endregion ITEM MODEL              #
        ####################################


        ####################################
        #region    LOOT TABLE              #
        ####################################

        function Get-LootTable($Name) {
            Write-Output @"
{
	"type": "minecraft:block",
	"pools": [{
		"rolls": 1,
		"bonus_rolls": 0.0,
		"entries": [{
			"type": "minecraft:item",
			"name": "spectrum:$Name"
		}],
		"conditions": [{
			"condition": "minecraft:survives_explosion"
		}]
	}]
}
"@
        }


        ####################################
        #endregion LOOT TABLE              #
        ####################################


        ####################################
        #region    ANIMATION               #
        ####################################


        function Get-McMetaFile($Frametime) {
            Write-Output @"
    {
    "animation": {
    "interpolate": true,
    "frametime": $FrameTime
    }
}
"@
        }

        ####################################
        #endregion ANIMATION               #
        ####################################


        ####################################
        #endregion FUNCTIONS               #
        ####################################
    }

    Process {
        Get-BlockObjects -Name $BlockNames
        Get-RegisterBlockAndItems -Name $BlockNames
        Get-LangEntries -Names $BlockNames
        Write-Output ""
        Write-Output "- Mineable Block tags"
        Write-Output "- Manual Entry"
        Write-Output "- Recipes"

        $BlockNames | ForEach-Object {

            # BLOCK STATES
            if($blockType -eq [BlockType]::Default) {
                $blockState = Get-BlockStateDefault -Name $_
            } elseif ($blockType -eq [BlockType]::Log) {
                $blockState = Get-BlockStateLog -Name $_
            } elseif ($blockType -eq [BlockType]::Lamp) {
                $blockState = Get-BlockStateLamp -Name $_
            } elseif ($blockType -eq [BlockType]::Upgrade) {
                $blockState = Get-BlockStateUpgrade -Name $_
            }
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\blockstates\") -Name "$_`.json" -ItemType File -Force -Value $blockState | Out-Null
    
            # BLOCK MODELS
            if($blockType -eq [BlockType]::Default) {
                $blockModel = Get-BlockModel -Name $_
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $blockModel | Out-Null
            } elseif ($blockType -eq [BlockType]::Log) {
                $blockModelLog = Get-BlockModelLog -Name $_
                $blockModelLogHorizontal = Get-BlockModelLogHorizontal -Name $_
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $blockModelLog | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_horizontal.json" -ItemType File -Force -Value $blockModelLogHorizontal | Out-Null
            } elseif ($blockType -eq [BlockType]::Lamp) {
                $blockModelLampOn = Get-BlockModelLampOn -Name $_
                $blockModelLampOff = Get-BlockModelLampOff -Name $_
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $blockModelLampOff | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_on.json" -ItemType File -Force -Value $blockModelLampOn | Out-Null
            } elseif ($blockType -eq [BlockType]::Upgrade) {
                $NameWithoutNumber = $_ -replace "[0-9]", ""
                $blockModel = Get-BlockModelUpgrade -Name $NameWithoutNumber
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$NameWithoutNumber`.json" -ItemType File -Force -Value $blockModel | Out-Null
            }

            # ITEM MODEL
            if($blockType -eq [BlockType]::Upgrade) {
                $itemModel = Get-ItemModel -Name ($_ -replace "[0-9]", "")
            } else {
                $itemModel = Get-ItemModel -Name $_
            }
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\item\") -Name "$_`.json" -ItemType File -Force -Value $itemModel -ErrorAction SilentlyContinue | Out-Null

            # TEXTURE
            # create empty pngs
            if($blockType -eq [BlockType]::Default) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.png" -ItemType File -Force | Out-Null
                if($AnimationFrameTime -ne 0) {
                    $mcMetaContent = Get-McMetaFile -Frametime $frameTime
                    New-Item -Path $(Join-Path -Path $pach -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.mcmeta" -ItemType File -Value $mcMetaContent | Out-Null
                }
            } elseif($blockType -eq [BlockType]::Log) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.png" -ItemType File -Force | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`_top.png" -ItemType File -Force | Out-Null
                if($AnimationFrameTime -ne 0) {
                    $mcMetaContent = Get-McMetaFile -Frametime $frameTime
                    New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.mcmeta" -ItemType File -Value $mcMetaContent | Out-Null
                    New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`_top.mcmeta" -ItemType File -Value $mcMetaContent | Out-Null
                }
            } elseif($blockType -eq [BlockType]::Lamp) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.png" -ItemType File -Force | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`_off.png" -ItemType File -Force | Out-Null
                if($AnimationFrameTime -ne 0) {
                    $mcMetaContent = Get-McMetaFile -Frametime $frameTime
                    New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.mcmeta" -ItemType File -Value $mcMetaContent | Out-Null
                    New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`_off.mcmeta" -ItemType File -Value $mcMetaContent | Out-Null
                }
            } elseif($blockType -eq [BlockType]::Upgrade) {
                $NameWithoutNumber = $_ -replace "[0-9]", ""
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$NameWithoutNumber`_inner.png" -ItemType File -Force | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$NameWithoutNumber`_outer.png" -ItemType File -Force | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$NameWithoutNumber`_base.png" -ItemType File -Force | Out-Null
            }

            # LOOT TABLE
            # To make your block drop items when broken, you will need a loot table. The following file will cause your block to drop its respective item form when broken
            $lootTable = Get-LootTable -Name $_
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\data\spectrum\loot_tables\blocks\") -Name "$_`.json" -ItemType File -Force -Value $lootTable | Out-Null
        }
    }

    End {
        
    }
}


Generate-BlockFiles -BlockNames $new -BlockType ([BlockType]::Default)