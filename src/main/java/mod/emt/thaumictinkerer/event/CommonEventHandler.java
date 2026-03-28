package mod.emt.thaumictinkerer.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.item.ItemFelineCharm;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEventHandler {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        // Feline Charm
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntityLiving();
            EntityPlayer player = creeper.world.getClosestPlayerToEntity(creeper, 10.0D);

            if(player != null && !player.isCreative()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);

                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);

                    if(!stack.isEmpty() && stack.getItem() instanceof ItemFelineCharm) {
                        if(creeper.getAttackTarget() == player) {
                            creeper.setAttackTarget(null);

                            // Prevents the creepers from exploding
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
    }
}
