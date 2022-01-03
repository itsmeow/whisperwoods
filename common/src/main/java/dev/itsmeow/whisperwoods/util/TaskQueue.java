package dev.itsmeow.whisperwoods.util;

import com.google.common.collect.ImmutableSet;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.itsmeow.imdlib.util.ClassLoadHacks;

import java.util.HashSet;
import java.util.Set;

public class TaskQueue {

    public static final TaskQueue QUEUE_SERVER = new TaskQueue(Env.SERVER);
    public static final TaskQueue QUEUE_CLIENT = new TaskQueue(Env.CLIENT);

    private final Set<MutablePair<Integer, Runnable>> taskQueue = new HashSet<MutablePair<Integer, Runnable>>();

    public TaskQueue(Env logicalSide) {
        if(logicalSide == Env.CLIENT) {
            ClassLoadHacks.runIf(Platform.getEnvironment() == Env.CLIENT, () -> () -> ClientTickEvent.CLIENT_PRE.register(client -> this.tick()));
        } else {
            TickEvent.SERVER_PRE.register(server -> this.tick());
        }
    }

    public void schedule(int ticks, Runnable run) {
        synchronized (taskQueue) {
            taskQueue.add(new MutablePair<>(ticks, run));
        }
    }

    public void tick() {
        synchronized (taskQueue) {
            for (MutablePair<Integer, Runnable> p : ImmutableSet.copyOf(taskQueue)) {
                if (p.getLeft() <= 0) {
                    taskQueue.remove(p);
                    p.getRight().run();
                } else {
                    p.setLeft(p.getLeft() - 1);
                }
            }
        }
    }

}
