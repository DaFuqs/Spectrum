package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class CrystalApothecarySimulationsDataLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
	
	public static final String ID = "crystal_apothecary_simulation";
	public static final CrystalApothecarySimulationsDataLoader INSTANCE = new CrystalApothecarySimulationsDataLoader();
	
	public static final HashMap<Block, SimulatedBlockGrowthEntry> COMPENSATIONS = new HashMap<>();
	
	public record SimulatedBlockGrowthEntry(Collection<Block> validNeighbors, int ticksForCompensationLootPerValidNeighbor, ItemStack compensatedStack) {
		
		public static final Codec<SimulatedBlockGrowthEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				BuiltInRegistries.BLOCK.byNameCodec().listOf().xmap(list -> (Collection<Block>) list, set -> set.stream().toList()).fieldOf("valid_neighbor_blocks").forGetter(c -> c.validNeighbors),
				Codec.INT.optionalFieldOf("ticks_for_compensation_loot_per_valid_neighbor", 10000).forGetter(c -> c.ticksForCompensationLootPerValidNeighbor),
				ItemStack.CODEC.fieldOf("compensated_loot").forGetter(c -> c.compensatedStack)
		).apply(i, SimulatedBlockGrowthEntry::new));
		
	}
	
	private CrystalApothecarySimulationsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
		COMPENSATIONS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject object = jsonElement.getAsJsonObject();
			
			DataResult<Block> buddingBlock = BuiltInRegistries.BLOCK.byNameCodec().decode(JsonOps.INSTANCE, object.get("budding_block")).map(Pair::getFirst);
			if (buddingBlock.error().isPresent() || buddingBlock.result().isEmpty()) {
				SpectrumCommon.logError("Crystal apothecary simulation error for " + identifier + ": " + buddingBlock.error().get() + ". Ignoring that one.");
				return;
			}
			
			DataResult<SimulatedBlockGrowthEntry> entry = SimulatedBlockGrowthEntry.CODEC.decode(JsonOps.INSTANCE, jsonElement).map(Pair::getFirst);
			if (entry.error().isPresent() || entry.result().isEmpty()) {
				SpectrumCommon.logError("Crystal Apothecary Simulation error for " + identifier + ": " + entry.error().get() + ". Ignoring that one.");
				return;
			}
			
			COMPENSATIONS.put(buddingBlock.result().get(), entry.result().get());
		});
	}
	
	@Override
	public ResourceLocation getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
}
