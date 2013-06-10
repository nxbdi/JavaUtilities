package com.nxbdi.util.logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.MessagingException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * @author spannt
 * 
 */
public class LogScraper {

  /**
	 * @param args
	 */
	public static void main(String[] args) {
		String SFTPHOST = "url";
		int SFTPPORT = 22;
		String SFTPUSER = "username";
		String SFTPPASS = "password";
		String SFTPWORKINGDIR = "/directory";
		String SERRORFILE = "Error.log";
		String SOUTFILE = "Out.log";
		boolean sendEmail = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		StringBuilder out = new StringBuilder();

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			
			System.out.println("Error File");
			out.append("Error File:").append(
					LogScraper.parseStream(channelSftp.get(SERRORFILE)));
			
			System.out.println("Output File");
			out.append("Output File:").append(
					LogScraper.parseStream(channelSftp.get(SOUTFILE)));

		} catch (Exception ex) {
			ex.printStackTrace();
			out.append(ex.getLocalizedMessage());
		}

		System.out.println("Logs=" + out.toString());
	}

	/**
	 * 
	 *  line.contains("Exception") || 
	 *  
	 * @param file
	 * @return String of error data
	 */
	public static String parseStream(InputStream inputFileStream) {
		if ( null == inputFileStream ) { return "Log Empty"; }
		StringBuilder out = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputFileStream));

		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("OutOfMemoryError")) {
					out.append(line).append(System.lineSeparator());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			out.append(e.getLocalizedMessage());
		}

		return out.toString();
	}
}
