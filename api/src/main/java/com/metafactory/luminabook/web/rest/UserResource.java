package com.metafactory.luminabook.web.rest;

import com.metafactory.luminabook.service.UserService;
import com.metafactory.luminabook.service.dto.AdminUserDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getAllUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Users");

        Page<AdminUserDTO> page = userService.getAllManagedUsers(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("items", page.getContent());
        response.put("pageNumber", page.getNumber());
        response.put("pageSize", page.getSize());
        response.put("pageCount", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable("id") String id) {
        LOG.debug("REST request to get User : {}", id);
        return userService
            .getUserWithAuthoritiesById(id)
            .map(AdminUserDTO::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
