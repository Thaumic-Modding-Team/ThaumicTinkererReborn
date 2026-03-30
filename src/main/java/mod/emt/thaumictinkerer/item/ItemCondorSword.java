package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Objects;

public class ItemCondorSword extends ItemSword implements IItemAddition {
    public ItemCondorSword() {
        super(ThaumcraftMaterials.TOOLMAT_ELEMENTAL); // TODO: Unique tool material
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "condor_sword");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
        if(world.isRemote) {
            Vec3d vec = player.getLookVec();
            double velocity = 1.35D;

            if(player.isSneaking()) {
                velocity = -1.35D;
            }

            player.motionX = vec.x * velocity;
            player.motionY = vec.y * velocity;
            player.motionZ = vec.z * velocity;

            for(int a = 0; a < 20; ++a) {
                FXDispatcher.INSTANCE.smokeSpiral(player.posX, player.getEntityBoundingBox().minY + player.height / 2.0F, player.posZ, 1.5F, player.world.rand.nextInt(360), (int) (player.getEntityBoundingBox().minY - 2.0D), 14540253);
            }

            world.playSound(player, player.getPosition(), SoundsTC.wind, SoundCategory.PLAYERS, 0.5F, 1.5F + player.world.rand.nextFloat() * 0.2F);
        }

        player.fallDistance = 0;
        player.getHeldItem(hand).damageItem(2, player);
        player.getCooldownTracker().setCooldown(this, 5);
        player.swingArm(hand);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            EnumInfusionEnchantment.addInfusionEnchantment(stack, EnumInfusionEnchantment.ARCING, 3);

            if(ModEnchantsTT.EDUCATIONAL != null) {
                EnumInfusionEnchantment.addInfusionEnchantment(stack, ModEnchantsTT.EDUCATIONAL, 1);
            }

            items.add(stack);
        }
    }
}
