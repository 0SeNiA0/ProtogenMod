package net.zaharenko424.protogenmod.transformation;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.protogenmod.network.packet.TransformPacket;
import net.zaharenko424.protogenmod.registry.AttachmentRegistry;
import net.zaharenko424.protogenmod.transformation.progression.TransformProgression;
import net.zaharenko424.protogenmod.util.AttributeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransformHandler {

    public static TransformHandler of(LivingEntity entity){
        if(entity instanceof TransformEntity<?,?>) return null;
        //Check supported entity
        return entity.getData(AttachmentRegistry.TRANSFORM_HANDLER);
    }

    private final LivingEntity holder;

    private TransformType<?, ?> transformType;
    private TransformProgression transformProgression;
    private AbstractTransform<?, ?> transform;
    private AttributeMap previous;//INFO needed for lerping attributes between transforms
    private AttributeMap attributes;

    public TransformHandler(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity living)) throw new RuntimeException();

        this.holder = living;
    }

    public boolean isTransforming(){
        return transformProgression != null;
    }

    public boolean isTransformed(){
        return transform != null;
    }

    public boolean isFullyTransformed(){
        return transformProgression == null && transform != null;
    }

    public TransformType<?, ?> transformType(){
        return transformType;
    }

    public AbstractTransform<?, ?> transform(){
        return transform;
    }

    public AttributeMap attributes(){
        return attributes;
    }

    public void transform(TransformType<?, ?> transformType){
        if(!(holder instanceof Player player)) return;

        AttributeMap playerAttribs = player.getAttributes();
        transform = this.transformType.createTransform(Either.right(player));

        if(player instanceof ServerPlayer sPlayer) PacketDistributor.sendToPlayer(sPlayer, new TransformPacket(true));

        attributes = AttributeUtil.mergeAttributes(playerAttribs, this.transformType.getAttributes());//Mainly needed on the client to make sure that no attribs are missing
    }

    public void untransform(){
        if(transform == null) return;

        if(holder instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new TransformPacket(false));
        }

        transformType = null;
        transform = null;
        attributes = null;
    }

    public void tick(){
        if(transformProgression != null) {
            if (transformProgression.getProgress() >= 1) {
                //finish full transform
                transformProgression = null;//INFO merge only if switching transform -> transform!
                AttributeUtil.mapBaseValuesAndAttributes(attributes, holder.getAttributes(), transformType.getAttributes(), 1);
                //merged = AttributeUtil.mergeAndMapAttributes(holder.getAttributes(), transformType.getAttributes(), 1);
            } else {
                transformProgression.tick();//TODO allow for transform -> transform transition?
                AttributeUtil.mapBaseValuesAndAttributes(attributes, holder.getAttributes(), transformType.getAttributes(), transformProgression.getProgress());
            }
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransformHandler> {

        public static final Serializer INSTANCE = new Serializer();

        public static final Codec<TransformType<?, ?>> TRANSFORM_TYPE_CODEC = TransformType.CODEC
                .fieldOf("transformType").codec();

        private Serializer(){}

        @Override
        public @NotNull TransformHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            TransformHandler handler = new TransformHandler(holder);

            DataResult<TransformType<?, ?>> tfType = TRANSFORM_TYPE_CODEC.parse(NbtOps.INSTANCE, tag);
            if(tfType.isSuccess()) handler.transformType = tfType.getOrThrow();

            DataResult<TransformProgression> progression = TransformProgression.CODEC.parse(NbtOps.INSTANCE, tag);
            if(progression.isSuccess()) handler.transformProgression = progression.getOrThrow();

            if(tag.contains("transform")) {
                handler.transform = handler.transformType.createTransform(Either.right((Player) handler.holder));

                CompoundTag transformData = tag.getCompound("transform");
                if(!transformData.isEmpty()) handler.transform.load(transformData);
            }

            if(handler.isTransforming() || handler.isTransformed()) {
                handler.attributes = AttributeUtil.mergeAttributes(handler.holder.getAttributes(), handler.transformType().getAttributes());
                if(handler.isTransforming()) AttributeUtil.mapBaseValuesAndAttributes(handler.attributes, handler.holder.getAttributes(), handler.transformType.getAttributes(), handler.transformProgression.getProgress());

                if (handler.isTransformed() && tag.contains("attributes"))
                    handler.attributes.load(tag.getList("attributes", Tag.TAG_COMPOUND));
            }

            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransformHandler attachment, HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();

            if(attachment.transformType != null) TRANSFORM_TYPE_CODEC.encode(attachment.transformType, NbtOps.INSTANCE, tag);

            if(attachment.isTransforming()) TransformProgression.CODEC.encode(attachment.transformProgression, NbtOps.INSTANCE, tag);

            if(attachment.isTransformed()) {
                CompoundTag transformData = attachment.transform.save();
                if(transformData == null) transformData = new CompoundTag();
                tag.put("transform", transformData);
            }

            if(attachment.isTransformed()) tag.put("attributes", attachment.attributes.save());

            return !tag.isEmpty() ? tag : null;
        }
    }
}
