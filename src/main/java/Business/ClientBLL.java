package Business;

import Model.Client;
import Dao.ClientDAO;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Clasa responsabila de gestionarea regulilor de afaceri specifice entitatii Client.
 * Face legatura intre componentele prezentarii (UI) si stratul de acces la date (DAO).
 * @author Poliec Elena
 * @version 1.0
 */

public class ClientBLL {
    private ClientDAO clientDAO;

    /**
     * Constructor implicit care initializeaza instanaa utilizata pentru
     * accesul la metodele specifice clasei {@link Dao.ClientDAO}.
     */

    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    /**
     * Cauta un client in baza de date dupa identificatorul sau unic.
     * * @param id Identificatorul unic (ID) al clientului cautat.
     * @return Obiectul {@link Model.Client} corespunzator ID-ului transmis.
     * @throws NoSuchElementException Dacă nu a fost gasit niciun client cu ID-ul specificat.
     */

    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }


    /**
     * Returneaza o lista cu toti clientii inregistrati in sistem.
     * @return O colectie de tip {@link java.util.List} ce contine toate obiectele Client.
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Valideaza si insereaza un client nou in baza de date.
     * Inainte de inserare, utilizeaza tehnici de reflexie pentru a tipari in consola
     * proprietatile obiectului in scop de debugging.
     * @param client Obiectul Client care conține datele ce vor fi salvate.
     * @return Valoarea 1 daca operația de inserare a avut succes, altfel 0.
     */

    public int insertClient(Client client) {
        System.out.println("--- Debug Reflection: Se insereaza un client nou ---");
        Sstart.Reflection.retrieveProperties(client);
        System.out.println("------------------------------------------------");

        return clientDAO.insert(client) != null ? 1 : 0;
    }


    /**
     * Actualizeaza informatiile unui client existent in baza de date.
     * @param client Obiectul Client care contine ID-ul existent si noile valori modificate.
     * @return Valoarea 1 daca actualizarea a reusit, altfel 0.
     */
    public int updateClient(Client client) {
        return clientDAO.update(client) != null ? 1 : 0;
    }

    /**
     * Ș
     * Sterge un client din baza de date pe baza id-ului.
     * @param id identificatorul unic al clientului care urmeaza sa fie eliminat.
     * @return True daca stergerea randului a fost efectuata cu succes, false altfel.
     */
    public boolean deleteClient(int id) {
        return clientDAO.delete(id);
    }
}