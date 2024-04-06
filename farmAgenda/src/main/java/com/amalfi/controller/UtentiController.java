package com.amalfi.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amalfi.entity.Utenti;
import com.amalfi.exception.EmailNullException;
import com.amalfi.exception.UserAlreadyExistException;
import com.amalfi.repository.UtentiRepository;

@RestController
public class UtentiController {

	@Autowired
	UtentiRepository userRepo;

	@GetMapping("/login")
	public ResponseEntity<Utenti> loginUtente(String username, String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++)
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		String md5PWD = sb.toString();
		return new ResponseEntity<Utenti>(userRepo.findByEmailAndPassword(username, md5PWD), HttpStatus.OK);
	}

	@GetMapping("listaUtenti")
	public List<Utenti> listaUtenti() {
		return userRepo.findAll();
	}

	@PostMapping("/inserisciUtente")
	public ResponseEntity<Utenti> insertUtente(@RequestBody Utenti user) throws NoSuchAlgorithmException {
		if (!user.getEmail().equals(null) && !user.getEmail().isBlank() && !user.getEmail().isEmpty() ) {
			List<Utenti> userDB = userRepo.findByEmail(user.getEmail());
			if (userDB.size() == 0) {
				Utenti userToSave = new Utenti();

				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(user.getPassword().getBytes());
				byte byteData[] = md.digest();

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++)
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				String md5PWD = sb.toString();

				userToSave.setAbilitato(true);
				userToSave.setCognome(user.getCognome());
				userToSave.setEliminato(false);
				userToSave.setNome(user.getNome());
				userToSave.setEmail(user.getEmail());
				userToSave.setPassword(md5PWD);
				Utenti r = userRepo.save(userToSave);
				return new ResponseEntity<Utenti>(r, HttpStatus.OK);
			}else {
				throw new UserAlreadyExistException("Utente già registrato con questa Email");
			}
		}else {
			throw new EmailNullException("Utente non registrabile senza email");
		}
	}
}
