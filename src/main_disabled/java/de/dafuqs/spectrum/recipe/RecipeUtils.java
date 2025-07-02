package de.dafuqs.spectrum.recipe;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import net.minecraft.commands.arguments.blocks.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.state.*;

public class RecipeUtils {
	
	public static BlockState blockStateFromString(String string) throws CommandSyntaxException {
		return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(string), true).blockState();
	}
	
	public static DataResult<BlockState> blockStateDataFromString(String string) {
		try {
			return DataResult.success(blockStateFromString(string));
		} catch (CommandSyntaxException e) {
			return DataResult.error(e::getMessage);
		}
	}
	
	public static String blockStateToString(BlockState state) {
		return BlockStateParser.serialize(state);
	}
	
}
