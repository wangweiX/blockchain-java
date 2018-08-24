/*
	File: NormalSocketFactory.java
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


package one.wangwei.blockchain.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NormalSocketFactory extends AbstractSocketFactory {

    @Override
    public SocketInterface makeSocket(String host, int port) throws IOException, UnknownHostException {
        return new NormalSocket(host, port);
    }

    @Override
    public SocketInterface makeSocket(Socket socket) throws IOException {
        return new NormalSocket(socket);
    }

}
