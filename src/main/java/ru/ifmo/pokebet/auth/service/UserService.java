package ru.ifmo.pokebet.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pokebet.auth.model.Role;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.repo.UserRepository;
import ru.ifmo.pokebet.exception.NotFoundException;
import ru.ifmo.pokebet.exception.UserAlreadyExists;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    public User update(User user) {
        return repository.update(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new UserAlreadyExists(user.getUsername());
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public User getById(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean isCurrentUserAdmin() {
        return getCurrentUser().getRole() == Role.ADMIN;
    }

    @Transactional
    public void changeRole(User user, Role role) {
        user.setRole(role);
        repository.update(user);
    }
}
