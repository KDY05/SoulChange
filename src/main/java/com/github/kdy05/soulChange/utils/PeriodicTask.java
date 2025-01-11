package com.github.kdy05.soulChange.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class PeriodicTask {
    private final JavaPlugin plugin;
    private final Runnable task;
    private final Random random = new Random();
    private BukkitTask bukkitTask; // 작업을 관리할 객체
    private final long intervalTicks; // 주기 (틱 단위)
    private final double probability; // 실행 확률

    /**
     * 60초마다 특정 확률로 함수를 실행하는 클래스를 생성합니다.
     *
     * @param plugin      플러그인 인스턴스
     * @param task        실행할 함수
     */
    public PeriodicTask(JavaPlugin plugin, Runnable task) {
        this.plugin = plugin;
        this.task = task;
        FileConfiguration config = plugin.getConfig();
        this.intervalTicks = config.getLong("timer.interval-seconds", 30) * 20L; // 초 -> 틱 변환
        this.probability = config.getDouble("timer.probability", 0.33);
    }

    /**
     * 주기적으로 실행되는 작업을 시작합니다.
     */
    public void start() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            return; // 이미 실행 중이면 무시
        }
        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (random.nextDouble() < probability) {
                task.run();
                Bukkit.getLogger().info("타이머에 의해 작업이 실행되었습니다.");
            }  else {
                Bukkit.getLogger().info("타이머가 작동 중이지만 확률에 의해 실행되지 않았습니다.");
            }
        }, intervalTicks, intervalTicks);
    }

    /**
     * 실행 중인 작업을 멈춥니다.
     */
    public void stop() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }
    }
}
