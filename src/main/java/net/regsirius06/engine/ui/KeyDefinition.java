package net.regsirius06.engine.ui;

import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyDefinition {
    public static final KeyDefinition DEFAULT = new KeyDefinition() {{
        setKey(Action.FORWARD_MOVE, KeyEvent.VK_W);
        setKey(Action.BACK_MOVE, KeyEvent.VK_S);
        setKey(Action.LEFT_ROTATION, KeyEvent.VK_A);
        setKey(Action.RIGHT_ROTATION, KeyEvent.VK_D);
        setKey(Action.MINIMAP, KeyEvent.VK_M);
    }};

    public enum Action {
        FORWARD_MOVE,
        BACK_MOVE,
        LEFT_ROTATION,
        RIGHT_ROTATION,
        MINIMAP,
        GET_LIGHT,
        SET_LIGHT,
        CHOICE_FORWARD,
        CHOICE_BACK
    }

    private final Map<Action, Integer> keyBindings;

    public KeyDefinition() {
        keyBindings = new HashMap<>();
    }

    public @Nullable Integer getKey(Action action) {
        return keyBindings.get(action);
    }

    public void setKey(Action action, int keyCode) {
        keyBindings.put(action, keyCode);
    }
}

