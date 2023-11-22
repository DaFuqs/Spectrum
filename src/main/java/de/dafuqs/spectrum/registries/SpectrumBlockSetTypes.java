package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.type.*;
import net.minecraft.block.*;
import net.minecraft.sound.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumBlockSetTypes {
    
    public static final BlockSetType POLISHED_BASALT = new BlockSetTypeBuilder()
        .soundGroup(BlockSoundGroup.BASALT)
        .doorCloseSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE)
        .doorOpenSound(SoundEvents.BLOCK_IRON_DOOR_OPEN)
        .trapdoorCloseSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE)
        .trapdoorOpenSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN)
        .pressurePlateClickOffSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF)
        .pressurePlateClickOnSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON)
        .buttonClickOffSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
        .buttonClickOnSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
        .build(locate("polished_basalt"));

    public static final BlockSetType POLISHED_CALCITE = new BlockSetTypeBuilder()
        .soundGroup(BlockSoundGroup.CALCITE)
        .doorCloseSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE)
        .doorOpenSound(SoundEvents.BLOCK_IRON_DOOR_OPEN)
        .trapdoorCloseSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE)
        .trapdoorOpenSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN)
        .pressurePlateClickOffSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF)
        .pressurePlateClickOnSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON)
        .buttonClickOffSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
        .buttonClickOnSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
        .build(locate("polished_calcite"));

    public static final BlockSetType POLISHED_BLACKSLAG = new BlockSetTypeBuilder()
        .soundGroup(BlockSoundGroup.DEEPSLATE)
        .doorCloseSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE)
        .doorOpenSound(SoundEvents.BLOCK_IRON_DOOR_OPEN)
        .trapdoorCloseSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE)
        .trapdoorOpenSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN)
        .pressurePlateClickOffSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF)
        .pressurePlateClickOnSound(SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON)
        .buttonClickOffSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
        .buttonClickOnSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
        .build(locate("polished_blackslag"));

    // TODO - Custom wood sounds? Maybe?
    public static final BlockSetType NOXWOOD = BlockSetTypeBuilder.copyOf(BlockSetType.ACACIA).register(locate("noxwood"));
    
    public static final BlockSetType COLORED_WOOD = BlockSetTypeBuilder.copyOf(BlockSetType.ACACIA).register(locate("colored_wood"));
}
