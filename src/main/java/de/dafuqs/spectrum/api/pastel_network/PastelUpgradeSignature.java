package de.dafuqs.spectrum.api.pastel_network;

import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public final class PastelUpgradeSignature {

	public static final String INNER_RING_BASE_PATH = "textures/block/pastel_node_inner_ring_";
	public static final String OUTER_RING_BASE_PATH = "textures/block/pastel_node_outer_ring_";
	public static final String REDSTONE_RING_BASE_PATH = "textures/block/pastel_node_redstone_ring_";

	public final Item upgradeItem;
	public final String name;
	public final ResourceLocation outerRing, innerRing;
	public final int stack, speed, slotRows;
	public final float stackMult, speedMult;
	public final boolean light, priority, triggerTransfer, lamp, sensor;
	public final Category category;
	public final RedstoneStateModifier preProcessor;
	public final RedstoneStateModifier postProcessor;
	
	private PastelUpgradeSignature(Item upgradeItem, String name, ResourceLocation outerRing, ResourceLocation innerRing, int stack, int speed, int slotRows, float stackMult, float speedMult, boolean light, boolean priority, boolean triggerTransfer, boolean lamp, boolean sensor, Category category, RedstoneStateModifier preProcessor, RedstoneStateModifier postProcessor) {
		this.upgradeItem = upgradeItem;
		this.name = name;
		this.outerRing = outerRing;
		this.innerRing = innerRing;
		this.stack = stack;
		this.speed = speed;
		this.slotRows = slotRows;
		this.stackMult = stackMult;
		this.speedMult = speedMult;
		this.light = light;
		this.priority = priority;
		this.triggerTransfer = triggerTransfer;
		this.lamp = lamp;
		this.category = category;
		this.preProcessor = preProcessor;
		this.postProcessor = postProcessor;
		this.sensor = sensor;
	}
	
	public ResourceLocation outerRing() {
		return outerRing;
	}
	
	public ResourceLocation innerRing() {
		return innerRing;
	}

	public static Builder builder(Item upgradeItem, Category category, String namespace) {
		return new Builder(upgradeItem, category, namespace);
	}

	public static final class Builder {
		private final Item upgradeItem;
		private final Category category;
		private final String namespace;
		private String name, outerRing, innerRing;
		private int stackMod, speedMod, slotRowMod;
		private float stackMult = 1, speedMult = 1;
		private boolean light, priority, triggerTransfer, lamp, sensor;
		private RedstoneStateModifier preProcessor = RedstoneStateModifier.PASS;
		private RedstoneStateModifier postProcessor = RedstoneStateModifier.PASS;

		public Builder(Item upgradeItem, Category category, String namespace) {
			this.upgradeItem = upgradeItem;
			this.category = category;
			this.namespace = namespace;
		}

		public Builder named(String name) {
			name(name);
			outerRing(name);
			innerRing(name);
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder outerRing(String name) {
			this.outerRing = OUTER_RING_BASE_PATH + name + ".png";
			return this;
		}

		public Builder innerRing(String name) {
			this.innerRing = INNER_RING_BASE_PATH + name + ".png";
			return this;
		}

		public Builder redstone(String name) {
			name(name);
			this.outerRing = REDSTONE_RING_BASE_PATH + name + ".png";
			return this;
		}

		public Builder stackMod(int stackMod) {
			this.stackMod = stackMod;
			return this;
		}

		public Builder speedMod(int speedMod) {
			this.speedMod = speedMod;
			return this;
		}

		public Builder slotRowMod(int slotRowMod) {
			this.slotRowMod = slotRowMod;
			return this;
		}

		public Builder stackMult(float stackMult) {
			this.stackMult = stackMult;
			return this;
		}

		public Builder speedMult(float speedMult) {
			this.speedMult = speedMult;
			return this;
		}

		public Builder light() {
			this.light = true;
			return this;
		}

		public Builder priority() {
			this.priority = true;
			return this;
		}

		public Builder triggerTransfer() {
			this.triggerTransfer = true;
			return this;
		}

		public Builder lamp() {
			this.lamp = true;
			return this;
		}

		public Builder sensor() {
			this.sensor = true;
			return this;
		}

		public Builder redstonePreProcess(RedstoneStateModifier modifier) {
			this.preProcessor = modifier;
			return this;
		}

		public Builder redstonePostProcess(RedstoneStateModifier modifier) {
			this.postProcessor = modifier;
			return this;
		}

		public PastelUpgradeSignature build() {
			verify();

			return new PastelUpgradeSignature(
					upgradeItem,
					name,
					ResourceLocation.fromNamespaceAndPath(namespace, outerRing),
					ResourceLocation.fromNamespaceAndPath(namespace, innerRing),
					stackMod,
					speedMod,
					slotRowMod,
					stackMult,
					speedMult,
					light,
					priority,
					triggerTransfer,
					lamp,
					sensor,
					category,
					preProcessor,
					postProcessor
			);
		}

		public PastelUpgradeSignature buildRedstone() {
			verify();

			return new PastelUpgradeSignature(
					upgradeItem,
					name,
					ResourceLocation.fromNamespaceAndPath(namespace, outerRing),
					null,
					0,
					0,
					0,
					0,
					0,
					false,
					false,
					triggerTransfer,
					lamp,
					sensor,
					category,
					preProcessor,
					postProcessor
			);
		}

		private void verify() {
			if (name == null)
				throw new IllegalStateException("PastelUpgradeSignature name can't be null");

			if (outerRing == null)
				throw new IllegalStateException("PastelUpgradeSignature main path can't be null");
		}
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

	public abstract static class Category {
		public abstract boolean compoundsWith(Category other);

		public static Category simple() {
			return new Category() {
				@Override
				public boolean compoundsWith(Category other) {
					return other == this;
				}
			};
		}

		public static Category nonCompounding() {
			return new Category() {
				@Override
				public boolean compoundsWith(Category other) {
					return false;
				}
			};
		}

		public static Category redstone() {
			return new Category() {
				@Override
				public boolean compoundsWith(Category other) {
					return false;
				}

				@Override
				public boolean isRedstone() {
					return true;
				}
			};
		}

		public boolean isRedstone() {
			return false;
		}
	}
}
