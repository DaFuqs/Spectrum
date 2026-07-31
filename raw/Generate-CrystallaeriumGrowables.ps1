$entries = @(
    [PSCustomObject] @{
        IntegrationPack = "create";
	    Name = "zinc";
	    SourceIngredient = "create:raw_zinc";
        TargetItem = "create:zinc_ingot";
        InkColor = "spectrum:brown";
        Fluid = "spectrum:liquid_crystal"
    },
    [PSCustomObject] @{
        IntegrationPack = "ae2";
	    Name = "certus_quartz";
	    SourceIngredient = "ae2:certus_quartz_dust";
        TargetItem = "ae2:certus_quartz_crystal";
        InkColor = "spectrum:brown";
        Fluid = "spectrum:liquid_crystal"
    },
    [PSCustomObject] @{
        IntegrationPack = "ae2";
	    Name = "fluix";
	    SourceIngredient = "ae2:fluix_dust";
        TargetItem = "ae2:fluix_crystal";
        InkColor = "spectrum:brown";
        Fluid = "spectrum:liquid_crystal"
    },
    [PSCustomObject] @{
        IntegrationPack = "gobber2";
	    Name = "globette";
	    SourceIngredient = "gobber2:gobber2_globette";
        TargetItem = "gobber2:gobber2_ingot";
        InkColor = "spectrum:cyan";
        Fluid = "minecraft:water"
    },
    [PSCustomObject] @{
        IntegrationPack = "gobber2";
	    Name = "globette_nether";
	    SourceIngredient = "gobber2:gobber2_globette_nether";
        TargetItem = "gobber2:gobber2_ingot_nether";
        InkColor = "spectrum:orange";
        Fluid = "minecraft:lava"
    },
    [PSCustomObject] @{
        IntegrationPack = "gobber2";
	    Name = "globette_end";
	    SourceIngredient = "gobber2:gobber2_globette_end";
        TargetItem = "gobber2:gobber2_ingot_end";
        InkColor = "spectrum:gray";
        Fluid = "minecraft:water"
    }
)

function ClusterBlockState($block) {
    New-Item -Path ".\blockstate\$block.json" -ItemType File -Force -Value @"
{
  "variants": {
    "facing=down": {
      "model": "spectrum:block/$block",
      "x": 180
    },
    "facing=east": {
      "model": "spectrum:block/$block",
      "x": 90,
      "y": 90
    },
    "facing=north": {
      "model": "spectrum:block/$block",
      "x": 90
    },
    "facing=south": {
      "model": "spectrum:block/$block",
      "x": 90,
      "y": 180
    },
    "facing=up": {
      "model": "spectrum:block/$block"
    },
    "facing=west": {
      "model": "spectrum:block/$block",
      "x": 90,
      "y": 270
    }
  }
}
"@
}



