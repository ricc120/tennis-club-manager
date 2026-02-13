package it.tennis_club.business_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servizio singleton per la gestione delle notifiche in-memory.
 * Le notifiche vengono memorizzate per utente e consumate al primo accesso.
 */
public class NotificationService {

    // Istanza singleton
    private static NotificationService instance;

    // Mappa che associa ID utente alla lista di messaggi
    private final Map<Integer, List<String>> userNotifications;

    /**
     * Costruttore privato per il pattern Singleton.
     */
    private NotificationService() {
        this.userNotifications = new HashMap<>();
    }

    /**
     * Ottiene l'istanza singleton del NotificationService.
     * 
     * @return l'unica istanza di NotificationService
     */
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    /**
     * Aggiunge una notifica per un utente specifico.
     * 
     * @param userId  l'ID dell'utente destinatario
     * @param message il messaggio di notifica
     */
    public void addNotification(Integer userId, String message) {
        if (userId == null || message == null || message.trim().isEmpty()) {
            return;
        }

        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
    }

    /**
     * Recupera e consuma tutte le notifiche di un utente.
     * Dopo la chiamata, la lista di notifiche dell'utente viene svuotata.
     * 
     * @param userId l'ID dell'utente
     * @return lista di messaggi di notifica (vuota se non ce ne sono)
     */
    public List<String> getAndClearNotifications(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        List<String> notifications = userNotifications.remove(userId);
        return notifications != null ? notifications : new ArrayList<>();
    }

    /**
     * Verifica se un utente ha notifiche pendenti.
     * 
     * @param userId l'ID dell'utente
     * @return true se ci sono notifiche, false altrimenti
     */
    public boolean hasNotifications(Integer userId) {
        if (userId == null) {
            return false;
        }

        List<String> notifications = userNotifications.get(userId);
        return notifications != null && !notifications.isEmpty();
    }

    /**
     * Conta il numero di notifiche pendenti per un utente.
     * 
     * @param userId l'ID dell'utente
     * @return numero di notifiche
     */
    public int getNotificationCount(Integer userId) {
        if (userId == null) {
            return 0;
        }

        List<String> notifications = userNotifications.get(userId);
        return notifications != null ? notifications.size() : 0;
    }

    /**
     * Pulisce tutte le notifiche (utile per testing).
     */
    public void clearAllNotifications() {
        userNotifications.clear();
    }
}
