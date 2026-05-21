package Model;

/**
 * Clasa reprezinta entitatea Orders din aplicatie.
 * Este mapata direct pe tabelul corespunzator din baza de date PostgreSQL
 * si incapsuleaza toate atributele specifice unei comenzi plasate de un client.
 * @author Poliec Elena
 * @version 1.0
 */

public class Orders {
    private int id;
    private int clientId;
    private int productId;
    private int quantity;


    /**
     * Constructor implicit (fara parametri).
     * Este obligatoriu pentru instantierea dinamica a obiectului in Data Access Layer
     * utilizand mecanismele de Java Reflection atunci cand se mapeaza randurile din ResultSet.
     */

    // Constructor gol - Obligatoriu pentru Reflection!
    public Orders() {
    }

    /**
     * Constructor complet pentru initializarea unei comenzi cu toate atributele sale,
     * inclusiv identificatorul unic preluat din baza de date.
     * @param id identificatorul unic al comenzii.
     * @param clientId  identificatorul unic al clientului care a plasat comanda.
     * @param productId identificatorul unic al produsului comandat.
     * @param quantity  cantitatea de produse comandata.
     */

    public Orders(int id, int clientId, int productId, int quantity) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Constructor fara ID
    public Orders(int clientId, int productId, int quantity) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Returneaza identificatorul unic al comenzii.
     * @return ID-ul comenzii ca valoare intreaga.
     */

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Order [id=" + id + ", clientId=" + clientId + ", productId=" + productId + ", quantity=" + quantity + "]";
    }

}
