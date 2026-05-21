package Model;

/**
 * Reprezintă o factură imutabilă (Bill) generată automat la finalizarea unei comenzi.
 * Conform cerințelor, această clasă este un Java Record și datele pot fi doar introduse sau citite, fără modificări.
 * * @param orderId     ID-ul comenzii asociate.
 * @param clientName  Numele clientului care a plasat comanda.
 * @param productName Denumirea produsului achiziționat.
 * @param quantity    Cantitatea cumpărată.
 * @param totalPrice  Valoarea totală a facturii.
 * * @author Poliec Elena
 */

public record Bill(int id, int orderId, String clientName, String productName, int quantity, double totalPrice) {

    // Constructor secundar: permite sa creez o factura noua in OrderBLL scrind doar:
    // new Bill(orderId, clientName, productName, quantity, totalPrice);
    public Bill(int orderId, String clientName, String productName, int quantity, double totalPrice) {
        this(0, orderId, clientName, productName, quantity, totalPrice);
    }
}