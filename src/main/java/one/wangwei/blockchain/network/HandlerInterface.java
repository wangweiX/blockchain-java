/*
	File: HandlerInterface.java
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
	Jan 25 2007	Nadeem Abdul Hamid	Created
 */
package one.wangwei.blockchain.network;

/**
 * The interface for handling a new, incoming connection from a peer.
 * When a peer connects and sends a message to this node, the resulting
 * connection is encapsulated in a PeerConnection object and passed,
 * along with the message, to an object of a class that implements this
 * interface. This dispatch is carried out by the Node object with
 * which the handler has been registered. Messages are registered and
 * dispatched according to their type.
 *
 * @author Nadeem Abdul Hamid
 **/
public interface HandlerInterface {
    /**
     * Invoked when a peer connects and sends a message to this node.
     *
     * @param peerconn
     * @param msg
     */
    public void handleMessage(PeerConnection peerconn, PeerMessage msg);
}
