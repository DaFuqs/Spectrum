$new = @("black_wood", "blue_wood", "brown_wood", "cyan_wood", "gray_wood", "green_wood", "light_blue_wood", "light_gray_wood", "lime_wood", "magenta_wood", "orange_wood", "pink_wood", "purple_wood", "red_wood", "white_wood", "yellow_wood")

enum BlockType {
    Default
    Lamp
    Log
    Wood
    Upgrade
    Crystallarieum
    Stairs
    Slab
    Wall
    Button
    PressurePlate
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
                "registerBlockWithItem(`"$_`", $o, fabricItemSettings, DyeColor.XXX);"
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
                "`"block.spectrum.$_`": `"$translation`","
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

                function Get-BlockStateWood($Name) {
            Write-Output @"
{
  "variants": {
    "axis=x": {
      "model": "spectrum:block/$Name",
      "x": 90,
      "y": 90
    },
    "axis=y": {
      "model": "spectrum:block/$Name"
    },
    "axis=z": {
      "model": "spectrum:block/$Name",
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


function Get-BlockStateSlab($Name) {
  Write-Output @"
  {
    "variants": {
      "type=bottom": {
        "model": "spectrum:block/$Name`"
      },
      "type=double": {
        "model": "spectrum:block/$Name`"
      },
      "type=top": {
        "model": "spectrum:block/$Name`_top"
      }
    }
  }
"@
}

        function Get-BlockStateCrystallarieum($Name) {
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

                
        function Get-BlockStateStairs($Name) {
            Write-Output @"
{
	"variants": {
		"facing=east,half=bottom,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"y": 270,
			"uvlock": true
		},
		"facing=east,half=bottom,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner"
		},
		"facing=east,half=bottom,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"y": 270,
			"uvlock": true
		},
		"facing=east,half=bottom,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer"
		},
		"facing=east,half=bottom,shape=straight": {
			"model": "spectrum:block/$Name"
		},
		"facing=east,half=top,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"uvlock": true
		},
		"facing=east,half=top,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 90,
			"uvlock": true
		},
		"facing=east,half=top,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"uvlock": true
		},
		"facing=east,half=top,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 90,
			"uvlock": true
		},
		"facing=east,half=top,shape=straight": {
			"model": "spectrum:block/$Name",
			"x": 180,
			"uvlock": true
		},
		"facing=north,half=bottom,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"y": 180,
			"uvlock": true
		},
		"facing=north,half=bottom,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"y": 270,
			"uvlock": true
		},
		"facing=north,half=bottom,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"y": 180,
			"uvlock": true
		},
		"facing=north,half=bottom,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"y": 270,
			"uvlock": true
		},
		"facing=north,half=bottom,shape=straight": {
			"model": "spectrum:block/$Name",
			"y": 270,
			"uvlock": true
		},
		"facing=north,half=top,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 270,
			"uvlock": true
		},
		"facing=north,half=top,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"uvlock": true
		},
		"facing=north,half=top,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 270,
			"uvlock": true
		},
		"facing=north,half=top,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"uvlock": true
		},
		"facing=north,half=top,shape=straight": {
			"model": "spectrum:block/$Name",
			"x": 180,
			"y": 270,
			"uvlock": true
		},
		"facing=south,half=bottom,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner"
		},
		"facing=south,half=bottom,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"y": 90,
			"uvlock": true
		},
		"facing=south,half=bottom,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer"
		},
		"facing=south,half=bottom,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"y": 90,
			"uvlock": true
		},
		"facing=south,half=bottom,shape=straight": {
			"model": "spectrum:block/$Name",
			"y": 90,
			"uvlock": true
		},
		"facing=south,half=top,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 90,
			"uvlock": true
		},
		"facing=south,half=top,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 180,
			"uvlock": true
		},
		"facing=south,half=top,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 90,
			"uvlock": true
		},
		"facing=south,half=top,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 180,
			"uvlock": true
		},
		"facing=south,half=top,shape=straight": {
			"model": "spectrum:block/$Name",
			"x": 180,
			"y": 90,
			"uvlock": true
		},
		"facing=west,half=bottom,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"y": 90,
			"uvlock": true
		},
		"facing=west,half=bottom,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"y": 180,
			"uvlock": true
		},
		"facing=west,half=bottom,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"y": 90,
			"uvlock": true
		},
		"facing=west,half=bottom,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"y": 180,
			"uvlock": true
		},
		"facing=west,half=bottom,shape=straight": {
			"model": "spectrum:block/$Name",
			"y": 180,
			"uvlock": true
		},
		"facing=west,half=top,shape=inner_left": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 180,
			"uvlock": true
		},
		"facing=west,half=top,shape=inner_right": {
			"model": "spectrum:block/$Name`_inner",
			"x": 180,
			"y": 270,
			"uvlock": true
		},
		"facing=west,half=top,shape=outer_left": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 180,
			"uvlock": true
		},
		"facing=west,half=top,shape=outer_right": {
			"model": "spectrum:block/$Name`_outer",
			"x": 180,
			"y": 270,
			"uvlock": true
		},
		"facing=west,half=top,shape=straight": {
			"model": "spectrum:block/$Name",
			"x": 180,
			"y": 180,
			"uvlock": true
		}
	}
}
"@
        }

        
        function Get-BlockStateWall($Name) {
            Write-Output @"
{
	"multipart": [
		{
			"when": {
				"up": "true"
			},
			"apply": {
				"model": "spectrum:block/$Name`_post"
			}
		},
		{
			"when": {
				"north": "low"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side",
				"uvlock": true
			}
		},
		{
			"when": {
				"east": "low"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side",
				"y": 90,
				"uvlock": true
			}
		},
		{
			"when": {
				"south": "low"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side",
				"y": 180,
				"uvlock": true
			}
		},
		{
			"when": {
				"west": "low"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side",
				"y": 270,
				"uvlock": true
			}
		},
		{
			"when": {
				"north": "tall"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side_tall",
				"uvlock": true
			}
		},
		{
			"when": {
				"east": "tall"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side_tall",
				"y": 90,
				"uvlock": true
			}
		},
		{
			"when": {
				"south": "tall"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side_tall",
				"y": 180,
				"uvlock": true
			}
		},
		{
			"when": {
				"west": "tall"
			},
			"apply": {
				"model": "spectrum:block/$Name`_side_tall",
				"y": 270,
				"uvlock": true
			}
		}
	]
}
"@
        }

        
        function Get-BlockStateButton($Name) {
            Write-Output @"
{
  "variants": {
    "face=ceiling,facing=east,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 270,
      "x": 180
    },
    "face=ceiling,facing=east,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 270,
      "x": 180
    },
    "face=ceiling,facing=north,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 180,
      "x": 180
    },
    "face=ceiling,facing=north,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 180,
      "x": 180
    },
    "face=ceiling,facing=south,powered=false": {
      "model": "spectrum:block/$Name",
      "x": 180
    },
    "face=ceiling,facing=south,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "x": 180
    },
    "face=ceiling,facing=west,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 90,
      "x": 180
    },
    "face=ceiling,facing=west,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 90,
      "x": 180
    },
    "face=floor,facing=east,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 90
    },
    "face=floor,facing=east,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 90
    },
    "face=floor,facing=north,powered=false": {
      "model": "spectrum:block/$Name"
    },
    "face=floor,facing=north,powered=true": {
      "model": "spectrum:block/$Name`_pressed"
    },
    "face=floor,facing=south,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 180
    },
    "face=floor,facing=south,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 180
    },
    "face=floor,facing=west,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 270
    },
    "face=floor,facing=west,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 270
    },
    "face=wall,facing=east,powered=false": {
      "model": "spectrum:block/$Name",
      "y": 90,
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=east,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 90,
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=north,powered=false": {
      "model": "spectrum:block/$Name`",
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=north,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=south,powered=false": {
      "model": "spectrum:block/$Name`",
      "y": 180,
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=south,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 180,
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=west,powered=false": {
      "model": "spectrum:block/$Name`",
      "y": 270,
      "x": 90,
      "uvlock": true
    },
    "face=wall,facing=west,powered=true": {
      "model": "spectrum:block/$Name`_pressed",
      "y": 270,
      "x": 90,
      "uvlock": true
    }
  }
}
"@
        }

        
        function Get-BlockStatePressurePlate($Name) {
            Write-Output @"
{
  "variants": {
    "powered=false": {
      "model": "spectrum:block/$Name"
    },
    "powered=true": {
      "model": "spectrum:block/$Name`_down"
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


        function Get-BlockModelCrystallarieum($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/crop",
  "textures": {
    "crop": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelSlab($Name) {
          Write-Output @"
{
  "parent": "minecraft:block/slab",
  "textures": {
    "bottom": "spectrum:block/$Name",
    "top": "spectrum:block/$Name",
    "side": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelSlabTop($Name) {
          Write-Output @"
{
  "parent": "minecraft:block/slab_top",
  "textures": {
    "bottom": "spectrum:block/$Name",
    "top": "spectrum:block/$Name",
    "side": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelStairs($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/stairs",
  "textures": {
    "bottom": "spectrum:block/$Name",
    "top": "spectrum:block/$Name",
    "side": "spectrum:block/$Name"
  }
}
"@
        }
        

        function Get-BlockModelStairsInner($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/inner_stairs",
  "textures": {
    "bottom": "spectrum:block/$Name",
    "top": "spectrum:block/$Name",
    "side": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelStairsOuter($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/outer_stairs",
  "textures": {
    "bottom": "spectrum:block/$Name",
    "top": "spectrum:block/$Name",
    "side": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelWallInventory($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/wall_inventory",
  "textures": {
    "wall": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelWallPost($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/template_wall_post",
  "textures": {
    "wall": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelWallSide($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/template_wall_side",
  "textures": {
    "wall": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelWallSideTall($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/template_wall_side_tall",
  "textures": {
    "wall": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelButton($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/button",
  "textures": {
    "texture": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelButtonInventory($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/button_inventory",
  "textures": {
    "texture": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelButtonPressed($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/button_pressed",
  "textures": {
    "texture": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelPressurePlate($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/pressure_plate_up",
  "textures": {
    "texture": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelPressurePlateDown($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/pressure_plate_down",
  "textures": {
    "texture": "spectrum:block/$Name"
  }
}
"@
        }

        function Get-BlockModelCubeColumn($Name) {
            Write-Output @"
{
  "parent": "minecraft:block/cube_column",
  "textures": {
    "end": "minecraft:block/$Name",
    "side": "minecraft:block/$Name"
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

        function Get-LootTableSlab($Name) {
            Write-Output @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:item",
          "functions": [
            {
              "function": "minecraft:set_count",
              "conditions": [
                {
                  "condition": "minecraft:block_state_property",
                  "block": "spectrum:$Name",
                  "properties": {
                    "type": "double"
                  }
                }
              ],
              "count": 2.0,
              "add": false
            },
            {
              "function": "minecraft:explosion_decay"
            }
          ],
          "name": "spectrum:$Name"
        }
      ]
    }
  ]
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
        Write-Output "- Guidebook Entry"
        Write-Output "- Recipes"

        $BlockNames | ForEach-Object {

            # BLOCK STATES
            if($blockType -eq [BlockType]::Default) {
                $blockState = Get-BlockStateDefault -Name $_
            } elseif ($blockType -eq [BlockType]::Log) {
                $blockState = Get-BlockStateLog -Name $_
            } elseif ($blockType -eq [BlockType]::Wood) {
                $blockState = Get-BlockStateWood -Name $_
            } elseif ($blockType -eq [BlockType]::Lamp) {
                $blockState = Get-BlockStateLamp -Name $_
            } elseif ($blockType -eq [BlockType]::Upgrade) {
                $blockState = Get-BlockStateUpgrade -Name $_
            } elseif ($blockType -eq [BlockType]::Crystallarieum) {
                $blockState = Get-BlockStateCrystallarieum -Name $_
            } elseif ($blockType -eq [BlockType]::Stairs) {
                $blockState = Get-BlockStateStairs -Name $_
            } elseif ($blockType -eq [BlockType]::Wall) {
                $blockState = Get-BlockStateWall -Name $_
            } elseif ($blockType -eq [BlockType]::Button) {
                $blockState = Get-BlockStateButton -Name $_
            } elseif ($blockType -eq [BlockType]::PressurePlate) {
                $blockState = Get-BlockStatePressurePlate -Name $_
            } elseif ($blockType -eq [BlockType]::Slab) {
              $blockState = Get-BlockStateSlab -Name $_
          }
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\blockstates\") -Name "$_`.json" -ItemType File -Force -Value $blockState | Out-Null
    
            # BLOCK MODELS
            if($blockType -eq [BlockType]::Default) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModel -Name $_) | Out-Null
            } elseif ($blockType -eq [BlockType]::Log) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelLog -Name $_) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_horizontal.json" -ItemType File -Force -Value $(Get-BlockModelLogHorizontal -Name $_) | Out-Null
            } elseif ($blockType -eq [BlockType]::Wood) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelCubeColumn -Name $_) | Out-Null
            } elseif ($blockType -eq [BlockType]::Lamp) {
                $blockModelLampOn = Get-BlockModelLampOn -Name $_
                $blockModelLampOff = Get-BlockModelLampOff -Name $_
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelLampOff -Name $_) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_on.json" -ItemType File -Force -Value $(Get-BlockModelLampOn -Name $_) | Out-Null
            } elseif ($blockType -eq [BlockType]::Upgrade) {
                $NameWithoutNumber = $_ -replace "[0-9]", ""
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$NameWithoutNumber`.json" -ItemType File -Force -Value $(Get-Get-BlockModelUpgrade -Name $NameWithoutNumber) | Out-Null
            } elseif ($blockType -eq [BlockType]::Crystallarieum) {
                $blockModel = Get-BlockModelCrystallarieum -Name $_
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelCrystallarieum -Name $_) | Out-Null
            } elseif ($blockType -eq [BlockType]::Stairs) {
                $textureName = $_.Substring(0, $_.LastIndexOf("_")) + "s"
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelStairs -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_inner.json" -ItemType File -Force -Value $(Get-BlockModelStairsInner -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_outer.json" -ItemType File -Force -Value $(Get-BlockModelStairsOuter -Name $textureName) | Out-Null
            } elseif ($blockType -eq [BlockType]::Wall) {
                $textureName = $_.Substring(0, $_.LastIndexOf("_")) + "s"
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_inventory.json" -ItemType File -Force -Value $(Get-BlockModelWallInventory -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_post.json" -ItemType File -Force -Value $(Get-BlockModelWallPost -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_side.json" -ItemType File -Force -Value $(Get-BlockModelWallSide -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_side_tall.json" -ItemType File -Force -Value $(Get-BlockModelWallSideTall -Name $textureName) | Out-Null
            } elseif ($blockType -eq [BlockType]::Button) {
                $textureName = $_.Substring(0, $_.LastIndexOf("_")) + "s"
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelButton -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_inventory.json" -ItemType File -Force -Value $(Get-BlockModelButtonInventory -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_pressed.json" -ItemType File -Force -Value $(Get-BlockModelButtonPressed -Name $textureName) | Out-Null
            } elseif ($blockType -eq [BlockType]::PressurePlate) {
                $textureName = $_.Substring(0, $_.LastIndexOf("_")) + "s"
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelPressurePlate -Name $textureName) | Out-Null
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_down.json" -ItemType File -Force -Value $(Get-BlockModelPressurePlateDown -Name $textureName) | Out-Null
            } elseif ($blockType -eq [BlockType]::Slab) {
              $textureName = $_.Substring(0, $_.LastIndexOf("_")) + "s"
              New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`.json" -ItemType File -Force -Value $(Get-BlockModelSlab -Name $textureName) | Out-Null
              New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\models\block\") -Name "$_`_top.json" -ItemType File -Force -Value $(Get-BlockModelSlabTop -Name $textureName) | Out-Null
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
            } elseif($blockType -eq [BlockType]::Crystallarieum) {
                New-Item -Path $(Join-Path -Path $destination -ChildPath "\resources\assets\spectrum\textures\block\") -Name "$_`.png" -ItemType File -Force | Out-Null
            }

            # LOOT TABLE
            # To make your block drop items when broken, you will need a loot table. The following file will cause your block to drop its respective item form when broken
            if($blockType -eq [BlockType]::Slab) {
              $lootTable = Get-LootTableSlab -Name $_
            } else {
              $lootTable = Get-LootTable -Name $_
            }
            New-Item -Path $(Join-Path -Path $destination -ChildPath "\data\spectrum\loot_tables\blocks\") -Name "$_`.json" -ItemType File -Force -Value $lootTable | Out-Null
          
        }
    }

    End {
        
    }
}


Generate-BlockFiles -BlockNames $new -BlockType ([BlockType]::Wood)