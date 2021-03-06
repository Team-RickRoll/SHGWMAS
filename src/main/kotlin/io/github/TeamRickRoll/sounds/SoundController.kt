package io.github.TeamRickRoll.sounds

import io.github.TeamRickRoll.Game
import io.github.TeamRickRoll.mob.MobController
import net.kyori.adventure.sound.Sound
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
import net.minestom.server.timer.SchedulerManager
import net.minestom.server.timer.Task
import net.minestom.server.utils.time.TimeUnit
import java.time.Duration
import kotlin.random.Random

class SoundController(private val game: Game) {
    // In case somehow multiple frenzies start, they will all stop correctly by looping through all of them in this list
    private var currentTasks: List<Task>? = mutableListOf()
    private val schedulerManager: SchedulerManager = MinecraftServer.getSchedulerManager()

    /* Basically static, but in kotlin */
    companion object {
        private val sounds: List<SoundEvent> = listOf(
            SoundEvent.AMBIENT_CAVE,
            SoundEvent.ENTITY_ZOMBIE_AMBIENT,
            SoundEvent.ENTITY_GHAST_AMBIENT,
            SoundEvent.ENTITY_GHAST_HURT,
            SoundEvent.ENTITY_GHAST_SCREAM,
            SoundEvent.ENTITY_GHAST_SHOOT,
            SoundEvent.ENTITY_CREEPER_PRIMED,
            SoundEvent.ENTITY_SKELETON_AMBIENT,
            SoundEvent.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,
            SoundEvent.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
            SoundEvent.ENTITY_LIGHTNING_BOLT_IMPACT,
            SoundEvent.ENTITY_LIGHTNING_BOLT_THUNDER,
            SoundEvent.ENTITY_COD_DEATH,
            SoundEvent.ENTITY_DONKEY_EAT,
            SoundEvent.ENTITY_ENDERMITE_DEATH,
            SoundEvent.ENTITY_EVOKER_DEATH,
            SoundEvent.ENTITY_EVOKER_PREPARE_ATTACK,
            SoundEvent.ENTITY_EVOKER_PREPARE_SUMMON,
            SoundEvent.ENTITY_EVOKER_PREPARE_WOLOLO,
            SoundEvent.ENTITY_AXOLOTL_DEATH,
            SoundEvent.ENTITY_AXOLOTL_HURT,
            /* Needs more sounds */
        )

        /* Extension function (Player#spook() in kotlin, SoundController#spook(Player) in java */
        @JvmStatic /* SoundController#Companion.spook(Player) becomes SoundController#spook(Player) cool, huh? */
        fun Player.spook() {
            playSound(
                Sound.sound(sounds[Random.nextInt(sounds.size)], Sound.Source.MASTER, 1f, Random.nextFloat()),
                /* Picks a random position around the player, so it's not right on them */
                (position.x() - 8) + Random.nextDouble(16.toDouble()),
                (position.y() - 8) + Random.nextDouble(16.toDouble()),
                (position.z() - 8) + Random.nextDouble(16.toDouble()),
            )

        }
    }

    fun soundFrenzy() {
        var i = 0
        /* Adds the task to the list, no cancel() method like in bukkit, so we have to do it externally.
         More complicated than it should be becuz im a bad programmer */
        currentTasks = currentTasks?.plus(schedulerManager.buildTask {
            // cancels after 60 sounds
            if (i >= 60) {
                for (task in currentTasks!!) {
                    task.cancel()
                }
            }

            game.forplayers {
                // extension function explained above
                spook()
            }

            i++

        }.repeat(250, TimeUnit.MILLISECOND).schedule()) // repeats every 250 ms (1/4 of a second or 5 ticks)
    }

    fun soundLoop() {
        schedulerManager.buildTask {
            if (Random.nextInt(3) == 1) {
                if (Random.nextInt(20) == 1) {
                    soundFrenzy()
                }
                game.forplayers {
                    spook()
                }
            }

        }.repeat(Duration.ofSeconds(7)).schedule()
    }
}