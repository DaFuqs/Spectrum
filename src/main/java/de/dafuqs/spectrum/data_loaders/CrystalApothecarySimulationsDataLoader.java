package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class CrystalApothecarySimulationsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "crystal_apothecary_simulations";
	public static final CrystalApothecarySimulationsDataLoader INSTANCE = new CrystalApothecarySimulationsDataLoader();
	
	public static final HashMap<Block, SimulatedBlockGrowthEntry> COMPENSATIONS = new HashMap<>();
	
	public record SimulatedBlockGrowthEntry(Collection<Block> validNeighbors,
											int ticksForCompensationLootPerValidNeighbor, ItemStack compensatedStack) {
	}
	
	private CrystalApothecarySimulationsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject object = jsonElement.getAsJsonObject();
			
			String buddingBlockString = JsonHelper.getString(object, "budding_block");
			Block buddingBlock = Registry.BLOCK.get(Identifier.tryParse(buddingBlockString));
			if (buddingBlock == Blocks.AIR) {
				SpectrumCommon.logError("Crystal Apothecary Simulation '" + identifier + "' has a non-existant 'budding_block' entry: '" + buddingBlockString + "'. Ignoring that one.");
				return;
			}
			
			Set<Block> validNeighbors = new HashSet<>();
			for (JsonElement entry : object.get("valid_neighbor_blocks").getAsJsonArray()) {
				Identifier validNeighborBlockId = Identifier.tryParse(entry.getAsString());
				Block validNeighborBlock = Registry.BLOCK.get(validNeighborBlockId);
				if (validNeighborBlock == Blocks.AIR && !validNeighborBlockId.equals(new Identifier("air"))) {
					SpectrumCommon.logError("Crystal Apothecary Simulation '" + identifier + "' has a non-existant 'valid_neighbor_block' entry: '" + validNeighborBlockId + "'. Ignoring that one.");
				} else {
					validNeighbors.add(validNeighborBlock);
				}
			}
			int ticksForCompensationLootPerValidNeighbor = JsonHelper.getInt(object, "ticks_for_compensation_loot_per_valid_neighbor", 10000);
			
			ItemStack compensatedStack;
			try {
				compensatedStack = RecipeUtils.itemStackWithNbtFromJson(object.get("compensated_loot").getAsJsonObject());
			} catch (JsonSyntaxException e) {
				SpectrumCommon.logError("Crystal Apothecary Simulation '" + identifier + "' has an invalid 'compensated_loot' tag, perhaps with a non-existing item. Ignoring that one.");
				return;
			}
			
			COMPENSATIONS.put(buddingBlock, new SimulatedBlockGrowthEntry(validNeighbors, ticksForCompensationLootPerValidNeighbor, compensatedStack));
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
}