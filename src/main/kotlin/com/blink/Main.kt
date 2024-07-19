package com.blink

import com.blink.ability.a.Dracula
import com.blink.ability.a.Wind
import com.blink.ability.blink.Berserker
import com.blink.ability.blink.Hades
import com.blink.ability.blink.Jeus
import com.blink.ability.blink.RegionController
import com.blink.ability.s.Pyrokinesis
import com.blink.ability.s.SoulCollector
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarFile


class Main : JavaPlugin() {
    companion object {
        val abilityList = HashMap<UUID, ArrayList<Ability>>()
        fun Player.containsAbility(ability: Ability) : Boolean {
            (abilityList[this.uniqueId] ?: arrayListOf()).forEach {
                if (it.getAbility() == ability.getAbility()) return true
            }
            return false
        }
        private fun ItemStack.editedMeta(consumer:(ItemMeta) -> Unit) : ItemStack{
            this.editMeta(consumer)
            return this
        }
        val abilityItem = ItemStack(Material.HEART_OF_THE_SEA).editedMeta { im ->
            im.displayName(
                Component.text("능력 사용 Use Ability")
                    .color(TextColor.color(128, 200, 255))
            )
            im.lore(
                mutableListOf(
                    Component.text("우클릭하여 능력을 사용합니다")
                        .color(TextColor.color(153, 162, 255)),
                    Component.text("Right Click To Use Ability")
                        .color(TextColor.color(153, 162, 255))
                )
            )
        }
    }
    override fun onEnable() {
        getAbilityClasses().forEach {
            try {
                val instance = it.getDeclaredConstructor().newInstance()
                logger.info(it.getDeclaredMethod("getName").invoke(instance) as String)
                server.pluginManager.registerEvents(
                    it.getDeclaredMethod("listener").invoke(instance) as Listener,
                    this@Main
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun joinEvent(event : PlayerJoinEvent) {
                if (!abilityList.containsKey(event.player.uniqueId)) {
                    abilityList[event.player.uniqueId] = arrayListOf()
                }
            }
        }, this)

        Bukkit.getOnlinePlayers().forEach {
            if (!abilityList.containsKey(it.uniqueId)) {
                abilityList[it.uniqueId] = arrayListOf()
            }
        }

        Bukkit.getCommandMap().register("Blink", object : Command("giveitem") {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender !is Player) {
                    return false
                }

                val player : Player = sender

                if (!player.isOp) {
                    return false
                }

                player.inventory.addItem(abilityItem)
                return true
            }
        })

        Bukkit.getCommandMap().register("Blink", object : Command("ability") {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender !is Player) {
                    return false
                }

                val player : Player = sender

                if (args.isEmpty()) {
                    return false
                }

                when (args[0]) {
                    "hades" -> {
                        abilityList[player.uniqueId]!!.add(Hades())
                    }
                    "jeus" -> {
                        abilityList[player.uniqueId]!!.add(Jeus())
                    }
                    "region_controller" -> {
                        abilityList[player.uniqueId]!!.add(RegionController())
                    }
                    "pyrokinesis" -> {
                        abilityList[player.uniqueId]!!.add(Pyrokinesis())
                    }
                    "soul_collector" -> {
                        abilityList[player.uniqueId]!!.add(SoulCollector())
                    }
                    "wind" -> {
                        abilityList[player.uniqueId]!!.add(Wind())
                    }
                    "berserker" -> {
                        abilityList[player.uniqueId]!!.add(Berserker())
                    }
                    "dracula" -> {
                        abilityList[player.uniqueId]!!.add(Dracula())
                    }
                    "reset" -> {
                        abilityList[player.uniqueId] = arrayListOf()
                    }
                }
                return true
            }
        })
    }

    override fun onDisable() {
        logger.info(":-<")
    }

    private fun getAbilityClasses(): List<Class<*>> {
        val classesList = mutableListOf<Class<*>>()

        try {
            val jarFile = JarFile(File(javaClass.protectionDomain.codeSource.location.toURI()).path)
            val entries = jarFile.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.name.endsWith(".class")) {
                    val className = entry.name.removeSuffix(".class").replace('/', '.')
                    val clazz = Class.forName(className)
                    if (Ability::class.java.isAssignableFrom(clazz)) classesList.add(clazz)
                }
            }

            jarFile.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return classesList
    }
}