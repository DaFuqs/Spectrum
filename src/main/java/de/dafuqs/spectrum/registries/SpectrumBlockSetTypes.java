package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.sounds.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;

public class SpectrumBlockSetTypes {
	
	public static final BlockSetType POLISHED_BASALT = BlockSetType.register(new BlockSetType(
			SpectrumCommon.MOD_ID + "_polished_basalt",
			true, true, false,
			BlockSetType.PressurePlateSensitivity.MOBS,
			SoundType.BASALT,
			SoundEvents.IRON_DOOR_CLOSE,
			SoundEvents.IRON_DOOR_OPEN,
			SoundEvents.IRON_TRAPDOOR_CLOSE,
			SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
			SoundEvents.STONE_BUTTON_CLICK_OFF,
			SoundEvents.STONE_BUTTON_CLICK_ON
	));
	
	public static final BlockSetType POLISHED_CALCITE = BlockSetType.register(new BlockSetType(
			SpectrumCommon.MOD_ID + "_polished_calcite",
			true, true, false,
			BlockSetType.PressurePlateSensitivity.MOBS,
			SoundType.CALCITE,
			SoundEvents.IRON_DOOR_CLOSE,
			SoundEvents.IRON_DOOR_OPEN,
			SoundEvents.IRON_TRAPDOOR_CLOSE,
			SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
			SoundEvents.STONE_BUTTON_CLICK_OFF,
			SoundEvents.STONE_BUTTON_CLICK_ON
	));
	
	public static final BlockSetType POLISHED_BLACKSLAG = BlockSetType.register(new BlockSetType(
			SpectrumCommon.MOD_ID + "_polished_blackslag",
			true, true, false,
			BlockSetType.PressurePlateSensitivity.MOBS,
			SoundType.DEEPSLATE,
			SoundEvents.IRON_DOOR_CLOSE,
			SoundEvents.IRON_DOOR_OPEN,
			SoundEvents.IRON_TRAPDOOR_CLOSE,
			SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
			SoundEvents.STONE_BUTTON_CLICK_OFF,
			SoundEvents.STONE_BUTTON_CLICK_ON
	));
	
	public static final BlockSetType NOXWOOD = BlockSetType.register(new BlockSetType(SpectrumCommon.MOD_ID + "_noxwood"));
	public static final BlockSetType COLORED_WOOD = BlockSetType.register(new BlockSetType(SpectrumCommon.MOD_ID + "_colored_wood"));
	public static final BlockSetType WEEPING_GALA = BlockSetType.register(new BlockSetType(SpectrumCommon.MOD_ID + "_weeping_gala"));
}
