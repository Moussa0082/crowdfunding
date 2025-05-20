package com.example.crowdfunding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Contact;

public interface ContactRepository  extends JpaRepository<Contact, String>{
    
}
