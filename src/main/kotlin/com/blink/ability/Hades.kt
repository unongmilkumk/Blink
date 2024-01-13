package com.blink.ability

import com.blink.Ability
import com.blink.Main.Companion.containsAbility
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Hades : Ability() {
    val killCounter = HashMap<UUID, Int>()
    override fun getRank() = "Blink"
    override fun getName() = "죽음의 신 하데스 The God of Death Hades"
    override fun getDescription() = "10번의 킬을 할시 가장 가까운 적이 죽습니다. Each killing ten player, kill nearest player."
    override fun listener() = object : Listener {
        @EventHandler
        fun onKill(event : PlayerDeathEvent) {
            if (event.entity.killer == null) return
            val p = event.entity.killer!!
            if (!p.containsAbility(Hades())) return
            if (!killCounter.containsKey(p.uniqueId)) killCounter[p.uniqueId] = 0
            killCounter[p.uniqueId] = killCounter[p.uniqueId]!! + 1
            if (killCounter[p.uniqueId]!! == 10) {
                val distance = HashMap<Double, UUID>()
                Bukkit.getOnlinePlayers().forEach {
                    if (it.uniqueId != p.uniqueId && it.location.world == p.location.world) {
                        val dst = sqrt(abs(it.location.x - p.location.x).pow(2) +
                                abs(it.location.y - p.location.y).pow(2) +
                                abs(it.location.z - p.location.z).pow(2))
                        distance[dst] = it.uniqueId
                    }
                }
                Bukkit.getPlayer(distance[distance.keys.getLowestNumber()]!!)!!.damage(10000000.0, p)
            }
        }
    }
    fun Set<Double>.getLowestNumber(): Double {
        var l = this.stream().toList()[0]
        this.forEach {
            if (it < l) l = it
        }
        return l
    }
}