package Business;

import Model.Client;
import Model.Orders;
import Model.Product;
import Model.Bill;
import Dao.ClientDAO;
import Dao.OrdersDAO;
import Dao.ProductDAO;
import Dao.BillDAO;
import Sstart.Reflection;

import java.util.NoSuchElementException;

/**
 * Clasa din stratul Business Logic care controleaza fluxul de creare a unei comenzi.
 * Implementeaza verificarile de stoc si generarea automata a facturilor de tip Bill.
 * * @author Poliec Elena
 */

public class OrderBLL {
    private final OrdersDAO orderDAO;
    private final ProductDAO productDAO;
    private final ClientDAO clientDAO;
    private final BillDAO billDAO;

    public OrderBLL() {
        orderDAO = new OrdersDAO();
        productDAO = new ProductDAO();
        clientDAO = new ClientDAO();
        billDAO = new BillDAO();
    }

    /**
     * Valideaza si plaseaza o comanda noua in sistem.
     * Verifica stocul disponibil; daca este suficient, scade cantitatea din tabelul Product
     * si insereaza o factura imutabila in jurnalul Log_Bill folosind tehnici de Reflection.
     * * @param clientId  ID-ul clientului care comanda.
     * @param productId ID-ul produsului solicitat.
     * @param quantity  Cantitatea dorita.
     * @throws IllegalArgumentException Daca stocul este insuficient sau daca ID-urile sunt invalide.
     */

    public void createOrder(int clientId, int productId, int quantity) {
        Client client = clientDAO.findById(clientId);
        Product product = productDAO.findById(productId);

        if (client == null) throw new NoSuchElementException("Clientul cu ID-ul " + clientId + " nu a fost gasit!");
        if (product == null) throw new NoSuchElementException("Produsul cu ID-ul " + productId + " nu a fost gasit!");

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Stoc insuficient! Disponibil: " + product.getStock());
        }

        Orders order = new Orders(clientId, productId, quantity);
        orderDAO.insert(order);

        product.setStock(product.getStock() - quantity);
        productDAO.update(product);

        // generam factura (foloseste constructorul fara ID, deci pleaca cu id=0 in memorie)
        double totalPrice = product.getPrice() * quantity;
        Bill billInitial = new Bill(order.getId(), client.getName(), product.getName(), quantity, totalPrice);

        // O salvam in DB si o sa primesc id-ul real din postgres
        Bill billSalvat = billDAO.insert(billInitial);

        // folosim clasa reflection si se va printa ID-ul real (1, 2, 3...) generat de Postgres
        System.out.println("\n--- [LOG SYSTEM] Factura salvata in DB (Inspectata prin Reflection) ---");
        Reflection.retrieveProperties(billSalvat);
        System.out.println("-----------------------------------------------------------------------\n");
    }
}