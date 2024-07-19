package com.blink.ability.a

import com.blink.Ability
import com.blink.Main
import com.blink.Main.Companion.containsAbility
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Wind : Ability() {
    val coolhash = HashMap<UUID, Long>()
    override fun getRank() = "A"
    override fun getAbility() = "wind"
    override fun getName() = "바람 마법사 Wind Magician"
    override fun getDescription() = "[액티브 Active] 반경 4 내의 적에게 8데미지 (4♥) 를 주고 뒤로 날린다. Damage 8 (4♥) to Enemy in radius 4 and push it."
    override fun getCooltime() = 240
    override fun listener() = object : Listener {
        @EventHandler
        fun onUsing(event : PlayerInteractEvent) {
            if (!event.player.containsAbility(Wind())) return

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
                        if (dst in (0.1.. 4.0)) {
                            it.damage(8.0)
                            val loc1: Location = p.location
                            val loc2: Location = p.location.add(-2.0, 0.0, -2.0)

                            val v = Vector(loc2.x - loc1.x, 1.0, loc2.z - loc1.z)
                            it.velocity = v
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