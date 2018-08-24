/*
	File: PeerMessage.java
	Copyright 2007 by Nadeem Abdul Hamid

	Permission to use, copy, modify, and distribute this software and its
	documentation for any purpose and without fee is hereby granted, provided
	that the above copyright notice appear in all copies and that both the
	copyright notice and this permission notice and warranty disclaimer appear
	in supporting documentation, and that the names of the authors or their
	employers not be used in advertising or publicity pertaining to distri-
	bution of the software without specific, written prior permission.

	The authors and their employers disclaim all warranties with regard to
	this software, including all implied warranties of merchantability and
	fitness. In no event shall the authors or their employers be liable for
	any special, indirect or consequential damages or any damages whatsoever
	resulting from loss of use, data or profits, whether in an action of
	contract, negligence or other tortious action, arising out of or in
	connection with the use or performance of this software, even if
	advised of the possibility of such damage.

	Date		Author				Changes
	Jan 31 2007	Nadeem Abdul Hamid	Created
 */

package one.wangwei.blockchain.network;


import one.wangwei.blockchain.network.socket.SocketInterface;

import java.io.IOException;


/**
 * Represents a message, composed of type and data fields, in the PeerBase
 * system. Also provides functionality for converting messages to/from
 * byte arrays in a portable manner. The type of every message is a 4-byte
 * value (i.e. 4-character string).
 *
 * @author Nadeem Abdul Hamid
 */
public class PeerMessage {

    private byte[] type;
    private byte[] data;

    /**
     * Constructs a new PeerMessage object.
     *
     * @param type the message type (4 bytes)
     * @param data the message data
     */
    public PeerMessage(byte[] type, byte[] data) {
        this.type = (byte[]) type.clone();
        this.data = (byte[]) data.clone();
    }


    /**
     * Constructs a new PeerMessage object.
     *
     * @param type the message type (4 characters)
     * @param data the message data
     */
    public PeerMessage(String type, String data) {
        this(type.getBytes(), data.getBytes());
    }


    /**
     * Constructs a new PeerMessage object.
     *
     * @param type the message type (4 characters)
     * @param data the message data
     */
    public PeerMessage(String type, byte[] data) {
        this(type.getBytes(), data);
    }


    /**
     * Constructs a new PeerMessage object by reading data
     * from the given socket connection.
     *
     * @param s a socket connection object
     * @throws IOException if I/O error occurs
     */
    public PeerMessage(SocketInterface s) throws IOException {
        type = new byte[4];
        byte[] thelen = new byte[4]; // for reading length of message data
        if (s.read(type) != 4)
            throw new IOException("EOF in PeerMessage constructor: type");
        if (s.read(thelen) != 4)
            throw new IOException("EOF in PeerMessage constructor: thelen");

        int len = byteArrayToInt(thelen);
        data = new byte[len];

        if (s.read(data) != len)
            throw new IOException("EOF in PeerMessage constructor: " +
                    "Unexpected message data length");
    }


    /**
     * Returns the message type as a String.
     *
     * @return the message type (4-character String)
     */
    public String getMsgType() {
        return new String(type);
    }


    /**
     * Returns the message type.
     *
     * @return the message type (4-byte array)
     */
    public byte[] getMsgTypeBytes() {
        return (byte[]) data.clone();
    }


    /**
     * Returns the message data as a String.
     *
     * @return the message data
     */
    public String getMsgData() {
        return new String(data);
    }


    /**
     * Returns the message data.
     *
     * @return the message data
     */
    public byte[] getMsgDataBytes() {
        return (byte[]) data.clone();
    }


    /**
     * Returns a packed representation of this message as an
     * array of bytes.
     *
     * @return byte array of message data
     */
    public byte[] toBytes() {
        byte[] bytes = new byte[4 + 4 + data.length];
        byte[] lenbytes = intToByteArray(data.length);

        for (int i = 0; i < 4; i++) bytes[i] = type[i];
        for (int i = 0; i < 4; i++) bytes[i + 4] = lenbytes[i];
        for (int i = 0; i < data.length; i++) bytes[i + 8] = data[i];

        return bytes;
    }

    @Override
    public String toString() {
        return "PeerMessage[" + getMsgType() + ":" + getMsgData() + "]";
    }

    /* Taken from http://forum.java.sun.com/thread.jspa?threadID=609364&messageID=3336342 */

    /**
     * Returns a byte array containing the two's-complement representation of the integer.<br>
     * The byte array will be in big-endian byte-order with a fixes length of 4
     * (the least significant byte is in the 4th element).<br>
     * <br>
     * <b>Example:</b><br>
     * <code>intToByteArray(258)</code> will return { 0, 0, 1, 2 },<br>
     * <code>BigInteger.valueOf(258).toByteArray()</code> returns { 1, 2 }.
     *
     * @param integer The integer to be converted.
     * @return The byte array of length 4.
     */
    public static byte[] intToByteArray(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (integer >>> (n * 8));

        return (byteArray);
    }

    public static int byteArrayToInt(byte[] byteArray) {
        int integer = 0;
        for (int n = 0; n < 4; n++) {
            integer = (integer << 8) | (((int) byteArray[n]) & 0xff);
        }

        //if (integer > 1000)
        //    LoggerUtil.getLogger().fine("%x %x %x %x ==> %d\n", byteArray[0], byteArray[1], byteArray[2], byteArray[3], integer);

        return integer;
    }

}
