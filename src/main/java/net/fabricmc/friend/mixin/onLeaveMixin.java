package net.fabricmc.friend.mixin;

import net.fabricmc.friend.FriendListManager;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class onLeaveMixin {
    @Inject(method = "remove", at = @At("RETURN"))
    private void injectMethod(ServerPlayerEntity player, CallbackInfo info) {
        FriendListManager.removeFriendList(player.getUuid());
    }
}
