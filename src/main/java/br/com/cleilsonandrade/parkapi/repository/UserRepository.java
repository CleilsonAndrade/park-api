package br.com.cleilsonandrade.parkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
