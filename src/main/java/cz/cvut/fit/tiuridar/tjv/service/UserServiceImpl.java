package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.domain.User;
import cz.cvut.fit.tiuridar.tjv.repository.ReviewRepository;
import cz.cvut.fit.tiuridar.tjv.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@Component
public class UserServiceImpl extends CrudServiceImpl<User, String> implements UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    public UserServiceImpl(UserRepository userRepository, ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void update(String username, User data) {
        Optional<User> updateUser = userRepository.findById(username);
        if(updateUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        data.setUsername(username);
        if(data.getPassword() == null)
            data.setPassword(updateUser.get().getPassword());
        if(data.getDateOfBirth() == null)
            data.setDateOfBirth(updateUser.get().getDateOfBirth());
        if(data.getWrittenByUser() == null)
            data.setWrittenByUser(updateUser.get().getWrittenByUser());
        if(data.getRealName() == null)
            data.setRealName(updateUser.get().getRealName());
        userRepository.save(data);
    }

    @Override
    public Collection<User> findUsersWhoWrittenMoreThanReviews(Integer sizeReviews){
        return userRepository.findUsersWhoWrittenMoreThanReviews(sizeReviews);
    }

    @Override
    public void deleteById(String id) {
        if(getRepository().findById(id).isEmpty())
            throw new IllegalArgumentException();
        for(Review i : getRepository().findById(id).get().getWrittenByUser())
            reviewRepository.deleteById(i.getId());
        userRepository.deleteById(id);
    }

    @Override
    protected CrudRepository<User, String> getRepository() {
        return userRepository;
    }
}
