package Model;

/**
 * Aceasta clasa reprezinta entitatea Client din baza de date.
 * Detine informatiile personale si de contact ale clientilor depozitului.
 * * @author Poliec Elena
 * @version 1.0
 */

public class Client {
    private int id;
    private String name;
    private String address;
    private String email;


    /**
     * Constructorul implicit (fara parametri) utilizat de tehnologia Reflection.
     */

    public Client() {
    }

    /**
     * Constructor utilizat pentru editarea sau preluarea unui client existent din baza de date.
     * * @param id      ID-ul unic al clientului primit din baza de date.
     * @param name    Numele intreg al clientului.
     * @param address Adresa de livrare a clientului.
     * @param email   Adresa de contact a clientului.
     */

    public Client(int id, String name, String address, String email) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * Constructor utilizat pentru crearea unui client nou (inainte de a primi un ID de la baza de date).
     * * @param name    Numele intreg al clientului.
     * @param address Adresa de livrare a clientului.
     * @param email   Adresa de contact a clientului.
     */

    public Client(String name, String address, String email) {
        super();
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + ", address=" + address + ", email=" + email + "]";
    }

}
