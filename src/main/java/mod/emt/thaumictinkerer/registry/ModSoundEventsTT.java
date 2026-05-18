package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundEventsTT {
    BLOCK_NECROMANCY_TABLET_SUMMON_FINISH("block.necromancy_tablet.summon_finish"),
    BLOCK_NECROMANCY_TABLET_SUMMON_LOOP("block.necromancy_tablet.summon_loop"),
    BLOCK_NECROMANCY_TABLET_SUMMON_START("block.necromancy_tablet.summon_start");

    private final SoundEvent soundEvent;

    ModSoundEventsTT(String path) {
        ResourceLocation resourceLocation = new ResourceLocation(ThaumicTinkerer.MOD_ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}
