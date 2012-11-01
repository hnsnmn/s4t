package org.chimi.s4t.infra.ffmpeg;

import org.chimi.s4t.domain.job.AudioCodec;
import org.chimi.s4t.domain.job.VideoCodec;

import com.xuggle.xuggler.ICodec;

public class CodecValueConverter {

	public static ICodec.ID fromDomain(VideoCodec videoCodec) {
		if (videoCodec == null) {
			return null;
		}
		switch (videoCodec) {
		case H264:
			return ICodec.ID.CODEC_ID_H264;
		}
		return null;
	}

	public static VideoCodec toDomainVideoCodec(ICodec.ID codecId) {
		if (codecId == null) {
			return null;
		}
		switch (codecId) {
		case CODEC_ID_H264:
			return VideoCodec.H264;
		}
		return null;
	}

	public static ICodec.ID fromDomain(AudioCodec audioCodec) {
		if (audioCodec == null) {
			return null;
		}
		switch (audioCodec) {
		case AAC:
			return ICodec.ID.CODEC_ID_AAC;
		}
		return null;
	}

	public static AudioCodec toDomainAudioCodec(ICodec.ID codecId) {
		if (codecId == null) {
			return null;
		}
		switch (codecId) {
		case CODEC_ID_AAC:
			return AudioCodec.AAC;
		}
		return null;
	}
}
