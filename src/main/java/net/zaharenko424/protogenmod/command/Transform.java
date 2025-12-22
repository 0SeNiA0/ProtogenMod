package net.zaharenko424.protogenmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import net.zaharenko424.protogenmod.transform.TransformHandler;
import net.zaharenko424.protogenmod.transform.TransformType;
import org.jetbrains.annotations.NotNull;

public class Transform {

    private static final SuggestionProvider<CommandSourceStack> suggestions = SuggestionProviders.register(
            ProtogenMod.resourceLoc("transform_types"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(TransformRegistry.TRANSFORM_REGISTRY.keySet().stream(), builder));

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        ArgumentBuilder<CommandSourceStack, ?> transform =
                Commands.argument("transform", ResourceLocationArgument.id())
                    .suggests(suggestions)
                    .executes(
                        context -> context.getSource().isPlayer() ? execute(ResourceLocationArgument.getId(context,"transform"), context.getSource().getPlayer()) : 0
                    )
                    .then(
                        Commands.argument("target", EntityArgument.player())
                                .executes(
                                        context -> execute(ResourceLocationArgument.getId(context,"transform"), EntityArgument.getPlayer(context,"target"))
                                )
                    );

        dispatcher.register(
                Commands.literal("transform")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(transform)
        );

        dispatcher.register(
                Commands.literal("tf")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(transform)
        );

        dispatcher.register(
                Commands.literal("untransform")
                        .requires(CommandSourceStack::isPlayer)
                        .executes(context -> execute1(context.getSource().getPlayer()))
        );
    }

    private static int execute(@NotNull ResourceLocation loc, @NotNull ServerPlayer player){
        TransformType<?, ?> transformType = TransformRegistry.TRANSFORM_REGISTRY.get(loc);
        if(transformType == null) return 0;

        TransformHandler.of(player).transform(transformType);
        return Command.SINGLE_SUCCESS;
    }

    private static int execute1(@NotNull ServerPlayer player){
        TransformHandler.of(player).untransform();
        return Command.SINGLE_SUCCESS;
    }
}
