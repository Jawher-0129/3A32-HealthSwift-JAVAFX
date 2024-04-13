package service;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class SystemNotification {

    public static void showNotification(String title, String message) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("C:\\Users\\admin\\Desktop\\PidevUserFirasJavaFx\\3A32-HealthSwift-JAVAFX\\src\\main\\images\\téléchargement.png"); // Chemin vers l'icône de votre application

        // Créez un objet TrayIcon
        TrayIcon trayIcon = new TrayIcon(image, "Application Name");
        trayIcon.setImageAutoSize(true);

        // Ajoutez l'objet TrayIcon à SystemTray
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        // Affichez la notification
        trayIcon.displayMessage(title, message, MessageType.INFO);
    }
}
