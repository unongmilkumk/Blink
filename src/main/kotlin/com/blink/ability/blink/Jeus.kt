package com.blink.ability.blink

import com.blink.Ability
import com.blink.Main
import com.blink.Main.Companion.containsAbility
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Jeus : Ability() {
    val coolhash = HashMap<UUID, Long>()
    override fun getRank() = "Blink"
    override fun getAbility() = "jeus"
    override fun getName() = "번개의 신 제우스 The God of Lightning, Jeus"
    override fun getDescription() = "[액티브 Active]반경 10칸 내에 있는 적의 피의 반을 없애고 번개를 소환한다. Make enemy in radius 10 health half of it and spawn lightning on them."
    override fun getCooltime() = 200
    override fun listener() = object : Listener {
        @EventHandler
        fun onUsing(event : PlayerInteractEvent) {
            if (!event.player.containsAbility(Jeus())) return

            if (!coolhash.containsKey(event.player.uniqueId))
                coolhash[event.player.uniqueId] = System.currentTimeMillis()

            if (event.player.inventory.itemInMainHand != Main.abilityItem) return

            if (coolhash[event.player.uniqueId]!! <= System.currentTimeMillis()) {
                coolhash[event.player.uniqueId] = coolhash[event.player.uniqueId]!! + (getCooltime() * 1000)
                val p = event.player
                Bukkit.getOnlinePlayers().forEach {
                    if (it.uniqueId != p.uniqueId && it.location.world == p.location.world) {
                        val dst = sqrt(
                            abs(it.location.x - p.location.x).pow(2) +
                                    abs(it.location.y - p.location.y).pow(2) +
                                    abs(it.location.z - p.location.z).pow(2))
                        if (dst in (0.1.. 20.0)) {
                            it.damage(it.health / 2)
                            it.world.strikeLightning(it.location)
                        }
                    }
                }
            } else {
                val cool = (coolhash[event.player.uniqueId]!! - System.currentTimeMillis()) / 1000
                event.player.sendMessage(Component.text("쿨타임이 ${cool}초 남았습니다. On Cool : ${cool}s"))
            }
        }
    }
}