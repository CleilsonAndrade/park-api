package br.cleilsonandrade.parkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cleilsonandrade.parkapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
