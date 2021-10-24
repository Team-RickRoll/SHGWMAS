package io.github.TeamRickRoll

import io.github.TeamRickRoll.jumpscare.Jumpscare
import io.github.TeamRickRoll.sounds.SoundController
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.potion.Potion
import net.minestom.server.potion.PotionEffect
import net.minestom.server.tag.Tag
import net.minestom.server.timer.Task
import java.time.Duration
import kotlin.random.Random

class Game(val instance: Instance) {
    var gameState = 0

    private val startTitle = Title.title(
        Component.text("Good luck", NamedTextColor.GOLD),
        Component.text("I've hid them well...", NamedTextColor.RED)
    )
    private val winTitle = Title.title(
        Component.text("ALL OF THE CANDY HAS BEEN FOUND!", NamedTextColor.GREEN),
        Component.text("You are free to leave >:), until later. Alligator", NamedTextColor.DARK_GREEN)
    )
    private var task: Task? = null
    private var timer: Int = 480
    private var currentCandy = 0
    private val players = mutableListOf<Player>()
    private val candyLocs = mutableListOf(
        Pos(-302.0, 72.0, -308.0),
        Pos(-316.0, 71.0, -340.0),
        Pos(-321.0, 71.0, -320.0),
        Pos(-327.0, 72.0, -320.0),
        Pos(-342.0, 71.0, -343.0),
        Pos(-324.0, 58.0, -344.0),
        Pos(-314.0, 58.0, -331.0),
        Pos(-311.0, 58.0, -352.0),
        Pos(-293.0, 58.0, -346.0),
        Pos(-349.0, 71.0, -326.0),
        Pos(-343.0, 71.0, -322.0),
        Pos(-344.0, 71.0, -302.0),
        Pos(-348.0, 71.0, -314.0),
        Pos(-321.0, 73.0, -316.0),
        Pos(-330.0, 71.0, -302.0),
        Pos(-312.0, 71.0, -304.0),
        Pos(-342.0, 80.0, -334.0),
        Pos(-342.0, 80.0, -323.0),
        Pos(-331.0, 80.0, -302.0),
        Pos(-298.0, 80.0, -306.0),
        Pos(-308.0, 80.0, -309.0),
        Pos(-309.0, 81.0, -322.0),
        Pos(-312.0, 81.0, -359.0),
        Pos(-299.0, 80.0, -346.0),
        Pos(-318.0, 80.0, -351.0),
        Pos(-316.0, 81.0, -358.0),
        Pos(-327.0, 80.0, -363.0),
        Pos(-334.0, 80.0, -346.0),
        Pos(-330.0, 91.0, -357.0),
        Pos(-344.0, 82.0, -352.0),
        Pos(-343.0, 82.0, -343.0),
        Pos(-332.0, 80.0, -325.0),
    )
    private val bossBar = BossBar.bossBar(
        Component.text("Time Left: ${formatTime(timer)}"),
        (timer / 480).toFloat(),
        BossBar.Color.RED,
        BossBar.Overlay.PROGRESS
    )

    init {
        registerEvents()
    }

    private fun spawnPlayers() {
        for (player in instance.players) {
            player.teleport(Pos(-310.0, 71.0, -329.5, 90f, 0f))
            player.addEffect(
                Potion(
                    PotionEffect.BLINDNESS,
                    1,
                    Short.MAX_VALUE.toInt(),
                    false,
                    false
                )
            )
            players.add(player)
            player.showTitle(
                startTitle
            )
            player.gameMode = GameMode.ADVENTURE
        }
    }

    private fun spawnCandy() {
        for(i in 1..24){
            val random = Random.nextInt(candyLocs.size)
            instance.setBlock(candyLocs.get(random), Block.AIR)
            candyLocs.removeAt(random)
        }
        for (pos in candyLocs) {
            instance.setBlock(pos, Block.AMETHYST_BLOCK.withTag(Tag.String("candy"), "yes"))
        }
    }

    fun startGame() {
        spawnCandy()
        spawnPlayers()
        timer()
        val soundController = SoundController(instance)
        soundController.soundLoop()
    }

    private fun registerEvents() {
        val eventNode = EventNode.type("player-listener", EventFilter.PLAYER)
        eventNode.listenOnly<PlayerBlockInteractEvent> {
            if (hand != Player.Hand.MAIN) return@listenOnly
            if (block.hasTag(Tag.String("candy"))) {
                player.instance?.setBlock(blockPosition, Block.AIR)
                currentCandy++
                announce(
                    Title.title(
                        Component.text("CANDY #$currentCandy", NamedTextColor.GOLD),
                        Component.text("has been found!", NamedTextColor.RED)
                    )
                )
                // When player found candy, there is a chance of 40% to send a jumpscare!
                if(Jumpscare.getChance() <= 40){
                   Jumpscare().sendJumpscare(player)
                }
                if (currentCandy == 8) {
                    MinecraftServer.getSchedulerManager().buildTask {
                        announce(
                            winTitle
                        )
                        MinecraftServer.getSchedulerManager().buildTask {
                            cleanupGame()
                        }.delay(Duration.ofSeconds(5)).schedule()
                    }.delay(Duration.ofSeconds(3)).schedule()
                }
            }
        }
        MinecraftServer.getGlobalEventHandler().addChild(eventNode)
    }

    private fun cleanupGame() {
        for (player in instance.players) {
            player.kick(
                Component.text("nyboyoutboyuibtyutoi", NamedTextColor.RED)
                    .decoration(TextDecoration.OBFUSCATED, true)
            )
        }

        // Am I lazy? Yes. Do I care? No.
        MinecraftServer.getServer().stop()
        println("the server is stopped, you have to restart it to play again. SORRY! (idk how to close this window)")
    }

    // Totally didn't steal this, nope, nada, zip, never happened.
    inline fun <reified E : Event> EventNode<in E>.listenOnly(noinline lambda: E.() -> Unit) {
        this.addListener(
            E::class.java,
            lambda
        )
    }

    private fun announce(title: Title) {
        forplayers {
            showTitle(
                title
            )
        }
    }

    // More laziness
    private fun forplayers(lambda: Player.() -> Unit) {
        for (player in players) {
            lambda.invoke(player)
        }
    }

    // It's a countdown timer, I mean I think it is?
    private fun timer() {
        val timerTask = MinecraftServer.getSchedulerManager().buildTask {
            forplayers {
                showBossBar(bossBar)
            }

            bossBar.name(Component.text("Time Left: ${formatTime(timer)}"))

            // Mafs
            // Nico here, when I tried playing, the bossbar didn't work because it had a bad progress
            // your value was 300 and the progress got to 1.6 and errors, so I found the perfect value
            bossBar.progress((timer.toFloat() / 495.toFloat()))
            timer--
            if (timer <= 0) {
                task?.cancel()
                forplayers {
                    showTitle(
                        Title.title(
                            Component.text("Out of time eh? such a shame.", NamedTextColor.RED),
                            Component.text("Mwuahahhahaha", NamedTextColor.GOLD)
                        )
                    )
                }

                MinecraftServer.getSchedulerManager().buildTask {
                    cleanupGame()
                }.delay(Duration.ofSeconds(4)).schedule()
            }
        }.repeat(Duration.ofSeconds(1)).schedule()
        task = timerTask
    }

    private fun formatTime(seconds: Int): String {
        return "${(seconds / 60)}m ${(seconds % 60)}s"
    }
}