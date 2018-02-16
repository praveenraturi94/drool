package com.drool.main;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
		ByteBuf frame = (ByteBuf) msg;
		//
		try {

			// System.out.println("<read start>");
			// byte[] message = new byte[in.readableBytes()];
			// in.getBytes(0, message);
			// System.out.println(bytesToHex(message));
			//
			//// System.out.print(in.toString(io.netty.util.CharsetUtil.UTF_8));
			// ByteBuf data = (ByteBuf) decode(ctx, in);
			//// System.out.print("#####################"+data.toString(io.netty.util.CharsetUtil.UTF_8));
			//// int length=in.array().length;
			//// byte[] finalData = new byte[1024];
			// int length = data.readableBytes();
			// System.out.println("length "+length);
			// byte[] messageWithoutHeaders = new byte[length];
			// data.getBytes(data.readerIndex(), messageWithoutHeaders);
			// System.out.println(bytesToHex(messageWithoutHeaders));

			// byte[] finalData = new byte[getHeaderLength()];
			// in.getBytes(in.readerIndex(), finalData);
			// System.out.println("header "+bytesToHex(finalData));
			//
			// byte[] finalData1 = new byte[in.readableBytes()];
			// in.getBytes(in.readerIndex(), finalData1);
			// System.out.println("data "+bytesToHex(finalData1));
			Logger logger = new Logger();
			try {
				 byte[] finalData = null;
				if(frame.readableBytes() != 0 ) {
					 finalData = new byte[frame.readableBytes()];
					 frame.getBytes(frame.readerIndex(), finalData);
					 System.out.println("data "+bytesToHex(finalData));
					 System.out.println("#################");
				}
				
				GenericPackager packager = new GenericPackager("basic1987_2.xml");
				ISOMsg isoMsg = new ISOMsg();
				isoMsg.setDirection(ISOMsg.INCOMING);
				isoMsg.setPackager(packager);
				
                logger.addListener(new SimpleLogListener());
                packager.setLogger(logger,"packager");
                isoMsg.unpack(bytesToHex(finalData).getBytes());
                System.out.printf("MTI = %s%n", isoMsg.getMTI());
                for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                    System.out.println("---------------------------------");
                    if (isoMsg.hasField(i)) {
                        System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                    }}
//				unpack(isoMsg,bytesToHex(finalData).getBytes());
				
			} catch (ISOException e) {
				ctx.close();
				e.printStackTrace();
			} finally {
				frame.release();
			}
			// isoMsg.setHeader(frame.readBytes(getHeaderLength()).array());
			// unpack(isoMsg, frame.readBytes(frame.readableBytes()).array());
			// isoMsg.setHeader(finalData);
			// unpack(isoMsg,finalData1);
			// ISOUtil.hexdump(isoMsg.getBytes());
			// try {
			// System.out.println(" MTI : " + isoMsg.getMTI());
			// for (int i=1;i<=isoMsg.getMaxField();i++) {
			// if (isoMsg.hasField(i)) {
			// System.out.println(" Field-"+i+" : "+isoMsg.getString(i));
			// }
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// } finally {
			// System.out.println("--------------------");
			// }
			// byte[] dataArray = data.readBytes(getHeaderLength()).array();
			// System.out.println(bytesToHex(dataArray));
			// System.out.println(data.readBytes(data.readShort()).array());
			// byte[] dataArray = data.readBytes(data.readableBytes()).array();
			// System.out.println(bytesToHex(dataArray));
			// System.out.println(data.);
			// System.out.println(data.);
			// byte [] byteArray = data.readBytes(data.readShort()).array();
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

	// protected byte[] decode(ChannelHandlerContext ctx, ByteBuf in) {
	//
	//
	// if (in.readableBytes() < 4) {
	// return null;
	// }
	// in.markReaderIndex();
	//
	//
	//
	// byte[] array = new byte[4];
	//// in.readBytes(array); // it will read and remove the size of the element
	// from the message
	// in.getBytes(0, array);
	//
	//
	// byte[] message = new byte[in.readableBytes()];
	// in.getBytes(0, message);
	// System.out.println("in"+bytesToHex(message));
	// System.out.println("array"+bytesToHex(array));
	//
	//
	//
	//// int dataLength = 0;
	//// try {
	//// // dataLength = Integer.parseInt(new String(array));
	//// // dataLength = ((((int) array[0]) & 0xFF) << 24) | ((((int) array[0]) &
	// 0xFF)
	//// // << 16) | ((((int) array[0]) & 0xFF) << 8) | (((int) array[1]) & 0xFF);
	////
	//// dataLength = (array[0] & 0xFF) >> 8 | array[1] & 0xFF;
	//// System.out.println("dataLength"+dataLength);
	//// // in.skipBytes(2);
	//// } catch (Exception ex) {
	//// // LOGGER.error("Invalid data length received {}", new String(array),
	//// // ex);
	//// in.resetReaderIndex();
	//// return null;
	//// }
	//// if (in.readableBytes() < dataLength) {
	//// in.resetReaderIndex();
	//// return null;
	//// }
	//
	// byte[] frame = extractFrame(ctx, in, array.length);
	//// in.readerIndex(in.readerIndex() + dataLength);
	//
	// // if (isDumpRawBytes()) {
	// // LOGGER.debug("incoming message-->length(bytes):" + dataLength);
	// // }
	// return frame;
	// }
	//
	// protected byte[] extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int
	// index) {
	//// byte[] message = new byte[buffer.readableBytes()];
	//// buffer.getBytes(0, message);
	//// System.out.println("buffer"+bytesToHex(message));
	////
	// int msgLength = buffer.readableBytes() - index;
	//// System.out.println("index"+index);
	//// System.out.println("msgLength"+msgLength);
	// ByteBuf frame = ctx.alloc().buffer(msgLength);
	// frame.writeBytes(buffer, index, msgLength);
	//
	//// byte[] message1 = new byte[frame.readableBytes()];
	//// frame.getBytes(0, message1);
	//// System.out.println("frame"+bytesToHex(message1));
	//
	// byte[] originalMessage = removeHeaders(ctx, frame);
	// return originalMessage;
	// }

	// ------------------------------------------
	// protected Object decode(ChannelHandlerContext ctx, ByteBuf in){
	//
	// if (in.readableBytes() < 4) {
	// return null;
	// }
	// in.markReaderIndex();
	//
	// //////
	// byte[] message = new byte[in.readableBytes()];
	// in.getBytes(in.readerIndex(), message);
	// System.out.println("incomming message in docoder : "+bytesToHex(message));
	// ////////
	//
	// byte[] array = new byte[4];
	// in.readBytes(array);
	// System.out.println("readed bytes"+bytesToHex(array));
	// ///////
	// byte[] message1 = new byte[in.readableBytes()];
	// in.getBytes(in.readerIndex(), message1);
	// System.out.println("after reading bytes the message is :
	// "+bytesToHex(message1));
	// //////
	//
	// System.out.println("array0 "+Byte.toString(array[0]));
	// System.out.println("array1 "+Byte.toString(array[1]));
	//
	// int dataLength = 0;
	// try {
	// //dataLength = Integer.parseInt(new String(array));
	// //dataLength = ((((int) array[0]) & 0xFF) << 24) | ((((int) array[0]) & 0xFF)
	// << 16) | ((((int) array[0]) & 0xFF) << 8) | (((int) array[1]) & 0xFF);
	//// System.out.println("array0 & 0xFF "+(array[0] & 0xFF));
	//// System.out.println("array1 & 0xFF "+(array[1] & 0xFF));
	// dataLength = (array[0] & 0xFF) << 8 | array[1] & 0xFF;
	//
	// ///////
	// System.out.println("dataLength : "+dataLength);
	// //in.skipBytes(2);
	// } catch (Exception ex) {
	//// LOGGER.error("Invalid data length received {}", new String(array),
	//// ex);
	// in.resetReaderIndex();
	// return null;
	// }
	// if (in.readableBytes() < dataLength) {
	// in.resetReaderIndex();
	// return null;
	// }
	//
	// ByteBuf frame = extractFrame(ctx, in, in.readerIndex(), dataLength);
	//
	// //////
	// byte[] message3 = new byte[frame.readableBytes()];
	// frame.getBytes(frame.readerIndex(), message3);
	// System.out.println(" message given by extractFrame : "+bytesToHex(message3));
	// ////////
	//
	//// ByteBuf dataRead = in.readerIndex(in.readerIndex() + dataLength);
	//
	// ByteBuf dataRead = in.readerIndex(in.readerIndex() + getHeaderLength());
	//
	// //////
	// System.out.println("index after reading ");
	// byte[] message4 = new byte[dataRead.readableBytes()];
	// dataRead.getBytes(dataRead.readerIndex(), message4);
	// System.out.println("incomming message after extractFrame dataread :
	// "+bytesToHex(message4));
	// ////////
	//
	// //////
	// byte[] message5 = new byte[in.readableBytes()];
	// in.getBytes(in.readerIndex(), message5);
	// System.out.println("incomming message after extractFrame:
	// "+bytesToHex(message5));
	// ////////
	//
	//
	//
	//// if (isDumpRawBytes()) {
	//// LOGGER.debug("incoming message-->length(bytes):" + dataLength);
	//// }
	//// return frame;
	// return dataRead;
	// }
	//
	// protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer,
	// int index, int length) {
	//
	// ByteBuf frame = ctx.alloc().buffer(length);
	// frame.writeBytes(buffer, index, length);
	// return frame;
	// }

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