package com.blink.ability.a

import com.blink.Ability
import com.blink.Main.Companion.containsAbility
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Dracula : Ability() {
    override fun getRank() = "A"
    override fun getAbility() = "dracula"
    override fun getName() = "드라큘라 Dracula"
    override fun getDescription() = "[패시브 Passive] 공격량의 25%를 회복합니다. Heal 25% of your attacked damage"
    override fun listener() = object : Listener {
        @EventHandler
        fun onAttack(event : EntityDamageByEntityEvent) {
            if (event.damager is Player) {
                val p = event.damager as Player
                if (p.containsAbility(Dracula())) {
                    p.health += event.damage * 0.25
                }
            }
        }
    }
}