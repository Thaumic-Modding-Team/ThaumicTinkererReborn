package mod.emt.thaumictinkerer.utils.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakePlayerTT extends FakePlayer {
    private final WorldServer worldServer;
    private TileEntity fakePlayerTile;

    public FakePlayerTT(WorldServer world, GameProfile name) {
        super(world, name);
        this.connection = new FakeNetHandlerTT(this);
        this.worldServer = world;
        this.fakePlayerTile = null;
    }

    @Nullable
    public TileEntity getFakePlayerTile() {
        return fakePlayerTile;
    }

    public void setFakePlayerTile(TileEntity tile) {
        this.fakePlayerTile = tile;
    }

    public void setTicksSinceLastSwing(int ticksSinceLastSwing) {
        this.ticksSinceLastSwing = ticksSinceLastSwing;
    }

    @Override
    public void displayGUIChest(@NotNull IInventory chestInventory) {}

    @Override
    public void displayGuiCommandBlock(@NotNull TileEntityCommandBlock commandBlock) {}

    @Override
    public void sendAllContents(@NotNull Container containerToSend, @NotNull NonNullList<ItemStack> itemsList) {}

    @Override
    public void onUpdate() {
        this.interactionManager.updateBlockRemoving();
    }

    @Override
    public float getEyeHeight() {
        return 0;
    }

    @Override
    protected void onNewPotionEffect(@NotNull PotionEffect id) {}

    @Override
    protected void onChangedPotionEffect(@NotNull PotionEffect id, boolean p_70695_2_) {}

    @Override
    protected void onFinishedPotionEffect(@NotNull PotionEffect effect) {}

    @Override
    protected void playEquipSound(@NotNull ItemStack stack) {}

    @Override
    public @NotNull WorldServer getServerWorld() {
        return this.worldServer;
    }
}
