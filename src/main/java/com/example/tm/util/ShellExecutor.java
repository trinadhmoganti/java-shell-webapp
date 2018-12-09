package com.example.tm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.tm.constants.ServerConstants;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellExecutor {

	public static List<String> executeScript(String userInput) {

		List<String> result = new ArrayList<String>();
		
		if (userInput == null || userInput.equals("")) {
			return result;
		}

		try {
			Session session = getSession();
			session.connect();
			System.out.println("Connected to " + ServerConstants.HOST);

			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			InputStream in = channelExec.getInputStream();

			System.out.println("Input from UI:  " + userInput);

			StringBuffer commands = new StringBuffer();
			// commands.append("ls");
			commands.append(userInput);

			System.out.println(commands);

			channelExec.setCommand(commands.toString());
			channelExec.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line;

			while ((line = reader.readLine()) != null) {
				result.add(line);
				System.out.println(line);
			}

			channelExec.disconnect();
			session.disconnect();

			int exitStatus = channelExec.getExitStatus();

			System.out.println("Channel Exit Status: " + exitStatus);
			System.out.println(result);

			return result;

		} catch (JSchException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Session getSession() {
		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(ServerConstants.USERNAME, ServerConstants.HOST, ServerConstants.PORT);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(ServerConstants.PASSWORD);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return session;
	}

}
