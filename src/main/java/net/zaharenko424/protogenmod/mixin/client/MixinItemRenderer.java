package net.zaharenko424.protogenmod.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.protogenmod.event.ClientMod;
import net.zaharenko424.protogenmod.registry.ItemRegistry;
import net.zaharenko424.protogenmod.util.ItemTransformUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Unique
    private static final int ANIM_DURATION = 3;

    @Unique
    private float invAnim;
    @Unique
    private float deltaAccumulator;
    @Unique
    private float lastDelta;

    @Unique
    private void progressInvAnim(){
        if(Minecraft.getInstance().isPaused()) return;

        float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        if(delta == lastDelta) return;
        lastDelta = delta;

        float accumDecimals = deltaAccumulator % 1;
        if(delta > accumDecimals) {
            deltaAccumulator += Mth.abs(delta - accumDecimals);
        } else if (delta < accumDecimals){
            deltaAccumulator += (1 + delta) - accumDecimals;
        }// delta 0.2 accum 0.3 -> +0.9 | delta 0.56 accum 1.4 -> + 0.16

        if (deltaAccumulator > invAnim){
            invAnim = 0;
            deltaAccumulator = 0;
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;handleCameraTransforms(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemDisplayContext;Z)Lnet/minecraft/client/resources/model/BakedModel;"),
            method = "render")
    private BakedModel replaceItemTransform(PoseStack poseStack, BakedModel model, ItemDisplayContext cameraTransformType, boolean applyLeftHandTransform, Operation<BakedModel> original, @Local(argsOnly = true) ItemStack stack){
        if(!stack.is(ItemRegistry.THE_RIFLE)) return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);
        if(!cameraTransformType.firstPerson()){
            if(invAnim != 0) progressInvAnim();
            return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);
        }

        Player player = Minecraft.getInstance().player;
        if(player == null) return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);

        if(player.getUseItem() != stack){
            if(invAnim == 0) return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);

            progressInvAnim();
            if(invAnim == 0) return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);

            ItemTransformUtil.apply(applyLeftHandTransform ? ClientMod.RIFLE_FP_L : ClientMod.RIFLE_FP_R, model.getTransforms().getTransform(cameraTransformType), (1 - invAnim / ANIM_DURATION) + deltaAccumulator / ANIM_DURATION, poseStack, applyLeftHandTransform);

            return model;
        }

        int usingFor = player.getTicksUsingItem();

        ItemTransform itemTransform = applyLeftHandTransform ? ClientMod.RIFLE_FP_L : ClientMod.RIFLE_FP_R;
        if(usingFor < ANIM_DURATION){
            invAnim = usingFor + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
            ItemTransformUtil.apply(model.getTransforms().getTransform(cameraTransformType), itemTransform, invAnim / (float) ANIM_DURATION, poseStack, applyLeftHandTransform);
        } else {
            invAnim = ANIM_DURATION;
            ItemTransformUtil.apply(itemTransform, poseStack, applyLeftHandTransform);
        }
        deltaAccumulator = 0;

        return model;
    }
}
