package at.demanda.lib.tagespost;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Uploads a PDF file to www.tages-post.at for further processing.
 * 
 * Alongside the PDF file provided as InputStream also a {@link TagesPostJobSettings} element must be
 * provided which describes how print production at tages-post.at should process the file.
 * 
 * @author matthias
 */
public class TagesPostClient {
	
	static Logger logger = LoggerFactory.getLogger(TagesPostClient.class);
	
	public static final String TAGES_POST_HOST = "www.tages-post.at";
	public static final int TAGES_POST_PORT = 22;
	
	public static void send(String username, InputStream identityFileInputStream, TagesPostJobSettings file, InputStream localFileInputStream) throws IOException, JSchException, SftpException {
		File tmpIdentityFile = File.createTempFile("tagespost-", ".key");
		FileUtils.copyInputStreamToFile(identityFileInputStream, tmpIdentityFile);
		send(username, tmpIdentityFile, file, localFileInputStream);
		tmpIdentityFile.delete();
	}
	
	public static void send(String username, File identityFile, TagesPostJobSettings file, InputStream localFileInputStream) throws IOException, JSchException, SftpException {
		send(username, identityFile.getAbsolutePath(), file, localFileInputStream);
	}
	
	public static void send(String username, String identityFile, TagesPostJobSettings file, InputStream localFileInputStream) throws IOException, JSchException, SftpException {
		upload(username, identityFile, file.generateFilename(), localFileInputStream);
	}

	public static void upload(String username, String identityFile, String remoteFileName, InputStream localFileInputStream) throws JSchException, IOException, SftpException {
		logger.info("Uploading {} to remote server", remoteFileName);
		JSch jsch = new JSch();
		jsch.addIdentity(identityFile);
		Session session = jsch.getSession(username, TAGES_POST_HOST, TAGES_POST_PORT);
		
		Properties sessionProperties = new Properties();
		sessionProperties.setProperty("StrictHostKeyChecking", "no");
		session.setConfig(sessionProperties);
		session.connect();
		logger.info("Session connected");
		
		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect();
		channel.put(localFileInputStream, remoteFileName);
		
		localFileInputStream.close();
		
		channel.disconnect();
		session.disconnect();
		logger.info("SSH system closed down");
	}
	
}
