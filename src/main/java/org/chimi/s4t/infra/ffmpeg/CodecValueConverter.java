package org.chimi.s4t.infra.ffmpeg;

import org.chimi.s4t.domain.job.AudioCodec;
import org.chimi.s4t.domain.job.VideoCodec;

import com.xuggle.xuggler.ICodec;

public class CodecValueConverter {

	public static ICodec.ID fromDomain(VideoCodec codecName) {
		if (codecName == null) {
			return null;
		}
		switch (codecName) {
		case H264:
			return ICodec.ID.CODEC_ID_H264;
		default:
			break;
		}
		return null;
	}

	public static ICodec.ID fromDomain(AudioCodec codecName) {
		if (codecName == null) {
			return null;
		}
		switch (codecName) {
		case AAC:
			return ICodec.ID.CODEC_ID_AAC;
		default:
			break;
		}
		return null;
	}
}
