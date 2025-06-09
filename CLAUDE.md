# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- `./gradlew build` - Build the plugin
- `./gradlew shadowJar` - Create shaded JAR (if applicable)
- Configuration reloading: `/sc reload` in-game

## Project Architecture

This is a Minecraft Spigot/Paper plugin for soul swapping between players. The plugin exchanges player states including location, inventory, health, skin, and name tags.

### Core Components

- **SoulChange.java** - Main plugin class, manages DisguiseAPI dependency and static managers
- **ChangeStatus.java** - Core soul swapping logic that exchanges all player states
- **SkinManager.java** - Handles skin changes via DisguiseAPI with caching to avoid Mojang API rate limits
- **NameCacheManager.java** - Manages player name mappings for disguises
- **SoulChangeListener.java** - Event handler for damage-triggered swaps and player join/leave

### Key Dependencies

- **DisguiseAPI** - Required dependency for skin/name changes (must be locally installed)
- **Spigot API 1.21.1** - Target Minecraft version
- Java 21 target compatibility

### Configuration System

The plugin uses config.yml for:
- Timer-based swapping (interval and probability)
- Damage-triggered swapping
- Damage sharing between players
- Notification settings (TITLE/CHAT/NONE)

### Architecture Notes

- Static managers are initialized in SoulChange.onEnable()
- Skin caching prevents Mojang API rate limiting
- DisguiseProvider dependency is checked at startup
- All soul swapping excludes spectator mode players
- Player states preserved across reconnections via NameCacheManager