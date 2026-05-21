package Business;

import Model.Product;
import Dao.ProductDAO;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Clasă din stratul Business Logic responsabilă de validarea și gestionarea operațiilor pentru Produse.
 * Face legătura între Presentation Layer și ProductDAO.
 * * @author Poliec Elena
 */

public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    /**
     * Cauta un produs dupa ID-ul sau si arunca o exceptie daca acesta nu exista.
     * * @param id ID-ul produsului cautat.
     * @return Obiectul Product gasit.
     * @throws NoSuchElementException Daca produsul cu ID-ul specificat nu exista in baza de date.
     */

    public Product findProductById(int id) {
        Product p = productDAO.findById(id);
        if (p == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return p;
    }


    /**
     * Returneaza o lista completa cu toate produsele inregistrate in stocul curent al depozitului.
     * @return O colectie de tip {@link java.util.List} ce contine toate obiectele Product.
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Insereaza un produs nou in baza de date si valideaza succesul operatiei.
     * @param product Obiectul Product care contine atributele noii entitati (nume, stoc, preț).
     * @return Valoarea 1 daca produsul a fost adaugat cu succes, altfel 0.
     */
    public int insertProduct(Product product) {
        return productDAO.insert(product) != null ? 1 : 0;
    }

    /**
     * Actualizeaza informatiile unui produs existent in baza de date.
     * @param product Obiectul Product care contine ID-ul existent si noile proprietati modificate.
     * @return Valoarea 1 daca actualizarea datelor a reusit, altfel 0
     */
    public int updateProduct(Product product) {
        return productDAO.update(product) != null ? 1 : 0;
    }

    /**
     * Sterge un produs din sistem pe baza ID-ului primit din interfata.
     * * @param id ID-ul produsului care se doreste a fi sters.
     * @return true daca stergerea a reusit, false altfel.
     */

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }
}