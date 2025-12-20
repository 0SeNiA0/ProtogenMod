package net.zaharenko424.protogenmod.transformation.progression;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class TransformProgression {

    public static final Codec<TransformProgression> CODEC = RecordCodecBuilder.create(a ->
            a.group(Codec.INT.fieldOf("duration").forGetter(tp -> tp.duration),
                    Codec.INT.fieldOf("ticks").forGetter(tp -> tp.ticks))
                    .apply(a, TransformProgression::new));

    protected int duration;
    protected int ticks;

    public TransformProgression(int duration){
        this(duration, 0);
    }

    public TransformProgression(int duration, int ticks){
        this.duration = duration;
        this.ticks = ticks;
    }

    public float getProgress(){
        return duration == 0 ? 1 : Math.min((float) ticks / duration, 1);
    }

    public void tick(){
        ticks++;
    }
}
