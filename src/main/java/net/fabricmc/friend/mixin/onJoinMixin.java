package net.fabricmc.friend.mixin;

import net.fabricmc.friend.FriendList;
import net.fabricmc.friend.FriendListManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerManager.class)
public class onJoinMixin<class_3222> {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void injectMethod(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        System.out.println("Player connected");
        FriendList temp = new FriendList();
        FriendListManager.appendNewFriendList(player.getUuid(), temp);
        System.out.println("Created new list for " + player.getUuid().toString());
    }
}