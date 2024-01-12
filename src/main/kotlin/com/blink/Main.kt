package com.blink

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {
    companion object {
        val abilityItem = ItemStack(Material.HEART_OF_THE_SEA).apply {
            this.itemMeta.let { im ->
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
    }
    override fun onEnable() {
        logger.info(":->")
    }

    override fun onDisable() {
        logger.info(":-<")
    }
}