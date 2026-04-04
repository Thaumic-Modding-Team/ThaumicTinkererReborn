package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.SoundsTC;

import java.util.Random;

public class ItemEscapeSigil extends AbstractItemAddition {
    public ItemEscapeSigil() {
        super("escape_sigil");
        this.setMaxStackSize(16);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if(!this.teleportUp(world, player)) {
            this.teleportRandomly(world, player);
        }
        if(!player.isCreative()) {
            heldStack.shrink(1);
        }
        player.playSound(SoundsTC.tool, 1.0F, 1.0F);
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    public boolean teleportUp(World world, EntityPlayer player) {
        BlockPos tpPos = player.getPosition();
        if(world.canSeeSky(tpPos)) {
            return false;
        }

        int horizontalAttempts = 0;
        while(!world.isOutsideBuildHeight(tpPos) || horizontalAttempts > 10) {
            if(world.getBlockState(tpPos).getBlock() == Blocks.BEDROCK) {
                return false;
            } else if(this.isValidPosition(player, world, tpPos)) {
                if(world.getBlockState(tpPos).getMaterial() == Material.LAVA) {
                    tpPos = this.shiftPositionHorizontally(world, tpPos);
                    horizontalAttempts++;
                } else {
                    return this.teleportTo(player, tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5);
                }
            } else {
                tpPos = tpPos.up();
            }
        }
        return false;
    }

    public boolean isValidPosition(EntityPlayer player, World world, BlockPos pos) {
        return world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty() && world.canBlockSeeSky(pos);
    }

    public BlockPos shiftPositionHorizontally(World world, BlockPos pos) {
        int xShift = (world.rand.nextInt(16) + 4) - 10;
        int zShift = (world.rand.nextInt(16) + 4) - 10;
        return pos.add(xShift, 0, zShift);
    }

    public boolean teleportRandomly(World world, EntityPlayer player) {
        for(int i = 0; i < 64; i++) {
            double posX = player.posX + (world.rand.nextDouble() - 0.5D) * 64.0D;
            double posY = player.posY + (double) (world.rand.nextInt(64) - 32);
            double posZ = player.posZ + (world.rand.nextDouble() - 0.5D) * 64.0D;
            if(this.teleportTo(player, posX, posY, posZ)) {
                return true;
            }
        }
        return false;
    }

    public boolean teleportTo(EntityPlayer player, double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;

        boolean flag = this.attemptTeleport(player, event.getTargetX(), event.getTargetY(), event.getTargetZ());
        if (flag) {
            player.fallDistance = 0;
            player.world.playSound(null,
                    player.prevPosX, player.prevPosY, player.prevPosZ,
                    SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory(),
                    1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
        return flag;
    }

    public boolean attemptTeleport(EntityPlayer player, double x, double y, double z) {
        double d0 = player.posX;
        double d1 = player.posY;
        double d2 = player.posZ;
        player.posX = x;
        player.posY = y;
        player.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(player);
        World world = player.world;
        Random random = player.getRNG();

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --player.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
                if (world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            player.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            for (int j = 0; j < 128; ++j) {
                double d6 = (double)j / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (player.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)player.width * 2.0D;
                double d4 = d1 + (player.posY - d1) * d6 + random.nextDouble() * (double)player.height;
                double d5 = d2 + (player.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)player.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }
            return true;
        }
    }

}
