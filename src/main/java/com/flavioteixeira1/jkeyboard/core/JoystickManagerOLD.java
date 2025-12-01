package com.flavioteixeira1.jkeyboard.core;

import net.java.games.input.*;
import net.java.games.input.Component;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.SwingUtilities;

public class JoystickManagerOLD {
    private Controller joystick;
    private MainWindow.DevicePanel uiPanel;
    private Thread pollingThread;
    private volatile boolean running = true;
    private Map<Integer, Integer> buttonMap; // idx botão -> KeyEvent.VK_?
    private boolean[] btnLast = new boolean[12]; // para 12 botões

    // Para mapeamento, pode expandir!
    private int[] buttonToKey = new int[] {
        KeyEvent.VK_X, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_Z,
        -1, -1, -1, -1, KeyEvent.VK_W, KeyEvent.VK_Q, -1, -1
    };

    public JoystickManagerOLD(Controller ctl, MainWindow.DevicePanel pnl) {
        this.joystick = ctl;
        this.uiPanel = pnl;
        startPolling();
    }

    public void startPolling() {
        pollingThread = new Thread(() -> {
            while (running) {
                joystick.poll();
                Component[] comps = joystick.getComponents();
                for (int i = 0; i < comps.length; i++) {
                    Component c = comps[i];
                    if (c.getIdentifier() instanceof Component.Identifier.Button) {
                        int btnIdx = getButtonIdx(c.getIdentifier());
                        if (btnIdx >= 0 && btnIdx < btnLast.length) {
                            boolean pressed = c.getPollData() > 0.5f;
                            if (pressed != btnLast[btnIdx]) {
                                int btnCopy = btnIdx;
                                SwingUtilities.invokeLater(() -> uiPanel.highlightButton(btnCopy, pressed));
                                btnLast[btnIdx] = pressed;
                            }
                        }
                    }
                }
                try { Thread.sleep(12); } catch (InterruptedException e) { break; }
            }
        });
        pollingThread.setDaemon(true);
        pollingThread.start();
    }
    public void stopPolling() { running = false; pollingThread.interrupt(); }

    private int getButtonIdx(Component.Identifier id) {
        String nm = id.getName().replaceAll("[^0-9]", "");
        try { return Integer.parseInt(nm); } catch(Exception e) { return -1; }
    }

    public int getKeyForButton(int buttonIdx) {
        return buttonMap.getOrDefault(buttonIdx, -1);
    }

     public void setButtonMapping(int buttonIdx, int keyCode) {
        buttonMap.put(buttonIdx, keyCode);
    }

    public String getJoystickName() {
        return joystick != null ? joystick.getName() : "";
    }
}