/**
 * Sahi - Web Automation and Test Tool
 *
 * Copyright  2006  V Narayan Raman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.sahi.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class SocketPool {
    private final List unused = new LinkedList();
    private final int START_PORT = 13300;
    private int lastPort;

    public SocketPool(final int size) {
        for (int i = 0; i < size; i++) {
            returnToPool(START_PORT + i);
        }
        lastPort = START_PORT + size;
    }

    private Socket createSocket(final int port) throws IOException {
        Socket socket = new Socket();
        try {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(port));
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return socket;
    }

    Socket get() throws IOException {
        Socket socket;
        int port;
        synchronized (unused) {
            while (unused.isEmpty()) {
                try {
                    System.out.println("Waiting for socket");
                    unused.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted!");
                }
            }
            port = ((Integer) unused.remove(0)).intValue();
        }
        socket = createSocket(port);
//		System.out.println("Get:     " + port);
        return socket;
    }

    public Socket get(final String host, final int port) throws IOException {
        Socket socket = get();
        try {
//        	System.out.println("Trying: " + socket.getLocalPort());
            socket.connect(new InetSocketAddress(host, port));
        } catch (Exception e) {
//        	e.printStackTrace();
            lastPort++;
            System.out.println("### Creating New Socket : "+lastPort);
            socket = createSocket(lastPort);
            socket.connect(new InetSocketAddress(host, port));
        }
        return socket;
    }

    public void release(final Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnToPool(socket.getLocalPort());
    }

    void returnToPool(final int port) {
//		System.out.println("returned to Pool " + port);
        synchronized (unused) {
            unused.add(unused.size(), new Integer(port));
            unused.notifyAll();
        }
    }

}
