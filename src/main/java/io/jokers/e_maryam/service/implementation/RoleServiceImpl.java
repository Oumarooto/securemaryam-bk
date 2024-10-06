package io.jokers.e_maryam.service.implementation;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.repository.RoleRepository;
import io.jokers.e_maryam.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository<Role> roleRepository;
    @Override
    public Role getRoleByUserId(Long userId) {
        return roleRepository.getRoleByUserId(userId);
    }
}
