package com.denkitronik.clienteservice.controller;

import com.denkitronik.clienteservice.entities.Region;
import com.denkitronik.clienteservice.model.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/${api.version}/cliente-service")
public class ClienteRestController {

    @Autowired
    private IClienteDao clienteDao;

    @GetMapping("/clientes/regiones")
    public ResponseEntity<List<Region>> listarRegiones() {
        return ResponseEntity.ok(clienteDao.findAllRegiones());
    }
}