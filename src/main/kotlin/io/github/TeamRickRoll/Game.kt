package io.github.TeamRickRoll

import io.github.TeamRickRoll.sounds.SoundController
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.potion.Potion
import net.minestom.server.potion.PotionEffect
import net.minestom.server.tag.Tag
import net.minestom.server.timer.Task
import java.time.Duration

class Game(private val instance: Instance) {
    var gameState = 0

    private val startTitle = Title.title(
        Component.text("Good luck", NamedTextColor.GOLD),
        Component.text("I've hid them well...", NamedTextColor.RED)
    )
    private val winTitle = Title.title(
        Component.text("ALL OF THE CANDY HAS BEEN FOUND!"),
        Component.text("You are free to leave >:), until later. Alligator")
    )
    private var task: Task? = null
    private var timer: Int = 300
    private var currentCandy = 0
    private val players = mutableListOf<Player>()
    private val candyLocs = listOf(
        Pos(0.toDouble(), 40.toDouble(), 0.toDouble()),
        Pos(1.toDouble(), 40.toDouble(), 0.toDouble()),
        Pos(2.toDouble(), 40.toDouble(), 0.toDouble()),
        Pos(3.toDouble(), 40.toDouble(), 0.toDouble()),
        Pos(0.toDouble(), 41.toDouble(), 0.toDouble()),
        Pos(1.toDouble(), 41.toDouble(), 0.toDouble()),
        Pos(2.toDouble(), 41.toDouble(), 0.toDouble()),
        Pos(3.toDouble(), 41.toDouble(), 0.toDouble())

    )
    private val bossBar = BossBar.bossBar(
        Component.text("Time Left: ${formatTime(timer)}"),
        (timer / 300).toFloat(),
        BossBar.Color.RED,
        BossBar.Overlay.PROGRESS
    )

    init {
        registerEvents()
    }

    private fun spawnPlayers() {
        for (player in instance.players) {
            player.teleport(Pos(0.toDouble(), 40.toDouble(), 0.toDouble()))
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
        }
    }

    private fun spawnCandy() {
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
            bossBar.progress((timer.toFloat() / 300.toFloat()))

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