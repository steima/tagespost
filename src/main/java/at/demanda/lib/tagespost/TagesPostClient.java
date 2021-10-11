package at.demanda.lib.tagespost;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

	public static final String TAGES_POST_HOST = "transfer.sendhybrid.com";
	public static final int TAGES_POST_PORT = 22;

	public static void send(String username, String password, TagesPostJobSettings file, InputStream localFileInputStream) throws IOException, JSchException, SftpException {
		upload(username, password, file.generateFilename(), localFileInputStream);
	}

	public static void upload(String username, String password, String remoteFileName, InputStream localFileInputStream) throws JSchException, IOException, SftpException {
		logger.info("Uploading {} to remote server", remoteFileName);
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, TAGES_POST_HOST, TAGES_POST_PORT);
		session.setPassword(password);

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
