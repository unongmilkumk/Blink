package com.blink

import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarFile


class Main : JavaPlugin() {
    companion object {
        val abilityList = HashMap<UUID, ArrayList<Ability>>()
        fun Player.containsAbility(ability: Ability) : Boolean{
            return (abilityList[this.uniqueId] ?: arrayListOf()).contains(ability)
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

        kommand {
            register("giveitem") {
                requires { isPlayer && isOp }
                executes {
                    player.inventory.addItem(abilityItem)
                }
            }
        }
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