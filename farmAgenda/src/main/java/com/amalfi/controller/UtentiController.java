package com.amalfi.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amalfi.entity.Utenti;
import com.amalfi.repository.UtentiRepository;

@RestController
public class UtentiController {
	
	@Autowired
	UtentiRepository userRepo;
	
	@GetMapping("/login")
	public Utenti loginUtente(String username,String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        String md5PWD = sb.toString();
        return userRepo.findByEmailAndPassword(username,md5PWD);
	}

}
