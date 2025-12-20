package net.zaharenko424.protogenmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.protogenmod.transformation.TransformHandler;
import org.jetbrains.annotations.NotNull;

public class Transform {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("transform")
                        .requires(CommandSourceStack::isPlayer)
                        .executes(context -> execute(context.getSource().getPlayer()))
        );

        dispatcher.register(
                Commands.literal("untransform")
                        .requires(CommandSourceStack::isPlayer)
                        .executes(context -> execute1(context.getSource().getPlayer()))
        );
    }

    private static int execute(@NotNull ServerPlayer player){
        TransformHandler.of(player).transform(null);
        return Command.SINGLE_SUCCESS;
    }

    private static int execute1(@NotNull ServerPlayer player){
        TransformHandler.of(player).untransform();
        return Command.SINGLE_SUCCESS;
    }
}
