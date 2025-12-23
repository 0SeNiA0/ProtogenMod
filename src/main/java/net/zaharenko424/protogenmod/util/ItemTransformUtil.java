package net.zaharenko424.protogenmod.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.TransformationHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ItemTransformUtil {

    private static final ThreadLocal<Quaternionf> QUAT = ThreadLocal.withInitial(Quaternionf::new);
    private static final ThreadLocal<Quaternionf> QUAT1 = ThreadLocal.withInitial(Quaternionf::new);
    private static final ThreadLocal<Quaternionf> QUAT2 = ThreadLocal.withInitial(Quaternionf::new);

    public static void apply(ItemTransform transform, PoseStack stack, boolean leftHand){
        if (transform == ItemTransform.NO_TRANSFORM) return;

        Vector3f rotation = transform.rotation;
        Vector3f translation = transform.translation;
        Vector3f scale = transform.scale;
        Vector3f rightRotation = transform.rightRotation;

        int handMul = leftHand ? -1 : 1;
        stack.translate(handMul * translation.x(), translation.y(), translation.z());
        stack.mulPose(quatFromXYZ(rotation.x(), rotation.y() * handMul, rotation.z() * handMul, true, QUAT.get()));
        stack.scale(scale.x(), scale.y(), scale.z());
        stack.mulPose(quatFromXYZ(rightRotation.x(), rightRotation.y() * handMul, rightRotation.z() * handMul, true, QUAT.get()));
    }

    public static void apply(ItemTransform transform, ItemTransform other, float delta, PoseStack stack, boolean leftHand){
        if (transform == ItemTransform.NO_TRANSFORM) return;

        Vector3f rotation = transform.rotation;
        Vector3f translation = transform.translation;
        Vector3f scale = transform.scale;
        Vector3f rightRotation = transform.rightRotation;

        Vector3f otherRotation = other.rotation;
        Vector3f otherTranslation = other.translation;
        Vector3f otherScale = other.scale;
        Vector3f otherRightRotation = other.rightRotation;

        int handMul = leftHand ? -1 : 1;
        stack.translate(handMul * Mth.lerp(delta, translation.x(), otherTranslation.x()), Mth.lerp(delta, translation.y(), otherTranslation.y()), Mth.lerp(delta, translation.z(), otherTranslation.z()));

        stack.mulPose(quatFromXYZ(rotation.x(), rotation.y() * handMul, rotation.z() * handMul, true, QUAT.get())
                        .slerp(quatFromXYZ(otherRotation.x(), otherRotation.y() * handMul, otherRotation.z() * handMul, true, QUAT1.get()), delta, QUAT2.get()));

        stack.scale(Mth.lerp(delta, scale.x(), otherScale.x()), Mth.lerp(delta, scale.y(), otherScale.y()), Mth.lerp(delta, scale.z(), otherScale.z()));

        stack.mulPose(quatFromXYZ(rightRotation.x(), rightRotation.y() * handMul, rightRotation.z() * handMul, true, QUAT.get())
                .slerp(quatFromXYZ(otherRightRotation.x(), otherRightRotation.y() * handMul, otherRightRotation.z() * handMul, true, QUAT1.get()), delta, QUAT2.get()));
    }

    /**
     * Copy of {@link TransformationHelper#quatFromXYZ} with reusable Quaternionf.
     */
    public static Quaternionf quatFromXYZ(float x, float y, float z, boolean degrees, Quaternionf dest) {
        float conversionFactor = degrees ? Mth.DEG_TO_RAD : 1;
        return dest.rotationXYZ(x * conversionFactor, y * conversionFactor, z * conversionFactor);
    }
}
