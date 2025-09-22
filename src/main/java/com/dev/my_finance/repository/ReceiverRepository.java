package com.dev.my_finance.repository;

import com.dev.my_finance.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {

    Optional<Receiver> findByIdAndLenderId(Long id, Long lenderId);

    List<Receiver> findByLenderId(Long lenderId);

}
