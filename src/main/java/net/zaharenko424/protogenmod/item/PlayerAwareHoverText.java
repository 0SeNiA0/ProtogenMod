package net.zaharenko424.protogenmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PlayerAwareHoverText {

    void appendHoverText(ItemStack stack, Item.TooltipContext context, @Nullable Player player, List<Component> tooltipComponents, TooltipFlag tooltipFlag);
}
