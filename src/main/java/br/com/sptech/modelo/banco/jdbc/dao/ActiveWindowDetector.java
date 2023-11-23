package br.com.sptech.modelo.banco.jdbc.dao;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveWindowDetector {

    public List<WindowInfo> getActiveWindowInfo() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return getWindowsInfo();
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getLinuxInfo();
        } else {
            System.out.println("Sistema operacional não suportado.");
            return Collections.emptyList();
        }
    }

    public List<WindowInfo> getWindowsInfo() {
        List<WindowInfo> windowInfoList = new ArrayList<>();

        // Enumera todas as janelas do sistema
        User32.INSTANCE.EnumWindows((hwnd, pointer) -> {
            char[] windowText = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, windowText, 512);

            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

            boolean isForeground = User32.INSTANCE.GetForegroundWindow().equals(hwnd);

            String windowName = Native.toString(windowText);
            int processId = pid.getValue();

            WindowInfo windowInfo = new WindowInfo(windowName, processId, isForeground);
            windowInfoList.add(windowInfo);

            return true;
        }, null);

        return windowInfoList;
    }


    private int pid() {
        return 0;
    }

    private List<WindowInfo> getLinuxInfo() {
        try {
            List<WindowInfo> windowInfoList = new ArrayList<>();

            // Obtém o nome de usuário do sistema
            String username = System.getProperty("user.name");

            // Obtém informações sobre todas as janelas usando wmctrl
            Process process = Runtime.getRuntime().exec("wmctrl -l -p");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+", 9);

                // Certifica-se de que há pelo menos 7 campos (ID da janela, desktop, PID, nome da aplicação)
                if (parts.length >= 7) {
                    String windowId = parts[0];
                    int pid = Integer.parseInt(parts[2]);
                    String windowName = extractWindowName(parts[parts.length-1], username); // O campo do nome da aplicação

                    // Adiciona as informações da janela à lista
                    windowInfoList.add(new WindowInfo(windowName, pid, true));

                    // Adicione logs para ajudar na depuração
                    System.out.println("Window ID: " + windowId);
                    System.out.println("Window Name: " + windowName);
                    System.out.println("PID: " + pid);
                }
            }

            return windowInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String extractWindowName(String windowName, String username) {
        // Usar uma expressão regular para encontrar o que vem após o nome do usuário
        String regex = username + "\\s*(.*?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(windowName);

        if (matcher.find()) {
            // Se a expressão regular encontrar uma correspondência, obter o grupo capturado
            return matcher.group(1).trim();
        } else {
            // Se não houver correspondência, retornar a string original
            return windowName;
        }
    }

    public static class WindowInfo {
        private final String windowName;
        private final int processId;
        private final boolean inForeground;

        public WindowInfo(String windowName, int processId, boolean inForeground) {
            this.windowName = windowName;
            this.processId = processId;
            this.inForeground = inForeground;
        }

        public String getWindowName() {
            return windowName;
        }

        public int getProcessId() {
            return processId;
        }

        public boolean isInForeground() {
            return inForeground;
        }
    }
}
