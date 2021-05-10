package com.mantoo.mtic.common.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.regex.Pattern;

public class ByteUtil {

	public static final Pattern HEXPATTERN = Pattern.compile("(?<=[0-9A-F])(?=([0-9A-F]{2})+($|\r\n|\n))");

	private static final char[] HEXMAP = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
			'D', 'E', 'F' };

	private final static byte[] crc8_tab = { (byte) 0, (byte) 94, (byte) 188, (byte) 226, (byte) 97, (byte) 63,
			(byte) 221, (byte) 131, (byte) 194, (byte) 156, (byte) 126, (byte) 32, (byte) 163, (byte) 253, (byte) 31,
			(byte) 65, (byte) 157, (byte) 195, (byte) 33, (byte) 127, (byte) 252, (byte) 162, (byte) 64, (byte) 30,
			(byte) 95, (byte) 1, (byte) 227, (byte) 189, (byte) 62, (byte) 96, (byte) 130, (byte) 220, (byte) 35,
			(byte) 125, (byte) 159, (byte) 193, (byte) 66, (byte) 28, (byte) 254, (byte) 160, (byte) 225, (byte) 191,
			(byte) 93, (byte) 3, (byte) 128, (byte) 222, (byte) 60, (byte) 98, (byte) 190, (byte) 224, (byte) 2,
			(byte) 92, (byte) 223, (byte) 129, (byte) 99, (byte) 61, (byte) 124, (byte) 34, (byte) 192, (byte) 158,
			(byte) 29, (byte) 67, (byte) 161, (byte) 255, (byte) 70, (byte) 24, (byte) 250, (byte) 164, (byte) 39,
			(byte) 121, (byte) 155, (byte) 197, (byte) 132, (byte) 218, (byte) 56, (byte) 102, (byte) 229, (byte) 187,
			(byte) 89, (byte) 7, (byte) 219, (byte) 133, (byte) 103, (byte) 57, (byte) 186, (byte) 228, (byte) 6,
			(byte) 88, (byte) 25, (byte) 71, (byte) 165, (byte) 251, (byte) 120, (byte) 38, (byte) 196, (byte) 154,
			(byte) 101, (byte) 59, (byte) 217, (byte) 135, (byte) 4, (byte) 90, (byte) 184, (byte) 230, (byte) 167,
			(byte) 249, (byte) 27, (byte) 69, (byte) 198, (byte) 152, (byte) 122, (byte) 36, (byte) 248, (byte) 166,
			(byte) 68, (byte) 26, (byte) 153, (byte) 199, (byte) 37, (byte) 123, (byte) 58, (byte) 100, (byte) 134,
			(byte) 216, (byte) 91, (byte) 5, (byte) 231, (byte) 185, (byte) 140, (byte) 210, (byte) 48, (byte) 110,
			(byte) 237, (byte) 179, (byte) 81, (byte) 15, (byte) 78, (byte) 16, (byte) 242, (byte) 172, (byte) 47,
			(byte) 113, (byte) 147, (byte) 205, (byte) 17, (byte) 79, (byte) 173, (byte) 243, (byte) 112, (byte) 46,
			(byte) 204, (byte) 146, (byte) 211, (byte) 141, (byte) 111, (byte) 49, (byte) 178, (byte) 236, (byte) 14,
			(byte) 80, (byte) 175, (byte) 241, (byte) 19, (byte) 77, (byte) 206, (byte) 144, (byte) 114, (byte) 44,
			(byte) 109, (byte) 51, (byte) 209, (byte) 143, (byte) 12, (byte) 82, (byte) 176, (byte) 238, (byte) 50,
			(byte) 108, (byte) 142, (byte) 208, (byte) 83, (byte) 13, (byte) 239, (byte) 177, (byte) 240, (byte) 174,
			(byte) 76, (byte) 18, (byte) 145, (byte) 207, (byte) 45, (byte) 115, (byte) 202, (byte) 148, (byte) 118,
			(byte) 40, (byte) 171, (byte) 245, (byte) 23, (byte) 73, (byte) 8, (byte) 86, (byte) 180, (byte) 234,
			(byte) 105, (byte) 55, (byte) 213, (byte) 139, (byte) 87, (byte) 9, (byte) 235, (byte) 181, (byte) 54,
			(byte) 104, (byte) 138, (byte) 212, (byte) 149, (byte) 203, (byte) 41, (byte) 119, (byte) 244, (byte) 170,
			(byte) 72, (byte) 22, (byte) 233, (byte) 183, (byte) 85, (byte) 11, (byte) 136, (byte) 214, (byte) 52,
			(byte) 106, (byte) 43, (byte) 117, (byte) 151, (byte) 201, (byte) 74, (byte) 20, (byte) 246, (byte) 168,
			(byte) 116, (byte) 42, (byte) 200, (byte) 150, (byte) 21, (byte) 75, (byte) 169, (byte) 247, (byte) 182,
			(byte) 232, (byte) 10, (byte) 84, (byte) 215, (byte) 137, (byte) 107, 53 };

	/**
	 * 计算数组的CRC8校验值
	 * 
	 * @param data
	 *            需要计算的数组
	 * @return CRC8校验值
	 */
	public static byte calcCrc8(byte[] data) {
		return calcCrc8(data, 0, data.length, (byte) 0);
	}

	/**
	 * 计算CRC8校验值
	 * 
	 * @param data
	 *            数据
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @return 校验值
	 */
	public static byte calcCrc8(byte[] data, int offset, int len) {
		return calcCrc8(data, offset, len, (byte) 0);
	}

	/**
	 * 计算CRC8校验值
	 * 
	 * @param data
	 *            数据
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @param preval
	 *            之前的校验值
	 * @return 校验值
	 */
	public static byte calcCrc8(byte[] data, int offset, int len, byte preval) {
		byte ret = preval;
		for (int i = offset; i < (offset + len); i++) {
			ret = crc8_tab[(0x00ff & (ret ^ data[i]))];
		}
		return ret;
	}

	public static int CRC16(byte[] msg, int offset, int length) {
		int crc = 0xFFFF;
		int polynomial = 0x0000a001;
		int i, j;
		for (i = offset; i < length; i++) {
			crc ^= ((msg[i] & 0xFFFF) & 0xff);
			for (j = 0; j < 8; j++) {
				if ((crc & 0x00000001) != 0) {
					crc >>= 1;
					crc ^= polynomial;
				} else {
					crc >>= 1;
				}
			}
		}
		return crc;
	}

	public static long byte2Num64(byte[] b) {
		return byte2Num64(b, true);
	}

	public static long byte2Num64(byte[] b, boolean isBigEndian) {
		if (b == null)
			throw new ErrBytesException();
		if (b.length <= 8 && b.length >= 0) {
			return byte2Long(b, isBigEndian);
		} else
			throw new ErrBytesException();
	}

	public static int byte2Num32(byte[] b) {
		return byte2Num32(b, true);
	}

	public static int byte2Num32(byte[] b, boolean isBigEndian) {
		if (b == null)
			throw new ErrBytesException();
		if (b.length <= 4 && b.length >= 0) {
			return byte2Int(b, isBigEndian);
		} else
			throw new ErrBytesException();
	}

	public static byte[] long2Byte(long l) {
		return long2Byte(l, true);
	}

	public static byte[] long2Byte(long l, boolean isBigEndian) {
		if (isBigEndian)
			return new byte[] { (byte) (l >>> 56), (byte) ((l << 8) >>> 56), (byte) ((l << 16) >>> 56),
					(byte) ((l << 24) >>> 56), (byte) ((l << 32) >>> 56), (byte) ((l << 40) >>> 56),
					(byte) ((l << 48) >>> 56), (byte) ((l << 56) >>> 56) };
		else
			return new byte[] { (byte) ((l << 56) >>> 56), (byte) ((l << 48) >>> 56), (byte) ((l << 40) >>> 56),
					(byte) ((l << 32) >>> 56), (byte) ((l << 24) >>> 56), (byte) ((l << 16) >>> 56),
					(byte) ((l << 8) >>> 56), (byte) (l >>> 56) };

	}

	public static long byte2Long(byte[] b) {
		return byte2Long(b, true);
	}

	public static long byte2Long(byte[] b, boolean isBigEndian) {
		long res = 0L;
		if (b == null)
			throw new ErrBytesException();
		if (b.length > 8)
			throw new ErrBytesException();
		if (isBigEndian) {
			for (int i = 0; i < b.length; i++) {
				res = res + ((long) ((b[i] + 256) % 256) << (b.length - 1 - i) * 8);
			}
		} else {
			for (int i = 0; i < b.length; i++) {
				res = res + ((long) ((b[i] + 256) % 256) << i * 8);
			}
		}

		return res;
	}

	public static byte[] int2Byte(int l) {
		return int2Byte(l, true);
	}

	public static byte[] int2Byte(int l, boolean isBigEndian) {
		if (isBigEndian)
			return new byte[] { (byte) (l >>> 24), (byte) ((l << 8) >>> 24), (byte) ((l << 16) >>> 24),
					(byte) ((l << 24) >>> 24) };
		else
			return new byte[] { (byte) ((l << 24) >>> 24), (byte) ((l << 16) >>> 24), (byte) ((l << 8) >>> 24),
					(byte) (l >>> 24) };

	}

	public static int byte2Int(byte[] b) {
		return byte2Int(b, true);
	}

	public static int byte2Int(byte[] b, boolean isBigEndian) {
		int res = 0;
		if (b == null)
			throw new ErrBytesException();
		if (b.length > 4)
			throw new ErrBytesException();
		if (isBigEndian)
			for (int i = 0; i < b.length; i++) {
				res = res + ((int) ((b[i] + 256) % 256) << (b.length - i - 1) * 8);
			}
		else {

			for (int i = 0; i < b.length; i++) {
				res = res + ((int) ((b[i] + 256) % 256) << i * 8);
			}
		}

		return res;
	}

	public static byte[] short2Byte(short l) {
		return short2Byte(l, true);
	}

	public static byte[] short2Byte(short l, boolean isBigEndian) {
		if (isBigEndian)
			return new byte[] { (byte) (l >>> 8), (byte) ((l << 8) >>> 8) };
		else
			return new byte[] { (byte) ((l << 8) >>> 8), (byte) (l >>> 8) };

	}

	public static short byte2Short(byte[] b) {
		return byte2Short(b, true);
	}

	public static short byte2Short(byte[] b, boolean isBigEndian) {
		short res = 0;

		if (b == null)
			throw new ErrBytesException();
		if (b.length > 2)
			throw new ErrBytesException();
		if (isBigEndian)
			for (int i = 0; i < b.length; i++) {
				res = (short) (res + ((int) ((b[i] + 256) % 256) << (b.length - i - 1) * 8));
			}
		else
			for (int i = 0; i < b.length; i++) {
				res = (short) (res + ((int) ((b[i] + 256) % 256) << i * 8));
			}

		return res;
	}

	public static String toNumString(byte[] b) {
		if (b == null)
			return "";
		else {
			StringBuilder res = new StringBuilder();
			res.append("[");
			for (int e : b) {
				e = (e + 256) % 256;
				res.append(e + ",");
			}
			int index = res.lastIndexOf(",");
			if (index > 0)
				res.deleteCharAt(index);
			res.append("]");
			return res.toString();
		}
	}

	public static String toHexString(byte[] b) {
		if (b == null)
			return "";
		else {
			StringBuilder res = new StringBuilder();
			for (int e : b) {
				e = e & 0xFF;
				res.append("" + HEXMAP[e >> 4] + HEXMAP[e & 0x0F]);
			}
			return res.toString();
		}
	}

	public static String toSpaceHexString(String hexStr) {
		return HEXPATTERN.matcher(hexStr).replaceAll(" ");
	}

	public static String toHexStringByLine(byte[] b) {
		return toHexStringByLine(b, 16);
	}

	public static String toHexStringByLine(byte[] b, int lineLength) {
		if (b == null)
			return "";
		else {
			StringBuilder res = new StringBuilder();

			int i = 0;
			for (int e : b) {
				if (i % lineLength == 0)
					res.append("[");
				e = e & 0xFF;
				res.append("" + HEXMAP[e >> 4] + HEXMAP[e & 0xF] + ",");
				i++;
				if (i % lineLength == 0) {
					int index = res.lastIndexOf(",");
					if (index > 0)
						res.deleteCharAt(index);
					res.append("]");
					if (i < b.length)
						res.append("\n");
				}
			}
			if (res.length() > 0 && res.charAt(res.length() - 1) != ']') {
				int index = res.lastIndexOf(",");
				if (index > 0)
					res.deleteCharAt(index);
				res.append("]");
			}

			return res.toString();
		}
	}

	// 2017-03-01

	public static byte reverseByteContent(byte b) {
		int val = (b + 256) % 256;
		int temp = 0;
		for (int i = 0; i < 8; i++) {
			temp = temp + ((val >>> i << 7) % 256 >> i);
		}
		return (byte) temp;
	}

	// 2017-03-01

	public static byte[] reverseBytesContent(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = reverseByteContent(bytes[i]);
		}
		return bytes;
	}

	// 2017-03-02

	public static byte[] reverseBytes(byte[] bytes) {
		byte temp;
		for (int i = 0; i < bytes.length / 2; i++) {
			temp = bytes[i];
			bytes[i] = bytes[bytes.length - i - 1];
			bytes[bytes.length - i - 1] = temp;
		}
		return bytes;
	}

	// 2017-03-01
	public static byte getBits(byte b, int offset, int length) {
		if (offset < 0 || length < 0 || offset > 8 || offset + length > 8) {
			System.out.println("offset:" + offset + "," + "length:" + length);
		}
		int res = (b + 256) % 256;
		res = (res << offset) % 256 >>> (8 - length);
		return (byte) res;
	}

	public static String toBinaryString(byte b) {
		StringBuilder sbu = new StringBuilder();
		int val = b & 0xFF;
		for (int i = 0; i < 8; i++) {
			sbu.append((val >>> (7 - i)) & 1);
		}

		return sbu.toString();
	}

	// 2017-03-01

	public static String toBinaryString(byte[] b, int lineLength) {
		if (b == null) {
			return "";
		} else {
			StringBuilder res = new StringBuilder();

			int i = 0;
			for (byte e : b) {
				if (i % lineLength == 0)
					res.append("[");
				res.append("" + toBinaryString(e) + ",");
				i++;
				if (i % lineLength == 0) {
					int index = res.lastIndexOf(",");
					if (index > 0)
						res.deleteCharAt(index);
					res.append("]");
					if (i < b.length)
						res.append("\n");
				}
			}
			if (res.length() > 0 && res.charAt(res.length() - 1) != ']') {
				int index = res.lastIndexOf(",");
				if (index > 0)
					res.deleteCharAt(index);
				res.append("]");
			}

			return res.toString();
		}
	}

	// 2017-03-01

	public static String toBinaryString(byte[] b) {
		return toBinaryString(b, 16);
	}

	// 2017-3-6
	public static class BitUnit {
		public byte[] bits;
		public int length;
		public boolean isAlignRight;

		public BitUnit(byte[] bits, int length, boolean isAlignRight) {
			this.bits = bits;
			this.length = length;
			this.isAlignRight = isAlignRight;
		}

		public BitUnit alignLeft() {
			return bitAlignLeft(this);
		}

		public BitUnit alignRight() {
			return bitAlignRight(this);
		}

		public void trim() {
			if (isAlignRight) {
				bitMoveLeft(bits, bits.length * 8 - length);
				bitMoveRight(bits, bits.length * 8 - length);
			} else {
				bitMoveRight(bits, bits.length * 8 - length);
				bitMoveLeft(bits, bits.length * 8 - length);
			}
		}

		public boolean match(BitUnit bits) {
			if (bits.length != length)
				return false;
			bits.trim();
			trim();
			int count = (length >> 3) + ((length & 7) == 0 ? 0 : 1);

			if (bits.isAlignRight || isAlignRight) {
				alignLeft();
				bits.alignLeft();
			}

			for (int i = 0; i < count; i++) {
				if (this.bits[i] != bits.bits[i])
					return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			return length;
		}

		@Override
		public String toString() {
			return "BitUnit [bits=" + toBinaryString(bits) + ", length=" + length + ", isAlignRight=" + isAlignRight
					+ "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof BitUnit)
				return match((BitUnit) obj);
			return false;
		}

	}

	// 2017-3-6
	/**
	 * 将bit位整体左移length位<br>
	 * 
	 * @param bits
	 * @param length
	 * @return
	 */
	public static byte[] bitMoveLeft(byte[] bits, int length) {
		int dataFreeByteLength = length >> 3;
		int dataByteLength = bits.length - dataFreeByteLength;
		System.arraycopy(bits, dataFreeByteLength, bits, 0, dataByteLength);
		for (int i = 0; i < dataFreeByteLength; i++) {
			bits[bits.length - i - 1] = 0;
		}
		int offset = length & 7;
		for (int i = 0; i < dataByteLength; i++) {
			int val = (bits[i] & 0xFF) << offset;
			bits[i] = (byte) (val);
			int count = 0;
			while (val > 255 && i - count > 0) {
				val = bits[i - 1 - count] + (val >>> 8);
				bits[i - 1 - count] = (byte) (val);
				count++;
			}
		}
		return bits;
	}

	// 2017-3-6
	/**
	 * 将bit位整体右移length位<br>
	 * 
	 * @param bits
	 * @param length
	 * @return
	 */
	public static byte[] bitMoveRight(byte[] bits, int length) {
		int dataFreeByteLength = length >> 3;
		int dataByteLength = bits.length - dataFreeByteLength;
		System.arraycopy(bits, 0, bits, dataFreeByteLength, dataByteLength);
		for (int i = 0; i < dataFreeByteLength; i++) {
			bits[i] = 0;
		}
		int offset = length & 7;
		for (int i = 0; i < dataByteLength; i++) {
			int oldval = bits[bits.length - i - 1] & 0xFF;
			bits[bits.length - i - 1] = (byte) ((bits[bits.length - i - 1] & 0xFF) >>> offset);

			if (i > 0 && oldval - ((oldval & 0xFF) >>> offset << offset) > 0) {
				bits[bits.length - i] = (byte) (bits[bits.length - i] + (byte) (oldval << (8 - offset)));
			}
		}
		return bits;
	}

	// 2017-3-6
	/**
	 * 将bits左对齐(BitUnit对象原本右对齐)<br>
	 * 
	 * @param bits<br>
	 * @return 返回传入的BitUnit对象<br>
	 */
	public static BitUnit bitAlignLeft(BitUnit bits) {
		if (bits.isAlignRight)
			bitMoveLeft(bits.bits, bits.bits.length * 8 - bits.length);
		bits.isAlignRight = false;
		return bits;
	}

	// 2017-3-6
	/**
	 * 将bits右对齐(BitUnit对象原本左对齐)<br>
	 * 
	 * @param bits<br>
	 * @return 返回传入的BitUnit对象<br>
	 */
	public static BitUnit bitAlignRight(BitUnit bits) {
		if (!bits.isAlignRight)
			bitMoveRight(bits.bits, bits.bits.length * 8 - bits.length);
		bits.isAlignRight = true;
		return bits;
	}

	/**
	 * 得出两数组指定范围连续相同的长度
	 * 
	 * @param src0
	 *            数组1
	 * @param offset0
	 *            数组1偏移量
	 * @param length0
	 *            数组1长度
	 * @param src1
	 *            数组2
	 * @param offset1
	 *            数组2偏移量
	 * @param length1
	 *            数组2长度
	 * @return 相同数量
	 */
	public static int equalLength(byte[] src0, int offset0, int length0, byte[] src1, int offset1, int length1) {
		if (length0 <= 0) {
			length0 = src0.length - offset0;
		}

		if (length1 <= 0) {
			length1 = src1.length - offset1;
		}

		if (verify(src0, offset0, length0) && verify(src1, offset1, length1)) {
			int max = Math.min(length0, length1);
			int i = 0;
			for (i = 0; i < max; i++) {
				if (src0[offset0 + i] != src1[offset1 + i]) {
					break;
				}
			}
			return i;
		} else {
			return -1;
		}

	}

	/**
	 * 比较数组长度是否相等
	 * 
	 * @param byte1
	 * @param byte2
	 * @return
	 */
	public static boolean equal(byte[] byte1, byte[] byte2) {
		if (byte1.length != byte2.length) {
			return false;
		} else {
			return byte1.length == equalLength(byte1, 0, byte1.length, byte2, 0, byte1.length);
		}
	}

	public static int byteSearch(byte[] src, int offset, int length, byte[] search) {
		if (length <= 0)
			length = src.length - offset;
		if (!verify(src, offset, length))
			return -1;
		if (search.length == 0 || search.length > length)
			return -1;

		int limit = length + offset;
		for (int i = offset; i < limit; i++) {
			int res = equalLength(src, i, search.length, search, 0, search.length);
			if (res < 0)
				return -1;
			if (res == search.length) {
				return i;
			}
		}
		return -1;

	}

	public static boolean verify(byte[] src, int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > src.length)
			return false;
		else
			return true;
	}

	public static class ErrBytesException extends RuntimeException {

		public ErrBytesException() {
			super();
			//
		}

		public ErrBytesException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// Auto-generated constructor stub
		}

		public ErrBytesException(String message, Throwable cause) {
			super(message, cause);

		}

		public ErrBytesException(String message) {
			super(message);
			// Auto-generated constructor stub
		}

		public ErrBytesException(Throwable cause) {
			super(cause);
			// Auto-generated constructor stub
		}

	}

	private static int charToByte(char c) {
		int res = "0123456789ABCDEF".indexOf(c);
		if (res < 0) {
			throw new RuntimeException("invalid char:" + c);
		}
		return res;
	}

	public static byte[] hexString2Bytes(String hex) {

		if ((hex == null) || (hex.equals(""))) {
			return null;
		} else if (hex.length() % 2 != 0) {
			return null;
		} else {
			hex = hex.toUpperCase();
			int len = hex.length() / 2;
			byte[] b = new byte[len];
			char[] hc = hex.toCharArray();
			for (int i = 0; i < len; i++) {
				int p = 2 * i;
				b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
			}
			return b;
		}

	}

	public static float byte2float(final byte[] b, final int index) throws Exception {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
		float f = dis.readFloat();
		dis.close();
		return f;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(calcCrc8(new byte[] { ((byte) 97) }));
		System.out.println("a".getBytes("GBK").length);
	}

	/**
	 * 十进制数转十六进制字符串(xjy)
	 */
	public static String intToHexString(int a){
		String s=Integer.toHexString(a).toUpperCase();
		if(s.length()%2!=0){
			s="0"+s;
		}
		return s;
	}

	//从字符串转换为十六进制字符串(xjy)
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch).toUpperCase();
			str = str + s4 +" ";
		}
		return str;
	}

	/**
	 * 十进制数转十六进制字符串(xjy)
	 */
	public static String longToHexString(long a){
		String s=Long.toHexString(a).toUpperCase();
		if(s.length()%2!=0){
			s="0"+s;
		}
		return s;
	}

	/**
	 * 十六进制字符串转数组
	 */
	public static String[] hexStringTOArr(String s){
		int length=s.length();
		String []a=new String[length/2];
		for(int i=0;i<length;i=i+2){
			a[i/2]=s.substring(i, i+2);
		}
		return a;
	}

	/**
	 * 十六进制转字符串
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
}
