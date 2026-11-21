package mod.emt.thaumictinkerer.core.mixins;

import com.google.common.base.Predicate;
import mod.emt.thaumictinkerer.item.bauble.ItemBlackHoleRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.common.items.casters.foci.FocusEffectExchange;
import thaumcraft.common.lib.events.ServerEvents;

@Mixin(value = FocusEffectExchange.class, remap = false)
public class FocusEffectExchangeMixin {
    /**
     * @author Invadermonky
     * @reason Allows the exchange spell effect to pull blocks from any Black Hole Rings in the player's inventory.
     */
    @Redirect(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lthaumcraft/common/lib/events/ServerEvents;addSwapper(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;ZILnet/minecraft/entity/player/EntityPlayer;ZZIZZILcom/google/common/base/Predicate;F)V"
            )
    )
    private void pullFromBlackHoleRing(World world, BlockPos pos, Object source, ItemStack target, boolean consumeTarget, int life, EntityPlayer player, boolean fx, boolean fancy, int color, boolean pickup, boolean silk, int fortune, Predicate<ServerEvents.SwapperPredicate> allowSwap, float visCost) {
        boolean fromRing = false;
        if(!player.isCreative()) {
            fromRing = ItemBlackHoleRing.extractStacksFromRings(player, target, 1, false) > 0;
        }
        ServerEvents.addSwapper(world, pos, source, target, !fromRing, life, player, fx, fancy, color, pickup, silk, fortune, allowSwap, visCost);
    }
}
