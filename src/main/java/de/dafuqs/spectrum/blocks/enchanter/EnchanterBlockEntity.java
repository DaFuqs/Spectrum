package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

public class EnchanterBlockEntity extends BlockEntity {
	
	public static final int INVENTORY_SIZE = 2; // 0: any itemstack, 1: Knowledge Drop;
	protected SimpleInventory inventory;
	@Nullable
	private Direction itemFacing;
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.ENCHANTER, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		if(nbt.contains("item_facing", NbtElement.STRING_TYPE)) {
			this.itemFacing = Direction.valueOf(nbt.getString("item_facing").toUpperCase(Locale.ROOT));
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		if(this.itemFacing != null) {
			nbt.putString("item_facing", this.itemFacing.toString().toUpperCase(Locale.ROOT));
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void setItemFacingDirection(Direction facingDirection) {
		this.itemFacing = facingDirection;
	}
	
	public Direction getItemFacingDirection() {
		// if placed via pipe or other sources
		return Objects.requireNonNullElse(this.itemFacing, Direction.NORTH);
	}
	
	public static void clientTick(World world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack experienceStack = enchanterBlockEntity.getInventory().getStack(1);
		if(!experienceStack.isEmpty() && experienceStack.getItem() instanceof ExperienceStorageItem) {
			int experience = ((ExperienceStorageItem) experienceStack.getItem()).getStoredExperience(experienceStack);
			int amount = Support.getExperienceOrbSizeForExperience(experience);
			
			if(world.random.nextInt(10) < amount) {
				float randomX = world.getRandom().nextFloat();
				float randomZ = world.getRandom().nextFloat();
				world.addParticle(SpectrumParticleTypes.LIME_SPARKLE_RISING, blockPos.getX() + randomX, blockPos.getY() + 2, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
			}
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
	
	}
	
}
