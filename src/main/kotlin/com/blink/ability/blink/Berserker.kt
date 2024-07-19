package com.blink.ability.blink

import com.blink.Ability
import com.blink.Main.Companion.containsAbility
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Berserker : Ability() {
    override fun getRank() = "Blink"
    override fun getAbility() = "berserker"
    override fun getName() = "버서커 Berserker"
    override fun getDescription() = "[패시브 Passive] 대미지가 없어진 체력 양의 두배만큼 강해집니다 (체력 5 -> 150% 대미지 (2.5배)) Damage increase 2 times of damaged health (health 5 -> 150% Damage Increase (2.5x))"
    override fun listener() = object : Listener {
        @EventHandler
        fun onAttack(event : EntityDamageByEntityEvent) {
            if (event.damager is Player) {
                val p = event.damager as Player
                if (p.containsAbility(Berserker())) {
                    event.damage *= 3 - (2 * p.health / p.healthScale)
                }
            }
        }
    }
}