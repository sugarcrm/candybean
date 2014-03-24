package com.sugarcrm.candybean.utilities;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;
import org.monte.media.Registry;
import com.sugarcrm.candybean.configuration.Configuration;

public class SpecializedScreenRecorder extends ScreenRecorder {

	/**
	 * The default maximum duration of a recording in milliseconds (20 minutes)
	 */
	private static final String MAX_DURATION_DEFAULT_MS = "1200000";

	/**
	 * The default maximum size of a recording in kilobytes (500 MB)
	 */
	private static final String MAX_SIZE_DEFAULT_KB = "512000";

	private String name;

	/**
	 * Candybean configurations
	 */
	private Configuration config;

	public SpecializedScreenRecorder(GraphicsConfiguration cfg, String name,
			Configuration config) throws IOException, AWTException {
		super(cfg, new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey,
				config.getValue("video.format", MIME_QUICKTIME)), new Format(
				MediaTypeKey, MediaType.VIDEO, EncodingKey,
				config.getValue("video.encoding",
						ENCODING_QUICKTIME_CINEPAK),
				CompressorNameKey, config.getValue("video.compression",
						COMPRESSOR_NAME_QUICKTIME_CINEPAK), DepthKey,
				(int) 24, FrameRateKey, Rational.valueOf(Integer
						.parseInt(config.getValue("video.format.frameRate",
								"15"))), QualityKey, 1.0f, KeyFrameIntervalKey,
				(int) (15 * 60)), new Format(MediaTypeKey, MediaType.VIDEO,
				EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null);
		this.name = name;
		this.config = config;
		this.setMaxFileSize(Long.parseLong(config.getValue("maxFileSize",MAX_SIZE_DEFAULT_KB)));
		this.setMaxRecordingTime(Long.parseLong(config.getValue("maxRecordingTime",MAX_DURATION_DEFAULT_MS)));
	}

	public SpecializedScreenRecorder(GraphicsConfiguration cfg,
			Rectangle captureArea, Format fileFormat, Format screenFormat,
			Format mouseFormat, Format audioFormat, File movieFolder,
			String name) throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat,
				audioFormat, movieFolder);
		this.name = name;
	}

	@Override
	protected File createMovieFile(Format fileFormat) throws IOException {
		String platformPath = config.getPathValue("video.directory");
		if (StringUtils.isNotEmpty(platformPath)) {
			movieFolder = new File(platformPath);
		}
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if (!movieFolder.isDirectory()) {
			throw new IOException("\"" + movieFolder + "\" is not a directory.");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH.mm.ss");
		return new File(movieFolder, name + "-" + dateFormat.format(new Date())
				+ "." + Registry.getInstance().getExtension(fileFormat));
	}
}
