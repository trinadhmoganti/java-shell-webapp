package com.example.tm.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tm.util.ShellExecutor;

@RestController
public class WelcomeController {

	@Autowired
	private ApplicationContext appCtx;

	@RequestMapping("/")
	public ResponseEntity<String> index() {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Cache-Control", "no-store");
		try {
			String indexContent = IOUtils.toString(
					appCtx.getResource("classpath:static/html/index.html").getInputStream(), StandardCharsets.UTF_8);
			System.out.println("rendering index.html");
			return new ResponseEntity<String>(indexContent, responseHeaders, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("<html><body><h1>" + e.toString() + "</h1></body></html>",
					responseHeaders, HttpStatus.ACCEPTED);
		}
	}

	@PostMapping("/executeScript")
	public ResponseEntity<String> executeCommand(@RequestParam("selectScript") String command) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Cache-Control", "no-store");

		if (command == null || command.equals("")) {
			return new ResponseEntity<String>("Empty Command", responseHeaders, HttpStatus.BAD_REQUEST);
		}

		List<String> shellOutput = ShellExecutor.executeScript(command);

		JSONArray resultArray = new JSONArray(shellOutput);

		return new ResponseEntity<String>(resultArray.toString(), responseHeaders, HttpStatus.OK);
	}
	

}
