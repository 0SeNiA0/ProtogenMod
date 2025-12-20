package net.zaharenko424.protogenmod.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.zaharenko424.protogenmod.command.Transform;
import net.zaharenko424.protogenmod.transformation.AbstractTransform;
import net.zaharenko424.protogenmod.transformation.TransformHandler;
import net.zaharenko424.protogenmod.transformation.TransformUtil;

@EventBusSubscriber
public class CommonEvent {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        Transform.register(dispatcher);
    }

    @SubscribeEvent
    public static void onBreathe(LivingBreatheEvent event){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(event.getEntity());
        if(transform != null) transform.breathe(event);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(event.getEntity());
        if(transform != null) transform.tickTransform();
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event){
        if(!(event.getEntity() instanceof LivingEntity entity)) return;

        TransformHandler handler = TransformHandler.of(entity);
        if(handler != null) handler.tick();
    }
}
