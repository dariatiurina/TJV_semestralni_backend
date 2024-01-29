package cz.cvut.fit.tiuridar.tjv.repository;

import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Test
    void testGetByReviews(){
        User user1 = new User();
        user1.setUsername("username1");
        User user2 = new User();
        user2.setUsername("username2");
        Review r1 = new Review();
        r1.setId(1L);
        r1.setAuthor(user1);
        Review r2 = new Review();
        r2.setId(2L);
        r2.setAuthor(user1);
        Review r3 = new Review();
        r3.setId(3L);
        r3.setAuthor(user1);
        Review r4 = new Review();
        r4.setId(4L);
        r4.setAuthor(user2);

        userRepository.saveAll(List.of(user1, user2));
        reviewRepository.saveAll(List.of(r1, r2, r3, r4));

        Assertions.assertEquals(List.of(user1, user2), userRepository.findAll());
        Assertions.assertEquals(List.of(user1, user2), userRepository.findUsersWhoWrittenMoreThanReviews(1));
        Assertions.assertEquals(List.of(user1), userRepository.findUsersWhoWrittenMoreThanReviews(3));
    }
}
