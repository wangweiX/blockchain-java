/*
	File: PeerConnection.java
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
package one.wangwei.blockchain.network;


import one.wangwei.blockchain.network.socket.AbstractSocketFactory;
import one.wangwei.blockchain.network.socket.SocketInterface;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Encapsulates a socket connection to a peer, providing simple, reliable
 * send and receive functionality. All data sent to a peer through this
 * class must be formatted as a PeerMessage object.
 *
 * @author Nadeem Abdul Hamid
 */
public class PeerConnection {

    private PeerInfo pd;
    private SocketInterface s;

    /**
     * Opens a new connection to the specified peer.
     *
     * @param info the peer node to connect to
     * @throws IOException          if an I/O error occurs
     * @throws UnknownHostException
     */
    public PeerConnection(PeerInfo info)
            throws IOException, UnknownHostException {
        pd = info;
        s = AbstractSocketFactory.getSocketFactory().makeSocket(pd.getHost(),
                pd.getPort());
    }


    /**
     * Constructs a connection for which a socket has already been
     * opened.
     *
     * @param info
     * @param socket
     */
    public PeerConnection(PeerInfo info, SocketInterface socket) {
        pd = info;
        s = socket;
    }


    /**
     * Sends a PeerMessage to the connected peer.
     *
     * @param msg the message object to send
     */
    public void sendData(PeerMessage msg) {
        try {
            s.write(msg.toBytes());
        } catch (IOException e) {
            LoggerUtil.getLogger().warning("Error sending message: " + e);
        }
    }


    /**
     * Receives a PeerMessage from the connected peer.
     *
     * @return the message object received, or null if error
     */
    public PeerMessage recvData() {
        try {
            PeerMessage msg = new PeerMessage(s);
            return msg;
        } catch (IOException e) {
            // it is normal for EOF to occur if there is no more replies coming
            // back from this connection.
            if (!e.getMessage().equals("EOF in PeerMessage constructor: type"))
                LoggerUtil.getLogger().warning("Error receiving message: " + e);
            else
                LoggerUtil.getLogger().finest("Error receiving message: " + e);
            return null;
        }
    }


    /**
     * Closes the peer connection.
     */
    public void close() {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                LoggerUtil.getLogger().warning("Error closing: " + e);
            }
            s = null;
        }
    }


    public PeerInfo getPeerInfo() {
        return pd;
    }


    public String toString() {
        return "PeerConnection[" + pd + "]";
    }

}
