/*
	File: SimplePingStabilizer.java
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
import one.wangwei.blockchain.network.PeerConnection;
import one.wangwei.blockchain.network.PeerMessage;
import one.wangwei.blockchain.network.StabilizerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple stabilization routine that simply sends "PING" messages to
 * every peer in a node's list of peers, and removes from the list any
 * that fail to accept the connection. Whether peers <i>reply</i> to the
 * actual message or not does not matter to this stabilizer.
 *
 * @author Nadeem Abdul Hamid
 */
public class SimplePingStabilizer implements StabilizerInterface {
    private Node peer;
    private String msgtype;

    public SimplePingStabilizer(Node peer) {
        this(peer, "PING");
    }

    public SimplePingStabilizer(Node peer, String msgtype) {
        this.peer = peer;
        this.msgtype = msgtype;
    }

    @Override
    public void stabilizer() {
        List<String> todelete = new ArrayList<String>();
        for (String pid : peer.getPeerKeys()) {
            boolean isconn = false;
            PeerConnection peerconn = null;
            try {
                peerconn = new PeerConnection(peer.getPeer(pid));
                peerconn.sendData(new PeerMessage(msgtype, ""));
                isconn = true;
            } catch (IOException e) {
                todelete.add(pid);
            }
            if (isconn)
                peerconn.close();
        }

        for (String pid : todelete) {
            peer.removePeer(pid);
        }
    }
}

