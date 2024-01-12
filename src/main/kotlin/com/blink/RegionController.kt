package com.blink

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

class RegionController : Ability() {
    val coolhash = HashMap<UUID, Long>()
    override fun getRank() = "Blink"
    override fun getName() = "공간지배자 Region Controller"
    override fun getDescription() = "랜덤한 세명의 적군이 나약함 3 디버프를 10초동안 가지며 자신에게 tp된다."
    override fun getCooltime() = 120
    @Suppress("UNCHECKED_CAST")
    override fun listener() = object : Listener {
        @EventHandler
        fun onUsing(event : PlayerInteractEvent) {
            if (!coolhash.containsKey(event.player.uniqueId))
                coolhash[event.player.uniqueId] = System.currentTimeMillis()

            if (coolhash[event.player.uniqueId]!! <= System.currentTimeMillis()) {
                if (event.player.inventory.itemInMainHand == Main.abilityItem) {
                    val pli = Bukkit.getOnlinePlayers() as ArrayList<Player>
                    pli.remove(event.player)
                    pli.shuffle()
                    val ott = arrayListOf(pli[0], pli[1], pli[2])
                    ott.forEach {
                        it.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 200, 2))
                        it.teleport(event.player)
                    }
                    coolhash[event.player.uniqueId] = System.currentTimeMillis() + 1000 * getCooltime()
                }
            } else {
                val cool = (coolhash[event.player.uniqueId]!! - System.currentTimeMillis()) / 1000
                event.player.sendMessage(Component.text("쿨타임이 ${cool}초 남았습니다. On Cool : ${cool}s"))
            }
        }
    }
}