package com.blink.ability.s

import com.blink.Ability
import com.blink.Main.Companion.containsAbility
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

class Pyrokinesis : Ability() {
    val coolhash = HashMap<UUID, Long>()
    override fun getRank() = "S"
    override fun getAbility() = "pyrokinesis"
    override fun getName() = "염화력 Pyrokinesis"
    override fun getDescription() = "[패시브 Passive] 공격 시 상대방은 3의 대미지를 더 받게 되고, 4초간 불에 타게 된다 [ 쿨타임 2초 ] . When you Attack, damage get +3 and make victim flaming for 4s. [Cooldown for 2s]"
    override fun getCooltime() = 2
    override fun listener() = object : Listener {
        @EventHandler
        fun onAttack(event : EntityDamageByEntityEvent) {
            val p = event.damager as Player
            if (!p.containsAbility(Pyrokinesis())) return

            if (!coolhash.containsKey(p.uniqueId))
                coolhash[p.uniqueId] = System.currentTimeMillis()

            if (coolhash[p.uniqueId]!! > System.currentTimeMillis()) return

            event.damage += 3
            event.entity.fireTicks += 80

            coolhash[p.uniqueId] = System.currentTimeMillis() + 1000 * getCooltime()
        }
    }
}