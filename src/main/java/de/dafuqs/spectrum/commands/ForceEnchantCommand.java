package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.commands.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.fml.loading.*;
import org.apache.commons.io.output.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;


public class ForceEnchantCommand {
	
	private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType((o) -> Component.translatableEscape("commands.enchant.failed.entity", o));
	private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType((o) -> Component.translatableEscape("commands.enchant.failed.itemless", o));
	private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));
	
	public static void register(LiteralCommandNode<CommandSourceStack> root, CommandBuildContext context) {
		LiteralCommandNode<CommandSourceStack> node = Commands.literal("force_enchant")
				.requires((source) -> source.hasPermission(4))
				.then(Commands.argument("targets", EntityArgument.entities())
						.then(((RequiredArgumentBuilder) Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT))
								.executes((c) -> enchant(c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getEnchantment(c, "enchantment"), 1)))
						.then(Commands.argument("level", IntegerArgumentType.integer(0))
								.executes((c) -> enchant(c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getEnchantment(c, "enchantment"), IntegerArgumentType.getInteger(c, "level"))))))
				.build();
		root.addChild(node);
	}
	
	private static int enchant(CommandSourceStack source, Collection<? extends Entity> targets, Holder<Enchantment> enchantment, int level) throws CommandSyntaxException {
		int i = 0;
		
		for(Entity entity : targets) {
			if (entity instanceof LivingEntity livingentity) {
				ItemStack stack = livingentity.getMainHandItem();
				if (!stack.isEmpty()) {
					if(!stack.supportsEnchantment(enchantment)) {
						source.sendSuccess(() -> Component.translatable("commands.spectrum.force_enchant.enchantment_not_supported", stack.getDisplayName().getString(), enchantment.getRegisteredName()), true);
					}
					if(!EnchantmentHelper.isEnchantmentCompatible(stack.getEnchantments().keySet(), enchantment)) {
						source.sendSuccess(() -> Component.translatable("commands.spectrum.force_enchant.has_incompatible_enchantments", stack.getDisplayName().getString()), true);
					}
					
					Tuple<Boolean, ItemStack> result = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(stack, enchantment, level, true, true);
					livingentity.setItemInHand(InteractionHand.MAIN_HAND, result.getB());
					
					i++;
				} else if (targets.size() == 1) {
					throw ERROR_NO_ITEM.create(livingentity.getName().getString());
				}
			} else if (targets.size() == 1) {
				throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
			}
		}
		
		if (i == 0) {
			throw ERROR_NOTHING_HAPPENED.create();
		} else {
			if (targets.size() == 1) {
				source.sendSuccess(() -> Component.translatable("commands.enchant.success.single", Enchantment.getFullname(enchantment, level), targets.iterator().next().getDisplayName()), true);
			} else {
				source.sendSuccess(() -> Component.translatable("commands.enchant.success.multiple", Enchantment.getFullname(enchantment, level), targets.size()), true);
			}
			
			return i;
		}
	}
	
}
