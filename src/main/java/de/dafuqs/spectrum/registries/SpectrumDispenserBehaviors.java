package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.item.*;

public class SpectrumDispenserBehaviors {
    
    public static void register() {
        DispenserBehavior fluidBucketBehavior = SpectrumBlocks.ENDER_DROPPER.getDefaultBehaviorForItem(Items.WATER_BUCKET.getDefaultStack());
        DispenserBehavior armorEquipBehavior = SpectrumBlocks.ENDER_DROPPER.getDefaultBehaviorForItem(Items.PLAYER_HEAD.getDefaultStack());
        
        DispenserBlock.registerBehavior(SpectrumItems.BOTTOMLESS_BUNDLE, new BottomlessBundleItem.BottomlessBundlePlacementDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumBlocks.COLORFUL_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumBlocks.FIERY_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumBlocks.GEMSTONE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumBlocks.PRISTINE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
        DispenserBlock.registerBehavior(SpectrumItems.BEDROCK_SHEARS.asItem(), new ShearsDispenserBehavior());
        
        DispenserBlock.registerBehavior(SpectrumItems.MUD_BUCKET, fluidBucketBehavior);
        DispenserBlock.registerBehavior(SpectrumItems.LIQUID_CRYSTAL_BUCKET, fluidBucketBehavior);
        DispenserBlock.registerBehavior(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET, fluidBucketBehavior);
        
        for (Block skullBlock : SpectrumBlocks.MOB_HEADS.values()) {
            DispenserBlock.registerBehavior(skullBlock, armorEquipBehavior);
        }
        
    }


}
