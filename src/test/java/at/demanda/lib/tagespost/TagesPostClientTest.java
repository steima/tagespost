package at.demanda.lib.tagespost;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import at.demanda.lib.tagespost.enums.Approval;
import at.demanda.lib.tagespost.enums.ColorPrint;
import at.demanda.lib.tagespost.enums.DeliveryType;
import at.demanda.lib.tagespost.enums.DuplexPrint;
import at.demanda.lib.tagespost.enums.EnvelopeLogo;

public class TagesPostClientTest {
	
	static Logger logger = LoggerFactory.getLogger(TagesPostClientTest.class);

	@Test
	public void testClientConnection() throws IOException, JSchException, SftpException {
		logger.info("Testing upload to private SCP server");
		TagesPostClient.send("p_postAt_sftpin-demanda", "/Users/matthias/Desktop/tages-post.key", new TagesPostFile(Approval.ManualApproval, "mahnung.pdf"), new FileInputStream("/Users/matthias/Desktop/invoice-196.pdf"));
	}
	
	@Test
	public void testTagesPostFile() {
		String filename = "mahnung.pdf";
		TagesPostFile f = new TagesPostFile(Approval.ManualApproval, filename);
		String expected = "000000000000000-mahnung.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTagesPostFileWithUmlauts() {
		String filename = "mahnung-Österreicher.pdf";
		TagesPostFile f = new TagesPostFile(Approval.ManualApproval, filename);
		String expected = "000000000000000-mahnung__sterreicher.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTagesPostFileWithUmlautsColorDuplexNationalRegisteredNoLogo() {
		String filename = "mahnung-Österreicher.pdf";
		TagesPostFile f = new TagesPostFile(Approval.ManualApproval, ColorPrint.Color, DuplexPrint.Duplex, DeliveryType.NationalRegistered, EnvelopeLogo.No, filename);
		String expected = "001100100000000-mahnung__sterreicher.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
}
