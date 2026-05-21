package Dao;

/**
 * Clasa generica de tip Data Access Object (DAO) care implementeaza operatiile de baza CRUD.
 * Utilizeaza tehnici avansate de Java Reflection si Java Streams pentru a genera dinamic interogarile SQL la runtime.
 *
 * @param <T> Tipul entitatii de model mapate pe tabelul din baza de date (ex: Client, Product).
 * @author Poliec Elena
 */

import Connection.ConnectionFactory;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractDAO <T>{
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Returneaza numele corect al tabelului din baza de date.
     * Daca clasa Java se numește "Bill", in PostgreSQL tabelul este "Log_Bill".
     */
    private String getTableName() {
        if (type.getSimpleName().equalsIgnoreCase("Bill")) {
            return "Log_Bill";
        }
        return type.getSimpleName();
    }

    private String createSelectQuery(String field) {
        return "SELECT * FROM " + getTableName() + " WHERE " + field.toLowerCase() + " = ?";
    }

    /**
     * Returneaza toate inregistrarile dintr-un tabel din baza de date.
     *@return O lista de obiecte de tipul T.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + getTableName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return new ArrayList<>();
    }

    /**
     * Cauta o inregistrare specifica în baza de date pe baza ID-ului primar.
     *@param id ID-ul entitatii cautate.
     * @return Obiectul de tip T daca a fost gasit, sau null in caz contrar.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> list = createObjects(resultSet);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            // SUPORT PENTRU JAVA RECORDS (pt clasa imuabila Bill)
            if (type.isRecord()) {
                RecordComponent[] components = type.getRecordComponents();
                while (resultSet.next()) {
                    final ResultSet finalRs = resultSet;

                    // STREAMS: maparea coloanelor din ResultSet in array-ul de argumente al Record-ului
                    Object[] args = Arrays.stream(components)
                            .map(comp -> {
                                try {
                                    String name = comp.getName().toLowerCase();
                                    Object value = finalRs.getObject(name);
                                    if (value != null) {
                                        if (comp.getType() == double.class || comp.getType() == Double.class) {
                                            return ((Number) value).doubleValue();
                                        } else if (comp.getType() == int.class || comp.getType() == Integer.class) {
                                            return ((Number) value).intValue();
                                        }
                                    }
                                    return value;
                                } catch (SQLException e) {
                                    LOGGER.log(Level.FINE, "Eroare la citirea componentei de record", e);
                                    return null;
                                }
                            }).toArray();

                    // STREAMS: extragerea tipurilor de date pentru identificarea constructorului canonic
                    Class<?>[] argTypes = Arrays.stream(components)
                            .map(RecordComponent::getType)
                            .toArray(Class<?>[]::new);

                    Constructor<T> constructor = type.getDeclaredConstructor(argTypes);
                    list.add(constructor.newInstance(args));
                }
                return list;
            }

            // logica pt clasele simple (Client, Product, Orders)
            // STREAMS: cautarea constructorului fara parametri (default)
            Constructor<?> ctor = Arrays.stream(type.getDeclaredConstructors())
                    .filter(c -> c.getParameterCount() == 0)
                    .findFirst()
                    .orElse(null);

            while (resultSet.next()) {
                if (ctor == null) continue;
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                final ResultSet finalRs = resultSet;

                // STREAMS & LAMBDA: iterarea dinamica peste campurile clasei (Cazul special cu try-catch local pentru exceptii)
                Arrays.stream(type.getDeclaredFields()).forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        Object value = null;
                        try {
                            value = finalRs.getObject(fieldName.toLowerCase());
                        } catch (SQLException sqle) {
                            return; // echivalent cu 'continue' dintr-un for clasic, trece la urmatorul element din Stream
                        }

                        if (value != null) {
                            if (field.getType() == double.class || field.getType() == Double.class) {
                                value = ((Number) value).doubleValue();
                            } else if (field.getType() == int.class || field.getType() == Integer.class) {
                                value = ((Number) value).intValue();
                            } else if (field.getType() == float.class || field.getType() == Float.class) {
                                value = ((Number) value).floatValue();
                            } else if (field.getType() == long.class || field.getType() == Long.class) {
                                value = ((Number) value).longValue();
                            }
                        }

                        field.setAccessible(true);
                        field.set(instance, value);
                    } catch (IllegalAccessException e) {
                        LOGGER.log(Level.SEVERE, "Eroare la scrierea valorii prin reflexie: " + e.getMessage());
                    }
                });
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Eroare la maparea obiectelor prin Reflection: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Inseraza un obiect nou in tabelul corespunzator utilizand Reflection si Streams pentru a construi interogarea.
     *@param t Obiectul care urmează sa fie salvat in baza de date.
     * @return Obiectul inserat cu ID-ul populat sau generat de DB.
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        List<String> names = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        try {
            if (type.isRecord()) {
                // STREAMS: Colectarea numelor si valorilor din componentele Record-ului (adica Bill)
                Arrays.stream(type.getRecordComponents())
                        .filter(comp -> !comp.getName().equalsIgnoreCase("id"))
                        .forEach(comp -> {
                            try {
                                names.add(comp.getName().toLowerCase());
                                Method accessor = comp.getAccessor();
                                accessor.setAccessible(true);
                                values.add(accessor.invoke(t));
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Eroare colectare date Record insert: " + e.getMessage());
                            }
                        });
            } else {
                // STREAMS: colectarea numelor si valorilor din campurile clasei clasice (excluzand id-ul)
                Arrays.stream(type.getDeclaredFields())
                        .filter(field -> !field.getName().equalsIgnoreCase("id"))
                        .forEach(field -> {
                            try {
                                field.setAccessible(true);
                                names.add(field.getName().toLowerCase());
                                values.add(field.get(t));
                            } catch (IllegalAccessException e) {
                                LOGGER.log(Level.WARNING, "Eroare colectare date Field insert: " + e.getMessage());
                            }
                        });
            }

            // STREAMS: construirea dinamica a listei de coloane si a semnelor de intrebare
            String columns = names.stream().collect(Collectors.joining(", "));
            String placeholders = names.stream().map(n -> "?").collect(Collectors.joining(", "));

            String query = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";

            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // STREAMS: atasarea valorilor parametrilor in PreparedStatement folosind un contor atomic
            final PreparedStatement finalStmt = statement;
            java.util.concurrent.atomic.AtomicInteger index = new java.util.concurrent.atomic.AtomicInteger(1);
            values.forEach(val -> {
                try {
                    finalStmt.setObject(index.getAndIncrement(), val);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Eroare setare parametru insert: " + e.getMessage());
                }
            });

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);

                if (type.isRecord()) {
                    RecordComponent[] components = type.getRecordComponents();
                    Object[] args = new Object[components.length];
                    args[0] = generatedId;

                    // STREAMS: generarea indexata a restului de argumente preluate din record-ul original
                    IntStream.range(1, components.length).forEach(i -> {
                        try {
                            Method accessor = components[i].getAccessor();
                            accessor.setAccessible(true);
                            args[i] = accessor.invoke(t);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Eroare reconstructie Record argumente: " + e.getMessage());
                        }
                    });

                    Class<?>[] argTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
                    Constructor<T> constructor = type.getDeclaredConstructor(argTypes);
                    return constructor.newInstance(args);
                } else {
                    Field idField = type.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(t, generatedId);
                }
            }
            return t;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage(), e);
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Actualizeaza o inregistrare existenta din baza de date.
     *@param t Obiectul modificat care contine ID-ul primar neschimbat.
     * @return Obiectul actualizat sau null in caz de eroare.
     */
    public T update(T t) {
        if (type.isRecord()) {
            throw new UnsupportedOperationException("Modificarea inregistrarilor din tabelul de tip Log nu este permisa!");
        }

        Connection connection = null;
        PreparedStatement statement = null;

        // STREAMS: maparea listei de structuri "nume_camp = ?" unite prin virgula
        String setClause = Arrays.stream(type.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> field.getName().toLowerCase() + " = ?")
                .collect(Collectors.joining(", "));

        String query = "UPDATE " + getTableName() + " SET " + setClause + " WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            // STREAMS: colectam intr-o lista mutabila valorile tuturor campurilor non-id
            List<Object> values = Arrays.stream(type.getDeclaredFields())
                    .filter(field -> !field.getName().equalsIgnoreCase("id"))
                    .map(field -> {
                        try {
                            field.setAccessible(true);
                            return field.get(t);
                        } catch (IllegalAccessException e) {
                            LOGGER.log(Level.WARNING, "Eroare citire camp update: " + e.getMessage());
                            return null;
                        }
                    }).collect(Collectors.toCollection(ArrayList::new));

            // extragerea ID-ului separat pt a fi pozitionat la finalul parametrilor (clauza WHERE id = ?)
            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            values.add(idField.get(t));

            // STREAMS: injectarea tuturor valorilor colectate in statement
            final PreparedStatement finalStmt = statement;
            java.util.concurrent.atomic.AtomicInteger idx = new java.util.concurrent.atomic.AtomicInteger(1);
            values.forEach(val -> {
                try {
                    finalStmt.setObject(idx.getAndIncrement(), val);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Eroare setare parametru update: " + e.getMessage());
                }
            });

            statement.executeUpdate();
            return t;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Sterge o inregistrare din baza de date pe baza ID-ului sau unic.
     * Aceasta operatie este securizata si permisa doar pentru Clienti si Produse.
     *@param id ID-ul inregistrarii care va fi sterse.
     * @return true daca stergerea a avut loc cu succes, false in caz contrar.
     */
    public boolean delete(int id) {
        if (type.isRecord()) {
            throw new UnsupportedOperationException("Stergerea inregistrarilor din tabelul de tip Log nu este permisa!");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return false;
    }
}