package com.blink.ability.blink

import com.blink.Ability
import com.blink.Main
import com.blink.Main.Companion.containsAbility
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class RegionController : Ability() {
    val coolhash = HashMap<UUID, Long>()
    override fun getRank() = "Blink"
    override fun getAbility() = "region_controller"
    override fun getName() = "공간지배자 Region Controller"
    override fun getDescription() = "[액티브 Active] 랜덤한 세명의 적군이 나약함 3 디버프를 10초동안 가지며 자신에게 tp된다. Random 3 Enemy teleports to oneself with weakness 3 10s"
    override fun getCooltime() = 120
    override fun listener() = object : Listener {
        @EventHandler
        fun onUsing(event : PlayerInteractEvent) {
            if (!event.player.containsAbility(RegionController())) return

            if (!coolhash.containsKey(event.player.uniqueId))
                coolhash[event.player.uniqueId] = System.currentTimeMillis()

            if (event.player.inventory.itemInMainHand != Main.abilityItem) return

            if (coolhash[event.player.uniqueId]!! <= System.currentTimeMillis()) {
                coolhash[event.player.uniqueId] = coolhash[event.player.uniqueId]!! + (getCooltime() * 1000)
                val pli = Bukkit.getOnlinePlayers().stream().toList()
                pli.remove(event.player)
                pli.shuffle()
                val ott = arrayListOf(pli[0], pli[1], pli[2])
                ott.forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 200, 2))
                    it.teleport(event.player)
                }
                coolhash[event.player.uniqueId] = System.currentTimeMillis() + 1000 * getCooltime()
            } else {
                val cool = (coolhash[event.player.uniqueId]!! - System.currentTimeMillis()) / 1000
                event.player.sendMessage(Component.text("쿨타임이 ${cool}초 남았습니다. On Cool : ${cool}s"))
            }
        }
    }
}