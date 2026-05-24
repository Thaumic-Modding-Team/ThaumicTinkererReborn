package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ItemFelineCharm extends AbstractItemBauble implements IProxy {
    public ItemFelineCharm() {
        super("feline_charm");
        this.setMaxStackSize(1);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        entity.playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        entity.playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.CHARM;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        // Feline Charm
        if(event.getEntityLiving() instanceof EntityCreeper && !event.getEntityLiving().world.isRemote) {
            EntityCreeper creeper = (EntityCreeper) event.getEntityLiving();
            EntityPlayer player = creeper.world.getClosestPlayerToEntity(creeper, 10.0D);

            if(player != null && !player.isCreative()) {
                int slot = BaublesApi.isBaubleEquipped(player, this);
                if(slot > -1) {
                    if(creeper.getAttackTarget() == player || creeper.getCreeperState() == 1 || creeper.hasIgnited()) {
                        creeper.setAttackTarget(null);
                        creeper.setCreeperState(-1);
                    }

                    Vec3d vec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(creeper, 16, 7, new Vec3d(player.posX, player.posY, player.posZ));
                    if(vec != null) {
                        creeper.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1.5D);
                    }
                }
            }
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
