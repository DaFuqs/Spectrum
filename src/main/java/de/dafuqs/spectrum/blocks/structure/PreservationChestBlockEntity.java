package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

public class PreservationChestBlockEntity extends SpectrumChestBlockEntity {
	
	private UUIDMemory uuidMemory = new UUIDMemory();
	private @Nullable ResourceLocation requiredAdvancementIdentifierToOpen;
	private @Nullable Vec3i controllerOffset;
	
	public PreservationChestBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_CHEST.get(), pos, state);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.requiredAdvancementIdentifierToOpen != null) {
			tag.putString("RequiredAdvancement", this.requiredAdvancementIdentifierToOpen.toString());
		}
		
		if (this.controllerOffset != null) {
			tag.putInt("ControllerOffsetX", this.controllerOffset.getX());
			tag.putInt("ControllerOffsetY", this.controllerOffset.getY());
			tag.putInt("ControllerOffsetZ", this.controllerOffset.getZ());
		}
		
		tag.put("player_memory", this.uuidMemory.toNbt());
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.preservation_chest");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		if (tag.contains("RequiredAdvancement", Tag.TAG_STRING)) {
			this.requiredAdvancementIdentifierToOpen = ResourceLocation.tryParse(tag.getString("RequiredAdvancement"));
		}
		if (tag.contains("ControllerOffsetX")) {
			this.controllerOffset = new Vec3i(tag.getInt("ControllerOffsetX"), tag.getInt("ControllerOffsetY"), tag.getInt("ControllerOffsetZ"));
		}
		if(tag.contains("player_memory")) {
			this.uuidMemory = UUIDMemory.fromNbt(tag.getCompound("player_memory"));
		}
	}
	
	@Override
	public void onCloseSpectrum() {
		if (level instanceof ServerLevel serverWorld && controllerOffset != null) {
			BlockEntity blockEntity = serverWorld.getBlockEntity(Support.directionalOffset(this.worldPosition, this.controllerOffset, serverWorld.getBlockState(this.worldPosition).getValue(PreservationControllerBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				controller.openExit();
			}
		}
	}
	
	// Generate new loot for each player that has never opened this chest before
	@Override
	public void unpackLootTable(@Nullable Player player) {
		if (player != null && this.lootTable != null && this.getLevel() != null && !this.uuidMemory.hasUUID(player)) {
			supplyInventory(player);
			this.uuidMemory.addUUID(player);
			this.setChanged();
		}
	}
	
	public void supplyInventory(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			LootTable lootTable = serverPlayer.serverLevel().getServer().reloadableRegistries().getLootTable(this.lootTable);
			var builder = new LootParams.Builder(serverPlayer.serverLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
			builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
			lootTable.fill(this, builder.create(LootContextParamSets.CHEST), lootTableSeed);
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable);
			}
		}
	}
	
	public boolean canOpen(Player player) {
		if (this.requiredAdvancementIdentifierToOpen == null) {
			return true;
		} else {
			return AdvancementHelper.hasAdvancement(player, this.requiredAdvancementIdentifierToOpen);
		}
	}
	
	@Override
	public int getContainerSize() {
		return 27;
	}
	
}
