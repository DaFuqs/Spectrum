package de.dafuqs.spectrum.enchantments;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.revelationary.Revelationary;
import de.dafuqs.spectrum.items.SpectrumMobSpawnerItem;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ResonanceEnchantment extends SpectrumEnchantment {
	
	protected static final HashMap<Item, Item> RESONANCE_DROPS = new HashMap<>();
	
	public ResonanceEnchantment(Enchantment.Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static boolean checkResonanceForSpawnerMining(World world, BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
		if (blockState.isIn(SpectrumBlockTags.SPAWNERS) && EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
				ItemStack itemStack = SpectrumMobSpawnerItem.toItemStack(mobSpawnerBlockEntity);
				
				Block.dropStack(world, pos, itemStack);
				world.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getMinPower(int level) {
		return 25;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 75;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != SpectrumEnchantments.FOUNDRY && other != Enchantments.FORTUNE;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof ShearsItem;
	}
	
	public static ItemStack applyResonance(ItemStack stack) {
		Item item = stack.getItem();
		if(RESONANCE_DROPS.containsKey(item)) {
			ItemStack convertedStack = RESONANCE_DROPS.get(item).getDefaultStack();
			convertedStack.setCount(stack.getCount());
			return convertedStack;
		} else {
			return stack;
		}
	}
	
	public static class ResonanceDropsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
		
		public static final ResonanceDropsDataLoader INSTANCE = new ResonanceDropsDataLoader();
		
		private ResonanceDropsDataLoader() {
			super(new Gson(), "resonance_drops");
		}
		
		@Override
		protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
			prepared.forEach((identifier, jsonElement) -> {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				Item itemIn = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "input")));
				Item itemOut = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "output")));
				
				if(itemIn != Items.AIR && itemOut != Items.AIR) {
					RESONANCE_DROPS.put(itemIn, itemOut);
				}
			});
		}
		
		@Override
		public Identifier getFabricId() {
			return new Identifier(Revelationary.MOD_ID, "resonance_drops");
		}
		
	}
	
}