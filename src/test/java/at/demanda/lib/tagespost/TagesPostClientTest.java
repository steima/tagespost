package at.demanda.lib.tagespost;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.demanda.lib.tagespost.enums.Approval;
import at.demanda.lib.tagespost.enums.ColorPrint;
import at.demanda.lib.tagespost.enums.DeliveryType;
import at.demanda.lib.tagespost.enums.DuplexPrint;
import at.demanda.lib.tagespost.enums.EnvelopeLogo;

public class TagesPostClientTest {
	
	static Logger logger = LoggerFactory.getLogger(TagesPostClientTest.class);
	
	@Test
	public void testTagesPostFile() {
		String filename = "mahnung.pdf";
		TagesPostJobSettings f = new TagesPostJobSettings(Approval.ManualApproval, filename);
		String expected = "000000000000000-mahnung.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTagesPostFileWithUmlauts() {
		String filename = "mahnung-Österreicher.pdf";
		TagesPostJobSettings f = new TagesPostJobSettings(Approval.ManualApproval, filename);
		String expected = "000000000000000-mahnung__sterreicher.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTagesPostFileWithUmlautsColorDuplexNationalRegisteredNoLogo() {
		String filename = "mahnung-Österreicher.pdf";
		TagesPostJobSettings f = new TagesPostJobSettings(Approval.ManualApproval, ColorPrint.Color, DuplexPrint.Duplex, DeliveryType.NationalRegistered, EnvelopeLogo.No, filename);
		String expected = "001100100000000-mahnung__sterreicher.pdf";
		String actual = f.generateFilename();
		assertEquals(expected, actual);
	}
	
}
