package io.github.TeamRickRoll.sounds;

import net.kyori.adventure.key.Key;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.NamespaceID;

public class SoundsManager {

    //TODO: CUSTOM SOUNDS
    public static SoundEvent GHOST_WHISPERS(){
        return SoundEvent.fromNamespaceId(NamespaceID.from(Key.key("custom.ghost_whispers")));
    }

}
