package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.item.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

public class ListItemTagsCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root, CommandBuildContext commandBuildContext) {
		LiteralCommandNode<CommandSourceStack> node = Commands.literal("list_item_tags")
				.executes((context) -> execute(context.getSource(), null))
				.build();
		
		ArgumentCommandNode<CommandSourceStack, ItemInput> targets = Commands.argument("item", ItemArgument.item(commandBuildContext))
				.executes((context) -> execute(context.getSource(), ItemArgument.getItem(context, "item"))).build();
		
		node.addChild(targets);
		root.addChild(node);
	}
	
	private static int execute(CommandSourceStack source, @Nullable ItemInput itemInput) {
		@Nullable ServerPlayer player = source.getPlayer();
		
		Item item;
		if (itemInput != null) {
			item = itemInput.getItem();
		} else if (player != null) {
			item = player.getMainHandItem().getItem();
		} else {
			item = Items.AIR;
		}
		
		if (item == Items.AIR) {
			return 1;
		}
		
		source.sendSuccess(() -> Component.translatable("commands.spectrum.list_item_tags.list", item.getDefaultInstance().getDisplayName()), true);
		Registry<Item> registry = source.getLevel().registryAccess().registry(Registries.ITEM).get();
		registry.getTags().forEach(tagKeyNamedPair -> {
			TagKey<Item> tag = tagKeyNamedPair.getSecond().key();
			boolean contained = tagKeyNamedPair.getSecond().contains(registry.wrapAsHolder(item));
			
			if (contained) {
				source.sendSuccess(() -> Component.literal(tag.location().toString()), true);
			}
		});
		
		return 0;
	}
	
}
