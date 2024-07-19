package com.blink.ability.s

import com.blink.Ability
import com.blink.Main.Companion.containsAbility
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class SoulCollector : Ability() {
    val doubleUID = arrayListOf<UUID>()
    override fun getRank() = "S"
    override fun getAbility() = "soul_collector"
    override fun getName() = "영혼 수집가 Soul Collector"
    override fun getDescription() = "[패시브 Passive] 킬 수가 홀수일때 공격력이 두배가 됨 If kill count is even number, Attack Damage is double"
    override fun listener() = object : Listener {
        @EventHandler
        fun onKill(event : PlayerDeathEvent) {
            if (event.entity.killer == null) return
            val p = event.entity.killer!!
            if (!p.containsAbility(SoulCollector())) return
            if (doubleUID.contains(p.uniqueId)) {
                doubleUID.remove(p.uniqueId)
            } else {
                doubleUID.add(p.uniqueId)
            }
        }
        @EventHandler
        fun onAttack(event : EntityDamageByEntityEvent) {
            if (event.damager is Player) {
                val p = event.damager as Player
                if (doubleUID.contains(p.uniqueId)) {
                    event.damage *= 2.0
                }
            }
        }
    }
}