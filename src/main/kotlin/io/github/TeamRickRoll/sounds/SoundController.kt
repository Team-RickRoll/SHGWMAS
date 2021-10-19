package io.github.TeamRickRoll.sounds

import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
import kotlin.random.Random

class SoundController {
    private val sounds: List<SoundEvent> = listOf(
        SoundEvent.AMBIENT_CAVE,
        SoundEvent.ENTITY_ZOMBIE_AMBIENT,
        SoundEvent.ENTITY_GHAST_AMBIENT,
        SoundEvent.ENTITY_GHAST_DEATH,
        SoundEvent.ENTITY_GHAST_HURT,
        SoundEvent.ENTITY_GHAST_SCREAM,
        SoundEvent.ENTITY_GHAST_SHOOT,
        SoundEvent.ENTITY_CREEPER_PRIMED,
        SoundEvent.ENTITY_SKELETON_AMBIENT,
    )
    fun Player.spookPlayer(){
        playSound(Sound.sound(sounds[Random.nextInt(sounds.size)], Sound.Source.MASTER, 1f, 1f))
    }
}