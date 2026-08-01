package de.dafuqs.spectrum.compat.botania;

import com.mojang.datafixers.util.*;
import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.registries.*;
import vazkii.botania.common.item.*;

import java.util.function.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.simple;
import static de.dafuqs.spectrum.registries.SpectrumItems.*;

@SuppressWarnings("unused")
public class BotaniaCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static DeferredItem<Item> LEAST_BLACK_LOTUS = SpectrumItems.register("least_black_lotus", () -> new LeastBlackLotusItem(IS.of()));
	public static DeferredItem<Item> BLACKEST_LOTUS = SpectrumItems.register("blackest_lotus", () -> new BlackestLotusItem(IS.of()));
	
	@Override
	public void register(IEventBus modBus) {
		NeoForge.EVENT_BUS.addListener(BotaniaCompat::addItemsToSubTabs);
	}
	
	@SubscribeEvent
	public static void addItemsToSubTabs(CreativeSubTabEvent event) {
		ResourceLocation subGroupId = event.subGroup().getIdentifier();
		
		if (subGroupId.equals(ItemGroupIDs.SUBTAB_EQUIPMENT)) {
			event.getItemDisplayBuilder().accept(LEAST_BLACK_LOTUS);
			event.getItemDisplayBuilder().accept(BLACKEST_LOTUS);
		}
	}
	
	@Override
	public void registerClient(FMLClientSetupEvent event) {
	
	}
}
