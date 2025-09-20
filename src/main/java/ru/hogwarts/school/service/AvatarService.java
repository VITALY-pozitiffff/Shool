package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

import java.util.Optional;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final Logger logger = LoggerFactory.getLogger(AvatarService.class); // Добавляем логгер

    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Page<Avatar> paginatedAvatars(SpringDataWebProperties.Pageable pageable) {
        logger.debug("Invoked method paginatedAvatars"); // Логируем вызов метода с уровнем DEBUG
        return avatarRepository.findAll((Pageable) pageable);
    }

    public Avatar saveAvatar(Avatar avatar) {
        logger.info("Invoked method saveAvatar"); // Логируем сохранение аватара с уровнем INFO
        return avatarRepository.save(avatar);
    }

    public Optional<Avatar> findAvatarById(Long id) {
        logger.debug("Invoked method findAvatarById with id={}", id); // Логируем получение аватара с уровнем DEBUG
        return avatarRepository.findById(id);
    }

    public void deleteAvatar(Long id) {
        logger.warn("Deleting avatar with id={}", id); // Логируем удаление аватара с уровнем WARN
        avatarRepository.deleteById(id);
    }
}