foreach($entry in $entries.GetEnumerator()) {
    $IntegrationPack = $entry.IntegrationPack
    $name = $entry.Name
    $SourceIngredient = $entry.SourceIngredient
    $TargetItem = $entry.TargetItem
    $InkColor = $entry.InkColor
    $Fluid = $entry.Fluid
    
    $small_bud = "small_" + $name + "_bud"
    $large_bud = "large_" + $name + "_bud"
    $cluster = $name + "_cluster"
    $pure_item = "pure_" + $name
    $pure_block = "pure_" + $name + "_block"

    ClusterBlockState $small_bud
    ClusterBlockState $large_bud
    ClusterBlockState $cluster

    New-Item -Path ".\blockstate\$pure_block.json" -ItemType File -Force -Value @"
{
  "variants": {
    "": {
      "model": "spectrum:block/$pure_block"
    }
  }
}
"@

    New-Item -Path ".\block_model\$pure_block.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "spectrum:block/$pure_block"
  }
}
"@

    New-Item -Path ".\block_model\$small_bud.json" -ItemType File -Force -Value @"
{
  "parent": "spectrum:templates/crystallarieum_farmable",
  "textures": {
    "cross": "spectrum:block/$small_bud"
  }
}
"@

    New-Item -Path ".\block_model\$large_bud.json" -ItemType File -Force -Value @"
{
  "parent": "spectrum:templates/crystallarieum_farmable",
  "textures": {
    "cross": "spectrum:block/$large_bud"
  }
}
"@

    New-Item -Path ".\block_model\$cluster.json" -ItemType File -Force -Value @"
{
  "parent": "spectrum:templates/crystallarieum_farmable",
  "textures": {
    "cross": "spectrum:block/$cluster"
  }
}
"@

    New-Item -Path ".\loot\pure_$name`_block.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:pure_$name`_block"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ],
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\loot\small_$name`_bud.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:small_$name`_bud"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:match_tool",
          "predicate": {
            "predicates": {
              "minecraft:enchantments": [
                {
                  "enchantments": "minecraft:silk_touch",
                  "levels": {
                    "min": 1
                  }
                }
              ]
            }
          }
        }
      ]
    }
  ],
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\loot\large_$name`_bud.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "spectrum:large_$name`_bud"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:match_tool",
          "predicate": {
            "predicates": {
              "minecraft:enchantments": [
                {
                  "enchantments": "minecraft:silk_touch",
                  "levels": {
                    "min": 1
                  }
                }
              ]
            }
          }
        }
      ]
    }
  ],
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\loot\$name`_cluster.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "bonus_rolls": 0,
      "entries": [
        {
          "type": "minecraft:alternatives",
          "children": [
            {
              "type": "minecraft:item",
              "name": "spectrum:$name`_cluster",
              "conditions": [
                {
                  "condition": "minecraft:match_tool",
                  "predicate": {
                    "predicates": {
                      "minecraft:enchantments": [
                        {
                          "enchantments": "minecraft:silk_touch",
                          "levels": {
                            "min": 1
                          }
                        }
                      ]
                    }
                  }
                }
              ]
            },
            {
              "type": "minecraft:item",
              "name": "spectrum:pure_$name`",
              "functions": [
                {
                  "function": "minecraft:set_count",
                  "count": {
                    "min": 3,
                    "max": 5
                  },
                  "add": false
                }
              ]
            }
          ]
        }
      ]
    }
  ],
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\anvil_crushing\$name`_from_buds.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:anvil_crushing",
  "ingredient": [
    {
      "item": "spectrum:small_$name`_bud"
    },
    {
      "item": "spectrum:large_$name`_bud"
    }
  ],
  "crushedItemsPerPointOfDamage": 1.0,
  "experience": 2.0,
  "result": {
    "id": "$TargetItem",
    "count": 2
  },
  "particleEffectIdentifier": "explosion",
  "soundEventIdentifier": "block.amethyst_cluster.break",
  "required_advancement": "spectrum:lategame/collect_pure_resource",
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\anvil_crushing\$name`_from_cluster.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:anvil_crushing",
  "ingredient": {
    "id": "spectrum:$name`_cluster"
  },
  "crushedItemsPerPointOfDamage": 1.0,
  "experience": 3.0,
  "result": {
    "id": "$TargetItem",
    "count": 6
  },
  "particleEffectIdentifier": "explosion",
  "soundEventIdentifier": "block.amethyst_cluster.break",
  "required_advancement": "spectrum:lategame/collect_pure_resource",
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\item_models\small_$name`_bud.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/small_amethyst_bud",
  "textures": {
    "layer0": "spectrum:block/small_$name`_bud"
  }
}
"@

    New-Item -Path ".\item_models\large_$name`_bud.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/large_amethyst_bud",
  "textures": {
    "layer0": "spectrum:block/large_$name`_bud"
  }
}
"@

    New-Item -Path ".\item_models\$name`_cluster.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/amethyst_cluster",
  "textures": {
    "layer0": "spectrum:block/$name`_cluster"
  }
}
"@

    New-Item -Path ".\item_models\$pure_block`.json" -ItemType File -Force -Value @"
{
  "parent": "spectrum:block/$pure_block"
}
"@

    New-Item -Path ".\item_models\pure_$name_`.json" -ItemType File -Force -Value @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "spectrum:item/pure_$name"
  }
}
"@

    New-Item -Path ".\blasting_recipes\pure_$name`.json" -ItemType File -Force -Value @"
{
  "type": "minecraft:blasting",
  "ingredient": {
    "item": "spectrum:pure_$name"
  },
  "result": {
    "id": "$TargetItem",
  },
  "experience": 0.5,
  "cookingtime": 100,
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\cinderhearth_recipes\pure_$name`.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:cinderhearth",
  "ingredient": {
    "item": "spectrum:pure_$name"
  },
  "results": [
    {
      "id": "$TargetItem",
      "count": 2.0
    }
  ],
  "experience": 0.5,
  "time": 200,
  "required_advancement": "spectrum:lategame/collect_pure_resource",
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

    New-Item -Path ".\crystallarieum\$name`.json" -ItemType File -Force -Value @"
{
  "type": "spectrum:crystallarieum_growing",
  "fluid": {
    "fluid": "$Fluid"
  },
  "ingredient": {
    "item": "$SourceIngredient"
  },
  "ink_color": "$InkColor",
  "ink_cost_tier": xxx,
  "seconds_per_growth_stage": 60,
  "additional_recipe_manager_results": [
    {
      "count": 1,
      "id": "spectrum:pure_$name"
    }
  ],
  "catalysts": [
    {
      "consume_chance_per_second": x.x,
      "growth_acceleration_mod": x.x,
      "ink_consumption_mod": 0.4,
      "ingredient": {
        "item": "minecraft:xxx"
      }
    },
    {
      "consume_chance_per_second": 0.05,
      "growth_acceleration_mod": 16.0,
      "ink_consumption_mod": 0.4,
      "ingredient": {
        "item": "spectrum:xxx"
      }
    },
    {
      "consume_chance_per_second": 0.4,
      "growth_acceleration_mod": 0.75,
      "ink_consumption_mod": 0.4,
      "ingredient": {
        "item": "spectrum:xxx"
      }
    }
  ],
  "growth_stage_states": [
    {
      "Name": "spectrum:small_$name`_bud",
      "Properties": {
        "facing": "up",
        "waterlogged": "false"
      }
    },
    {
      "Name": "spectrum:large_$name`_bud",
      "Properties": {
        "facing": "up",
        "waterlogged": "false"
      }
    },
    {
      "Name": "spectrum:$name`_cluster",
      "Properties": {
        "facing": "up",
        "waterlogged": "false"
      }
    }
  ],
  "neoforge:conditions": [
    {
      "type": "spectrum:integration_pack_active",
      "integration_pack": "$IntegrationPack"
    }
  ]
}
"@

}

