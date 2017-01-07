package com.ushine.log.utils;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DESCryptoUtil {

	/**
	 * 初始密钥为8个1
	 */
	public static String key = "11111111";

	public static String decrypt(String src) {
		return decrypt(src, DESCryptoUtil.key);
	}

	public static String encrypt(String src) {
		return encrypt(src, DESCryptoUtil.key);
	}

	public static String decrypt(String src, String key) {
		if (key == null)
			key = DESCryptoUtil.key;
		SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			byte[] sIndate = cipher.doFinal(transHexStringToBytes(src, "-"));
			return new String(sIndate);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(String src, String key) {
		if (key == null)
			key = DESCryptoUtil.key;
		SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			byte[] sIndate = cipher.doFinal(src.getBytes());
			return transBytesToHexString(sIndate, "-");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String transBytesToHexString(byte[] src, String append) {
		StringBuilder sbIndate = new StringBuilder();
		for (int i = 0; i < src.length; i++) {
			String tmp = Integer.toHexString(src[i]);
			if (tmp.length() == 1) {
				sbIndate.append("0");
				sbIndate.append(tmp);
			} else {
				sbIndate.append(tmp.substring(tmp.length() - 2));
			}
			if (i < src.length - 1)
				sbIndate.append(append);
		}
		return sbIndate.toString();
	}

	public static byte[] transHexStringToBytes(String src, String append) {
		src = src.replaceAll(append, "");
		int len = src.length();
		if (len % 2 == 1)
			return null;
		ByteBuffer buffer = ByteBuffer.allocate(src.getBytes().length);
		for (int i = 0; i < len;) {
			buffer.put((byte) (Integer.valueOf(src.substring(i, i + 2), 16)
					.intValue()));
			i += 2;
		}
		byte[] bb = new byte[buffer.position()];
		buffer.rewind();
		buffer.get(bb);
		return bb;
	}

	public static String dealMd5(String str) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			return DESCryptoUtil.transBytesToHexString(
					md.digest(str.getBytes()), "");
		} catch (NoSuchAlgorithmException e) {
			return str;
		}
		// 202cb962ac59075b964b07152d234b70
	}

	public static void main(String args[]) throws ParseException {
		// System.out.println(encrypt("20121020",null));
		// System.out.println(decrypt("18-0f-e9-f6-6d-d6-b3-35-ad-6a-88-b4-fa-37-83-3d",null));
		// System.out.println(new
		// SimpleDateFormat("yyyyMMdd").parse(decrypt("18-0f-e9-f6-6d-d6-b3-35-ad-6a-88-b4-fa-37-83-3d")));
		// System.out.println(decrypt("18-0f-e9-f6-6d-d6-b3-35-ad-6a-88-b4-fa-37-83-3d"));

		System.out.println(dealMd5("123"));

	}
}
