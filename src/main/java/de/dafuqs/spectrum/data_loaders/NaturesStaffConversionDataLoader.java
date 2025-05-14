package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.profiling.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffConversionDataLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
	
	public static final String ID = "natures_staff_conversion";
	public static final NaturesStaffConversionDataLoader INSTANCE = new NaturesStaffConversionDataLoader();
	
	public static final HashMap<Block, BlockState> CONVERSIONS = new HashMap<>();
	public static final HashMap<Block, ResourceLocation> UNLOCK_IDENTIFIERS = new HashMap<>();
	
	private NaturesStaffConversionDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
		CONVERSIONS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Block input = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "input_block")));
			
			BlockState output;
			try {
				output = RecipeUtils.blockStateFromString(jsonObject.get("output_state").getAsString());
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
			
			if (input != Blocks.AIR && !output.isAir()) {
				CONVERSIONS.put(input, output);
				if (GsonHelper.isStringValue(jsonObject, "unlock_identifier")) {
					UNLOCK_IDENTIFIERS.put(input, ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "unlock_identifier")));
				}
			}
		});
	}
	
	@Override
	public ResourceLocation getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static @Nullable BlockState getConvertedBlockState(Block block) {
		return CONVERSIONS.getOrDefault(block, null);
	}
	
}