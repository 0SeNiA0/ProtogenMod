package net.zaharenko424.protogenmod.transform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class TransformProgress {

    public static final int DEF_DURATION = 200;

    public static final Codec<TransformProgress> CODEC = RecordCodecBuilder.create(a ->
            a.group(TransformType.CODEC.fieldOf("transformInto").forGetter(tp -> tp.transformInto),
                    Codec.INT.fieldOf("duration").forGetter(tp -> tp.duration),
                    Codec.INT.fieldOf("ticks").forGetter(tp -> tp.ticks))
                    .apply(a, TransformProgress::new));

    public static final StreamCodec<FriendlyByteBuf, TransformProgress> STR_CODEC = StreamCodec.composite(
            TransformType.STR_CODEC, tp -> tp.transformInto,
            ByteBufCodecs.VAR_INT, tp -> tp.duration,
            ByteBufCodecs.VAR_INT, tp -> tp.ticks,
            TransformProgress::new
    );

    protected final TransformType<?, ?> transformInto;
    protected int duration;
    protected int ticks;

    public TransformProgress(TransformType<?, ?> transformInto, int duration){
        this(transformInto, duration, 0);
    }

    public TransformProgress(TransformType<?, ?> transformInto, int duration, int ticks){
        this.transformInto = transformInto;
        this.duration = duration;
        this.ticks = ticks;
    }

    public TransformType<?, ?> getTransformInto() {
        return transformInto;
    }

    public float getProgress(){
        return duration == 0 ? 1 : Math.min((float) ticks / duration, 1);
    }

    public void tick(){
        ticks++;
    }
}
