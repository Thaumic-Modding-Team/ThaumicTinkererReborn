package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundEventsTT {
    BLOCK_NECROMANCY_TABLET_BEAM_PROCESS("block.necromancy_tablet.beam_process"),
    BLOCK_NECROMANCY_TABLET_BEAM_START("block.necromancy_tablet.beam_start"),
    BLOCK_NECROMANCY_TABLET_SPAWN_ENTITY("block.necromancy_tablet.spawn_entity");

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
