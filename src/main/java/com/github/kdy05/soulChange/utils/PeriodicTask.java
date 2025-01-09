package com.github.kdy05.soulChange.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class PeriodicTask {
    private final JavaPlugin plugin;
    private final Runnable task;
    private final Random random = new Random();
    private final double probability;
    private BukkitTask bukkitTask; // 작업을 관리할 객체

    /**
     * 60초마다 특정 확률로 함수를 실행하는 클래스를 생성합니다.
     *
     * @param plugin      플러그인 인스턴스
     * @param task        실행할 함수
     * @param probability 실행 확률 (0.0 ~ 1.0)
     */
    public PeriodicTask(JavaPlugin plugin, Runnable task, double probability) {
        this.plugin = plugin;
        this.task = task;
        this.probability = probability;
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
        }, 30 * 20L, 30 * 20L); // 60초 (20틱 * 60)
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
