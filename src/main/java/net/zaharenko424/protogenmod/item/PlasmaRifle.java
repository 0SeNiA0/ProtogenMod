package net.zaharenko424.protogenmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlasmaRifle extends AbstractWeapon {

    public PlasmaRifle(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return new InteractionResultHolder<>(InteractionResult.CONSUME_PARTIAL, stack);
    }

    @Override
    public void fire(LivingEntity shooter, ItemStack stack, InteractionHand hand) {
        shooter.sendSystemMessage(Component.literal("Test"));
        shooter.level().playSound(null, shooter, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }
}
