package net.fabricmc.friend.profiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class Profile implements NamedScreenHandlerFactory {
    private Text title;
    public Profile(int id, PlayerInventory inv, PlayerEntity player, Text title) {
        this.createMenu(id,inv,player);
        this.title = title;
    }
    @Override
    public Text getDisplayName() {
        return this.title;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, inv,new ProfileInventory(),5) {
            @Override
            public ItemStack onSlotClick(int slot, int data, SlotActionType action, PlayerEntity player) {
                return super.onSlotClick(slot, data, action, player);
            }
        };
    }
}
