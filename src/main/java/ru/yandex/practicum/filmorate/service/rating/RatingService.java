package ru.yandex.practicum.filmorate.service.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

/**
 * Класс RatingService предоставляет функциональность по
 * взаимодействию с перечнем рейтингов фильмов.
 */
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingStorage ratingDbStorage;


    /**
     * Метод добавляяет новый рейтинг в список в случае,
     * если он в нём отсутствует.
     * В случае успеха возвращает добавленный объект.
     * @param rating
     * @return
     */
    public MPA postRating(MPA rating) {

        if (ratingDbStorage.containsRating(rating.getId())) {
            return null;
        }

        ratingDbStorage.addRating(rating);
        return rating;
    }


    /**
     * Метод обновляет объект рейтинга в списке в случае,
     * если он в нём присутствует. В случае успеха возвращает обновлённый объект.
     * @param rating
     * @return
     */
    public MPA putRating(MPA rating) {

        if (!ratingDbStorage.containsRating(rating.getId())) {
            throw new NotFoundException("Рейтинг отсутствует в списке!");
        }

        ratingDbStorage.addRating(rating);
        return rating;
    }


    /**
     * Метод возвращает список рейтингов из хранилища.
     * @return
     */
    public List<MPA> getRatings() {

        return ratingDbStorage.getRatings();

    }


    /**
     * Метод возвращает объект рейтинга по его id из хранилища.
     * @param id
     * @return
     */
    public MPA getRating(long id) {

        MPA rating = ratingDbStorage.getRating(id);
        if (rating == null)
            throw new NotFoundException("Рейтинг " + id + " отсутствует в списке!");

        return rating;

    }


    /**
     * Метод удаляет объект рейтинга по его id из хранилища.
     * @param id
     * @return
     */
    public MPA deleteRating(long id) {

        MPA rating = ratingDbStorage.getRating(id);
        if (rating == null)
            throw new NotFoundException("Рейтинг " + id + " отсутствует в списке!");

        ratingDbStorage.deleteRating(id);
        return rating;

    }
}
