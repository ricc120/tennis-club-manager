package it.tennis_club.business_logic;

import it.tennis_club.domain_model.Prenotazione;
import it.tennis_club.domain_model.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Servizio per l'invio di notifiche email in modo asincrono.
 * 
 * L'asincronia (@Async) garantisce che l'invio della mail non blocchi
 * la risposta HTTP all'utente. Il metodo viene eseguito in un thread separato.
 * 
 * Se le notifiche sono disabilitate (app.notifications.enabled=false),
 * il servizio si limita a loggare il messaggio senza inviare email.
 */
@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.notifications.enabled:false}")
    private boolean notificationsEnabled;

    @Value("${spring.mail.username:noreply@tennisclub.it}")
    private String fromAddress;

    /**
     * Costruttore di default per quando JavaMailSender non è disponibile.
     * Le notifiche verranno solo loggate, non inviate via email.
     */
    public NotificationService() {
        this.mailSender = null;
    }

    @Autowired(required = false)
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Invia una conferma di prenotazione via email in modo asincrono.
     * Se le notifiche sono disabilitate, logga il messaggio.
     * 
     * @param utente       l'utente destinatario
     * @param prenotazione la prenotazione confermata
     */
    @Async
    public void inviaConfermaPrenotazione(Utente utente, Prenotazione prenotazione) {
        String oggetto = "Conferma Prenotazione - Tennis Club";
        String corpo = String.format(
                "Ciao %s,\n\n" +
                        "La tua prenotazione è stata confermata!\n\n" +
                        "Dettagli:\n" +
                        "- Campo: %s\n" +
                        "- Data: %s\n" +
                        "- Ora: %s\n\n" +
                        "A presto!\nTennis Club Manager",
                utente.getNome(),
                prenotazione.getCampo().getNome(),
                prenotazione.getData(),
                prenotazione.getOraInizio());

        inviaEmail(utente.getEmail(), oggetto, corpo);
    }

    /**
     * Invia una notifica di cancellazione prenotazione via email in modo asincrono.
     * Se le notifiche sono disabilitate, logga il messaggio.
     * 
     * @param utente       l'utente destinatario
     * @param prenotazione la prenotazione cancellata
     */
    @Async
    public void inviaNotificaCancellazione(Utente utente, Prenotazione prenotazione) {
        String oggetto = "Cancellazione Prenotazione - Tennis Club";
        String corpo = String.format(
                "Ciao %s,\n\n" +
                        "La tua prenotazione è stata cancellata.\n\n" +
                        "Dettagli della prenotazione cancellata:\n" +
                        "- Campo: %s\n" +
                        "- Data: %s\n" +
                        "- Ora: %s\n\n" +
                        "Per qualsiasi domanda, contattaci.\nTennis Club Manager",
                utente.getNome(),
                prenotazione.getCampo().getNome(),
                prenotazione.getData(),
                prenotazione.getOraInizio());

        inviaEmail(utente.getEmail(), oggetto, corpo);
    }

    /**
     * Metodo interno per inviare un'email.
     * Se le notifiche sono disabilitate, logga il messaggio senza inviarlo.
     */
    private void inviaEmail(String destinatario, String oggetto, String corpo) {
        if (!notificationsEnabled) {
            System.out.println("[NOTIFICATION - SIMULATA] Email a: " + destinatario);
            System.out.println("[NOTIFICATION - SIMULATA] Oggetto: " + oggetto);
            System.out.println(
                    "[NOTIFICATION - SIMULATA] (Notifiche email disabilitate, impostare app.notifications.enabled=true per abilitare)");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(destinatario);
            message.setSubject(oggetto);
            message.setText(corpo);

            mailSender.send(message);
            System.out.println("[NOTIFICATION] Email inviata con successo a: " + destinatario);

        } catch (Exception e) {
            // Non rilanciare l'eccezione: l'invio email non deve bloccare il flusso
            // principale
            System.err.println(
                    "[NOTIFICATION - ERRORE] Impossibile inviare email a " + destinatario + ": " + e.getMessage());
        }
    }
}
