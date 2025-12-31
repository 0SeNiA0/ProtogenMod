package net.zaharenko424.protogenmod.transform;

import com.google.common.base.Functions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.protogenmod.ProtogenModTags;
import net.zaharenko424.protogenmod.mixin.entity.attribute.AttributeMapAccess;
import net.zaharenko424.protogenmod.mixin.entity.attribute.AttributeSupplierAccess;
import net.zaharenko424.protogenmod.network.packet.transform.SyncTransformPacket;
import net.zaharenko424.protogenmod.network.packet.transform.SyncTransformProgressPacket;
import net.zaharenko424.protogenmod.registry.AttachmentRegistry;
import net.zaharenko424.protogenmod.transform.event.TransformStartEvent;
import net.zaharenko424.protogenmod.transform.event.TransformStartedEvent;
import net.zaharenko424.protogenmod.transform.event.TransformedEvent;
import net.zaharenko424.protogenmod.transform.event.UnTransformedEvent;
import net.zaharenko424.protogenmod.util.AttributeUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransformHandler {

    public static TransformHandler of(LivingEntity entity){
        if (entity instanceof TransformEntity<?,?>) return null;
        if (!(entity instanceof Player) && !entity.getType().is(ProtogenModTags.Entity.TRANSFORMABLE)) return null;

        //Check supported entity
        return entity.getData(AttachmentRegistry.TRANSFORM_HANDLER);
    }

    public static TransformHandler of(Player player){
        return player.getData(AttachmentRegistry.TRANSFORM_HANDLER);
    }

    private final LivingEntity holder;

    private TransformType<?, ?> transformType;
    private TransformProgress transformProgress;
    private AbstractTransform<?, ?> transform;
    private Object2DoubleMap<Holder<Attribute>> baseValues;

    public TransformHandler(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)) throw new RuntimeException();

        this.holder = living;
    }

    void captureBaseValues(){
        if (baseValues == null){
           baseValues = new Object2DoubleOpenHashMap<>();
        } else baseValues.clear();

        AttributeMap holderAttributes = holder.getAttributes();
        for (Holder<Attribute> attrib : ((AttributeSupplierAccess) ((AttributeMapAccess)holderAttributes).getSupplier()).getInstances().keySet()){
            baseValues.put(attrib, holderAttributes.getBaseValue(attrib));
        }
    }

    void interpolateBaseValues(){
        boolean transformed = isTransformed(), transforming = isTransforming();
        float health = holder.getHealth() / holder.getMaxHealth();

        AttributeMap attributes = holder.getAttributes();
        if(transformed && transforming){
            //INFO interpolate curr transform & next transform
            AttributeUtil.mapBaseValues(attributes, transformType.getAttributes(), transformingInto().getAttributes(), transformProgress.getProgress());
        } else if (transformed){
            //INFO set base values from current transform or return
            AttributeUtil.mapBaseValues(attributes, attributes, transformType.getAttributes(), 1);
        } else if (transforming){
            //INFO interpolate baseValues & next transform //TODO make possible to transform into null (do 1 - progress to flip lerp direction)? - basically untransform over time
            interpolateBaseValues(attributes, baseValues, transformingInto().getAttributes(), transformProgress.getProgress());
        } else {
            //INFO set baseValues
            AttributeInstance instance;
            for (Holder<Attribute> attrib : baseValues.keySet()){
                instance = attributes.getInstance(attrib);
                if(instance != null) instance.setBaseValue(baseValues.getDouble(attrib));
            }
        }

        holder.setHealth(holder.getMaxHealth() * health);
    }

    void interpolateBaseValues(AttributeMap attributes, Object2DoubleMap<Holder<Attribute>> baseValues, AttributeSupplier transform, float delta){
        double newBase;
        AttributeInstance targetInstance;
        for (Holder<Attribute> attrib : baseValues.keySet()){
            targetInstance = attributes.getInstance(attrib);
            if(targetInstance == null) continue;

            if(transform.hasAttribute(attrib)){
                newBase = Mth.lerp(baseValues.getDouble(attrib), transform.getBaseValue(attrib), delta);
            } else {
                newBase = Mth.lerp(baseValues.getDouble(attrib), attrib.value().getDefaultValue(), delta);
            }

            targetInstance.setBaseValue(newBase);
        }
    }

    public boolean isTransforming(){
        return transformProgress != null;
    }

    public TransformType<?, ?> transformingInto(){
        return isTransforming() ? transformProgress.getTransformInto() : null;
    }

    public boolean isTransformed(){
        return transform != null;
    }

    public TransformType<?, ?> transformType(){
        return transformType;
    }

    public AbstractTransform<?, ?> transform(){
        return transform;
    }

    public void transform(TransformType<?, ?> transformType){
        if (transformType() == transformType) return;
        if (!(holder instanceof Player)) return;

        TransformStartEvent event = NeoForge.EVENT_BUS.post(new TransformStartEvent(holder, transformType, TransformProgress.DEF_DURATION));
        transformProgress = new TransformProgress(transformType, event.newDuration());

        if(!isTransformed()){//Let interpolation happen on next tick
            captureBaseValues();
        }

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, new SyncTransformProgressPacket(holder.getId(), transformProgress));

        NeoForge.EVENT_BUS.post(new TransformStartedEvent(holder, transformType, transformProgress.duration));
    }

    void finishTransform(TransformType<?, ?> transformInto){
        if (!(holder instanceof Player player)) {
            holder.discard();
            TransformEntity<?, ?> entity = transformInto.createEntity(holder.level());
            entity.moveTo(holder.getX(), holder.getY(), holder.getZ(), holder.getYRot(), holder.getXRot());
            holder.level().addFreshEntity(entity);

            NeoForge.EVENT_BUS.post(new TransformedEvent(holder, transformType, transformInto));
            return;
        }

        TransformType<?, ?> prev = transformType;

        transformType = transformInto;
        transformProgress = null;
        transform = transformType.createTransform(Either.right(player));

        interpolateBaseValues();

        syncClients();

        NeoForge.EVENT_BUS.post(new TransformedEvent(holder, prev, transformType));
    }

    public void untransform(){
        if (holder.level().isClientSide) return;
        if (!isTransformed() && !isTransforming()) return;

        TransformType<?, ?> type = transformType;

        transformType = null;
        transform = null;
        interpolateBaseValues();
        baseValues.clear();

        syncClients();

        NeoForge.EVENT_BUS.post(new UnTransformedEvent(holder, type));
    }

    public void tick(){
        if (!isTransforming()) return;

        if (transformProgress.getProgress() >= 1) {
            finishTransform(transformProgress.getTransformInto());
        } else {
            transformProgress.tick();
            interpolateBaseValues();
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, new SyncTransformProgressPacket(holder.getId(), transformProgress));
        }
    }

    //================================================================================================================//

    public void syncClients(){
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, new SyncTransformPacket(holder.getId(), transformType, transformProgress, transform != null ? transform.save() : null));
    }

    public void syncClient(ServerPlayer receiver){
        PacketDistributor.sendToPlayer(receiver, new SyncTransformPacket(holder.getId(), transformType, transformProgress, transform != null ? transform.save() : null));
    }

    @ApiStatus.Internal
    public void loadFromPacket(SyncTransformProgressPacket packet){
        if(!holder.level().isClientSide) return;

        //if(!isTransformed()) captureBaseValues();//TODO test if clientside lerp is needed
        transformProgress = packet.progress();
        //interpolateBaseValues();
    }

    @ApiStatus.Internal
    public void loadFromPacket(SyncTransformPacket packet){
        if(!holder.level().isClientSide) return;

        transformType = packet.transformType();
        transformProgress = packet.transformProgress();
        if(transformType != null && transform == null) transform = transformType.createTransform(Either.right((Player) holder));
        if(packet.transformData() != null) transform.load(packet.transformData());
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransformHandler> {

        public static final Serializer INSTANCE = new Serializer();

        private static final Codec<TransformType<?, ?>> TRANSFORM_TYPE_CODEC = TransformType.CODEC
                .fieldOf("transformType").codec();
        private static final Codec<TransformProgress> PROGRESS_CODEC = TransformProgress.CODEC
                .fieldOf("progress").codec();

        private static final Codec<Object2DoubleMap<Holder<Attribute>>> BASE_VALUES = Codec.unboundedMap(
                    Attribute.CODEC, Codec.DOUBLE
                )
                .<Object2DoubleMap<Holder<Attribute>>>xmap(Object2DoubleOpenHashMap::new, Functions.identity())
                .fieldOf("baseValues").codec();

        private Serializer(){}

        @Override
        public @NotNull TransformHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            TransformHandler handler = new TransformHandler(holder);

            DataResult<TransformType<?, ?>> tfType = TRANSFORM_TYPE_CODEC.parse(NbtOps.INSTANCE, tag);
            if(tfType.isSuccess()) handler.transformType = tfType.getOrThrow();

            DataResult<TransformProgress> progress = PROGRESS_CODEC.parse(NbtOps.INSTANCE, tag);
            if(progress.isSuccess()) handler.transformProgress = progress.getOrThrow();

            if(tag.contains("transform")) {
                handler.transform = handler.transformType.createTransform(Either.right((Player) handler.holder));

                CompoundTag transformData = tag.getCompound("transform");
                if(!transformData.isEmpty()) handler.transform.load(transformData);
            }

            if (tag.contains("baseValues")){
                DataResult<Object2DoubleMap<Holder<Attribute>>> baseValues = BASE_VALUES.parse(NbtOps.INSTANCE, tag);
                if(baseValues.hasResultOrPartial()) handler.baseValues = baseValues.getPartialOrThrow();
            } else if (!handler.isTransformed() && handler.isTransforming()){
                handler.captureBaseValues();//might capture wrong base values but still better than none
            }

            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransformHandler attachment, HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();

            if(attachment.transformType != null) TRANSFORM_TYPE_CODEC.encode(attachment.transformType, NbtOps.INSTANCE, tag);

            if(attachment.isTransforming()) PROGRESS_CODEC.encode(attachment.transformProgress, NbtOps.INSTANCE, tag);

            if(attachment.isTransformed()) {
                CompoundTag transformData = attachment.transform.save();
                if(transformData == null) transformData = new CompoundTag();
                tag.put("transform", transformData);
            }

            Object2DoubleMap<Holder<Attribute>> baseValues = attachment.baseValues;
            if(baseValues != null && !baseValues.isEmpty()) BASE_VALUES.encode(baseValues, NbtOps.INSTANCE, tag);

            return !tag.isEmpty() ? tag : null;
        }
    }
}
