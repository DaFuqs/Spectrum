package de.dafuqs.spectrum.api.pastel_network;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

import java.util.*;
import java.util.function.*;

public final class PastelUpgradeSignature {
	
	public static Holder.Reference<PastelUpgradeSignature> of(Level level, Item item) {
		return level.registryAccess().registry(SpectrumRegistryKeys.PASTEL_UPGRADE).get().holders()
				.filter(pastelUpgradeSignature -> pastelUpgradeSignature.value().upgradeItem.value().equals(item))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Attempted to fetch an upgrade that does not exist"));
	}
	
	public enum RedstoneBehavior implements StringRepresentable {
		NONE("none"),
		ALWAYS_ACTIVE("always_active"),
		ALWAYS_INACTIVE("always_inactive"),
		INVERTED("inverted"),
		LAMP("lamp"),
		SENSOR("sensor");
		
		public static final Codec<RedstoneBehavior> CODEC = StringRepresentable.fromEnum(RedstoneBehavior::values);
		
		private final String name;
		
		RedstoneBehavior(String name) {
			this.name = name;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
	
	public static final Codec<PastelUpgradeSignature> CODEC = RecordCodecBuilder.create((i -> i.group(
			ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(c -> c.upgradeItem),
			Codec.STRING.fieldOf("name").forGetter(c -> c.name),
			Codec.INT.optionalFieldOf("stack_mod", 0).forGetter(c -> c.stack),
			Codec.INT.optionalFieldOf("speed_mod", 0).forGetter(c -> c.speed),
			Codec.INT.optionalFieldOf("filter_row_mod", 0).forGetter(c -> c.slotRows),
			Codec.FLOAT.optionalFieldOf("stack_multi", 1.0F).forGetter(c -> c.stackMult),
			Codec.FLOAT.optionalFieldOf("speed_multi", 1.0F).forGetter(c -> c.speedMult),
			Codec.FLOAT.optionalFieldOf("transfer_rate_mod", 1.0F).forGetter(c -> c.transferRateMult),
			Codec.BOOL.optionalFieldOf("light", false).forGetter(c -> c.light),
			Codec.BOOL.optionalFieldOf("trigger", false).forGetter(c -> c.triggerTransfer),
			Codec.BOOL.optionalFieldOf("lamp", false).forGetter(c -> c.lamp),
			Codec.BOOL.optionalFieldOf("sensor", false).forGetter(c -> c.sensor),
			Category.CODEC.fieldOf("category").forGetter(c -> c.category),
			RedstoneBehavior.CODEC.optionalFieldOf("redstone", RedstoneBehavior.NONE).forGetter(c -> c.redstone)
	).apply(i, PastelUpgradeSignature::new)));
	
	public static final String INNER_RING_BASE_PATH = "textures/block/pastel_node_inner_ring_";
	public static final String OUTER_RING_BASE_PATH = "textures/block/pastel_node_outer_ring_";
	public static final String REDSTONE_RING_BASE_PATH = "textures/block/pastel_node_redstone_ring_";
	
	public final Holder<Item> upgradeItem;
	public final String name;
	public final ResourceLocation outerRing, innerRing;
	public final int stack, speed, slotRows;
	public final float stackMult, speedMult, transferRateMult;
	public final boolean light, triggerTransfer, lamp, sensor;
	public final Category category;
	public final RedstoneBehavior redstone;
	public final RedstoneStateModifier preProcessor;
	public final RedstoneStateModifier postProcessor;
	
	private PastelUpgradeSignature(Holder<Item> upgradeItem, String name, int stack, int speed, int slotRows, float stackMult, float speedMult, float transferRateMult, boolean light, boolean triggerTransfer, boolean lamp, boolean sensor, Category category, RedstoneBehavior redstone) {
		this(upgradeItem, name, stack, speed, slotRows, stackMult, speedMult, transferRateMult, light, category == Category.REDSTONE || triggerTransfer, lamp, sensor, category, redstone, switch (redstone) {
			case ALWAYS_ACTIVE -> (RedstoneStateModifier) context -> InteractionResult.SUCCESS;
			case ALWAYS_INACTIVE -> (RedstoneStateModifier) context -> InteractionResult.FAIL;
			default -> RedstoneStateModifier.PASS;
		}, switch (redstone) {
			case INVERTED -> (RedstoneStateModifier) context -> context.active() ? InteractionResult.FAIL : InteractionResult.SUCCESS;
			default -> RedstoneStateModifier.PASS;
		});
	}
	
	private PastelUpgradeSignature(Holder<Item> upgradeItem, String name, int stack, int speed, int slotRows, float stackMult, float speedMult, float transferRateMult, boolean light, boolean triggerTransfer, boolean lamp, boolean sensor, Category category, RedstoneBehavior redstone, RedstoneStateModifier preProcessor, RedstoneStateModifier postProcessor) {
		this.upgradeItem = upgradeItem;
		this.name = name;
		this.innerRing = SpectrumCommon.locate(INNER_RING_BASE_PATH + name + ".png");
		this.outerRing = category == Category.REDSTONE ? SpectrumCommon.locate(REDSTONE_RING_BASE_PATH + name + ".png") : SpectrumCommon.locate(OUTER_RING_BASE_PATH + name + ".png");
		this.stack = stack;
		this.speed = speed;
		this.slotRows = slotRows;
		this.stackMult = stackMult;
		this.speedMult = speedMult;
		this.transferRateMult = transferRateMult;
		this.light = light;
		this.triggerTransfer = triggerTransfer;
		this.lamp = lamp;
		this.category = category;
		this.preProcessor = preProcessor;
		this.postProcessor = postProcessor;
		this.sensor = sensor;
		this.redstone = redstone;
	}
	
	public ResourceLocation outerRing() {
		return outerRing;
	}
	
	public ResourceLocation innerRing() {
		return innerRing;
	}
	
	/**
	 * SUCCESS = on<p>
	 * FAIL = off<p>
	 * PASS = allow normal logic flow to continue
	 */
	@FunctionalInterface
	public interface RedstoneStateModifier {
		RedstoneStateModifier PASS = (context) -> InteractionResult.PASS;
		
		InteractionResult apply(RedstoneContext context);
	}
	
	public record RedstoneContext(PastelUpgradeable upgradeable, Level world, BlockPos pos, boolean active) {
	}
	
	public enum Category implements StringRepresentable {
		SIMPLE("simple", false, false),
		NON_COMPOUNDING("non_compounding", true, false),
		REDSTONE("redstone", true, true);
		
		public static final Codec<Category> CODEC = StringRepresentable.fromEnum(Category::values);
		
		private final String name;
		private final boolean nonCompounding;
		private final boolean isRedstone;
		
		Category(String name, boolean nonCompounding, boolean isRedstone) {
			this.name = name;
			this.nonCompounding = nonCompounding;
			this.isRedstone = isRedstone;
		}
		
		public boolean compoundsWith(Category other) {
			return nonCompounding ? false : other == this;
		}
		
		public boolean isRedstone() {
			return isRedstone;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
