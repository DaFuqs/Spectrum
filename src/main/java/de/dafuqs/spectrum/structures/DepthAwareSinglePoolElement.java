package de.dafuqs.spectrum.structures;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.structure.processor.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class DepthAwareSinglePoolElement extends SinglePoolElement {
	
	public static final Codec<DepthAwareSinglePoolElement> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(locationGetter(), processorsGetter(), projectionGetter(), Codec.INT.fieldOf("max_depth").forGetter((pool) -> pool.maxDepth))
					.apply(instance, DepthAwareSinglePoolElement::new));
	
	protected int maxDepth;
	
	protected DepthAwareSinglePoolElement(Either<Identifier, StructureTemplate> location, RegistryEntry<StructureProcessorList> processors, StructurePool.Projection projection, int maxDepth) {
		super(location, processors, projection);
		this.maxDepth = maxDepth;
	}
	
	@Override
	public StructurePoolElementType<?> getType() {
		return SpectrumStructurePoolElementTypes.DEPTH_AWARE_SINGLE_POOL_ELEMENT;
	}
	
	@Override
	public String toString() {
		return "SingleDepthAware[" + this.location + "]";
	}
	
	public int getMaxDepth() {
		return this.maxDepth;
	}
	
}
