package net.fabricmc.friend;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


/*
    some breaking problems are if a user cache name expires, the whole thing breaks
    at the moment i have no idea how to fix this besides saving player names ourselves
 */

public class FriendCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("friend")
                .then(literal("accept").then(argument("player", GameProfileArgumentType.gameProfile()).executes(FriendCommand::acceptFriend)))
                .then(literal("add").then(argument("playerName", GameProfileArgumentType.gameProfile()).executes(FriendCommand::registerFriendAdd)))
                .then(literal("list").executes(FriendCommand::executeListFriends))
                .then(literal("requests").executes(FriendCommand::executeListRequests)));
    }

    private static int executeListRequests(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecraftServer server = context.getSource().getMinecraftServer();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UserCache userCache = server.getUserCache();

        if (FriendListManager.getFriendList(context.getSource().getPlayer().getUuid()).getRequests().size() <= 0) {
            context.getSource().sendFeedback(new LiteralText("No incoming friend requests").formatted(Formatting.GREEN), false);
            return Command.SINGLE_SUCCESS;
        }

        int i = 0;
        context.getSource().sendFeedback(new LiteralText("### incoming friend requests ###").formatted(Formatting.GREEN), false);
        for (UUID ids : FriendListManager.getFriendList(context.getSource().getPlayer().getUuid()).getRequests()) {
            i++;
            context.getSource().sendFeedback(new LiteralText("[" + i + "] Request from " + userCache.getByUuid(ids).getName()).formatted(Formatting.GREEN), false);
        }
        context.getSource().sendFeedback(new LiteralText("### incoming friend requests ###").formatted(Formatting.GREEN), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeListFriends(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecraftServer server = context.getSource().getMinecraftServer();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UserCache uCache = server.getUserCache();

        if (FriendListManager.getFriendList(context.getSource().getPlayer().getUuid()).getFriends().size() <= 0) {
            context.getSource().sendFeedback(new LiteralText("You have no friends...").formatted(Formatting.GREEN), false);
            return Command.SINGLE_SUCCESS;
        }

        int i = 0;
        FriendList friendList = FriendListManager.getFriendList(context.getSource().getPlayer().getUuid());

        for (UUID ids : friendList.getFriends()) {
            i++;
            context.getSource().sendFeedback(new LiteralText("[" + i + "] " + uCache.getByUuid(ids).getName()).formatted(Formatting.GREEN), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int acceptFriend(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // this may break if more then one GameProfile is specified
        for (GameProfile Profile : GameProfileArgumentType.getProfileArgument(context, "player")) {
            // TODO: prettify this code
            FriendList target = FriendListManager.getFriendList(Profile.getId());
            FriendList sender = FriendListManager.getFriendList(context.getSource().getPlayer().getUuid());

            if (FriendListManager.getFriendList(context.getSource().getPlayer().getUuid()).hasSentRequest(Profile.getId())) {
                sender.addFriend(Profile.getId());
                target.addFriend(context.getSource().getPlayer().getUuid());
                sender.removeRequest(Profile.getId()); // not needed but you never know
                target.removeRequest(context.getSource().getPlayer().getUuid());
                context.getSource().sendFeedback(new LiteralText("You accepted " + Profile.getName() + "friend request.").formatted(Formatting.GREEN), false);
                context.getSource().getMinecraftServer().getPlayerManager().getPlayer(Profile.getId()).sendMessage(new LiteralText(context.getSource().getName() + " accepted your friend request"), MessageType.SYSTEM, context.getSource().getPlayer().getUuid());
            } else {
                context.getSource().sendFeedback(new LiteralText("There are no incoming requests from that player"), false);
            }
            return Command.SINGLE_SUCCESS;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int registerFriendAdd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSender = context.getSource().getPlayer();
        MinecraftServer server = context.getSource().getMinecraftServer();

        for (GameProfile Profile : GameProfileArgumentType.getProfileArgument(context, "playerName")) {
            if (Profile.equals(context.getSource().getPlayer().getGameProfile())) {
                context.getSource().sendFeedback(new LiteralText("You can't friend yourself silly!").formatted(Formatting.GREEN), false);
                return Command.SINGLE_SUCCESS;
            }
            if (FriendListManager.getFriendList(commandSender.getUuid()).hasFriend(Profile.getId())) {
                context.getSource().sendFeedback(new LiteralText("You are already friends with that player").formatted(Formatting.GREEN), false);
                return Command.SINGLE_SUCCESS;
            }
            if (FriendListManager.getFriendList(Profile.getId()).addRequest(commandSender.getUuid())) {
                context.getSource().sendFeedback(new LiteralText("You sent a request to " + Profile.getName()), false);
                server.getPlayerManager().getPlayer(Profile.getId()).sendMessage(new LiteralText("You received a friend request from " + server.getUserCache().getByUuid(commandSender.getUuid()).getName()).formatted(Formatting.GREEN), MessageType.SYSTEM, commandSender.getUuid());
            } else {
                context.getSource().sendFeedback(new LiteralText("You already sent that player a request.").formatted(Formatting.GREEN), false);
                return 0;
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
