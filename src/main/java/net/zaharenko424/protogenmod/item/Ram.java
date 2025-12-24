package net.zaharenko424.protogenmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import net.zaharenko424.protogenmod.transform.TransformType;
import net.zaharenko424.protogenmod.transform.TransformUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Ram extends Item implements PlayerAwareHoverText {

    public Ram(Properties properties, FoodProperties food) {
        super(properties.food(food));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        TransformType<?, ?> transformType = TransformUtil.typeOf(player);
        if (!TransformRegistry.PROTOGEN.get().is(transformType)) return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @Nullable Player player, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (player == null || !TransformRegistry.PROTOGEN.get().is(TransformUtil.typeOf(player))) {
            tooltipComponents.add(Component.translatable("tooltip.protogenmod.ram_alt"));
        } else tooltipComponents.add(Component.translatable("tooltip.protogenmod.ram"));
    }
}
