package at.demanda.lib.tagespost;

import java.util.Arrays;

import at.demanda.lib.tagespost.enums.Approval;
import at.demanda.lib.tagespost.enums.ColorPrint;
import at.demanda.lib.tagespost.enums.DeliveryType;
import at.demanda.lib.tagespost.enums.DuplexPrint;
import at.demanda.lib.tagespost.enums.EnvelopeLogo;
import at.demanda.lib.tagespost.enums.EnvelopeSize;
import at.demanda.lib.tagespost.enums.Orientation;
import at.demanda.lib.tagespost.enums.PaperFormat;

/**
 * Allows to configure the filename for a tages-post.at upload, the filename has
 * print instructions encoded and must follow the specification provided by
 * tages-post.at
 * 
 * @author matthias
 */
public class TagesPostFile {

	public static final int APPROVAL = 0;
	public static final int PAPER_FORMAT = 1;
	public static final int COLOR_PRINT = 2;
	public static final int DUPLEX_PRINT = 3;
	public static final int ENVELOPE_SIZE = 4;
	public static final int ORIENTATION = 5;
	public static final int DELIVERY_TYPE = 6;
	public static final int ENVELOPE_LOGO = 7;
	public static final int RESERVED_FIELD_8 = 8;
	public static final int RESERVED_FIELD_9 = 9;
	public static final int RESERVED_FIELD_10 = 10;
	public static final int RESERVED_FIELD_11 = 11;
	public static final int RESERVED_FIELD_12 = 12;
	public static final int RESERVED_FIELD_13 = 13;
	public static final int RESERVED_FIELD_14 = 14;
	
	private final String ALLOWED_FILENAME_CHARS = "[^a-zA-Z0-9_]";

	private final Approval approval;
	private final PaperFormat paperFormat;
	private final ColorPrint colorPrint;
	private final DuplexPrint duplexPrint;
	private final EnvelopeSize envelopeSize;
	private final Orientation orientation;
	private final DeliveryType deliveryType;
	private final EnvelopeLogo envelopeLogo;

	private final String originalFilename;

	/**
	 * Constructs a {@link TagesPostFile} with all the possible print instructions
	 * as parameter.
	 * 
	 * @param approval
	 * @param paperFormat
	 * @param colorPrint
	 * @param duplexPrint
	 * @param envelopeSize
	 * @param orientation
	 * @param deliveryType
	 * @param envelopeLogo
	 * @param originalFilename
	 */
	public TagesPostFile(Approval approval, PaperFormat paperFormat, ColorPrint colorPrint, DuplexPrint duplexPrint,
			EnvelopeSize envelopeSize, Orientation orientation, DeliveryType deliveryType, EnvelopeLogo envelopeLogo,
			String originalFilename) {
		this.approval = approval;
		this.paperFormat = paperFormat;
		this.colorPrint = colorPrint;
		this.duplexPrint = duplexPrint;
		this.envelopeSize = envelopeSize;
		this.orientation = orientation;
		this.deliveryType = deliveryType;
		this.envelopeLogo = envelopeLogo;
		this.originalFilename = originalFilename;
	}

	/**
	 * Some of the print instructions are in fact not optional because currently
	 * there is only a single option allowed. This constructor hides the single
	 * option flags from the developer.
	 * 
	 * @param approval
	 * @param colorPrint
	 * @param duplexPrint
	 * @param deliveryType
	 * @param envelopeLogo
	 * @param originalFilename
	 */
	public TagesPostFile(Approval approval, ColorPrint colorPrint, DuplexPrint duplexPrint, DeliveryType deliveryType,
			EnvelopeLogo envelopeLogo, String originalFilename) {
		this(approval, PaperFormat.DIN_A4, colorPrint, duplexPrint, EnvelopeSize.DIN_C5, Orientation.Portrait,
				deliveryType, envelopeLogo, originalFilename);
	}

	/**
	 * This constructor builds a {@link TagesPostFile} with default options for most
	 * print instructions.
	 * 
	 * @param approval
	 * @param originalFilename
	 */
	public TagesPostFile(Approval approval, String originalFilename) {
		this(approval, ColorPrint.BlackAndWhite, DuplexPrint.Simplex, DeliveryType.National, EnvelopeLogo.No,
				originalFilename);
	}
	
	public String generateCodeSection() {
		int[] codes = new int[15];
		codes[APPROVAL] = this.approval.ordinal();
		codes[PAPER_FORMAT] = this.paperFormat.ordinal();
		codes[COLOR_PRINT] = this.colorPrint.ordinal();
		codes[DUPLEX_PRINT] = this.duplexPrint.ordinal();
		codes[ENVELOPE_SIZE] = this.envelopeSize.ordinal();
		codes[ORIENTATION] = this.orientation.ordinal();
		codes[DELIVERY_TYPE] = this.deliveryType.ordinal();
		codes[ENVELOPE_LOGO] = this.envelopeLogo.ordinal();
		codes[RESERVED_FIELD_8] = 0;
		codes[RESERVED_FIELD_9] = 0;
		codes[RESERVED_FIELD_10] = 0;
		codes[RESERVED_FIELD_11] = 0;
		codes[RESERVED_FIELD_12] = 0;
		codes[RESERVED_FIELD_13] = 0;
		codes[RESERVED_FIELD_14] = 0;
		return Arrays.stream(codes).mapToObj(c -> String.format("%d", c)).reduce((a, b) -> a.concat(b)).get();
	}
	
	public String generateFileSection() {
		String filename = this.originalFilename.toLowerCase();
		if(filename.endsWith(".pdf")) {
			filename = filename.substring(0, filename.length() - 4);
		}
		filename = filename.replaceAll(ALLOWED_FILENAME_CHARS, "_");
		return String.format("%s.pdf", filename);
	}
	
	public String generateFilename() {
		return String.format("%s-%s", this.generateCodeSection(), this.generateFileSection());
	}

}
