/*
	File: SocketInterface.java
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
	Jan 29 2007	Nadeem Abdul Hamid	Created
 */


package one.wangwei.blockchain.network.socket;

import java.io.IOException;

/**
 * The socket interface for the PeerBase system. Methods for reading
 * and writing are modelled after the basic InputStream/OutputStream
 * classes of the Java library.
 *
 * @author Nadeem Abdul Hamid
 */
public interface SocketInterface {

    /**
     * Writes b.length bytes from the specified byte array to this
     * socket connection.
     *
     * @param b the data
     * @throws IOException if an I/O error occurs
     **/
    public void write(byte[] b) throws IOException;


    /**
     * Reads the next byte of data from the socket connection. The value
     * byte is returned as an int in the range 0 to 255. If no byte
     * is available because the end of the input has been reached,
     * the value -1 is returned. This method blocks until input data
     * is available, the end of the input is detected, or an exception
     * is thrown.
     *
     * @return the next byte of data, or -1 if the end of the input is
     * reached
     * @throws IOException if an I/O error occurs
     */
    public int read() throws IOException;


    /**
     * Reads some number of bytes from the socket connection and stores them
     * into the buffer array b. The number of bytes actually read is
     * returned as an integer. This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     *
     * @param b the buffer into which the data is read
     * @return the total number of bytes read into the buffer, or -1 if
     * there is no more data because the end of the input has been reached
     * @throws IOException if an I/O error occurs
     * @see InputStream#read()
     */
    public int read(byte[] b) throws IOException;


    /**
     * Closes this connection and releases any system resources
     * associated with the socket.
     *
     * @throws IOException if an I/O error occurs
     **/
    public void close() throws IOException;
}
