package foreach.cda.Marmiton.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import foreach.cda.Marmiton.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByMail(String mail);

    Optional<User> findByMail(String mail);
}
