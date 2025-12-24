package net.zaharenko424.protogenmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zaharenko424.protogenmod.entity.projectile.PlasmaBolt;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlasmaBoltRenderer extends EntityRenderer<PlasmaBolt> {

    private final ModelPart root;

    public PlasmaBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        root = new ModelPart(List.of(new ModelPart.Cube(0, 0, -1f, -1f, -2f, 2f, 2f, 4f, 0, 0, 0, false, 0, 0, Set.of(Direction.values()))), Map.of());
    }

    @Override
    public void render(@NotNull PlasmaBolt plasmaBolt, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        root.resetPose();
        root.y = 2f;
        root.xRot = Mth.DEG_TO_RAD * plasmaBolt.getXRot();
        root.yRot = Mth.DEG_TO_RAD * plasmaBolt.getYRot();
        root.render(poseStack, bufferSource.getBuffer(RenderType.gui()), packedLight, OverlayTexture.NO_OVERLAY, -16711681);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PlasmaBolt entity) {
        return null;
    }
}
