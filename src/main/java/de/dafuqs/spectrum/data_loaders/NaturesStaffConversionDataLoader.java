package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.block.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffConversionDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "natures_staff_conversions";
	public static final NaturesStaffConversionDataLoader INSTANCE = new NaturesStaffConversionDataLoader();
	
	public static final HashMap<Block, BlockState> CONVERSIONS = new HashMap<>();
	public static final HashMap<Block, Identifier> UNLOCK_IDENTIFIERS = new HashMap<>();
	
	private NaturesStaffConversionDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		CONVERSIONS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Block input = Registry.BLOCK.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "input_block")));
			
			BlockState output;
			try {
				output = RecipeUtils.blockStateFromString(jsonObject.get("output_state").getAsString());
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
			
			if (input != Blocks.AIR && !output.isAir()) {
				CONVERSIONS.put(input, output);
				if (JsonHelper.hasString(jsonObject, "unlock_identifier")) {
					UNLOCK_IDENTIFIERS.put(input, Identifier.tryParse(JsonHelper.getString(jsonObject, "unlock_identifier")));
				}
			}
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static @Nullable BlockState getConvertedBlockState(Block block) {
		return CONVERSIONS.getOrDefault(block, null);
	}
	
}