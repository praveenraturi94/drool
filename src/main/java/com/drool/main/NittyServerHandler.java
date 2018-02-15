package com.drool.main;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class NittyServerHandler extends ChannelInboundHandlerAdapter {
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private final static byte[] header = ISOUtil.hex2byte("0160102003E0000000000000000000000000000000000");

	// private MemQueue mem;
	// private Thread t;
	//
	// public DiscardServerHandler() {
	// super();
	// System.out.println("called once");
	// mem = new MemQueue(new LinkedBlockingQueue<byte[]>());
	// t = new Thread(mem);
	// t.start();
	// }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		//
		try {

			System.out.println("<read start>");
			System.out.print(in.toString(io.netty.util.CharsetUtil.UTF_8));
			ByteBuf data = decode(ctx, in);
			GenericPackager packager = new GenericPackager("basic.xml");
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setDirection(ISOMsg.INCOMING);
			isoMsg.setPackager(packager);
			isoMsg.setHeader(data.readBytes(getHeaderLength()).array());
//System.out.println(data.readBytes(data.readShort()).array());
			// byte[] dataArray = data.readBytes(data.readableBytes()).array();
			// System.out.println(bytesToHex(dataArray));
			// System.out.println(data.);
			// System.out.println(data.);
			// byte [] byteArray = data.readBytes(data.readShort()).array();
			System.out.println("#################");
			// System.out.println(bytesToHex(byteArray));
			// Charset charset = Charset.forName("US-ASCII");
			// System.out.println(new String(in.array(),charset));
			// while (in.isReadable()) {
			// System.out.print((char) in.readByte());
			// }
			// System.out.println("<read end>");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.out.flush();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	protected ByteBuf decode(ChannelHandlerContext ctx, ByteBuf in) {

		if (in.readableBytes() < 4) {
			return null;
		}
		in.markReaderIndex();

		byte[] array = new byte[4];
		in.readBytes(array);

		int dataLength = 0;
		try {
			// dataLength = Integer.parseInt(new String(array));
			// dataLength = ((((int) array[0]) & 0xFF) << 24) | ((((int) array[0]) & 0xFF)
			// << 16) | ((((int) array[0]) & 0xFF) << 8) | (((int) array[1]) & 0xFF);

			dataLength = (array[0] & 0xFF) << 8 | array[1] & 0xFF;
			// in.skipBytes(2);
		} catch (Exception ex) {
			// LOGGER.error("Invalid data length received {}", new String(array),
			// ex);
			in.resetReaderIndex();
			return null;
		}
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return null;
		}
		ByteBuf frame = extractFrame(ctx, in, in.readerIndex(), dataLength);
		in.readerIndex(in.readerIndex() + dataLength);

		// if (isDumpRawBytes()) {
		// LOGGER.debug("incoming message-->length(bytes):" + dataLength);
		// }
		return frame;
	}

	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		ByteBuf frame = ctx.alloc().buffer(length);
		frame.writeBytes(buffer, index, length);
		return frame;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	protected void unpack(ISOMsg msg, byte[] b) throws ISOException {
		if (b.length > 0) {
			msg.unpack(b);
		} else {
			throw new ISOException("Zero length payload received");
		}
	}

	public static int getHeaderLength() {
		return header.length;
	}
}