# Block Registation
foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Name
    $nameUpper = $name.ToUpper()
    @"
    public static DeferredItem<Item> PURE_$nameUpper = SpectrumItems.register("pure_$name", () -> new Item(IS.of()));
	public static DeferredBlock<SpectrumClusterBlock> SMALL_$nameUpper`_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_$name`_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.xxx.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.xxx), ModelTemplates.CROSS));
	public static DeferredBlock<SpectrumClusterBlock> LARGE_$nameUpper`_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_$name`_bud", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ZINC_BUD.get()), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.xxx), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<SpectrumClusterBlock> $nameUpper`_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("$name`_cluster", () -> new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ZINC_BUD.get()), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.xxx), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static DeferredBlock<Block> PURE_$nameUpper`_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_$name`_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), InkColors.xxx)));

"@
}

# RenderLayer
foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Name
    $nameUpper = $name.ToUpper()
    @"
	ItemBlockRenderTypes.setRenderLayer(SMALL_$nameUpper`_BUD.get(), RenderType.cutout());
	ItemBlockRenderTypes.setRenderLayer(LARGE_$nameUpper`_BUD.get(), RenderType.cutout());
	ItemBlockRenderTypes.setRenderLayer($nameUpper`_CLUSTER.get(), RenderType.cutout());

"@
}

# Lang
foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Name
    $small_bud = "small_" + $name + "_bud"
    $large_bud = "large_" + $name + "_bud"
    $cluster = $name + "_cluster"
    $pure_item = "pure_" + $name
    $pure_block = "pure_" + $name + "_block"

    $textInfo = (Get-Culture).TextInfo
    $langName = $TextInfo.ToTitleCase($name)
    @"
      "item.spectrum.pure_$name": "Pure $langName",
      "block.spectrum.pure_$name`_block": "Block of Pure $langName",
      "block.spectrum.$name`_cluster": "$langName Cluster",
      "block.spectrum.large_$name`_bud": "Large $langName Bud",
      "block.spectrum.small_$name`_bud": "Small $langName Bud",

"@
}

# Lang
foreach($entry in $entries.GetEnumerator()) {
    $name = $entry.Name
    $small_bud = "small_" + $name + "_bud"
    $large_bud = "large_" + $name + "_bud"
    $cluster = $name + "_cluster"
    $pure_item = "pure_" + $name
    $pure_block = "pure_" + $name + "_block"

    $textInfo = (Get-Culture).TextInfo
    $langName = $TextInfo.ToTitleCase($name)
    @"
    BLOCK & ITEM TAG: pure_resource_blocks

    {
      "id": "spectrum:$pure_block",
      "required": false
    },

"@

    @"
    BLOCK & ITEM TAG: pure_resources

    {
      "id": "spectrum:$pure_item",
      "required": false
    },

"@


    @"
    BLOCK TAG: crystallarieum_growable_buds

    {
      "id": "spectrum:$small_bud",
      "required": false
    },
    {
      "id": "spectrum:$large_bud",
      "required": false
    },
"@

    @"
    BLOCK TAG: crystallarieum_growable_clusters

    {
      "id": "spectrum:$cluster",
      "required": false
    },
"@
}

# Block & Item Tags
# manual