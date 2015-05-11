package com.github.jrgen.test.domain;

import java.util.Arrays;

public class PrimitiveTypesBean {

	private byte aByte;
	private short aShort;
	private int anInt;
	private long aLong;
	private float aFloat;
	private double aDouble;
	private boolean aBoolean;
	
	private byte[] bytes;
	private short[] shorts;
	private int[] ints;
	private long[] longs;
	private float[] floats;
	private double[] doubles;
	private boolean[] booleans;
	
	public PrimitiveTypesBean() {
		super();
	}

	public byte getaByte() {
		return aByte;
	}

	public void setaByte(byte aByte) {
		this.aByte = aByte;
	}

	public short getaShort() {
		return aShort;
	}

	public void setaShort(short aShort) {
		this.aShort = aShort;
	}

	public int getAnInt() {
		return anInt;
	}

	public void setAnInt(int anInt) {
		this.anInt = anInt;
	}

	public long getaLong() {
		return aLong;
	}

	public void setaLong(long aLong) {
		this.aLong = aLong;
	}

	public float getaFloat() {
		return aFloat;
	}

	public void setaFloat(float aFloat) {
		this.aFloat = aFloat;
	}

	public double getaDouble() {
		return aDouble;
	}

	public void setaDouble(double aDouble) {
		this.aDouble = aDouble;
	}

	public boolean isaBoolean() {
		return aBoolean;
	}

	public void setaBoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public short[] getShorts() {
		return shorts;
	}

	public void setShorts(short[] shorts) {
		this.shorts = shorts;
	}

	public int[] getInts() {
		return ints;
	}

	public void setInts(int[] ints) {
		this.ints = ints;
	}

	public long[] getLongs() {
		return longs;
	}

	public void setLongs(long[] longs) {
		this.longs = longs;
	}

	public float[] getFloats() {
		return floats;
	}

	public void setFloats(float[] floats) {
		this.floats = floats;
	}

	public double[] getDoubles() {
		return doubles;
	}

	public void setDoubles(double[] doubles) {
		this.doubles = doubles;
	}

	public boolean[] getBooleans() {
		return booleans;
	}

	public void setBooleans(boolean[] booleans) {
		this.booleans = booleans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aBoolean ? 1231 : 1237);
		result = prime * result + aByte;
		long temp;
		temp = Double.doubleToLongBits(aDouble);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(aFloat);
		result = prime * result + (int) (aLong ^ (aLong >>> 32));
		result = prime * result + aShort;
		result = prime * result + anInt;
		result = prime * result + Arrays.hashCode(booleans);
		result = prime * result + Arrays.hashCode(bytes);
		result = prime * result + Arrays.hashCode(doubles);
		result = prime * result + Arrays.hashCode(floats);
		result = prime * result + Arrays.hashCode(ints);
		result = prime * result + Arrays.hashCode(longs);
		result = prime * result + Arrays.hashCode(shorts);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimitiveTypesBean other = (PrimitiveTypesBean) obj;
		if (aBoolean != other.aBoolean)
			return false;
		if (aByte != other.aByte)
			return false;
		if (Double.doubleToLongBits(aDouble) != Double
				.doubleToLongBits(other.aDouble))
			return false;
		if (Float.floatToIntBits(aFloat) != Float.floatToIntBits(other.aFloat))
			return false;
		if (aLong != other.aLong)
			return false;
		if (aShort != other.aShort)
			return false;
		if (anInt != other.anInt)
			return false;
		if (!Arrays.equals(booleans, other.booleans))
			return false;
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		if (!Arrays.equals(doubles, other.doubles))
			return false;
		if (!Arrays.equals(floats, other.floats))
			return false;
		if (!Arrays.equals(ints, other.ints))
			return false;
		if (!Arrays.equals(longs, other.longs))
			return false;
		if (!Arrays.equals(shorts, other.shorts))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrimitiveTypesBean [aByte=" + aByte + ", aShort=" + aShort
				+ ", anInt=" + anInt + ", aLong=" + aLong + ", aFloat="
				+ aFloat + ", aDouble=" + aDouble + ", aBoolean=" + aBoolean
				+ ", bytes=" + Arrays.toString(bytes) + ", shorts="
				+ Arrays.toString(shorts) + ", ints=" + Arrays.toString(ints)
				+ ", longs=" + Arrays.toString(longs) + ", floats="
				+ Arrays.toString(floats) + ", doubles="
				+ Arrays.toString(doubles) + ", booleans="
				+ Arrays.toString(booleans) + "]";
	}
	
}
