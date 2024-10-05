package com.galaxy.trabajofinal.security.UTIL;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import de.taimos.totp.TOTP;

import static com.galaxy.trabajofinal.security.constants.SecurityConstants.TOTP_KEY;

@Component
public class TOTPUtil {

	public String generateCode() {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(TOTP_KEY);
		String hexKey = Hex.encodeHexString(bytes);
		String code = TOTP.getOTP(hexKey);
		return code;
	}

	public Boolean verifyCode(String code) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(TOTP_KEY);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.validate(hexKey, code);
	}
}
