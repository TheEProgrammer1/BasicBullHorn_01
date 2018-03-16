package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface UserMessagesRepository extends CrudRepository<Message, Long>
{
    public Iterable<Message> findAllBySentby(String sentBy);
}
