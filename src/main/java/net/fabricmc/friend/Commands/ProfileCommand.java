package net.fabricmc.friend.Commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.friend.profiles.Profile;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ProfileCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("profile")
                .then(literal("open").then(argument("player", EntityArgumentType.player()).executes(ProfileCommand::openProfile))));
    }

    private static int openProfile(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        try {
            ctx.getSource().getPlayer().openHandledScreen(new Profile(1,ctx.getSource().getPlayer().inventory,(PlayerEntity) ctx.getSource().getPlayer(), new LiteralText("Testing")));
        }catch (NullPointerException err) {
            err.printStackTrace();
        }
        return 1;
    }


}
