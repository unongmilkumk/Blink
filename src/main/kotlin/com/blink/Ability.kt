package com.blink

import org.bukkit.event.Listener

open class Ability {
    open fun getRank() = "LLL+"
    open fun getAbility() = "Default"
    open fun getName() = "Default"
    open fun getDescription() = "Default"
    open fun getCooltime() = 0

    open fun listener() = object : Listener {}
}