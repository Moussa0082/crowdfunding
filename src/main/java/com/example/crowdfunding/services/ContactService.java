package com.example.crowdfunding.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crowdfunding.models.Alerte;
import com.example.crowdfunding.models.Contact;
import com.example.crowdfunding.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;
    @Autowired
    EmailService emailService;

    public Contact createContact(Contact contact) {
        contact.setIdContact(UUID.randomUUID().toString());
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        contact.setDateAjout(formattedDateTime);
        Alerte al = new Alerte(contact.getEmail(), contact.getMessage());
        emailService.sendSimpleMail(al);

        return contactRepository.save(contact);
    }

    public Contact updateContact(Contact contact, String id){
        Contact c = contactRepository.findById(id).orElseThrow(() -> new IllegalStateException("Couldn't find contact"));
        c.setNomComplet(contact.getNomComplet());
        c.setTelephone(contact.getTelephone());
        c.setEmail(contact.getEmail());
        c.setMessage(contact.getMessage());

        return contactRepository.save(c);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = contactRepository.findAll();

        if(contacts.isEmpty())
            throw new IllegalStateException("No contacts");

        contacts.sort(Comparator.comparing(Contact::getDateAjout).reversed());

        return contacts;
    }

    public String deleteContact(String id){
        Contact contact = contactRepository.findById(id).orElseThrow(()-> new IllegalStateException("Couldn't find contact"));

        contactRepository.delete(contact);
        return "Supprimé avec succèss";
    }
}