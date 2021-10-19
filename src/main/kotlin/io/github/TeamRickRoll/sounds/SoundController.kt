package io.github.TeamRickRoll.sounds

import net.kyori.adventure.sound.Sound
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.sound.SoundEvent
import net.minestom.server.timer.SchedulerManager
import net.minestom.server.timer.Task
import net.minestom.server.utils.time.TimeUnit
import kotlin.random.Random

class SoundController(private val instanceContainer: InstanceContainer) {
    private var currentTasks : List<Task>? = mutableListOf(); /* In case somehow multiple frenzies start, they will all stop correctly */
    private val schedulerManager: SchedulerManager = MinecraftServer.getSchedulerManager();

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
    )


    /* extention function (Player#spook() in kotlin, SoundController#spook(Player) in java */
    private fun Player.spook(){
        playSound(Sound.sound(sounds[Random.nextInt(sounds.size)], Sound.Source.MASTER, 1f, 1f))
    }



    fun soundFrenzy(){
        var i = 0;
        /* adds the task to the list, no cancel() method like in bukkit, so we have to do it externally.
         More complicated than it should be becuz im a bad programmer */
        currentTasks = currentTasks?.plus(schedulerManager.buildTask {
            // cancels after 60 sounds
            if(i >= 60){
                for(task in currentTasks!!){
                    task.cancel()
                }
            }

            // self-explanatory
            for (player in instanceContainer.players){
                // extension function explained above
                player.spook()
            }

            i++

        }.repeat(250, TimeUnit.MILLISECOND).schedule()) // repeats every 250 ms (1/4 of a second or 5 ticks)
    }
}