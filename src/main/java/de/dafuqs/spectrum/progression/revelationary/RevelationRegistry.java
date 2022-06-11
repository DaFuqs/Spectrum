package de.dafuqs.spectrum.progression.revelationary;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.RevelationAware;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RevelationRegistry {
    
    private static final Map<Identifier, List<BlockState>> BLOCK_ADVANCEMENT_REGISTRY = new HashMap<>();
    private static final Map<BlockState, BlockState> BLOCK_STATE_REGISTRY = new HashMap<>();
    
    private static final Map<Identifier, List<Item>> ITEM_ADVANCEMENT_REGISTRY = new HashMap<>();
    private static final Map<Item, Item> ITEM_REGISTRY = new HashMap<>();

    public static void clear() {
        //ADVANCEMENT_REGISTRY.clear();
        //BLOCK_STATE_REGISTRY.clear();
        //ITEM_REGISTRY.clear();
    }
    
    public static boolean hasCloak(Item item) {
        return ITEM_REGISTRY.containsKey(item);
    }
    
    public static boolean hasCloak(BlockState blockState) {
        return BLOCK_STATE_REGISTRY.containsKey(blockState);
    }
    
    @Nullable
    public static Item getCloak(Item item) {
        return ITEM_REGISTRY.getOrDefault(item, null);
    }

     @Nullable
    public static BlockState getCloak(BlockState blockState) {
        return BLOCK_STATE_REGISTRY.getOrDefault(blockState, null);
    }
    
    private static final Map<RevelationAware, Identifier> notedCloakables = new HashMap<>();
    public static void registerCloakable(RevelationAware revelationAware, Identifier advancementIdentifier) {
        notedCloakables.put(revelationAware, advancementIdentifier);
    }
    
    public static void registerCloakables() {
        for(Map.Entry<RevelationAware, Identifier> entry : notedCloakables.entrySet()) {
            RevelationAware revelationAware = entry.getKey();
            Identifier advancementIdentifier = entry.getValue();
            
            for (Map.Entry<BlockState, BlockState> states : revelationAware.getBlockStateCloaks().entrySet()) {
                registerBlockState(advancementIdentifier, states.getKey(), states.getValue());
            }
            Pair<Item, Item> item = revelationAware.getItemCloak();
            if (item != null) {
                registerItem(advancementIdentifier, item.getLeft(), item.getRight());
            }
        }
        
        notedCloakables.clear();
    }
    
    public static void registerFromJson(JsonObject jsonObject) {
        Identifier advancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "advancement"));
        
        for(Map.Entry<String, JsonElement> stateEntry : jsonObject.get("block_states").getAsJsonObject().entrySet()) {
            try {
                BlockState sourceBlockState = new BlockArgumentParser(new StringReader(stateEntry.getKey()), true).parse(false).getBlockState();
                BlockState targetBlockState = new BlockArgumentParser(new StringReader(stateEntry.getValue().getAsString()), true).parse(false).getBlockState();
    
                registerBlockState(advancementIdentifier, sourceBlockState, targetBlockState);
            } catch (Exception e) {
                SpectrumCommon.logError("Error parsing block state: " + e);
            }
        }
        for(Map.Entry<String, JsonElement> itemEntry : jsonObject.get("items").getAsJsonObject().entrySet()) {
            Identifier sourceId = Identifier.tryParse(itemEntry.getKey());
            Identifier targetId = Identifier.tryParse(itemEntry.getValue().getAsString());
            
            Item sourceItem = Registry.ITEM.get(sourceId);
            Item targetItem = Registry.ITEM.get(targetId);
    
            registerItem(advancementIdentifier, sourceItem, targetItem);
        }
    }
    
    private static void registerBlockState(Identifier advancementIdentifier, BlockState sourceBlockState, BlockState targetBlockState) {
        List<BlockState> list;
        if (BLOCK_ADVANCEMENT_REGISTRY.containsKey(advancementIdentifier)) {
            list = BLOCK_ADVANCEMENT_REGISTRY.get(advancementIdentifier);
            list.add(sourceBlockState);
        } else {
            list = new ArrayList<>();
            list.add(sourceBlockState);
            BLOCK_ADVANCEMENT_REGISTRY.put(advancementIdentifier, list);
        }
    
        Item sourceBlockItem = sourceBlockState.getBlock().asItem();
        if(sourceBlockItem != Items.AIR) {
            Item targetBlockItem = targetBlockState.getBlock().asItem();
            if(targetBlockItem != Items.AIR) {
                registerItem(advancementIdentifier, sourceBlockItem, targetBlockItem);
            }
        }
        
        BLOCK_STATE_REGISTRY.put(sourceBlockState, targetBlockState);
    }
    
    private static void registerItem(Identifier advancementIdentifier, Item sourceItem, Item targetItem) {
        if(ITEM_ADVANCEMENT_REGISTRY.containsKey(advancementIdentifier)) {
            List<Item> list = ITEM_ADVANCEMENT_REGISTRY.get(advancementIdentifier);
            if(list.contains(sourceItem)) {
                return;
            }
            list.add(sourceItem);
        } else {
            List<Item> list = new ArrayList<>();
            list.add(sourceItem);
            ITEM_ADVANCEMENT_REGISTRY.put(advancementIdentifier, list);
        }
        ITEM_REGISTRY.put(sourceItem, targetItem);
    }
    
    public static @NotNull Collection<Item> getRevealedItems(Identifier advancement) {
        List<Item> items = new ArrayList<>();
        if(ITEM_ADVANCEMENT_REGISTRY.containsKey(advancement)) {
            for (Object entry : ITEM_ADVANCEMENT_REGISTRY.get(advancement)) {
                if (entry instanceof Item item) {
                    items.add(item);
                }
            }
        }
        return items;
    }
    
    public static @NotNull Collection<BlockState> getRevealedBlockStates(Identifier advancement) {
        List<BlockState> blockStates = new ArrayList<>();
        if(BLOCK_ADVANCEMENT_REGISTRY.containsKey(advancement)) {
            for (Object entry : BLOCK_ADVANCEMENT_REGISTRY.get(advancement)) {
                if (entry instanceof BlockState blockState) {
                    blockStates.add(blockState);
                }
            }
        }
        return blockStates;
    }
    
    public static Map<Identifier, List<BlockState>> getBlockStateEntries() {
        return BLOCK_ADVANCEMENT_REGISTRY;
    }
    
    public static List<BlockState> getBlockStateEntries(Identifier advancement) {
        return BLOCK_ADVANCEMENT_REGISTRY.getOrDefault(advancement, Collections.EMPTY_LIST);
    }
    
    public static List<Block> getBlockEntries() {
        List<Block> blocks = new ArrayList<>();
        for(List<BlockState> states : BLOCK_ADVANCEMENT_REGISTRY.values()) {
            for (BlockState state : states) {
                Block block = state.getBlock();
                if (!blocks.contains(block)) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
    
    public static List<Block> getBlockEntries(Identifier advancement) {
        if(BLOCK_ADVANCEMENT_REGISTRY.containsKey(advancement)) {
            List<BlockState> states = BLOCK_ADVANCEMENT_REGISTRY.get(advancement);
            List<Block> blocks = new ArrayList<>();
            for(BlockState state : states) {
                Block block = state.getBlock();
                if(!blocks.contains(block)) {
                    blocks.add(block);
                }
            }
            return blocks;
        } else {
            return new ArrayList<>();
        }
    }
    
    public static Map<Identifier, List<Item>> getItemEntries() {
        return ITEM_ADVANCEMENT_REGISTRY;
    }
    
    public static List<Item> getItemEntries(Identifier advancement) {
        return ITEM_ADVANCEMENT_REGISTRY.getOrDefault(advancement, Collections.EMPTY_LIST);
    }

}