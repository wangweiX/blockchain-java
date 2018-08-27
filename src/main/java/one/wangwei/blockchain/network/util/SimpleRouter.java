/*
	File: SimpleRouter.java
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

package one.wangwei.blockchain.network.util;

import one.wangwei.blockchain.network.Node;
import one.wangwei.blockchain.network.PeerInfo;
import one.wangwei.blockchain.network.RouterInterface;

/**
 * A simple router that attempts to route messages by simply looking
 * for the destination peer information in the node's list of peers.
 * If the destination node is not an immediate neighbor, this function
 * fails.
 *
 * @author Nadeem Abdul Hamid
 */
public class SimpleRouter implements RouterInterface {
    private Node peer;

    public SimpleRouter(Node peer) {
        this.peer = peer;
    }

    @Override
    public PeerInfo route(String peerid) {
        for (String key : peer.getPeerKeys())
            if (peer.getPeer(key).getId().equals(peerid))
                return peer.getPeer(peerid);
        return null;
    }
}
