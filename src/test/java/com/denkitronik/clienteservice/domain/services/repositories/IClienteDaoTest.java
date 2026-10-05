package com.denkitronik.clienteservice.domain.repositories;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Integration tests (data slice) — IClienteDao con H2")
class IClienteDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private IClienteDao clienteDao;

    private Region region;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setNombre("Asia");
        em.persist(region);

        cliente = new Cliente();
        cliente.setNombre("Grace");
        cliente.setApellido("Hopper");
        cliente.setEmail("grace@navy.mil");
        cliente.setRegion(region);
        em.persist(cliente);

        em.flush();
    }

    // ── 1. Probar consulta personalizada ───────────────────────
    @Test
    @DisplayName("findAllRegiones — devuelve las regiones persistidas")
    void findAllRegiones_debeRetornarListaDeRegiones() {
        List<Region> regiones = clienteDao.findAllRegiones();

        assertThat(regiones).isNotEmpty();
        assertThat(regiones.get(0).getNombre()).isEqualTo("Asia");
    }

    // ── 2. Probar findById ────────────────────────────────────
    @Test
    @DisplayName("findById — encuentra el cliente existente")
    void findById_debeRetornarCliente() {
        Optional<Cliente> resultado = clienteDao.findById(cliente.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Grace");
        assertThat(resultado.get().getEmail()).isEqualTo("grace@navy.mil");
    }

    // ── 3. Probar findAll ─────────────────────────────────────
    @Test
    @DisplayName("findAll — retorna todos los clientes")
    void findAll_debeRetornarLista() {
        List<Cliente> clientes = clienteDao.findAll();

        assertThat(clientes).hasSize(1);
        assertThat(clientes.get(0).getNombre()).isEqualTo("Grace");
    }

    // ── 4. Probar paginación ──────────────────────────────────
    @Test
    @DisplayName("findAll(Pageable) — retorna página de clientes")
    void findAllPageable_debeRetornarPagina() {
        Page<Cliente> pagina = clienteDao.findAll(PageRequest.of(0, 5));

        assertThat(pagina.getContent()).hasSize(1);
        assertThat(pagina.getTotalElements()).isEqualTo(1);
    }

    // ── 5. Probar save ────────────────────────────────────────
    @Test
    @DisplayName("save — persiste un nuevo cliente y genera ID")
    void save_debePersistirNuevoCliente() {
        Cliente nuevo = new Cliente();
        nuevo.setNombre("Margaret");
        nuevo.setApellido("Hamilton");
        nuevo.setEmail("margaret@mit.edu");
        nuevo.setRegion(region);

        Cliente guardado = clienteDao.save(nuevo);
        em.flush();

        assertThat(guardado.getId()).isNotNull();
        assertThat(em.find(Cliente.class, guardado.getId())).isNotNull();
    }

    // ── 6. Probar delete ──────────────────────────────────────
    @Test
    @DisplayName("deleteById — elimina el cliente verificado con em.find")
    void deleteById_debeEliminarElCliente() {
        Long id = cliente.getId();

        clienteDao.deleteById(id);
        em.flush();

        assertThat(em.find(Cliente.class, id)).isNull();
    }
}