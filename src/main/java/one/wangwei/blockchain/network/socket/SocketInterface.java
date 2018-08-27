package one.wangwei.blockchain.network.socket;

import java.io.IOException;
import java.io.InputStream;

/**
 * Socket interface
 *
 * @author wangwei
 * @date 2018/08/24
 */
public interface SocketInterface {

    /**
     * 向socket连接中写入数据
     *
     * @param b
     * @throws IOException
     */
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
    /**
     * 从socket连接读取指定长度的byte[]数据，并存入byte[]中
     *
     * @param b
     * @return
     * @throws IOException
     */
    public int read(byte[] b) throws IOException;

    /**
     * 关闭socket连接，释放占用的系统资源
     *
     * @throws IOException
     */
    public void close() throws IOException;
}
