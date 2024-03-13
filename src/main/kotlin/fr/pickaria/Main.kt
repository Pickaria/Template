package fr.pickaria

import org.bukkit.plugin.java.JavaPlugin

internal open class Main : JavaPlugin() {
    override fun onEnable() {
        println("Hello World!")
    }
}