package com.sugarcrm.candybean.utilities;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import org.monte.screenrecorder.ScreenRecorder;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;
import org.monte.media.Registry;
import com.sugarcrm.candybean.configuration.Configuration;

public class SpecializedScreenRecorder extends ScreenRecorder {

	private String name;

	private Configuration config;
	
	private Logger logger;

	public SpecializedScreenRecorder(GraphicsConfiguration cfg, String name,
			Configuration config, Logger logger) throws IOException, AWTException {
		super(cfg, new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey,
				config.getValue("video.format", MIME_AVI)), new Format(
				MediaTypeKey, MediaType.VIDEO, EncodingKey,
				config.getValue("video.encoding",
						ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE),
				CompressorNameKey, config.getValue("video.encoding",
						ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE), DepthKey,
				(int) 24, FrameRateKey, Rational.valueOf(Integer
						.parseInt(config.getValue("video.format.frameRate",
								"15"))), QualityKey, 1.0f, KeyFrameIntervalKey,
				(int) (15 * 60)), new Format(MediaTypeKey, MediaType.VIDEO,
				EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null);
		this.name = name;
		this.config = config;
		this.logger = logger;
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
		File file = new File(movieFolder, name + "-" + dateFormat.format(new Date())
				+ "." + Registry.getInstance().getExtension(fileFormat));
		logger.info("Recording to: " + file.getAbsolutePath());
		return file;
	}
}
